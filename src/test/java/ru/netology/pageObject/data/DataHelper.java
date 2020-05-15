package ru.netology.pageObject.data;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.val;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {

    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getValidAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getAuthInfoWithInvalidPassword() {
        Faker faker = new Faker();
        return new AuthInfo("vasya", faker.internet().password());
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static String getVerificationCode() throws SQLException {
        String userId = null;
        val dataSQL = "SELECT id FROM users WHERE login = ?;";
        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1:3306/app", "app", "pass"
                );
                val idStmt = conn.prepareStatement(dataSQL);
        ) {
            idStmt.setString(1, "vasya");

            try (val rs = idStmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getString("id");
                }
            }
        }

        String code = null;
        val authCode = "SELECT code FROM auth_codes WHERE user_id = ?;";
        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1:3306/app", "app", "pass"
                );
                val codeStmt = conn.prepareStatement(authCode);
        ) {
            codeStmt.setString(1, userId);

            try (val rs = codeStmt.executeQuery()) {
                if (rs.next()) {
                    code = rs.getString("code");
                }
            }
        }
        return code;
    }
}
