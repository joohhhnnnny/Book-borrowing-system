package joption;

public class SessionManager {
    private static int currentUserID = -1; // Default to -1 (no user logged in)

    public static void setCurrentUserID(int userID) {
        currentUserID = userID;
    }

    public static int getCurrentUserID() {
        return currentUserID;
    }
}
