package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFrom2ndTo1st() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        int amount = generateValidAmount(secondCardBalance);
        var expectedFirstCardBalance = firstCardBalance + amount;
        var expectedSecondCardBalance = secondCardBalance - amount;

        var openTransferPage = dashboardPage.selectCardToTransfer(firstCard);
        dashboardPage = openTransferPage.makeTransfer(String.valueOf(amount), secondCard);

        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCard);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCard);

        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }

    @Test
    public void shouldTransferMoneyFrom1stTo2nd() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        int amount = generateValidAmount(firstCardBalance);
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;

        var openTransferPage = dashboardPage.selectCardToTransfer(secondCard);
        dashboardPage = openTransferPage.makeTransfer(String.valueOf(amount), firstCard);

        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCard);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCard);

        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }
    @Test
    void shouldAllowToTransferAll() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        int amount = secondCardBalance;
        var expectedFirstCardBalance = firstCardBalance + amount;
        var expectedSecondCardBalance = 0;

        var openTransferPage = dashboardPage.selectCardToTransfer(firstCard);
        dashboardPage = openTransferPage.makeTransfer(String.valueOf(amount), secondCard);

        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCard);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCard);

        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }
    @Test
    void shouldNotTransferFromInvalidCard() {
        var firstCard = getFirstCardInfo();
        var invalidCard = getInvalidCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        int amount = 100;

        var openTransferPage = dashboardPage.selectCardToTransfer(firstCard);
        dashboardPage = openTransferPage.makeTransfer(String.valueOf(amount), invalidCard);
        openTransferPage.findErrorMessage("Ошибка");
        openTransferPage.cancelTransfer();

        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);

        assertEquals(firstCardBalance, actualBalanceFirstCard);
    }
    /*@Test
    void shouldNotTransferInvalidAmount() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        int amount = generateInvalidAmount(secondCardBalance);

        var openTransferPage = dashboardPage.selectCardToTransfer(firstCard);
        dashboardPage = openTransferPage.makeTransfer(String.valueOf(amount), secondCard);
        openTransferPage.findErrorMessage("Ошибка");
        openTransferPage.cancelTransfer();

        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCard);

        assertEquals(firstCardBalance, actualBalanceFirstCard);
        assertEquals(secondCardBalance, actualBalanceSecondCard);
    }*/
}

