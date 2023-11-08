/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.Book;
import enums.BookCategory;
import enums.BookType;
import entity.Brand;
import entity.Product;
import entity.Stationery;
import enums.StationeryType;
import enums.Type;
import interfaces.DAOBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thanhcanhit
 */
public class Product_DAO implements DAOBase<Product> {

    @Override
    public Product getOne(String id) {
        Product result = null;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("Select * from Product where productID = ? ");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result = getProductData(rs);
                byte[] image = rs.getBytes("img");
                if (image != null) {
                    result.setImage(image);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Product> getAll() {
        ArrayList<Product> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("Select * from Product");
            while (rs.next()) {
                Product product = getProductData(rs);
                result.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getLength() {
        int length = 0;

        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("select length = count(*) from Product");

            if (rs.next()) {
                length = rs.getInt("length");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return length;
    }
     public boolean updateQuantity( String productID, int quantity) {
        int n = 0;

        try {
            Product product = getOne(productID);
            int newQuantity = product.getInventory()+quantity;

            PreparedStatement st = ConnectDB.conn.prepareStatement("UPDATE Product SET quantity = ? WHERE productID = ? ;");
            st.setInt(1, newQuantity);
            st.setString(2, productID);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return n > 0;
    }

    /**
     * Dùng cho logic chia trang Số phần tử 1 trang là 50
     *
     * @param page trang hiện tại (1,...)
     * @return ArrayList<Product>
     */
    public ArrayList<Product> getPage(int page) {
        ArrayList<Product> result = new ArrayList<>();
        String query = """
                       select * from product
                       order by productID
                       offset ? rows
                       FETCH NEXT 50 ROWS ONLY
                       """;
        int offsetQuantity = (page - 1) * 50;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            st.setInt(1, offsetQuantity);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (rs != null) {
                    Product product = getProductData(rs);
                    result.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(Product object
    ) {
        int n = 0;

//        Xác định câu truy vấn phù hợp cho từng loại
        String query = null;
        if (object.getType() == Type.BOOK) {
            query = """
                    INSERT INTO [dbo].[Product]
                    ([productID],[productType],[bookType],[bookCategory],[name],[author],[price],[costPirce],[img],[publishYear],[publisher],[pageQuantity],[isHardCover],[description],[language],[translater],[VAT],[inventory])
                    VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                    """;
        } else if (object.getType() == Type.STATIONERY) {
            query = """
                    INSERT INTO [dbo].[Product]
                    ([productID],[productType],[stationaryType],[name],[price],[costPirce],[img],[weight],[color],[material],[origin],[brandID],[VAT],[inventory])
                    VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                    """;
        }

//        Truyền tham số phù hợp cho từng loại
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            setParams(object, st);

            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, Product newObject
    ) {
        int n = 0;

//        Xác định câu truy vấn phù hợp cho từng loại
        String query = "";
        if (newObject.getType() == Type.BOOK) {
            query = """
                    UPDATE [dbo].[Product]
                    SET [productID] = ?,[productType] = ?,[bookType] = ?,[bookCategory] = ?,[name] = ?,[author] = ?,[price] = ?,[costPrice] = ?,[img] = ?,[publishYear] = ?,[publisher] = ?,[pageQuantity] = ?,[isHardCover] = ?,[description] = ?,[language] = ?,[translater] = ?,[VAT] = ?,[inventory] = ?
                    WHERE productID = ?
                    """;
        } else if (newObject.getType() == Type.STATIONERY) {
            query = """
                    UPDATE [dbo].[Product]
                    SET [productID] = ?,[productType] = ?,[stationaryType] = ?,[name] = ?,[price] = ?,[costPrice] = ?,[img] = ?,[weight] = ?,[color] = ?,[material] = ?,[origin] = ?,[brandID] = ?,[VAT] = ?,[inventory] = ?
                    WHERE productID = ?;
                    """;
        }

//        Truyền tham số phù hợp cho từng loại
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            int currentIndex = setParams(newObject, st) + 1;
            st.setString(currentIndex, id);

            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean updateInventory(String productID, int inventory) {
        int n = 0;

        String query = """
                    UPDATE [dbo].[Product]
                    SET [inventory] = ?
                    WHERE productID = ?
                    """;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            st.setInt(1, inventory);
            st.setString(2, productID);

            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean delete(String id
    ) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ArrayList<Product> findById(String searchQuery) {
        ArrayList<Product> result = new ArrayList<>();
        String query = """
                       select * from product
                       where productID like ?
                       """;
        try {

            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            st.setString(1, searchQuery + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (rs != null) {
                    result.add(getProductData(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * Truyền đủ các tham số để lọc danh sách sản phẩm
     *
     * @param name
     * @param isEmpty
     * @param type
     * @param bookType
     * @param stationeryType
     * @return Danh sách sản phẩm match với bộ lọc
     */
    public ArrayList<Product> filter(String name, boolean isEmpty, Type type, BookCategory bookType, StationeryType stationeryType) {
        ArrayList<Product> result = new ArrayList<>();
//        Index tự động tăng phụ thuộc vào số lượng biến số có
        int index = 1;
        String query = "select * from product where name like ?";
        if (isEmpty) {
            query += " and inventory = ?";
        }

//        Nếu loại sản phẩm là tất cả thì không xét đến 2 phần tử con
        if (type != null) {
            query += " and productType = ?";

//            Xét loại chi tiết
            if (bookType != null) {
                query += " and bookCategory = ?";
            }

            if (stationeryType != null) {
                query += " and stationeryType = ?";
            }
        }

        try {

            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            st.setString(index++, name + "%");

            if (isEmpty) {
                st.setInt(index++, 0);
            }

            if (type != null) {
                st.setInt(index++, type.getValue());

//                Xét loại chi tiết
                if (bookType != null) {
                    st.setInt(index++, bookType.getValue());
                }

                if (stationeryType != null) {
                    st.setInt(index++, stationeryType.getValue());
                }
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (rs != null) {
                    result.add(getProductData(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param rs Kết quả truy vấn cần lấy thông tin
     * @return Thông tin của Product hoặc null
     * @throws SQLException
     * @throws Exception
     */
    private Product getProductData(ResultSet rs) throws SQLException, Exception {
        Product result = null;

        //Lấy thông tin tổng quát của lớp product
        String id = rs.getString("productID");
        String name = rs.getString("name");
        Double costPrice = rs.getDouble("costPrice");
//        Để tránh gây giảm hiệu năng ứng dụng thì chỉ khi đọc 1 phần tử mới lấy img
//        byte[] image = rs.getBytes("img");
        Double VAT = rs.getDouble("VAT");
        Double price = rs.getDouble("price");
        int productType = rs.getInt("productType");
        int inventory = rs.getInt("inventory");

//      Định danh loại sản phẩm để lấy đủ thông tin cần cho đúng loại đối tượng
        if (Type.BOOK.compare(productType)) {
            String author = rs.getString("author");
            String publisher = rs.getString("publisher");
            int publishYear = rs.getInt("publishYear");
            String desc = rs.getString("description");
            int pageQuantity = rs.getInt("pageQuantity");
            boolean isHardCover = rs.getBoolean("isHardCover");
            String language = rs.getString("language");
            String translator = rs.getString("translater");
            int bookCategory = rs.getInt("bookCategory");
            int bookType = rs.getInt("bookType");

            result = new Book(author, publisher, publishYear, desc, pageQuantity, isHardCover, language, translator, BookType.fromInt(bookType), BookCategory.fromInt(bookCategory), id, name, costPrice, price, null, VAT, inventory, Type.BOOK);
        } else if (Type.STATIONERY.compare(productType)) {
            String color = rs.getString("color");
            Double weight = rs.getDouble("weight");
            String material = rs.getString("material");
            String origin = rs.getString("origin");
            String brandID = rs.getString("brandID");
            int stationeryType = rs.getInt("stationeryType");

            result = new Stationery(color, weight, material, origin, StationeryType.fromInt(stationeryType), new Brand(brandID), id, name, costPrice, price, null, VAT, inventory, Type.STATIONERY);
        }

        return result;
    }

    /**
     *
     * @param object Đối tượng mang thông tin để truyền vào truy vấn
     * @param st Câu truy vấn cần truyền tham số
     * @return int index hiện tại
     * @throws SQLException
     * @throws Exception
     */
    private int setParams(Product object, PreparedStatement st) throws SQLException, Exception {
        if (object.getType() == Type.BOOK) {
            Book book = (Book) object;
            st.setString(1, book.getProductID());
            st.setInt(2, book.getType().getValue());
            st.setInt(3, book.getBookOrigin().getValue());
            st.setInt(4, book.getBookCategory().getValue());
            st.setString(5, book.getName());
            st.setString(6, book.getAuthor());
            st.setDouble(7, book.getPrice());
            st.setDouble(8, book.getCostPrice());
            st.setBytes(9, book.getImage());
            st.setInt(10, book.getPublishYear());
            st.setString(11, book.getPublisher());
            st.setInt(12, book.getPageQuantity());
            st.setBoolean(13, book.getIsHardCover());
            st.setString(14, book.getDescription());
            st.setString(15, book.getLanguage());
            st.setString(16, book.getTranslator());
            st.setDouble(17, book.getVAT());
            st.setInt(18, book.getInventory());
            return 18;
        } else if (object.getType() == Type.STATIONERY) {
            Stationery stationery = (Stationery) object;
            st.setString(1, stationery.getProductID());
            st.setInt(2, stationery.getType().getValue());
            st.setInt(3, stationery.getStationeryType().getValue());
            st.setString(4, stationery.getName());
            st.setDouble(5, stationery.getPrice());
            st.setDouble(6, stationery.getCostPrice());
            st.setBytes(7, stationery.getImage());
            st.setDouble(8, stationery.getWeight());
            st.setString(9, stationery.getColor());
            st.setString(10, stationery.getMaterial());
            st.setString(11, stationery.getOrigin());
            st.setString(12, stationery.getBrand().getBrandID());
            st.setDouble(13, stationery.getVAT());
            st.setInt(14, stationery.getInventory());
            return 14;
        }
        return 0;
    }
    
    
    public ArrayList<Product> getTop10Product(String date) {

        ArrayList<Product> result = new ArrayList<>();

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("""
                                                                   select top 10 productID
                                                                    from OrderDetail as od join [Order] as o on od.orderID = o.orderID
                                                                    where CONVERT(varchar, orderAt, 23) = ?
                                                                    group by productID
                                                                    order by SUM(quantity) desc""");
            st.setString(1, date);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String productID = rs.getString(1);
                result.add(
                      getOne(productID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public ArrayList<Product> getTopProductInDay(String date) {

        ArrayList<Product> result = new ArrayList<>();

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("""
                                                                   select productID
                                                                    from OrderDetail as od join [Order] as o on od.orderID = o.orderID
                                                                    where CONVERT(varchar, orderAt, 23) = ?
                                                                    group by productID""");
            st.setString(1, date);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String productID = rs.getString(1);
                result.add(
                      getOne(productID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
     public int getQuantitySale(String productID,String date) {
        int result = 0;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("""
                                                                   select SUM(quantity) as sl
                                                                   from OrderDetail as od join [Order] as o on od.orderID = o.orderID
                                                                   where CONVERT(varchar, orderAt, 23) = ? and productID = ?
                                                                   group by productID 
                                                                   order by SUM(quantity) desc""");
            st.setString(1, date);
            st.setString(2, productID);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                result = rs.getInt("sl");
   
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public double getTotalProduct(String productID, String date) {
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("""
                                                                   select sum(od.lineTotal)
                                                                   from OrderDetail as od join [Order] as o on od.orderID = o.orderID
                                                                   where productID = ? and CONVERT(varchar, orderAt, 23) = ?
                                                                   group by productID                                               
                                                                   """);
            st.setString(1, productID);
            st.setString(2, date);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    public double[] getTotalInMonth(int month, int year) {
        double[] result = new double[31];

        for (int i = 0; i < result.length; i++) {
            result[i] = 0;
        }

        ;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("select DAY(orderAt) as day, sum(totalDue) as total from Order where YEAR(orderAt) = ? and MONTH(orderAt) = ? group by MONTH(orderAt)");
            st.setInt(2, month);
            st.setInt(1,year);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int day = rs.getInt("day");
                double total = rs.getDouble("total");

                result[day - 1] = total;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
