import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class SocialFriendNetwork {
    private final HashMap<String, User> users = new HashMap<>();
    private final HashMap<String, LinkedHashSet<String>> friends = new HashMap<>();

    public boolean addUser(String id, String name) {
        if (id == null || name == null
                || id.trim().isEmpty() || name.trim().isEmpty()) {
            return false;
        }

        String cleanId = id.trim();
        if (users.containsKey(cleanId)) {
            return false;
        }

        users.put(cleanId, new User(cleanId, name.trim()));
        friends.put(cleanId, new LinkedHashSet<>());
        return true;
    }

    public boolean removeUser(String id) {
        if (!users.containsKey(id)) {
            return false;
        }

        users.remove(id);
        friends.remove(id);
        for (LinkedHashSet<String> friendList : friends.values()) {
            friendList.remove(id);
        }
        return true;
    }

    public boolean addFriendship(String firstId, String secondId) {
        firstId = findUserId(firstId);
        secondId = findUserId(secondId);
        if (firstId == null || secondId == null) {
            return false;
        }
        if (!users.containsKey(firstId) || !users.containsKey(secondId)
                || firstId.equals(secondId)) {
            return false;
        }

        friends.get(firstId).add(secondId);
        friends.get(secondId).add(firstId);
        return true;
    }

    public boolean removeFriendship(String firstId, String secondId) {
        firstId = findUserId(firstId);
        secondId = findUserId(secondId);
        if (firstId == null || secondId == null) {
            return false;
        }
        if (!users.containsKey(firstId) || !users.containsKey(secondId)) {
            return false;
        }
        boolean existedInFirst = friends.get(firstId).remove(secondId);
        boolean existedInSecond = friends.get(secondId).remove(firstId);
        return existedInFirst || existedInSecond;
    }

    public List<User> bfs(String startId) {
        List<User> result = new ArrayList<>();
        startId = findUserId(startId);
        if (startId == null) {
            return result;
        }
        if (!users.containsKey(startId)) {
            return result;
        }

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        queue.offer(startId);
        visited.add(startId);

        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            result.add(users.get(currentId));
            for (String friendId : friends.get(currentId)) {
                if (!visited.contains(friendId)) {
                    visited.add(friendId);
                    queue.offer(friendId);
                }
            }
        }
        return result;
    }

    public List<User> dfs(String startId) {
        List<User> result = new ArrayList<>();
        startId = findUserId(startId);
        if (startId == null) {
            return result;
        }
        if (!users.containsKey(startId)) {
            return result;
        }

        Set<String> visited = new HashSet<>();
        dfsRecursive(startId, visited, result);
        return result;
    }

    private void dfsRecursive(String currentId, Set<String> visited,
                              List<User> result) {
        visited.add(currentId);
        result.add(users.get(currentId));

        for (String friendId : friends.get(currentId)) {
            if (!visited.contains(friendId)) {
                dfsRecursive(friendId, visited, result);
            }
        }
    }

    public List<User> findShortestPath(String sourceId, String targetId) {
        List<User> path = new ArrayList<>();
        sourceId = findUserId(sourceId);
        targetId = findUserId(targetId);
        if (sourceId == null || targetId == null) {
            return path;
        }
        if (!users.containsKey(sourceId) || !users.containsKey(targetId)) {
            return path;
        }

        Queue<String> queue = new ArrayDeque<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();
        queue.offer(sourceId);
        visited.add(sourceId);
        parent.put(sourceId, null);

        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            if (currentId.equals(targetId)) {
                break;
            }
            for (String friendId : friends.get(currentId)) {
                if (!visited.contains(friendId)) {
                    visited.add(friendId);
                    parent.put(friendId, currentId);
                    queue.offer(friendId);
                }
            }
        }

        if (!visited.contains(targetId)) {
            return path;
        }

        List<String> reversePath = new ArrayList<>();
        String currentId = targetId;
        while (currentId != null) {
            reversePath.add(currentId);
            currentId = parent.get(currentId);
        }
        Collections.reverse(reversePath);
        for (String id : reversePath) {
            path.add(users.get(id));
        }
        return path;
    }

    public List<User> mutualFriends(String firstId, String secondId) {
        List<User> result = new ArrayList<>();
        firstId = findUserId(firstId);
        secondId = findUserId(secondId);
        if (firstId == null || secondId == null) {
            return result;
        }
        if (!users.containsKey(firstId) || !users.containsKey(secondId)) {
            return result;
        }

        Set<String> mutualIds = new HashSet<>(friends.get(firstId));
        mutualIds.retainAll(friends.get(secondId));
        for (String id : mutualIds) {
            result.add(users.get(id));
        }
        return result;
    }

    public List<FriendSuggestion> suggestFriends(String id) {
        List<FriendSuggestion> suggestions = new ArrayList<>();
        id = findUserId(id);
        if (id == null || !users.containsKey(id)) {
            return suggestions;
        }

        for (String candidateId : users.keySet()) {
            if (candidateId.equals(id) || friends.get(id).contains(candidateId)) {
                continue;
            }

            Set<String> mutualIds = new HashSet<>(friends.get(id));
            mutualIds.retainAll(friends.get(candidateId));
            suggestions.add(new FriendSuggestion(users.get(candidateId),
                    mutualIds.size()));
        }

        suggestions.removeIf(suggestion -> suggestion.getMutualCount() == 0);
        suggestions.sort((a, b) -> {
            int mutualComparison = Integer.compare(
                    b.getMutualCount(), a.getMutualCount());
            if (mutualComparison != 0) {
                return mutualComparison;
            }
            return a.getUser().getName().compareToIgnoreCase(
                    b.getUser().getName());
        });
        return suggestions;
    }

    public List<User> getHighestDegreeUsers() {
        List<User> orderedUsers = new ArrayList<>(users.values());
        orderedUsers.sort((a, b) -> {
            int degreeComparison = Integer.compare(
                    friends.get(b.getId()).size(),
                    friends.get(a.getId()).size());
            if (degreeComparison != 0) {
                return degreeComparison;
            }
            return a.getName().compareToIgnoreCase(b.getName());
        });
        return orderedUsers;
    }

    public int getDegree(String id) {
        id = findUserId(id);
        if (id == null || !users.containsKey(id)) {
            return -1;
        }
        return friends.get(id).size();
    }

    public List<List<User>> getCommunities() {
        List<List<User>> communities = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for (String userId : users.keySet()) {
            if (visited.contains(userId)) {
                continue;
            }

            List<User> community = new ArrayList<>();
            Queue<String> queue = new ArrayDeque<>();
            queue.offer(userId);
            visited.add(userId);

            while (!queue.isEmpty()) {
                String currentId = queue.poll();
                community.add(users.get(currentId));
                for (String friendId : friends.get(currentId)) {
                    if (!visited.contains(friendId)) {
                        visited.add(friendId);
                        queue.offer(friendId);
                    }
                }
            }

            community.sort((a, b) ->
                    a.getName().compareToIgnoreCase(b.getName()));
            communities.add(community);
        }

        communities.sort((a, b) -> {
            int sizeComparison = Integer.compare(b.size(), a.size());
            if (sizeComparison != 0) {
                return sizeComparison;
            }
            return a.get(0).getName().compareToIgnoreCase(
                    b.get(0).getName());
        });
        return communities;
    }

    public String findUserId(String idOrName) {
        if (idOrName == null) {
            return null;
        }
        String input = idOrName.trim();
        if (users.containsKey(input)) {
            return input;
        }
        for (Map.Entry<String, User> entry : users.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(input)) {
                return entry.getKey();
            }
            if (entry.getValue().getName().equalsIgnoreCase(input)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Set<String> getFriendIds(String id) {
        if (!users.containsKey(id)) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(friends.get(id));
    }

    public boolean containsUser(String id) {
        return users.containsKey(id);
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>(users.values());
        allUsers.sort((a, b) -> a.getId().compareToIgnoreCase(b.getId()));
        return allUsers;
    }

    public int getUserCount() {
        return users.size();
    }

    public int getFriendshipCount() {
        int count = 0;
        for (Set<String> friendList : friends.values()) {
            count += friendList.size();
        }
        return count / 2;
    }
}
