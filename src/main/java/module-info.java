module me.julie.pokedex {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;


    opens me.julie.pokedex to javafx.fxml;
    exports me.julie.pokedex;
}