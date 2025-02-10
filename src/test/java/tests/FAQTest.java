package tests;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

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


@RunWith(Parameterized.class)
public class FAQTest {
    private WebDriver driver;
    private HomePage homePage;
    private WebDriverWait wait;

    // Параметры теста: индекс вопроса и ожидаемый текст ответа
    private int faqIndex;
    private String expectedAnswer;


    public FAQTest(int faqIndex, String expectedAnswer) {
        this.faqIndex = faqIndex;
        this.expectedAnswer = expectedAnswer;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                {0, "Сутки — 400 рублей. Оплата курьеру — наличными или картой."},
                {1, "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."},
                {2, "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."},
                {3, "Только начиная с завтрашнего дня. Но скоро станем расторопнее."},
                {4, "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."},
                {5, "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."},
                {6, "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."},
                {7, "Да, обязательно. Всем самокатов! И Москве, и Московской области."}
        });
    }


    @Before
    public void setUp() {
        // Указываем путь к chromedriver
        System.setProperty("webdriver.chrome.driver", "B:\\Downloads\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);

        // Инициализируем WebDriverWait с таймаутом 10 секунд
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Открываем главную страницу приложения
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Создаем объект главной страницы и принимаем куки
        homePage = new HomePage(driver);
        homePage.acceptCookies();
    }


    @Test
    public void testFAQAnswerVisibility() {
        // Кликаем по кнопке вопроса с заданным индексом
        homePage.getFAQQuestionButton(faqIndex).click();

        // Явное ожидание появления панели с ответом по заданному индексу
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accordion__panel-" + faqIndex)));

        // Получаем фактический текст ответа и удаляем лишние пробелы
        String actualAnswer = homePage.getFAQAnswerPanel(faqIndex).getText().trim();
        // Сравниваем полученный текст с ожидаемым
        assertEquals("Текст ответа не совпадает для вопроса с индексом " + faqIndex,
                expectedAnswer, actualAnswer);
    }


    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}