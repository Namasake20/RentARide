package com.rentride.rentaride;

public class Users {
    //must be equal to one existing on firebase
    String email,password,phone,username,image,address;

    public Users() {

    }

    public Users(String email, String password, String phone, String username, String image, String address) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.username = username;
        this.image = image;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
