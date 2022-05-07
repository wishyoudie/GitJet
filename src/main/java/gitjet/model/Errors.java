package gitjet.model;

public enum Errors {
    CLONE_ERROR("Something went wrong during the project cloning");

    private final String message;

    Errors(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}
