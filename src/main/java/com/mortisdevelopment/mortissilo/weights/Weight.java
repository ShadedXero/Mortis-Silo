package com.mortisdevelopment.mortissilo.weights;

public class Weight {

    private final double weight;

    public Weight(double weight) {
        this.weight = weight;
    }

    public double getWeight(int amount) {
        return weight * amount;
    }
}
