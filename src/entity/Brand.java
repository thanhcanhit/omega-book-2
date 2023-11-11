/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import static entity.Product.PRODUCT_ID_INVALID;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 *
 * @author thanhcanhit
 */
public final class Brand {

    private String brandID;
    private String name;
    private String country;

//    Hằng báo lỗi
    public static final String BRAND_ID_INVALID = "Brand id không hợp lệ";
    public static final String NAME_EMPTY = "Brand name không được rỗng";
    public static final String COUNTRY_EMPTY = "Brand country không được rỗng";

    public Brand() {
    }

    public Brand(String brandID) {
        this.brandID = brandID;
    }

    public Brand(String brandID, String name, String country) throws Exception {
        setBrandID(brandID);
        setName(name);
        setCountry(country);
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) throws Exception {
        String regex = "^TH[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(brandID).matches()) {
            throw new Exception(PRODUCT_ID_INVALID);
        }
        this.brandID = brandID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if (name.isBlank()) {
            throw new Exception(NAME_EMPTY);
        }
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) throws Exception {
        if (country.isBlank()) {
            throw new Exception(COUNTRY_EMPTY);
        }
        this.country = country;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.brandID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Brand other = (Brand) obj;
        return Objects.equals(this.brandID, other.brandID);
    }

    @Override
    public String toString() {
        return "Brand{" + "brandID=" + brandID + ", name=" + name + ", country=" + country + '}';
    }

}
