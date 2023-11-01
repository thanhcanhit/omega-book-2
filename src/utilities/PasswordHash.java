/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author thanhcanhit
 */
public class PasswordHash {

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        // Tạo đối tượng MessageDigest với thuật toán MD5
        MessageDigest md = MessageDigest.getInstance("MD5");

        // Băm mật khẩu thành một mảng byte
        byte[] bytes = md.digest(password.getBytes());

        // Chuyển đổi mảng byte thành chuỗi hex
        String hash = bytesToHex(bytes);

        return hash;
    }

    public static boolean comparePasswords(String password, String hashedPassword) throws NoSuchAlgorithmException {
        // Băm mật khẩu nhập vào
        String hashedInputPassword = hashPassword(password);

        // So sánh 2 chuỗi hash
        return hashedInputPassword.equals(hashedPassword);
    }

    private static String bytesToHex(byte[] bytes) {
        // Tạo một StringBuilder để lưu trữ kết quả
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            // Chuyển byte thành giá trị thập lục phân
            int value = b & 0xFF;
            String hex = Integer.toHexString(value);

            // Nếu giá trị thập lục phân có 1 chữ số, thêm 0 ở phía trước
            if (hex.length() == 1) {
                sb.append("0");
            }

            sb.append(hex);
        }

        return sb.toString();
    }
}
