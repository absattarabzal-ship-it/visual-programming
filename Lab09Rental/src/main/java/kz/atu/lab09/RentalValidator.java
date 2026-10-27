package kz.atu.lab09;

import java.util.LinkedHashMap;
import java.util.List;

public final class RentalValidator {

    public static ValidationResult validate(
            String id, String clientName, String carModel, String daysText, List<RentalItem> existingItems) {

        var errors = new LinkedHashMap<String, String>();

        // Проверка ID (уникальный идентификатор)
        if (id == null || id.trim().isBlank()) {
            errors.put("id", "ID не может быть пустым");
        } else {
            boolean exists = existingItems.stream()
                    .anyMatch(item -> item.getId().equalsIgnoreCase(id.trim()));
            if (exists) {
                errors.put("id", "ID должен быть уникальным (уже существует)");
            }
        }

        // Проверка ФИО клиента (2–80 символов)
        if (clientName == null || clientName.trim().length() < 2 || clientName.trim().length() > 80) {
            errors.put("clientName", "ФИО: от 2 до 80 символов");
        }

        // Проверка модели автомобиля
        if (carModel == null || carModel.isBlank()) {
            errors.put("carModel", "Выберите или укажите модель авто");
        }

        // Проверка количества дней аренды (целое число от 1 до 30)
        try {
            int d = Integer.parseInt(daysText.trim());
            if (d < 1 || d > 30) {
                errors.put("days", "Срок аренды: от 1 до 30 дней");
            }
        } catch (NumberFormatException | NullPointerException e) {
            errors.put("days", "Введите целое число дней");
        }

        return new ValidationResult(errors);
    }
}