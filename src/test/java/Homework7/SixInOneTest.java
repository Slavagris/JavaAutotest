package Homework7;

import java.io.IOException;
import java.util.*;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import static org.junit.jupiter.api.Assertions.*;

public class SixInOneTest extends AbstractTest {
    JavascriptExecutor js = (JavascriptExecutor) getWebDriver();

    // процедура логирования логов браузера для этого класса
    void saveBrowserLogs() {
        LogEntries browserLogs = getWebDriver().manage().logs().get(LogType.BROWSER);
        List<LogEntry> allLogRows = browserLogs.getAll();
        // здесь сохраняется лог тогда и только тогда, когда есть что сохранять (набор логов непустой))
        if (allLogRows.size() > 0) {
            allLogRows.forEach(logEntry -> {
                logger.debug("BROWSERLOGS: "+logEntry.getMessage());
            });
        }
    }

    @Test
    @DisplayName("Тест-кейс №1: Авторизация на сайте")
    @Link("ссылка на tms")
    @Severity(SeverityLevel.BLOCKER)
    @Epic("Тестирование интерфейса сайта")
    @Feature("Авторизация на сайте")
    @Story("Вход на сайт по имени пользователя и паролю")
    public void testCase1() throws IOException {
        // тестовые действия
        getWebDriver().get("https://ribomaniya.ru");
        new MainPage(getWebDriver()).pressLoginBtt();
        new LoginPage(getWebDriver())
                .setLogin("stendMerlin")
                .setPassword("D2EA_7abd")
                .pressInBtt();
        assertTrue(new MainPage(getWebDriver()).checkUser("Александр"));
        // сохранение логов браузера
        saveBrowserLogs();
        // сохранение скриншота с именем пользователя
        String fileName =  "test-case1-" + System.currentTimeMillis() + ".png";
        CommonUtils.makeScreenshot(getWebDriver(),fileName);
        // создался скриншот без вложения в отчет
        // логирование об успешности теста
        logger.info("Тест-кейс №1 пройден");   // выведется только если тест не упадет и условия удовлетворят assert
    }

    // Аннотации junit
    @Test
    @DisplayName("Тест-кейс №2: Авторизация на сайте (негативный тест)")
    // Аннотации allure
    @Link("https://ribomaniya.ru")
    @Severity(SeverityLevel.BLOCKER)
    @Epic("Тестирование интерфейса сайта")
    @Feature("Авторизация на сайте")
    @Story("Ошибка при неверном пользователе или пароле")
    public void testCase2() {
        getWebDriver().get("https://ribomaniya.ru");
        new MainPage(getWebDriver()).pressLoginBtt();
        assertTrue(
                new LoginPage(getWebDriver())
                        .setLogin("stendMerlin")
                        .setPassword("password")        // неверный пароль
                        .pressInBtt()
                        .isError()
        );
        saveBrowserLogs();
        logger.info("Тест-кейс №2 пройден");
    }

