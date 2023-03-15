package com.velozient.dronedeliveryapi.models;

import java.util.Objects;

public class Delivery {
    private String location;
    private Long packageWeight;

    public Delivery(String location, Long packageWeight) {
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Invalid location name");
        }

        if (packageWeight == null || packageWeight <= 0) {
            throw new IllegalArgumentException("Invalid package weight");
        }
        this.location = location;
        this.packageWeight = packageWeight;
    }

    public String getLocation() {
        return location;
    }

    public Long getPackageWeight() {
        return packageWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Delivery delivery = (Delivery) o;

        if (!location.equals(delivery.location)) return false;
        return Objects.equals(packageWeight, delivery.packageWeight);
    }

    @Override
    public int hashCode() {
        int result = location.hashCode();
        result = 31 * result + (packageWeight != null ? packageWeight.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "[" + location + ']';
    }
}
