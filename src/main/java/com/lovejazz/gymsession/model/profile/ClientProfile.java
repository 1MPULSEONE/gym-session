package com.lovejazz.gymsession.model.profile;

public record ClientProfile(
        Integer id,
        Integer userId,
        String name,
        String surname,
        String patronymic,
        String bio,
        String avatarUrl
) implements  Profile {
    @Override
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

    public String getBio() {
        return bio;
    }

    public  String getAvatarUrl() {
        return avatarUrl;
    };
}
