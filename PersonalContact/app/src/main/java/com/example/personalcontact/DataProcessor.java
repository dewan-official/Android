package com.example.personalcontact;

public class DataProcessor {
    private String Name;
    private String Phone;
    private String image;
    private String proImage;

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    private String Gender;

    public String getProImage() {
        return proImage;
    }

    public void setProImage(String proImage) {
        this.proImage = proImage;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    private String Birthday;
    private String Email;



    private String Key;
    public DataProcessor(){

    }
    public DataProcessor(String name, String phone, String image, String parent){
        this.Name = name;
        this.Phone = phone;
        this.image = image;
        this.Key = parent;
    }

    public void setName(String name){
        this.Name = name;
    }

    public void setPhone(String phone){
        this.Phone = phone;
    }
    public void setImage(String image){
        this.image = image;
    }


    public String getName(){
        return this.Name;
    }

    public String getPhone(){
        return this.Phone;
    }
    public String getImage(){
        return this.image;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
