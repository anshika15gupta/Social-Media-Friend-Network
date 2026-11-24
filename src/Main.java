import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SocialFriendNetwork network = new SocialFriendNetwork();
        loadSampleData(network);

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your option: ");

            switch (choice) {
                case 1:
                    addUser(network);
                    break;
                case 2:
                    removeUser(network);
                    break;
                case 3:
                    addFriendship(network);
                    break;
                case 4:
                    removeFriendship(network);
                    break;
                case 5:
                    showUsers(network);
                    break;
                case 6:
                    showFriends(network);
                    break;
                case 7:
                    bfsTraversal(network);
                    break;
                case 8:
                    dfsTraversal(network);
                    break;
                case 9:
                    shortestPath(network);
                    break;
                case 10:
                    mutualFriends(network);
                    break;
                case 11:
                    showNetworkStats(network);
                    break;
                case 12:
                    suggestFriends(network);
                    break;
                case 13:
                    showHighestDegrees(network);
                    break;
                case 14:
                    showCommunities(network);
                    break;
                case 0:
                    running = false;
                    System.out.println("\nThe program is exiting. Thank you!");
                    break;
                default:
                    System.out.println("\nInvalid option! Please choose between 0 and 14.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n========== Social Media Friend Network ==========");
        System.out.println("1. Add a new user");
        System.out.println("2. Remove a user");
        System.out.println("3. Add a friendship");
        System.out.println("4. Remove a friendship");
        System.out.println("5. Display all users");
        System.out.println("6. Display a user's friends");
        System.out.println("7. BFS traversal");
        System.out.println("8. DFS traversal");
        System.out.println("9. Shortest path (BFS)");
        System.out.println("10. Mutual friends");
        System.out.println("11. Network statistics");
        System.out.println("12. Friend suggestions");
        System.out.println("13. Highest degree / most friends");
        System.out.println("14. Communities");
        System.out.println("0. Exit");
    }

    private static void addUser(SocialFriendNetwork network) {
        String id = readLine("User ID: ").trim();
        String name = readLine("User Name: ").trim();
        if (network.addUser(id, name)) {
            System.out.println("User added successfully: " + network.getUser(id));
        } else {
            System.out.println("User could not be added. The ID is duplicate or the input is empty.");
        }
    }

    private static void removeUser(SocialFriendNetwork network) {
        String id = readLine("User ID to remove: ").trim();
        id = resolveId(network, id);
        if (!network.containsUser(id)) {
            System.out.println("User ID not found.");
            return;
        }

        User removedUser = network.getUser(id);
        if (network.removeUser(id)) {
            System.out.println("User removed successfully: "
                    + removedUser.getId() + " - " + removedUser.getName());
            showUsers(network);
        } else {
            System.out.println("User could not be removed.");
        }
    }

    private static void addFriendship(SocialFriendNetwork network) {
        String firstId = readLine("First user ID: ").trim();
        String secondId = readLine("Second user ID: ").trim();
        firstId = resolveId(network, firstId);
        secondId = resolveId(network, secondId);
        if (network.addFriendship(firstId, secondId)) {
            System.out.println("Friendship added successfully.");
        } else {
            System.out.println("Friendship could not be added. Check both IDs and ensure the users are different.");
        }
    }

    private static void removeFriendship(SocialFriendNetwork network) {
        String firstId = readLine("First user ID: ").trim();
        String secondId = readLine("Second user ID: ").trim();
        firstId = resolveId(network, firstId);
        secondId = resolveId(network, secondId);
        if (network.removeFriendship(firstId, secondId)) {
            System.out.println("Friendship removed successfully.");
        } else {
            System.out.println("Friendship does not exist.");
        }
    }

    private static String resolveId(SocialFriendNetwork network, String input) {
        String id = network.findUserId(input);
        return id == null ? input.trim() : id;
    }

    private static void showUsers(SocialFriendNetwork network) {
        List<User> users = network.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("The network contains no users.");
            return;
        }
        System.out.println("\n--- All Users ---");
        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void showFriends(SocialFriendNetwork network) {
        String id = readLine("User ID: ").trim();
        id = resolveId(network, id);
        if (!network.containsUser(id)) {
            System.out.println("User not found.");
            return;
        }

        Set<String> friendIds = network.getFriendIds(id);
        System.out.println("\nFriends of " + network.getUser(id).getName() + ":");
        if (friendIds.isEmpty()) {
            System.out.println("This user has no friends.");
            return;
        }
        for (String friendId : friendIds) {
            System.out.println(network.getUser(friendId));
        }
    }

    private static void bfsTraversal(SocialFriendNetwork network) {
        String id = readLine("Starting User ID: ").trim();
        List<User> traversal = network.bfs(id);
        printTraversal("BFS", traversal);
    }

    private static void dfsTraversal(SocialFriendNetwork network) {
        String id = readLine("Starting User ID: ").trim();
        List<User> traversal = network.dfs(id);
        printTraversal("DFS", traversal);
    }

    private static void shortestPath(SocialFriendNetwork network) {
        String sourceId = readLine("Source User ID: ").trim();
        String targetId = readLine("Destination User ID: ").trim();
        List<User> path = network.findShortestPath(sourceId, targetId);
        if (path.isEmpty()) {
            System.out.println("A user was not found, or no path exists between the users.");
            return;
        }

        System.out.print("Shortest Path: ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i).getName());
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println(" (" + (path.size() - 1) + " friendships)");
    }

    private static void mutualFriends(SocialFriendNetwork network) {
        String firstId = readLine("First user ID: ").trim();
        String secondId = readLine("Second user ID: ").trim();
        firstId = resolveId(network, firstId);
        secondId = resolveId(network, secondId);
        List<User> mutual = network.mutualFriends(firstId, secondId);
        if (!network.containsUser(firstId) || !network.containsUser(secondId)) {
            System.out.println("One or both user IDs were not found.");
            return;
        }
        if (mutual.isEmpty()) {
            System.out.println("No mutual friends found.");
            return;
        }

        System.out.println("\n--- Mutual Friends ---");
        for (User user : mutual) {
            System.out.println(user);
        }
    }

    private static void showNetworkStats(SocialFriendNetwork network) {
        System.out.println("\nTotal Users: " + network.getUserCount());
        System.out.println("Total Friendships: " + network.getFriendshipCount());
    }

    private static void suggestFriends(SocialFriendNetwork network) {
        String input = readLine("Enter a user name or ID: ").trim();
        String id = resolveId(network, input);
        List<FriendSuggestion> suggestions = network.suggestFriends(id);

        if (!network.containsUser(id)) {
            System.out.println("User not found.");
            return;
        }
        if (suggestions.isEmpty()) {
            System.out.println("No friend suggestions are available.");
            return;
        }

        System.out.println("\n--- Friend Suggestions for "
                + network.getUser(id).getName() + " ---");
        for (FriendSuggestion suggestion : suggestions) {
            System.out.println(suggestion.getUser().getName()
                    + " (" + suggestion.getMutualCount() + " mutual friends)");
        }
    }

    private static void showHighestDegrees(SocialFriendNetwork network) {
        List<User> users = network.getHighestDegreeUsers();
        if (users.isEmpty()) {
            System.out.println("The network contains no users.");
            return;
        }

        System.out.println("\n--- Users by Highest Degree ---");
        for (User user : users) {
            int degree = network.getDegree(user.getId());
            System.out.println(user.getName() + ": " + degree
                    + (degree == 1 ? " friend" : " friends"));
        }
    }

    private static void showCommunities(SocialFriendNetwork network) {
        List<List<User>> communities = network.getCommunities();
        if (communities.isEmpty()) {
            System.out.println("The network contains no users.");
            return;
        }

        System.out.println("\n--- Communities / Disconnected Groups ---");
        for (int i = 0; i < communities.size(); i++) {
            List<User> community = communities.get(i);
            System.out.print((i + 1) + ". (" + community.size() + " users): ");
            for (int j = 0; j < community.size(); j++) {
                System.out.print(community.get(j).getName());
                if (j < community.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    private static void printTraversal(String name, List<User> traversal) {
        if (traversal.isEmpty()) {
            System.out.println("Starting user not found.");
            return;
        }
        System.out.print(name + " Traversal: ");
        for (int i = 0; i < traversal.size(); i++) {
            System.out.print(traversal.get(i).getName());
            if (i < traversal.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    private static int readInt(String prompt) {
        while (true) {
            String input = readLine(prompt).trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static void loadSampleData(SocialFriendNetwork network) {
        String[] names = {
            "Riya", "Nisha", "Priya", "Aman", "Karan",
            "Sana", "Sam", "Kiran", "Tina", "Omar", "Pia"
        };
        for (int i = 0; i < names.length; i++) {
            network.addUser("U" + (i + 1), names[i]);
        }

        network.addFriendship("Riya", "Aman");
        network.addFriendship("Riya", "Nisha");
        network.addFriendship("Riya", "Sana");
        network.addFriendship("Priya", "Karan");
        network.addFriendship("Aman", "Sana");
        network.addFriendship("Aman", "Nisha");
        network.addFriendship("Nisha", "Sana");
        network.addFriendship("Nisha", "Sam");
        network.addFriendship("Kiran", "Sam");
        network.addFriendship("Tina", "Omar");
    }
}
