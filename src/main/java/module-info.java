module ru.spbstu.gitjet {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.eclipse.jgit;
    requires org.apache.commons.io;
    requires org.eclipse.egit.github.core;
    requires java.sql;
    requires org.json;
    requires org.kohsuke.github.api;

    opens gitjet to javafx.fxml;
    exports gitjet;
    exports gitjet.model;
    exports gitjet.model.clonerepo;
    exports gitjet.controller;
    opens gitjet.controller to javafx.fxml;
    opens gitjet.model to javafx.base, javafx.fxml;
    exports gitjet.model.collectinfo;
    exports gitjet.model.repository;
    opens gitjet.model.repository to javafx.base, javafx.fxml;
}