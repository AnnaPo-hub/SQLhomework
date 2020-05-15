package ru.netology.pageObject.test;

import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.pageObject.data.DataHelper;
import ru.netology.pageObject.page.LoginPage;

import java.sql.DriverManager;
import java.sql.SQLException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {
    @Test
    void shouldCheckLogin() throws SQLException {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getValidAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode();
        val dashboardPage = verificationPage.verify(verificationCode);
        $("h2[data-test-id=\"dashboard\"]").shouldBe(visible);
    }

    @Test
    void shouldCheckIfBlocked() throws SQLException {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfoWithInvalidPassword();
        loginPage.validLogin(authInfo);
        loginPage.validLogin(authInfo);
        loginPage.validLogin(authInfo);
        val statusSQL = "SELECT status FROM users WHERE login = ?;";
        try (val conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/app", "app", "pass");
             val statusStmt = conn.prepareStatement(statusSQL);) {
            statusStmt.setString(1, "vasya");
            try (val rs = statusStmt.executeQuery()) {
                if (rs.next()) {
                    val status = rs.getString("status");
                    assertTrue(statusSQL.equals("blocked"));
                }
            }
        }
    }
}
