package kz.atu.lab09;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class RentalController {

    @FXML private TextField txtId;
    @FXML private TextField txtClientName;
    @FXML private ComboBox<String> cmbCarModel;
    @FXML private TextField txtDays;

    @FXML private Label lblIdError;
    @FXML private Label lblClientNameError;
    @FXML private Label lblCarModelError;
    @FXML private Label lblDaysError;

    @FXML private TableView<RentalItem> tableRentals;
    @FXML private TableColumn<RentalItem, String> colId;
    @FXML private TableColumn<RentalItem, String> colClient;
    @FXML private TableColumn<RentalItem, String> colCar;
    @FXML private TableColumn<RentalItem, Integer> colDays;

    private final ObservableList<RentalItem> rentals = FXCollections.observableArrayList();
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");

    @FXML
    public void initialize() {
        cmbCarModel.getItems().addAll("Toyota Camry", "Hyundai Elantra", "Kia Rio", "BMW X5", "Chevrolet Cobalt");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        colCar.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        colDays.setCellValueFactory(new PropertyValueFactory<>("days"));

        tableRentals.setItems(rentals);

        // Ограничение ввода дней только цифрами через TextFormatter
        txtDays.setTextFormatter(new TextFormatter<>(change -> {
            String next = change.getControlNewText();
            return next.matches("\\d{0,2}") ? change : null;
        }));
    }

    @FXML
    private void onRegister() {
        ValidationResult result = RentalValidator.validate(
                txtId.getText(),
                txtClientName.getText(),
                cmbCarModel.getValue(),
                txtDays.getText(),
                rentals
        );

        showErrors(result);

        if (!result.isValid()) {
            new Alert(Alert.AlertType.WARNING, "Исправьте ошибки в форме аренды").showAndWait();
            return;
        }

        try {
            RentalItem item = new RentalItem(
                    txtId.getText().trim(),
                    txtClientName.getText().trim(),
                    cmbCarModel.getValue(),
                    Integer.parseInt(txtDays.getText().trim())
            );
            rentals.add(item);
            clearForm();
        } catch (RuntimeException e) {
            new Alert(Alert.AlertType.ERROR, "Не удалось добавить запись об аренде").showAndWait();
        }
    }

    @FXML
    private void onClear() {
        clearForm();
    }

    private void showErrors(ValidationResult r) {
        lblIdError.setText(r.error("id"));
        lblClientNameError.setText(r.error("clientName"));
        lblCarModelError.setText(r.error("carModel"));
        lblDaysError.setText(r.error("days"));

        txtId.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !r.error("id").isEmpty());
        txtClientName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !r.error("clientName").isEmpty());
        cmbCarModel.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !r.error("carModel").isEmpty());
        txtDays.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !r.error("days").isEmpty());
    }

    private void clearForm() {
        txtId.clear();
        txtClientName.clear();
        cmbCarModel.getSelectionModel().clearSelection();
        txtDays.clear();
        showErrors(new ValidationResult(java.util.Map.of()));
    }
}