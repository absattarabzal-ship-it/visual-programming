package kz.atu.lab08;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

public class MainController {
    @FXML private TableView<Car> tableCars;
    @FXML private TableColumn<Car, String> colBrand;
    @FXML private TableColumn<Car, String> colModel;
    @FXML private TableColumn<Car, String> colYear;
    @FXML private TableColumn<Car, String> colPrice;
    @FXML private Label lblStatus;

    private final ObservableList<Car> carList = FXCollections.observableArrayList();
    private File currentFile = null;

    @FXML
    public void initialize() {
        colBrand.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        colModel.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
        colYear.setCellValueFactory(cellData -> cellData.getValue().yearProperty());
        colPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

        // Начальные (тестовые) данные при запуске приложения
        carList.add(new Car("Toyota", "Camry", "2022", "25000"));
        carList.add(new Car("Hyundai", "Elantra", "2021", "18500"));
        carList.add(new Car("Kia", "Rio", "2020", "14000"));
        carList.add(new Car("BMW", "X5", "2023", "65000"));

        tableCars.setItems(carList);
    }

    @FXML
    private void onAddCar() throws IOException {
        openCarDialog(null, "Добавить автомобиль");
    }

    @FXML
    private void onEditCar() throws IOException {
        Car selected = tableCars.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Выберите автомобиль для редактирования");
            return;
        }
        openCarDialog(selected, "Редактировать автомобиль");
    }

    private void openCarDialog(Car car, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("car-dialog.fxml"));
        Parent root = loader.load();

        CarDialogController controller = loader.getController();
        if (car != null) {
            controller.setCarData(car);
        }

        Stage dialog = new Stage();
        dialog.initOwner(tableCars.getScene().getWindow());
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle(title);
        dialog.setScene(new Scene(root));
        dialog.showAndWait();

        controller.getResultCar().ifPresent(newCar -> {
            if (car == null) {
                carList.add(newCar);
                lblStatus.setText("Добавлен новый автомобиль");
            } else {
                car.setBrand(newCar.getBrand());
                car.setModel(newCar.getModel());
                car.setYear(newCar.getYear());
                car.setPrice(newCar.getPrice());
                tableCars.refresh();
                lblStatus.setText("Данные автомобиля обновлены");
            }
        });
    }

    @FXML
    private void onOpen() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Открыть каталог");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));
        File file = chooser.showOpenDialog(tableCars.getScene().getWindow());
        if (file == null) return;

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            carList.clear();
            String[] lines = content.split("\n");
            for (String line : lines) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    carList.add(new Car(parts[0], parts[1], parts[2], parts[3]));
                }
            }
            currentFile = file;
            lblStatus.setText("Открыт файл: " + file.getName());
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void onSave() {
        if (currentFile != null) {
            saveToFile(currentFile);
        } else {
            onSaveAs();
        }
    }

    @FXML
    private void onSaveAs() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Сохранить каталог");
        chooser.setInitialFileName("cars.txt");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));
        File file = chooser.showSaveDialog(tableCars.getScene().getWindow());
        if (file == null) return;

        saveToFile(file);
    }

    private void saveToFile(File file) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Car c : carList) {
                sb.append(c.getBrand()).append(";")
                        .append(c.getModel()).append(";")
                        .append(c.getYear()).append(";")
                        .append(c.getPrice()).append("\n");
            }
            Files.writeString(file.toPath(), sb.toString(), StandardCharsets.UTF_8);
            currentFile = file;
            lblStatus.setText("Сохранено в файл: " + file.getName());
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void onAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setHeaderText("Каталог автомобилей (Вариант 17)");
        alert.setContentText("Приложение разработано для лабораторной работы по визуальному программированию.");
        alert.showAndWait();
    }

    @FXML
    private void onExit() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение выхода");
        confirm.setHeaderText("Выход из приложения");
        confirm.setContentText("Вы действительно хотите выйти?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    private void showError(String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Ошибка");
        a.setHeaderText("Ошибка ввода-вывода");
        a.setContentText(message);
        a.showAndWait();
    }

    private void showWarning(String message) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Предупреждение");
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}