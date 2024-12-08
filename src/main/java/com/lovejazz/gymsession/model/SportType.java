package com.lovejazz.gymsession.model;

public enum SportType {
    GYM(1, "Тренажерный зал"),
    POOL(2, "Бассейн");

    private final Integer id;
    private final String name;

    SportType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }


    public static SportType findById(Integer id) {
        for (SportType sportType : values()) {
            if (sportType.id.equals(id)) {
                return sportType;
            }
        }
        return null;
    }
}