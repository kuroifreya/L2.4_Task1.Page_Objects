package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private final SelenideElement header = $(byText("Пополнение карты"));
    private final SelenideElement amountField = $("[data-test-id=amount] input");
    private final SelenideElement fromField = $("[data-test-id=from] input");
    private final SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private final SelenideElement errorMessage = $("[data-test-id=error-notification]");
    private final SelenideElement cancelButton = $("[data-test-id=action-cancel]");

    public TransferPage() {
        header.shouldBe(visible);
    }

    public DashboardPage makeTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        transfer(amountToTransfer, cardInfo);
        return new DashboardPage();
    }

    public void transfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        amountField.setValue(amountToTransfer);
        fromField.setValue(cardInfo.getCardNumber());
        transferButton.click();
    }

    public void findErrorMessage(String expectedText) {
        errorMessage.shouldHave(text(expectedText), Duration.ofSeconds(15)).shouldBe(visible);
    }

    public DashboardPage cancelTransfer (){
        cancelButton.click();
        return new DashboardPage();
    }
}
