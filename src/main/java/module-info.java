module ru.spbstu.gitjet {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.eclipse.jgit;
    requires org.apache.commons.io;

    opens gitjet to javafx.fxml;
    opens gitjet.model to javafx.base;
    exports gitjet;
    exports gitjet.model;
    exports gitjet.controller;
    opens gitjet.controller to javafx.fxml;
}