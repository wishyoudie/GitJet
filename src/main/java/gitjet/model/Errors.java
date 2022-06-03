package gitjet.model;

/**
 * Custom error messages.
 */
public enum Errors {
    HANDLE_ERROR("Something went wrong when handling"),
    SEARCH_ERROR("Something went wrong while searching for repositories."),
    DATA_ERROR("Couldn't open storage file. Consider checking its validity."),
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