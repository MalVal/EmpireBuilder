open module be.malval.empirebuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires com.google.gson;

    exports be.malval.empirebuilder;
    exports be.malval.empirebuilder.ui;
    exports be.malval.empirebuilder.renderer;
    exports be.malval.empirebuilder.controller;
    exports be.malval.empirebuilder.system;
    exports be.malval.empirebuilder.model;
}