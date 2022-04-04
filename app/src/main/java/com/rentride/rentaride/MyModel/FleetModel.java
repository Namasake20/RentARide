package com.rentride.rentaride.MyModel;

public class FleetModel {
    private String agentUsername,date,pid,pname,price,time;

    public FleetModel() {
    }

    public FleetModel(String agentUsername, String date, String pid, String pname, String price, String time) {
        this.agentUsername = agentUsername;
        this.date = date;
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.time = time;
    }

    public String getAgentUsername() {
        return agentUsername;
    }

    public void setAgentUsername(String agentUsername) {
        this.agentUsername = agentUsername;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
