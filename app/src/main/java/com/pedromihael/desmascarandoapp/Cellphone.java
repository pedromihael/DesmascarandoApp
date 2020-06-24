package com.pedromihael.desmascarandoapp;

public class Cellphone {

    private String name;
    private String brand;

    public Cellphone() { }

    public Cellphone(String name, String brand) {
        this.name = name;
        this.brand = brand;
    }

    public Cellphone(String name) {
        this.name = name;
        this.brand = name;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

}
