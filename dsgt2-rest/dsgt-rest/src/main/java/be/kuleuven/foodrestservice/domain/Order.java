package be.kuleuven.foodrestservice.domain;

import java.util.List;

public class Order {
    private String address;
    private List<String> mealIds;

    // Getters and Setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getMealIds() {
        return mealIds;
    }

    public void setMealIds(List<String> mealIds) {
        this.mealIds = mealIds;
    }
}