package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MoneyTransferTest {

  @BeforeEach
  void setUp() {
    open("http://localhost:9999");
    val loginPage = new LoginPage();
    val authInfo = DataHelper.getAuthInfo();
    val verificationPage = loginPage.validLogin(authInfo);
    val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
    verificationPage.validVerify(verificationCode);
  }

        @Test
        void getTransferMoneyFromSecondToFirstCard() {
            val transferAmountSum = 1000;
            val dashboardPage = new DashboardPage();
            int actualAmountFirst = dashboardPage.getCardBalance(getFirstCardId()) + transferAmountSum;
            if (transferAmountSum > dashboardPage.getCardBalance(getFirstCardId())) {
                actualAmountFirst = dashboardPage.getCardBalance(getFirstCardId());
            }
            int actualAmountSecond = dashboardPage.getCardBalance(getSecondCardId()) - transferAmountSum;
            if (transferAmountSum > dashboardPage.getCardBalance(getSecondCardId())) {
                actualAmountSecond = dashboardPage.getCardBalance(getSecondCardId());
            }
            val dashboardPageAfterTransfer = dashboardPage
                    .transferTo(getFirstCardId())
                    .transferFrom(transferAmountSum, getSecondCard());
            val balanceFirst = dashboardPageAfterTransfer.getCardBalance(getFirstCardId());
            val balanceSecond = dashboardPageAfterTransfer.getCardBalance(getSecondCardId());
            assertEquals(actualAmountFirst, balanceFirst);
            assertEquals(actualAmountSecond, balanceSecond);
        }

        @Test
        void getTransferMoneyFromFirstToSecondCard() {
            val transferAmountSum = 1000;
            val dashboardPage = new DashboardPage();
            int actualAmountFirst = dashboardPage.getCardBalance(getFirstCardId()) - transferAmountSum;
            if (transferAmountSum > dashboardPage.getCardBalance(getFirstCardId())) {
                actualAmountFirst = dashboardPage.getCardBalance(getFirstCardId());
            }
            int actualAmountSecond = dashboardPage.getCardBalance(getSecondCardId()) + transferAmountSum;
            if (transferAmountSum > dashboardPage.getCardBalance(getSecondCardId())) {
                actualAmountSecond = dashboardPage.getCardBalance(getSecondCardId());
            }
            val dashboardPageAfterTransfer = dashboardPage
                    .transferTo(getSecondCardId())
                    .transferFrom(transferAmountSum, getFirstCard());
            val balanceFirst = dashboardPageAfterTransfer.getCardBalance(getFirstCardId());
            val balanceSecond = dashboardPageAfterTransfer.getCardBalance(getSecondCardId());
            assertEquals(actualAmountFirst, balanceFirst);
            assertEquals(actualAmountSecond, balanceSecond);
        }


    @Test
   void getTransferOutsideAccountLimit() {
      val transferAmountSum = 100000;
    val dashboardPage = new DashboardPage();
    int actualAmountFirst = dashboardPage.getCardBalance(getFirstCardId()) - transferAmountSum;
    int actualAmountSecond = dashboardPage.getCardBalance(getSecondCardId()) + transferAmountSum;
        val dashboardPageAfterTransfer = dashboardPage
                .transferTo(getSecondCardId())
                .transferFrom(transferAmountSum, getFirstCard());
        val balanceFirst = dashboardPageAfterTransfer.getCardBalance(getFirstCardId());
        val balanceSecond = dashboardPageAfterTransfer.getCardBalance(getSecondCardId());
    $("[data-test-id=\"error-notification\"] .notification__title").shouldBe(Condition.visible)
            .shouldHave(Condition.text("Ошибка"));
  }
}
