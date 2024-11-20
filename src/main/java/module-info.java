module com.mateusz.mastermind.mastermind {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mateusz.mastermind.mastermind to javafx.fxml;
    exports com.mateusz.mastermind.mastermind;
}