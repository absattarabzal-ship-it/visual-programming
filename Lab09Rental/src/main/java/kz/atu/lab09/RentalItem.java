package kz.atu.lab09;

import javafx.beans.property.*;

public class RentalItem {
    private final StringProperty id; // Уникальный идентификатор
    private final StringProperty clientName;
    private final StringProperty carModel;
    private final IntegerProperty days;

    public RentalItem(String id, String clientName, String carModel, int days) {
        this.id = new SimpleStringProperty(id);
        this.clientName = new SimpleStringProperty(clientName);
        this.carModel = new SimpleStringProperty(carModel);
        this.days = new SimpleIntegerProperty(days);
    }

    public String getId() { return id.get(); }
    public StringProperty idProperty() { return id; }

    public String getClientName() { return clientName.get(); }
    public StringProperty clientNameProperty() { return clientName; }

    public String getCarModel() { return carModel.get(); }
    public StringProperty carModelProperty() { return carModel; }

    public int getDays() { return days.get(); }
    public IntegerProperty daysProperty() { return days; }
}