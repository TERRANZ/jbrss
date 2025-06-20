module me.vkorostelev.jbrss {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires static lombok;
    requires org.slf4j;
    requires com.rometools.rome;
    requires org.apache.commons.io;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.configuration2;

    opens me.vkorostelev.jbrss to javafx.fxml;
    opens me.vkorostelev.jbrss.view to javafx.fxml;

    exports me.vkorostelev.jbrss;
    exports me.vkorostelev.jbrss.view;
    exports me.vkorostelev.jbrss.rss.entity;
}