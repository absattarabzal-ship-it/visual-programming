package kz.atu.lab08;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Optional;

public class CarDialogController {
    @FXML private ComboBox<String> cmbBrand;
    @FXML private TextField txtModel;
    @FXML private TextField txtYear;
    @FXML private TextField txtPrice;

    private Car resultCar;

    @FXML
    public void initialize() {
        cmbBrand.getItems().addAll("Toyota", "Hyundai", "Kia", "BMW", "Mercedes", "Chevrolet", "Lada", "Volkswagen");
        cmbBrand.getSelectionModel().selectFirst();
    }

    public void setCarData(Car car) {
        cmbBrand.setValue(car.getBrand());
        txtModel.setText(car.getModel());
        txtYear.setText(car.getYear());
        txtPrice.setText(car.getPrice());
    }

    public Optional<Car> getResultCar() {
        return Optional.ofNullable(resultCar);
    }

    @FXML
    private void onSave() {
        resultCar = new Car(
                cmbBrand.getValue(),
                txtModel.getText(),
                txtYear.getText(),
                txtPrice.getText()
        );
        closeWindow();
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) cmbBrand.getScene().getWindow()).close();
    }
}