package Homework7.Homework5;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
public class Homework5 {

    static WebDriver driver;
    Logger logger = LoggerFactory.getLogger("Test-Case's 1-4");
    JavascriptExecutor js = (JavascriptExecutor) driver;

    @BeforeAll
    static void initClass() {
        // Инициализация веб драйвера
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--incognito");
        options.addArguments("disable-popup-blocking");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
        // стенд медленный, таймауты большие…

    }

    @BeforeEach
    void initTest() {
        // обработка предусловий
        driver.get("https://ribomaniya.ru/?logout=yes");
    }

    @AfterAll
    static void finalClass() {
        // Закрытие драйвера
        driver.quit();
    }

    @Test
    @DisplayName("Тест-кейс №1: Авторизация сайте")
    public void testCase1() {
        // тестовые действия
        driver.get("https://ribomaniya.ru/cabinet/auth/?login=yes&backurl=%2F");
        driver.findElement(By.xpath("//input[@name='USER_LOGIN']")).clear();
        driver.findElement(By.xpath("//input[@name='USER_PASSWORD']")).clear();
        driver.findElement(By.xpath("//input[@name='USER_LOGIN']")).sendKeys("stendMerlin");
        driver.findElement(By.xpath("//input[@name='USER_PASSWORD']")).sendKeys("D2EA_7abd");
        driver.findElement(By.xpath("//button[@name='Login']")).click();
        String s = driver.findElement(By.xpath("//a[@class='nav-link text-truncate pt-0']")).getText();
        assertTrue(s.equals("Александр"));

        //результат теста
        logger.info("Тест-кейс №1 пройден");   // выведется только если тест не упадет и условия удовлетворят assert
    }

    @Test
    @DisplayName("Тест-кейс №2: Авторизация на сайте (негативный сценарий)")
    public void testCase2() {
        // негативный сценарий
        // тестовые действия
        driver.get("https://ribomaniya.ru/cabinet/auth/?login=yes&backurl=%2F");
        driver.findElement(By.xpath("//input[@name='USER_LOGIN']")).clear();
        driver.findElement(By.xpath("//input[@name='USER_PASSWORD']")).clear();
        driver.findElement(By.xpath("//input[@name='USER_LOGIN']")).sendKeys("stendMerlin");
        driver.findElement(By.xpath("//input[@name='USER_PASSWORD']")).sendKeys("password"); // невалидный пароль
        driver.findElement(By.xpath("//button[@name='Login']")).click();
        String s = driver.findElement(By.xpath("//font[@class='errortext']")).getText();
        assertTrue(s.equals("Неверный логин или пароль."));

        //результат теста
        logger.info("Тест-кейс №2 пройден");    //выведется только если тест не упадет и условия удовлетворят assert

    }

    @Test
    @DisplayName("Тест-кейс №3: Проверка правильности отображения номера телефона на главной странице ")
    public void testCase3() {
        // тестовые действия
        driver.get("https://ribomaniya.ru/");
        String s = driver.findElement(By.xpath("//div[@class='text-nowrap pe-2']/a")).getText();
        assertTrue(s.equals("8 (800) 350-32-12"));     // проверка значения по тесткейсу

        //результат теста
        logger.info("Тест-кейс №3 пройден");      // выведется только если тест не упадет и условия удовлетворят

    }

    @Test
    @DisplayName("Тест-кейс №4: Выбор товара и добавление его в корзину, удаление его из корзины как позиции")
    public void testCase4() throws InterruptedException {
        // тестовые действия
        driver.findElement(By.xpath("//div[@class='col-12']//a[@href='/gruza/']")).click();
        // перейти к грузам (их много, но все ведут в одно место)
        driver.findElement(By.xpath("//div[@class='row']//a[@href='/gruza/bombardy/']")).click();
        // перейти к бомбардам (ссылок много, но все ведут в одно место)
        driver.findElement(By.xpath("//a[@href='/gruza/bombardy/trout-master-ridge-sbiro/']")).click();
        // Выбрать бомбарду (их много, но все ведут в одно место)
        // Нужно сделать скролл, потому что следующий объект скрыт за рамкой окна
        js.executeScript("window.scrollTo(0,700)");
        Thread.sleep(1000);
        // явное безусловное ожидание, необходимое для корректной работы теста на медленных и слабых стендах.
        driver.findElement(By.xpath("//td//strong[text()='12g Floating']/../..")).click();
        // Выбрать модель 12 грамм
        // но для этого нужно снова сделать скрол, ибо элемент за рамкой окна.
        js.executeScript("window.scrollTo(0, 700)");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@name='tocart[add]']")).click();
        // Добавить в корзину
        driver.get("https://ribomaniya.ru/cabinet/basket/");
        // Переход в корзину прямым переходом к странице
        js.executeScript("window.scrollTo(0, 400)");
        Thread.sleep(1000);
        // снова вертикальный скролл…
        String s = driver.findElement(By.xpath("//a/span[text()='Trout Master Ridge Sbiro (12g Floating)']")).getText();
        // Получил текст товарной позиции
        assertTrue (s.equals("Trout Master Ridge Sbiro (12g Floating)"));
        //проверка факта наличия в корзине товара с таким названием
        // проверяю явно, хотя если бы этот товар не сушествовал - мы бы его не нашли
        driver.findElement(By.xpath("//span[@class='basket-item-actions-remove']")).click();
        Thread.sleep(2500);
        // так долго потому что стоимость корзины должан пересчитаться (взял с запасом)
        // Удаление товара из корзины (он там один)
        String s1 = driver.findElement(By.xpath("//div[@data-entity='basket-total-price']")).getText();
        // Получение итоговой стоимости корзины (там 0р.)
        assertTrue (s1.equals("0 р."));
        // проверка факта нулевой стоимости корзины

