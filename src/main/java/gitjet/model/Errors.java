package gitjet.model;

public enum Errors {
    CLONE_ERROR("Something went wrong during the project cloning"),
    DELETE_ERROR("Something went wrong during the cleanup");

    private final String message;

    Errors(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}
