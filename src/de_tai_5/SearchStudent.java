// SearchStudent.java
package de_tai_5;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchStudent {

    public static Student searchStudent(String masv) {
        Student student = null;

        try {
            // Perform database connection and query to retrieve student information
            Connection connection = Connect_Database.connect();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT sv.tensv, sv.ngaysinh, sv.noisinh, sv.gioitinh, sv.khoa, khoa.tenkhoa, nganh.tennganh, sv.malop " +
                    "FROM SinhVien sv " +
                    "JOIN Lop lop ON sv.malop = lop.malop " +
                    "JOIN Nganh nganh ON lop.manganh = nganh.manganh " +
                    "JOIN Khoa khoa ON nganh.makhoa = khoa.makhoa " +
                    "WHERE sv.masv = ?"
                );
            statement.setString(1, masv);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String tenSV = rs.getString("tensv");
                boolean gioiTinh = rs.getBoolean("gioitinh");
                Date ngaySinh = rs.getDate("ngaysinh");
                String noiSinh = rs.getString("noisinh");
                int khoa = rs.getInt("khoa");
                String tenKhoa = rs.getString("tenkhoa");
                String tenNganh = rs.getString("tennganh");
                String maLop = rs.getString("malop");
                // Retrieve other details from the query

                // Create Student instance
                student = new Student(masv, tenSV, gioiTinh, ngaySinh, noiSinh, khoa,maLop, tenKhoa, tenNganh );
            }

            // Close database connections
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace();
        }

        return student;
    }
}
