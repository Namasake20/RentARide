package com.rentride.rentaride;

public class OrderModel {
    private String ddate,dlocation,dtime,pcharge,pdate,phone_number,plocation,pname,ptime;

    public OrderModel() {
    }

    public OrderModel(String ddate, String dlocation, String dtime, String pcharge, String pdate, String phone_number, String plocation, String pname, String ptime) {
        this.ddate = ddate;
        this.dlocation = dlocation;
        this.dtime = dtime;
        this.pcharge = pcharge;
        this.pdate = pdate;
        this.phone_number = phone_number;
        this.plocation = plocation;
        this.pname = pname;
        this.ptime = ptime;
    }

    public String getDdate() {
        return ddate;
    }

    public void setDdate(String ddate) {
        this.ddate = ddate;
    }

    public String getDlocation() {
        return dlocation;
    }

    public void setDlocation(String dlocation) {
        this.dlocation = dlocation;
    }

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }

    public String getPcharge() {
        return pcharge;
    }

    public void setPcharge(String pcharge) {
        this.pcharge = pcharge;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPlocation() {
        return plocation;
    }

    public void setPlocation(String plocation) {
        this.plocation = plocation;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }
}
