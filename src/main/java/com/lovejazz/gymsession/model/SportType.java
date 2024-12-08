package com.lovejazz.gymsession.model;

public enum SportType {
    GYM("Тренажерный зал"),
    POOL("Бассейн");

    private final String name;

    SportType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}