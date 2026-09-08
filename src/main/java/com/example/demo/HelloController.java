package com.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class HelloController {

    @FXML private ComboBox<String> cmbSelectMovie;
    @FXML private TextField txtTitle;
    @FXML private ComboBox<String> cmbGenre;
    @FXML private TextField txtYear;
    @FXML private TextField txtRating;
    @FXML private TextField txtDuration;

    @FXML private ComboBox<String> cmbCountry;
    @FXML private RadioButton rb2D;
    @FXML private RadioButton rb3D;
    @FXML private ToggleGroup groupFormat;
    @FXML private CheckBox chkSubtitles;

    @FXML private Label lblResult;
    @FXML private ImageView imgPoster;

    private static class MovieData {
        String title, genre, year, rating, duration, country, imagePath;

        MovieData(String title, String genre, String year, String rating, String duration, String country, String imagePath) {
            this.title = title;
            this.genre = genre;
            this.year = year;
            this.rating = rating;
            this.duration = duration;
            this.country = country;
            this.imagePath = imagePath;
        }
    }

    private final Map<String, MovieData> movieDatabase = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        cmbGenre.getItems().addAll("Боевик", "Фантастика", "Приключения", "Драма", "Комедия", "Триллер");
        cmbCountry.getItems().addAll("США", "Великобритания", "Франция", "Япония", "Казахстан");

        movieDatabase.put("Человек-паук (2002)",
                new MovieData("Человек-паук", "Фантастика", "2002", "7.4", "2 ч 01 мин", "США", "/com/example/demo/spiderman1.jpg"));

        movieDatabase.put("Человек-паук 2 (2004)",
                new MovieData("Человек-паук 2", "Фантастика", "2004", "7.5", "2 ч 07 мин", "США", "/com/example/demo/spiderman2.jpg"));

        movieDatabase.put("Человек-паук 3: Враг в отражении (2007)",
                new MovieData("Человек-паук 3: Враг в отражении", "Фантастика", "2007", "7.1", "2 ч 19 мин", "США", "/com/example/demo/spiderman3.jpg"));

        movieDatabase.put("Новый Человек-паук (2012)",
                new MovieData("Новый Человек-паук", "Боевик", "2012", "6.9", "2 ч 16 мин", "США", "/com/example/demo/amazing1.jpg"));

        movieDatabase.put("Новый Человек-паук: Высокое напряжение (2014)",
                new MovieData("Новый Человек-паук: Высокое напряжение", "Боевик", "2014", "6.6", "2 ч 22 мин", "США", "/com/example/demo/amazing2.jpg"));

        movieDatabase.put("Человек-паук: Возвращение домой (2017)",
                new MovieData("Человек-паук: Возвращение домой", "Фантастика", "2017", "7.1", "2 ч 13 мин", "США", "/com/example/demo/homecoming.jpg"));

        movieDatabase.put("Человек-паук: Вдали от дома (2019)",
                new MovieData("Человек-паук: Вдали от дома", "Приключения", "2019", "7.2", "2 ч 09 мин", "США", "/com/example/demo/farfromhome.jpg"));

        movieDatabase.put("Человек-паук: Нет пути домой (2021)",
                new MovieData("Человек-паук: Нет пути домой", "Фантастика", "2021", "8.2", "2 ч 28 мин", "США", "/com/example/demo/nowayhome.jpg"));

        movieDatabase.put("Человек-паук: Новый день (2026)",
                new MovieData("Человек-паук: Новый день", "Боевик", "2026", "8.5", "2 ч 15 мин", "США", "/com/example/demo/brandnewday.jpg"));

        cmbSelectMovie.getItems().addAll(movieDatabase.keySet());
        cmbSelectMovie.getSelectionModel().select("Человек-паук (2002)");
        onMovieSelected();
    }

    @FXML
    private void onMovieSelected() {
        String selectedKey = cmbSelectMovie.getValue();
        if (selectedKey != null && movieDatabase.containsKey(selectedKey)) {
            MovieData movie = movieDatabase.get(selectedKey);

            txtTitle.setText(movie.title);
            cmbGenre.getSelectionModel().select(movie.genre);
            txtYear.setText(movie.year);
            txtRating.setText(movie.rating);
            txtDuration.setText(movie.duration);
            cmbCountry.getSelectionModel().select(movie.country);

            URL imgUrl = getClass().getResource(movie.imagePath);
            if (imgUrl == null && movie.imagePath.startsWith("/")) {
                imgUrl = getClass().getClassLoader().getResource(movie.imagePath.substring(1));
            }

            if (imgUrl != null) {
                imgPoster.setImage(new Image(imgUrl.toExternalForm()));
            } else {
                imgPoster.setImage(null);
                System.err.println("❌ Файл не найден по пути: " + movie.imagePath);
            }
        }
    }

    @FXML
    private void onSelectImageClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите обложку");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(imgPoster.getScene().getWindow());

        if (selectedFile != null) {
            imgPoster.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    @FXML
    private void onCreateClick() {
        String title = txtTitle.getText().trim();
        String genre = cmbGenre.getValue();
        String yearStr = txtYear.getText().trim();
        String ratingStr = txtRating.getText().trim();
        String duration = txtDuration.getText().trim();
        String country = cmbCountry.getValue();

        if (title.isBlank() || yearStr.isBlank() || ratingStr.isBlank() || duration.isBlank()) {
            showError("Заполните все текстовые поля!");
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            showError("Год должен быть целым числом.");
            return;
        }

        double rating;
        try {
            rating = Double.parseDouble(ratingStr.replace(',', '.'));
        } catch (NumberFormatException e) {
            showError("Рейтинг должен быть числом.");
            return;
        }

        String format = rb2D.isSelected() ? "2D" : "3D / IMAX";
        String subtitles = chkSubtitles.isSelected() ? "Есть" : "Нет";

        lblResult.setText(
                "🎥 " + title.toUpperCase() + "\n" +
                        "• Жанр: " + genre + " | Длительность: " + duration + "\n" +
                        "• Год: " + year + " | Рейтинг: " + String.format("%.1f", rating) + "/10★\n" +
                        "• Страна: " + country + " | Формат: " + format + "\n" +
                        "• Субтитры: " + subtitles
        );
    }

    @FXML
    private void onClearClick() {
        cmbSelectMovie.getSelectionModel().clearSelection();
        txtTitle.clear();
        cmbGenre.getSelectionModel().selectFirst();
        txtYear.clear();
        txtRating.clear();
        txtDuration.clear();
        cmbCountry.getSelectionModel().selectFirst();
        rb2D.setSelected(true);
        chkSubtitles.setSelected(false);
        imgPoster.setImage(null);
        lblResult.setText("");
        txtTitle.requestFocus();
    }

    @FXML
    private void onExitClick() {
        Platform.exit();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка ввода");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}