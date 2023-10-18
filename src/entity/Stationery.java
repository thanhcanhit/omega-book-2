/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author thanhcanhit
 */
public final class Stationery extends Product {

    private String color;
    private Double weight;
    private String material;
    private String origin;
    private StationeryType stationeryType;
    private Brand brand;

//    Hằng báo lỗi
    public static final String COLOR_EMPTY = "Stationery color không được rỗng";
    public static final String WEIGHT_LOWER_ZERO = "Stationery weight không được rỗng";
    public static final String MATERIAL_EMPTY = "Stationery material không được rỗng";
    public static final String ORIGIN_EMPTY = "Stationery origin không được rỗng";

    public Stationery() {
    }

    public Stationery(String productID) throws Exception {
        super(productID);
    }

    public Stationery(String color, Double weight, String material, String origin, StationeryType stationeryType, Brand brand, String productID, String name, Double costPrice, Byte[] image, Double VAT, Integer inventory, Type type) throws Exception {
        super(productID, name, costPrice, image, VAT, inventory, type);
        setColor(color);
        setWeight(weight);
        setMaterial(material);
        setOrigin(origin);
        setStationeryType(stationeryType);
        setBrand(brand);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) throws Exception {
        if (color.isBlank()) {
            throw new Exception(COLOR_EMPTY);
        }
        this.color = color;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) throws Exception {
        if (weight < 0) {
            throw new Exception(WEIGHT_LOWER_ZERO);
        }
        this.weight = weight;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) throws Exception {
        if (material.isBlank()) {
            throw new Exception(MATERIAL_EMPTY);
        }
        this.material = material;
    }

    public String getOrigin() throws Exception {
        if (origin.isBlank()) {
            throw new Exception(ORIGIN_EMPTY);
        }
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public StationeryType getStationeryType() {
        return stationeryType;
    }

    public void setStationeryType(StationeryType stationeryType) {
        this.stationeryType = stationeryType;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Stationery{" + "color=" + color + ", weight=" + weight + ", material=" + material + ", origin=" + origin + ", stationeryType=" + stationeryType + ", brand=" + brand + '}';
    }

}
