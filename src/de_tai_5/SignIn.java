package de_tai_5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignIn {
    public User authenticateUser(User user, Connection connection) throws SQLException {
        String query = "SELECT * FROM NguoiDung WHERE tendangnhap = ? AND matkhau = ?";
        User authenticatedUser = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    authenticatedUser = new User();
                    authenticatedUser.setUsername(resultSet.getString("tendangnhap"));
                    authenticatedUser.setPassword(resultSet.getString("matkhau"));
                    authenticatedUser.setFullname(resultSet.getString("hoten"));
                    // Thêm các thông tin người dùng khác tại đây nếu cần
                }
            }
        }
        return authenticatedUser;
    }
}
