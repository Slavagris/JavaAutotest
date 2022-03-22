package Homework6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest extends AbstractTest{

    @Test
    @DisplayName("Тест-кейс №1: Авторизация на сайте")
    public void testCase1() {
        // тестовые действия
        getWebDriver().get("https://ribomaniya.ru");
        new MainPage(getWebDriver()).pressLoginBtt();
        new LoginPage(getWebDriver())
                .setLogin("stendMerlin")
                .setPassword("D2EA_7abd")
                .pressInBtt();
        assertTrue(new MainPage(getWebDriver()).checkUser("Александр"));
        logger.info("Тест-кейс №1 пройден");   // выведется только если тест не упадет и условия удовлетворят assert
    }

    @Test
    @DisplayName("Тест-кейс №2: Авторизация на сайте (негативный тест)")
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
        logger.info("Тест-кейс №2 пройден");
    }



}