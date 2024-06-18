package netology.test;

import netology.data.SQLHelper;
import netology.page.LoginPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.open;

public class BankLoginTest {
    LoginPage loginPage;
    @AfterEach
    void tearDown(){
        SQLHelper.cleanAuthCodes();}

    @AfterAll
    static void tearDownAll(){
        SQLHelper.cleanDataBase();
    }

    @BeforeEach
    void setUp(){
        loginPage = open("http://localhost:9999",LoginPage.class);

    }
    @Test
    void shouldSuccessLogin(){
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisible();
        var verificationPge = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationPge.getCode());

    }
    @Test
    void shouldGetErrorMessage(){
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }
    @Test
    void shouldSuccessLoginButWrongVerificationCoded(){
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisible();
        var verificationCode = DataHelper.generateRandomCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");

    }
}