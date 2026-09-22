package com.example.lab4;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LayoutController {

    @FXML private TextField txtTitle;
    @FXML private TextField txtGenre;
    @FXML private ComboBox<String> cmbYear;
    @FXML private TableView<Movie> tblMovies;
    @FXML private TableColumn<Movie, ImageView> colPoster;
    @FXML private TableColumn<Movie, String> colTitle;
    @FXML private TableColumn<Movie, String> colGenre;
    @FXML private TableColumn<Movie, String> colYear;
    @FXML private Label lblStatus;

    @FXML
    public void initialize() {
        cmbYear.getItems().addAll("2026", "2025", "2024", "2023", "2022", "2019", "2014", "2010", "2008", "Ранее");
        cmbYear.getSelectionModel().selectFirst();

        colPoster.setCellValueFactory(cellData -> {
            ImageView imageView = new ImageView();
            try {
                String url = cellData.getValue().getPosterUrl();
                if (url != null && !url.isEmpty()) {
                    Image img = new Image(url);
                    imageView.setImage(img);
                }
            } catch (Exception e) {
                // Если картинка не найдена
            }
            imageView.setFitWidth(45);
            imageView.setFitHeight(65);
            imageView.setPreserveRatio(true);
            return new SimpleObjectProperty<>(imageView);
        });

        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));

        // Безопасная загрузка локальных файлов из папки resources
        String p1 = getClass().getResource("/interstellar.jpg") != null ? getClass().getResource("/interstellar.jpg").toExternalForm() : "";
        String p2 = getClass().getResource("/inception.jpg") != null ? getClass().getResource("/inception.jpg").toExternalForm() : "";
        String p3 = getClass().getResource("/oppenheimer.jpg") != null ? getClass().getResource("/oppenheimer.jpg").toExternalForm() : "";

        tblMovies.getItems().addAll(
                new Movie("Интерстеллар", "Фантастика", "2014", p1),
                new Movie("Начало", "Триллер", "2010", p2),
                new Movie("Оппенгеймер", "Биография", "2023", p3)
        );
    }

    @FXML
    private void onSaveClick() {
        String title = txtTitle.getText().trim();
        String genre = txtGenre.getText().trim();
        String year = cmbYear.getValue();

        if (title.isEmpty() || genre.isEmpty()) {
            lblStatus.setText("Статус: заполните название и жанр фильма");
            return;
        }

        tblMovies.getItems().add(new Movie(title, genre, year, ""));

        lblStatus.setText("Статус: фильм успешно добавлен — " + title);
        onClearClick();
    }

    @FXML
    private void onClearClick() {
        txtTitle.clear();
        txtGenre.clear();
        cmbYear.getSelectionModel().selectFirst();
        txtTitle.requestFocus();
    }
}