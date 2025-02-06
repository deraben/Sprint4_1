package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;


public class OrderPage {
    private WebDriver driver;
    // Явное ожидание с таймаутом 10 секунд
    private WebDriverWait wait;

    // ******************** ПЕРВАЯ ФОРМА (ЛИЧНЫЕ ДАННЫЕ) ********************

    // Поле "Имя"
    private By firstNameField = By.xpath("//input[@placeholder='* Имя']");

    // Поле "Фамилия"
    private By lastNameField = By.xpath("//input[@placeholder='* Фамилия']");

    // Поле "Адрес: куда привезти заказ"
    private By addressField = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");

    // Выпадающий список "Станция метро"
    private By metroStationInput = By.xpath("//input[@placeholder='* Станция метро']");

    // Поле "Телефон: на него позвонит курьер"
    private By phoneField = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");

    // Кнопка "Далее"
    private By nextButton = By.xpath("//button[text()='Далее']");

    // ******************** ВТОРАЯ ФОРМА (ДАННЫЕ ЗАКАЗА) ********************

    // Поле "Когда привезти самокат"
    private By deliveryDateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");

    // Выпадающий список "Срок аренды"
    private By rentalPeriodDropdown = By.className("Dropdown-control");

    // Локаторы для двух вариантов цвета
    private By blackCheckbox = By.id("black");
    private By greyCheckbox = By.id("grey");

    // Поле "Комментарий для курьера"
    private By commentField = By.xpath("//input[@placeholder='Комментарий для курьера']");

    // Кнопка "Заказать" (на второй форме)
    private By orderButton = By.xpath("//*[@id=\"root\"]/div/div[2]/div[3]/button[2]");


    public OrderPage(WebDriver driver) {
        this.driver = driver;
        // Инициализируем WebDriverWait с таймаутом 10 секунд
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    public void fillOrderFormStepOne(String firstName, String lastName, String address, String metroStation, String phone) {
        // Вводим имя
        driver.findElement(firstNameField).sendKeys(firstName);
        // Вводим фамилию
        driver.findElement(lastNameField).sendKeys(lastName);
        // Вводим адрес доставки
        driver.findElement(addressField).sendKeys(address);

        // Работа с выпадающим списком "Станция метро"
        WebElement metroInput = driver.findElement(metroStationInput);
        metroInput.click(); // Открываем выпадающий список

        // Ожидаем, что поле для ввода будет кликабельно

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        wait.until(ExpectedConditions.elementToBeClickable(metroStationInput));

        // Вводим название станции метро
        metroInput.sendKeys(metroStation);
        // Выбираем нужный вариант через нажатие стрелки вниз и Enter
        metroInput.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);

        // Вводим номер телефона
        driver.findElement(phoneField).sendKeys(phone);
    }


    public void clickNextButton() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }


    public void fillOrderFormStepTwo(String deliveryDate, String rentalPeriod, String scooterColor, String comment) {
        // Вводим дату доставки
        WebElement deliveryField = driver.findElement(deliveryDateField);
        deliveryField.sendKeys(deliveryDate);
        // Нажимаем ESCAPE, чтобы скрыть всплывающее окно календаря
        deliveryField.sendKeys(Keys.ESCAPE);

        // Выбираем срок аренды: кликаем по выпадающему списку
        wait.until(ExpectedConditions.elementToBeClickable(rentalPeriodDropdown)).click();
        // Определяем локатор для нужного варианта срока аренды по его тексту
        By rentalPeriodOption = By.xpath("//div[@class='Dropdown-menu']/div[text()='" + rentalPeriod + "']");
        // Ждём, пока нужный вариант станет кликабелен, и кликаем по нему
        wait.until(ExpectedConditions.elementToBeClickable(rentalPeriodOption)).click();

        // Выбор цвета самоката
        selectScooterColor(scooterColor);
        // Ввод комментария для курьера
        driver.findElement(commentField).sendKeys(comment);
    }


    public void selectScooterColor(String color) {
        if (color.equalsIgnoreCase("чёрный жемчуг")) {
            wait.until(ExpectedConditions.elementToBeClickable(blackCheckbox)).click();
        } else if (color.equalsIgnoreCase("серая безысходность")) {
            wait.until(ExpectedConditions.elementToBeClickable(greyCheckbox)).click();
        }
    }


    public void clickOrderButton() {
        wait.until(ExpectedConditions.elementToBeClickable(orderButton)).click();
    }


    public boolean isOrderSuccessMessageDisplayed() {
        try {
            // Ожидаем появления элемента с текстом, подтверждающим оформление заказа
            By successMessageLocator = By.xpath("//*[contains(text(),'Хотите оформить заказ?')]");
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessageLocator));
            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}