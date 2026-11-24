# Social Media Friend Network

A console-based Java application that models users and friendships as an undirected graph. It demonstrates graph data structures and algorithms through common social-network operations.

## Features

- Add and remove users.
- Add and remove undirected friendships.
- Display all users or a user's friends.
- Traverse a connected group with Breadth-First Search (BFS) or Depth-First Search (DFS).
- Find the shortest friendship path between two users using BFS.
- Find mutual friends and suggest new friends based on mutual connections.
- View user degrees, network counts, and disconnected communities.
- Find users by ID or name (case-insensitive).

## How the graph works

- Each user is a **vertex**.
- Each friendship is an **undirected edge**: if A is friends with B, B is also recorded as a friend of A.
- A user's **degree** is the number of their direct friends.
- A **community** is a connected component: a group where every user is reachable from every other user through friendships.
- Friend suggestions exclude the selected user and their existing friends. Only candidates with at least one mutual friend are suggested.

The network is stored in adjacency lists. BFS uses a queue; DFS uses recursion. The shortest-path search stores each visited user's parent so it can reconstruct the route after BFS finds the destination.

## Project structure

```text
Social Medial friend network/
|-- src/
|   |-- Main.java
|   |-- SocialFriendNetwork.java
|   |-- User.java
|   `-- FriendSuggestion.java
|-- .gitignore
`-- README.md
```

| File | Purpose |
|---|---|
| `src/Main.java` | Console menu, input, and output |
| `src/SocialFriendNetwork.java` | Graph storage and network operations |
| `src/User.java` | User ID and name |
| `src/FriendSuggestion.java` | Suggested user and mutual-friend count |
| `.gitignore` | Excludes compiled output from Git |

The `out/` directory is created locally when you compile the project. It is ignored by Git.

## Requirements

- Java Development Kit (JDK) 8 or later
- PowerShell or a Java-enabled IDE such as VS Code

Check that Java is installed:

```powershell
java -version
javac -version
```

## Build and run

Open PowerShell in the project root (the folder containing `README.md`) and run:

```powershell
New-Item -ItemType Directory -Force .\out | Out-Null
$javaFiles = Get-ChildItem -Path .\src -Filter *.java | ForEach-Object { $_.FullName }
javac -d .\out $javaFiles
if ($LASTEXITCODE -ne 0) { throw "Compilation failed." }
java -cp .\out Main
```

The application loads its sample users and friendships each time it starts. To rebuild after changing the source, run the same commands again.

### Run from VS Code

1. Open the project folder in VS Code.
2. Open `src/Main.java`.
3. Run the `Main` class using the Java extension.
4. Enter menu choices in the integrated terminal.

## Using the application

At the menu, enter the number for the operation and press Enter. When prompted, use either a user's exact ID or their name; matching ignores letter case. For example, `U4` and `Aman` identify the same sample user. IDs are not zero-padded, so `U04` is different from `U4`.

| Option | Operation |
|---:|---|
| 1 | Add a user |
| 2 | Remove a user and their friendships |
| 3 | Add a friendship |
| 4 | Remove a friendship |
| 5 | Display all users |
| 6 | Display a user's friends |
| 7 | BFS traversal from a starting user |
| 8 | DFS traversal from a starting user |
| 9 | Find the shortest path between two users |
| 10 | Find mutual friends |
| 11 | Show total users and friendships |
| 12 | Suggest friends based on mutual friends |
| 13 | List users by friend count (highest first) |
| 14 | Display disconnected communities |
| 0 | Exit |

## Preloaded sample network

The application starts with these 11 users:

| ID | Name |
|---|---|
| U1 | Riya |
| U2 | Nisha |
| U3 | Priya |
| U4 | Aman |
| U5 | Karan |
| U6 | Sana |
| U7 | Sam |
| U8 | Kiran |
| U9 | Tina |
| U10 | Omar |
| U11 | Pia |

The 10 friendships are:

- Riya — Aman, Nisha, Sana
- Nisha — Aman, Sana, Sam
- Priya — Karan
- Sam — Kiran
- Tina — Omar

Friendships are undirected, so each pair above represents one connection. For example, Riya — Aman is stored for both users but counted as one friendship.

The sample has four connected communities: Riya, Nisha, Aman, Sana, Sam, and Kiran; Priya and Karan; Tina and Omar; and Pia on their own.

## Example

To find the shortest path from Aman to Sam, choose option `9`, then enter `Aman` and `Sam` (or `U4` and `U7`). The result is:

```text
Shortest Path: Aman -> Nisha -> Sam (2 friendships)
```

To get friend suggestions for Riya, choose option `12` and enter `Riya`. Sam is suggested because they share Nisha as a mutual friend.

## Manual checks

These are example checks to try in the console; the project does not include an automated test suite.

| Check | Input | Expected behavior |
|---|---|---|
| Network statistics | Option `11` | Shows 11 users and 10 friendships |
| Shortest path | Option `9`, Aman to Sam | Shows a path with 2 friendships |
| Friend suggestions | Option `12`, Riya | Suggests Sam with 1 mutual friend |
| No suggestions | Option `12`, Pia | Reports that no suggestions are available |
| Invalid friendship | Option `3`, Riya and Zoya | Reports that the friendship could not be added |
| Communities | Option `14` | Displays four connected groups |

## Data structures and complexity

Let `V` be the number of users, `E` the number of friendship connections, and `d(u)` the number of friends of the user being queried. Hash-based operation costs below are average-case.

| Operation | Time complexity |
|---|---:|
| Add a user | O(1) average |
| Add or remove a friendship | O(1) average |
| Remove a user | O(V) average |
| BFS or DFS traversal | O(V + E) |
| Shortest path using BFS | O(V + E) |
| Find mutual friends | O(d(u) + d(v)) |
| Generate suggestions for one user | O(V × d(u) + V log V) |
| Sort users by degree | O(V log V) |
| Find communities | O(V + E) traversal, plus O(V log V) sorting |

The graph uses `HashMap` and `LinkedHashSet` adjacency lists. `Queue` supports BFS; `HashSet` tracks visited users and mutual friends.

## License

No license file is currently included. Add a license if you want to specify how others may use or distribute this project.
