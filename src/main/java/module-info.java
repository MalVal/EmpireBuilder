module be.malval.empirebuilder {
    requires javafx.controls;
    requires javafx.fxml;


    opens be.malval.empirebuilder to javafx.fxml;
    exports be.malval.empirebuilder;
    exports be.malval.empirebuilder.ui;
    opens be.malval.empirebuilder.ui to javafx.fxml;
    exports be.malval.empirebuilder.renderer;
    opens be.malval.empirebuilder.renderer to javafx.fxml;
    exports be.malval.empirebuilder.controller;
    opens be.malval.empirebuilder.controller to javafx.fxml;
}