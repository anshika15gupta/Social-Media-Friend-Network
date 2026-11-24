public class FriendSuggestion {
    private final User user;
    private final int mutualCount;

    public FriendSuggestion(User user, int mutualCount) {
        this.user = user;
        this.mutualCount = mutualCount;
    }

    public User getUser() {
        return user;
    }

    public int getMutualCount() {
        return mutualCount;
    }
}