        //результат теста
        logger.info("Тест-кейс №4 пройден");      // выведется только если тест не упадет и условия удовлетворят
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/menudata.csv")
    @DisplayName("Тест-кейс №5: Проверка работы главных полей товарного каталога сайта")
    public void testCase5(String s_xpath, String s_title, String s_header) {
        // Тестовые действия для одного набора тестовых данных
        WebElement e = driver.findElement(By.xpath(s_xpath));
        String s = e.getText();
        e.click();
        String s1 = driver.findElement(By.xpath("//h1")).getText();
        logger.debug("Пункт меню — "+s);
        logger.debug("Заголовок целевой страницы — "+s1);
        assertTrue(s.equals(s_title));
        assertTrue(s1.equals(s_header));

        // результат теста
        logger.info("Тест-кейс №5 пройден ("+s_title+")");
    }

    @Test
    @DisplayName("Тест-кейс №6: Проверка работы формы ввода личных данных")
    public void testCase6() throws InterruptedException {
        // тестовые действия
        driver.get("https://ribomaniya.ru/cabinet/auth/?login=yes&backurl=%2F");
        driver.findElement(By.xpath("//input[@name='USER_LOGIN']")).clear();
        driver.findElement(By.xpath("//input[@name='USER_PASSWORD']")).clear();
        driver.findElement(By.xpath("//input[@name='USER_LOGIN']")).sendKeys("stendMerlin");
        driver.findElement(By.xpath("//input[@name='USER_PASSWORD']")).sendKeys("D2EA_7abd");
        driver.findElement(By.xpath("//button[@name='Login']")).click();
        String s = driver.findElement(By.xpath("//a[@class='nav-link text-truncate pt-0']")).getText();
        assertTrue(s.equals("Александр")); // факт того, что успешно авторизовались (шаг 3)
        driver.findElement(By.xpath("//a[@href='/cabinet/']//i")).click();
        driver.findElement(By.xpath("//a[@href='/cabinet/personal/']")).click();
        // ввод новых пользовательских данных
        driver.findElement(By.xpath("//input[@name='NAME']")).clear();
        driver.findElement(By.xpath("//input[@name='NAME']")).sendKeys("Иван");
        driver.findElement(By.xpath("//input[@name='SECOND_NAME']")).clear();
        driver.findElement(By.xpath("//input[@name='SECOND_NAME']")).sendKeys("Иванович");
        driver.findElement(By.xpath("//input[@name='LAST_NAME']")).clear();
        driver.findElement(By.xpath("//input[@name='LAST_NAME']")).sendKeys("Иванов");
        driver.findElement(By.xpath("//input[@name='NEW_PASSWORD']")).clear();
        // сохранение
        js.executeScript("window.scrollTo(0, 700)");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@name='save']")).click();
        // проверка
        driver.get("https://ribomaniya.ru/cabinet/personal/?"); // обновление страницы
        // можно было использовать navigate.refresh() но в этом случае на этом сайте нужна отправка формы

        /**
         в результате теста обнаружен дефект при котором в результате записи новых данных
         имя пользователя в шапке страницы не обновляется, данные о дефекте переданы владельцу сайта
         обновление страницы необходимо для того, чтобы обойти эту проблему
         */

        s = driver.findElement(By.xpath("//a[@class='nav-link text-truncate pt-0']")).getText();
        assertTrue(s.equals("Иван")); // факт того, что новый пользователь сохранился.

        // вернем назад тестового пользователя (чтобы потом этот же тест сработал)
        driver.findElement(By.xpath("//input[@name='NAME']")).clear();
        driver.findElement(By.xpath("//input[@name='NAME']")).sendKeys("Александр");
        driver.findElement(By.xpath("//input[@name='SECOND_NAME']")).clear();
        driver.findElement(By.xpath("//input[@name='SECOND_NAME']")).sendKeys("Николаевич");
        driver.findElement(By.xpath("//input[@name='LAST_NAME']")).clear();
        driver.findElement(By.xpath("//input[@name='LAST_NAME']")).sendKeys("Нескажу");
        driver.findElement(By.xpath("//input[@name='NEW_PASSWORD']")).clear();
        // сохранение
        js.executeScript("window.scrollTo(0, 700)");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@name='save']")).click();
        driver.get("https://ribomaniya.ru/cabinet/personal/?"); // обновление страницы
        s = driver.findElement(By.xpath("//a[@class='nav-link text-truncate pt-0']")).getText();
        assertTrue(s.equals("Александр")); // факт того, что параметры тестового пользователя восстановлены.

        // результат теста
        logger.info("Тест-кейс №6 пройден");

    }


}
