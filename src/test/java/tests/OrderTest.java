package tests;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.time.Duration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import pageObjects.HomePage;
import pageObjects.OrderPage;


@RunWith(Parameterized.class)
public class OrderTest {
    private WebDriver driver;
    private HomePage homePage;
    private OrderPage orderPage;
    // Явное ожидание с таймаутом 10 секунд
    private WebDriverWait wait;

    // Параметры тестовых данных:
    private String orderButtonLocation;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private String deliveryDate;
    private String rentalPeriod;
    private String scooterColor;
    private String comment;

    public OrderTest(String orderButtonLocation, String firstName, String lastName, String address,
                     String metroStation, String phone, String deliveryDate, String rentalPeriod,
                     String scooterColor, String comment) {
        this.orderButtonLocation = orderButtonLocation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.deliveryDate = deliveryDate;
        this.rentalPeriod = rentalPeriod;
        this.scooterColor = scooterColor;
        this.comment = comment;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        // Формирование даты доставки на основе текущей даты с добавлением смещения
        LocalDate date1 = LocalDate.now().plusDays(3);
        LocalDate date2 = LocalDate.now().plusDays(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return Arrays.asList(new Object[][] {
                {"header", "Иван", "Иванов", "ул. Ленина, д. 1", "Лубянка", "89123456789",
                        date1.format(formatter), "сутки", "чёрный жемчуг", "Комментарий теста 1"},
                {"footer", "Петр", "Петров", "ул. Пушкина, д. 2", "Пушкинская", "89876543210",
                        date2.format(formatter), "двое суток", "серая безысходность", "Комментарий теста 2"}
        });
    }


    @Before
    public void setUp() {
        // Указываем путь к драйверу Chrome
        System.setProperty("webdriver.chrome.driver", "B:\\Downloads\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);

        // Инициализируем WebDriverWait с таймаутом 10 секунд
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Открываем главную страницу приложения
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Инициализируем объект главной страницы и принимаем куки
        homePage = new HomePage(driver);
        homePage.acceptCookies();
    }

    @Test
    public void testOrderFlow() {
        // Выбираем точку входа для оформления заказа (кнопка в шапке или в подвале)
        if (orderButtonLocation.equalsIgnoreCase("header")) {
            homePage.clickHeaderOrderButton();
        } else if (orderButtonLocation.equalsIgnoreCase("footer")) {
            homePage.clickFooterOrderButton();
        }

        // Инициализируем страницу оформления заказа
        orderPage = new OrderPage(driver);

        // Ожидаем появления первой формы (поля "Имя")
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='* Имя']")));

        // Заполняем первую форму заказа (личные данные)
        orderPage.fillOrderFormStepOne(firstName, lastName, address, metroStation, phone);
        orderPage.clickNextButton();

        // Ожидаем появления второй формы заказа (поля "Когда привезти самокат")
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='* Когда привезти самокат']")));

        // Заполняем вторую форму заказа (данные по заказу)
        orderPage.fillOrderFormStepTwo(deliveryDate, rentalPeriod, scooterColor, comment);
        orderPage.clickOrderButton();

        // Ожидаем появления сообщения об успешном оформлении заказа
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Хотите оформить заказ?')]")));

        // Проверяем, что сообщение об успешном заказе отображается
        assertTrue("Сообщение об успешном заказе не отображается", orderPage.isOrderSuccessMessageDisplayed());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}