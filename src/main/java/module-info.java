module me.julie.pokedex {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens me.julie.pokedex to javafx.fxml;
    exports me.julie.pokedex;
    exports me.julie.pokedex.controllers;
    opens me.julie.pokedex.controllers to javafx.fxml;
}