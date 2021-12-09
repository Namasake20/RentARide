package com.rentride.rentaride;

public class Cars {
    public String name,description,price,img,pid,date,time,agentPhone,agentUsername;

    public Cars() {
    }

    public Cars(String name, String description, String price, String img, String pid, String date, String time, String agentPhone, String agentUsername) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.img = img;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.agentPhone = agentPhone;
        this.agentUsername = agentUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
