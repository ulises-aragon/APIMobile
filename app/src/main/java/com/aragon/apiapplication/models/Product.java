package com.aragon.apiapplication.models;

public class Product {
    private Integer code;
    private String name;
    private boolean status;

    public Product() {}

    public Product(String name, boolean status) {
        this.name = name;
        this.status = status;
    }

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
