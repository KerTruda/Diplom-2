package ru.yandex.praktikum.order;

import ru.yandex.praktikum.models.Order;

public class OrderGenerator {
    public static Order getListOrder() {

        return new Order()
                .setIngredients(OrderClient.getIngredientsFromOrder().path("data._id"));
    }
}
