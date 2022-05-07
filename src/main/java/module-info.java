module ru.spbstu.gitjet {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.eclipse.jgit;


    opens gitjet to javafx.fxml;
    exports gitjet;
}