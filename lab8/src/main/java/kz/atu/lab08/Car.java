package kz.atu.lab08;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Car {
    private final StringProperty brand;
    private final StringProperty model;
    private final StringProperty year;
    private final StringProperty price;

    public Car(String brand, String model, String year, String price) {
        this.brand = new SimpleStringProperty(brand);
        this.model = new SimpleStringProperty(model);
        this.year = new SimpleStringProperty(year);
        this.price = new SimpleStringProperty(price);
    }

    public String getBrand() { return brand.get(); }
    public void setBrand(String brand) { this.brand.set(brand); }
    public StringProperty brandProperty() { return brand; }

    public String getModel() { return model.get(); }
    public void setModel(String model) { this.model.set(model); }
    public StringProperty modelProperty() { return model; }

    public String getYear() { return year.get(); }
    public void setYear(String year) { this.year.set(year); }
    public StringProperty yearProperty() { return year; }

    public String getPrice() { return price.get(); }
    public void setPrice(String price) { this.price.set(price); }
    public StringProperty priceProperty() { return price; }
}