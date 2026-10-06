package kz.atu.lab6;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CatalogController {

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbFilterGenre;
    @FXML private TableView<Movie> tableMovies;
    @FXML private TableColumn<Movie, ImageView> colPoster;
    @FXML private TableColumn<Movie, String> colTitle;
    @FXML private TableColumn<Movie, String> colGenre;
    @FXML private TableColumn<Movie, Integer> colYear;
    @FXML private TableColumn<Movie, Double> colRating;

    @FXML private TextField txtTitle;
    @FXML private ComboBox<String> cmbGenre;
    @FXML private TextField txtYear;
    @FXML private TextField txtRating;
    @FXML private TextField txtImageUrl;
    @FXML private Label lblCount;
    @FXML private ListView<String> listGenres;

    private final ObservableList<Movie> movies = FXCollections.observableArrayList();
    private FilteredList<Movie> filteredMovies;

    @FXML
    public void initialize() {
        // Постерді кесте бағанында көрсету
        colPoster.setCellValueFactory(param -> {
            ImageView imageView = new ImageView();
            String url = param.getValue().getImageUrl();
            if (url != null && !url.isBlank()) {
                try {
                    Image image = new Image(url, 45, 65, true, true, true);
                    imageView.setImage(image);
                } catch (Exception ignored) {}
            }
            imageView.setFitWidth(45);
            imageView.setFitHeight(65);
            return new SimpleObjectProperty<>(imageView);
        });

        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Жаңартылған сілтемелермен толтырылған фильмдер тізімі
        movies.addAll(
                new Movie("Начало", "Фантастика", 2010, 8.8,
                        "https://avatars.mds.yandex.net/get-kinopoisk-image/1629390/8ab9a119-dd74-44f0-baec-0629797483d7/600x900"),
                new Movie("Побег из Шоушенка", "Драма", 1994, 9.3,
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPgahxIMx6U5cBAa7rMxPzfr4askqypMbOvMKPR7N9lxOReLeLhWYpXDs&s=10"),
                new Movie("Интерстеллар", "Фантастика", 2014, 8.6,
                        "https://avatars.mds.yandex.net/get-kinopoisk-image/1600647/430042eb-ee69-4818-aed0-a312400a26bf/600x900"),
                new Movie("Криминальное чтиво", "Комедия", 1994, 8.9,
                        "https://avatars.mds.yandex.net/get-kinopoisk-image/4716873/0a07a903-9025-4aff-bf7c-46bbb175888c/600x900"),
                new Movie("Темный рыцарь", "Боевик", 2008, 9.0,
                        "https://avatars.mds.yandex.net/get-kinopoisk-image/1599028/f27dd387-48ce-40ef-8182-665362d3895e/600x900"),
                new Movie("Матрица", "Фантастика", 1999, 8.7,
                        "https://avatars.mds.yandex.net/get-kinopoisk-image/4774061/cf1970bc-3f08-4e0e-a095-2fb57c3aa7c6/220x330"),
                new Movie("Остров проклятых", "Ужасы", 2010, 8.2,
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSnr8vNpHtzToKW1XogIM6pB246OGdVPmMJ_u10bLd-V8ZtJfclWxV0Hwg&s=10"),
                new Movie("1+1", "Комедия", 2011, 8.8,
                        "https://avatars.mds.yandex.net/get-ott/13074011/2a0000019504097dd3ec1bf7f8db56b4026e/375x375")
        );

        // Жанрлар
        String[] genres = {"Фантастика", "Драма", "Комедия", "Ужасы", "Боевик"};
        cmbGenre.getItems().addAll(genres);
        cmbFilterGenre.getItems().add("Все");
        cmbFilterGenre.getItems().addAll(genres);
        cmbFilterGenre.setValue("Все");

        listGenres.getItems().add("Все фильмы");
        listGenres.getItems().addAll(genres);

        // Фильтрация
        filteredMovies = new FilteredList<>(movies, m -> true);
        tableMovies.setItems(filteredMovies);

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        cmbFilterGenre.setOnAction(event -> applyFilter());

        listGenres.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cmbFilterGenre.setValue(newVal.equals("Все фильмы") ? "Все" : newVal);
                applyFilter();
            }
        });

        updateCount();
    }

    private void applyFilter() {
        String search = txtSearch.getText().trim().toLowerCase();
        String genre = cmbFilterGenre.getValue();

        filteredMovies.setPredicate(movie -> {
            boolean matchesSearch = movie.getTitle().toLowerCase().contains(search);
            boolean matchesGenre = genre == null || genre.equals("Все") || movie.getGenre().equals(genre);
            return matchesSearch && matchesGenre;
        });

        updateCount();
    }

    @FXML
    private void onAddClick() {
        String title = txtTitle.getText().trim();
        String genre = cmbGenre.getValue();
        String yearText = txtYear.getText().trim();
        String ratingText = txtRating.getText().trim();
        String imageUrl = txtImageUrl != null ? txtImageUrl.getText().trim() : "";

        if (title.isBlank() || genre == null || yearText.isBlank() || ratingText.isBlank()) {
            showError("Барлық міндетті өрістерді толтырыңыз.");
            return;
        }

        try {
            int year = Integer.parseInt(yearText);
            double rating = Double.parseDouble(ratingText);

            if (year < 1895 || year > 2026 || rating < 0 || rating > 10) {
                showError("Жылды (1895-2026) және рейтингті (0.0-10.0) тексеріңіз.");
                return;
            }

            movies.add(new Movie(title, genre, year, rating, imageUrl));
            clearInput();
            applyFilter();
        } catch (NumberFormatException e) {
            showError("Жыл бүтін сан, ал рейтинг бөлшек сан болуы керек.");
        }
    }

    @FXML
    private void onDeleteClick() {
        Movie selected = tableMovies.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Жою үшін фильмді таңдаңыз.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Растау");
        alert.setHeaderText(null);
        alert.setContentText("\"" + selected.getTitle() + "\" фильмін жойғыңыз келе ме?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                movies.remove(selected);
                applyFilter();
            }
        });
    }

    @FXML
    private void onClearFilterClick() {
        txtSearch.clear();
        cmbFilterGenre.setValue("Все");
        listGenres.getSelectionModel().clearSelection();
        applyFilter();
    }

    private void updateCount() {
        lblCount.setText("Найдено записей: " + tableMovies.getItems().size());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Қате");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearInput() {
        txtTitle.clear();
        txtYear.clear();
        txtRating.clear();
        if (txtImageUrl != null) txtImageUrl.clear();
        cmbGenre.getSelectionModel().clearSelection();
        txtTitle.requestFocus();
    }
}