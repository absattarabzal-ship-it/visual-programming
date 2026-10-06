module kz.atu.lab6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens kz.atu.lab6 to javafx.fxml;
    exports kz.atu.lab6;
}