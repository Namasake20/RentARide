package com.rentride.rentaride.MyModel;

public class OrderModel {
    private String pickUpDate,returnDate,total_amount,car_name,phone_number,time;

    public OrderModel() {
    }

    public OrderModel(String pickUpDate, String returnDate, String total_amount, String car_name, String phone_number, String time) {
        this.pickUpDate = pickUpDate;
        this.returnDate = returnDate;
        this.total_amount = total_amount;
        this.car_name = car_name;
        this.phone_number = phone_number;
        this.time = time;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
