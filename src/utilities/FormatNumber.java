/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author thanhcanhit
 */
public class FormatNumber {

    /**
     * Định dạng số kém với đơn vị đ
     *
     * @param value
     * @return string chuỗi sau khi được format
     */
    public static String toVND(Double value) {
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "vn"));
        return vndFormat.format(value);
    }

    /**
     * Định dạng số vd: "1000500000.574" -> "1,000,500,000.57"
     *
     * @param value
     * @return string là chuỗi số sau khi được format
     */
    public static String toNumberWithCommas(Double value) {
        double amount = Double.parseDouble(value.toString());
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        return formatter.format(amount);
    }

    /**
     * Định dạng số vd: "1000500000.574" -> "1,000,500,000.57"
     *
     * @param value
     * @return string là chuỗi số sau khi được format
     */
    public static String toNumberWithCommas(String value) {
        double amount = Double.parseDouble(value);
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        return formatter.format(amount);
    }
}
