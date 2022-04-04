package com.rentride.rentaride.MyModel;

public class Cars {
    public String name,price,img,pid,date,time,agentPhone,agentUsername,ac,doors,transmission,passengers;

    public Cars() {
    }

    public Cars(String name, String price, String img, String pid, String date, String time, String agentPhone, String agentUsername, String ac, String doors, String transmission, String passengers) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.agentPhone = agentPhone;
        this.agentUsername = agentUsername;
        this.ac = ac;
        this.doors = doors;
        this.transmission = transmission;
        this.passengers = passengers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAgentPhone() {
        return agentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone;
    }

    public String getAgentUsername() {
        return agentUsername;
    }

    public void setAgentUsername(String agentUsername) {
        this.agentUsername = agentUsername;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getDoors() {
        return doors;
    }

    public void setDoors(String doors) {
        this.doors = doors;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getPassengers() {
        return passengers;
    }

    public void setPassengers(String passengers) {
        this.passengers = passengers;
    }
}
