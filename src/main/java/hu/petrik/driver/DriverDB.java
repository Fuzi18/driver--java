package hu.petrik.driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDB {
    Connection conn;

    public static String DB_DRIVER = "mysql";
    public static String DB_HOST = "localhost";
    public static String DB_PORT = "3306";
    public static String DB_DBNAME = "driver";
    public static String DB_USER = "root";
    public static String DB_PASS = "";


    public DriverDB() throws SQLException {
        String url = String.format("jdbc:%s://%s:%s/%s", DB_DRIVER, DB_HOST, DB_PORT, DB_DBNAME);
        conn = DriverManager.getConnection(url, DB_USER, DB_PASS);
    }

    public boolean createDriver(Driver driver) throws SQLException {
        String sql = "INSERT INTO drivers(pilota, kor, nemzetiseg) VALUES (?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, driver.getPilota());
        stmt.setInt(2, driver.getKor());
        stmt.setString(3, driver.getNemzetiseg());
        return stmt.executeUpdate() > 0;
    }

    public List<Driver> readDriver() throws SQLException {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM drivers";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("id");
            String pilota = result.getString("pilota");
            int kor = result.getInt("kor");
            String nemzetiseg = result.getString("nemzetiseg");
            Driver driver = new Driver(id, pilota, kor, nemzetiseg);
            drivers.add(driver);
        }
        return drivers;
    }

    public boolean updateDriver(Driver driver) throws SQLException {
        String sql = "UPDATE drivers " +
                "SET pilota = ?, " +
                "kor = ?, " +
                "nemzetiseg = ?" +
                "WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, driver.getPilota());
        stmt.setInt(2, driver.getKor());
        stmt.setString(3, driver.getNemzetiseg());
        stmt.setInt(4, driver.getId());
        return stmt.executeUpdate() > 0;
    }

    public boolean deleteDriver(int id) throws SQLException {
        String sql = "DELETE FROM drivers WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        return stmt.executeUpdate() > 0;
    }
}
