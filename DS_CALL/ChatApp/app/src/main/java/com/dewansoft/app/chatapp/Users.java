package com.dewansoft.app.chatapp;

public class Users {
    private String name;
    private String story;
    private String image;
    private String phone;
    private String email;
    public Users(){

    }
    public Users(String name, String story,String image) {
        this.name = name;
        this.story = story;
        this.image = image;
    }
    public Users(String name,String image,String phone,String email) {
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.email = email;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
