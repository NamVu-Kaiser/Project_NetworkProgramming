/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de_tai_5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author DerKaiser
 */
public class Connect_Database {
    public static Connection connect(){
        Connection connection = null;
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String URL = "jdbc:sqlserver://localhost:1433;Database=DE_TAI_5;user=sa;password=123";
            connection = DriverManager.getConnection(URL);
            System.out.println("Đã kết nối tới cơ sở dữ liệu SQL Server.");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println("Lỗi kết nối tới cơ sở dữ liệu: " + e.getMessage());
        }
        return connection;
    }
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Đã đóng kết nối.");
            } catch (SQLException e) {
                System.out.println("Lỗi đóng kết nối: " + e.getMessage());
            }
        }
    }
}
