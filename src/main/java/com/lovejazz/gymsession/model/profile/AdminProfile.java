package com.lovejazz.gymsession.model.profile;

public record AdminProfile(
        Integer id,
        Integer userId,
        String name,
        String surname,
        String patronymic
) implements Profile {
    public Integer getId() {
        return id;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public String getPatronymic() {
        return patronymic;
    }
}
