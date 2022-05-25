package gitjet.model;

public enum Errors {
    CLONE_ERROR("Something went wrong during the project cloning"),
    DELETE_ERROR("Something went wrong during the cleanup"),
    DATA_ERROR("Couldn't open 'data.dat' file. Consider checking its existence."),
    DIRECTORY_ERROR("Couldn't open the directory. Consider checking its existence."),
    TOKEN_ERROR("Something went wrong with your GitHub token. Please check that you put valid token in preferences.");

    private final String message;

    Errors(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}