    @Test
    @DisplayName("Тест-кейс №3: Проверка правильности отображения номера телефона на главной странице")
    // Аннотации allure
    @Link("https://ribomaniya.ru")
    @Severity(SeverityLevel.TRIVIAL)
    @Epic("Тестирование интерфейса сайта")
    @Feature("Контактные данные")
    @Story("Поиск пользователем телефонного номера в шапке")
    public void testCase3() {
        getWebDriver().get("https://ribomaniya.ru");
        assertTrue(
                new MainPage(getWebDriver()).getPhoneNum().equals("8 (800) 350-32-12")
        );
        logger.info("Тест-кейс №3 пройден");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/menudata.csv")
    @DisplayName("Тест-кейс №5: Проверка работы главных полей товарного каталога сайта")
    // Аннотации allure
    @Link("https://ribomaniya.ru")
    @Severity(SeverityLevel.NORMAL)
    @Epic("Тестирование интерфейса сайта")
    @Feature("Навигация по сайту с помощью меню разделов")
    @Story("Переход пользователя м/у товарными категориями")
    public void testCase5(String s_xpath, String s_title, String s_header) throws InterruptedException {
        // Тестовые действия для одного набора тестовых данных
        getWebDriver().get("https://ribomaniya.ru");
        MainPage e = new MainPage(getWebDriver());
        assertTrue(e.checkMenuItemTitle(s_xpath,s_title)); // проверка названия до нажатия
        e.pressMenuItem(s_xpath);
        assertTrue(e.checkMenuItemTitle(s_xpath,s_title)); // проверка названия после нажатия
        assertTrue(e.checkContentHeader(s_header));
        saveBrowserLogs();
        logger.info("Тест-кейс №5 пройден ("+s_title+")");
    }

    @Test
    @DisplayName("Тест-кейс №4: Выбор товара и добавление его в корзину, удаление его из корзины как позиции")
    // Аннотации allure
    @Link("https://ribomaniya.ru")
    @Severity(SeverityLevel.CRITICAL)
    @Epic("Тестирвоание конктреной пользовательской истории")
    @Feature("Корзина")
    @Story("Выбор товара, добавление его в корзину, очистка корзины")
    public void testCase4() throws InterruptedException {
        // идет работа только с одним конкретным товаром, который принят в качестве тестового
        new CommodPage(getWebDriver())
                .pressMainMenuItem()
                .pressCategoryItem()
                .pressCommodItem()
                .pressCommodVersionItem()
                .pressAddCommodToBacket()
                .pressBacketBtt();
        assertTrue(new BacketPage(getWebDriver()).getCommodText().equals("Trout Master Ridge Sbiro (12g Floating)"));
        String s = new BacketPage(getWebDriver())
                .pressDelCommod()
                .getPriceText();
        assertTrue(s.equals("0 р."));

        //результат теста
        saveBrowserLogs();
        logger.info("Тест-кейс №4 пройден");      // выведется только если тест не упадет и условия удовлетворят
    }

    @Test
    @DisplayName("Тест-кейс №6: Проверка работы формы ввода личных данных")
    // Аннотации allure
    @Link("https://ribomaniya.ru")
    @Severity(SeverityLevel.CRITICAL)
    @Epic("Тестирвоание конктреной пользовательской истории")
    @Feature("Окно ввода пользовательских данных в ЛК")
    @Story("Редактирование данных в ЛК")
    public void testCase6()  throws InterruptedException {
        // тестовые действия
        getWebDriver().get("https://ribomaniya.ru");
        // new WebDriverWait(getWebDriver(), 120).until(ExpectedConditions.urlContains(".ru")); // /cabinet/
        // авторизация
        new MainPage(getWebDriver()).pressLoginBtt();
        new LoginPage(getWebDriver())
                .setLogin("stendMerlin")
                .setPassword("D2EA_7abd")
                .pressInBtt();
        assertTrue(new MainPage(getWebDriver()).checkUser("Александр"));
        logger.debug(" - тесткейс № 6 : авторизация успешна");
        // переход в личный кабинет
        new MainPage(getWebDriver()).pressCabinetBtt();
        // переход в персональным данным
        new CabinetPage(getWebDriver())
                .pressPersonlBtt()
                .setNameField("Иван")
                .setSecondNameField("Иванович")
                .setLastNameField("Иванов")
                .clearNewPasswordField()
                .pressApplyBtt();

        // в результате теста обнаружен дефект при котором в результате записи новых данных
        // имя пользователя в шапке страницы не обновляется, данные о дефекте переданы владельцу сайта
        // обновление страницы необходимо для того, чтобы обойти эту проблему

        // проверка
        getWebDriver().get("https://ribomaniya.ru/cabinet/personal/?"); // обновление страницы
        assertTrue(new MainPage(getWebDriver()).checkUser("Иван"));
        logger.debug(" - тесткейс № 6 : данные пользователя сохранены");
        // возврат к справочным данным тестового пользователя
        new CabinetPage(getWebDriver())
                .pressPersonlBtt()
                .setNameField("Александр")
                .setSecondNameField("Александрович")
                .setLastNameField("Александров")
                .clearNewPasswordField()
                .pressApplyBtt();
        getWebDriver().get("https://ribomaniya.ru/cabinet/personal/?"); // обновление страницы
        assertTrue(new MainPage(getWebDriver()).checkUser("Александр"));
        logger.debug(" - тесткейс № 6 : данные тостового пользователя восстановлены");
        saveBrowserLogs();
        logger.info("Тест-кейс №6 пройден");
    }

}