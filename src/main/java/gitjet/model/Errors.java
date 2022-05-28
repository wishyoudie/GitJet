package gitjet.model;

/**
 * Custom error messages.
 */
public enum Errors {
    CLONE_ERROR("Something went wrong during the project cloning"),
    DATA_ERROR("Couldn't open 'data.dat' file. Consider checking its existence."),
    DIRECTORY_ERROR("Couldn't open the directory. Consider checking its existence."),
    TOKEN_ERROR("Something wrong with your GitHub token. Please check that you put valid token value in settings."),
    SETTINGS_ERROR("Couldn't open 'settings.dat' file. Consider checking its existence.");

    private final String message;

    Errors(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}