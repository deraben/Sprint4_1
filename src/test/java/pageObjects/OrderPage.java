package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

/**
 * Класс для работы со страницей оформления заказа.
 * Содержит методы для заполнения форм и взаимодействия с элементами страницы.
 */
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

    // ******************** МОДАЛЬНОЕ ОКНО ПОДТВЕРЖДЕНИЯ ЗАКАЗА ********************
    // Кнопка "Да" в модальном окне с сообщением "Хотите оформить заказ?"
    private By confirmOrderButton = By.xpath("//*[@id=\"root\"]/div/div[2]/div[5]/div[2]/button[2]");

    /**
     * Конструктор страницы оформления заказа.
     */
    public OrderPage(WebDriver driver) {
        this.driver = driver;
        // Инициализируем WebDriverWait с таймаутом 10 секунд
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Метод для заполнения первой формы заказа (личные данные).
     */
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

    /**
     * Метод для нажатия кнопки "Далее" на первой форме.
     */
    public void clickNextButton() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    /**
     * Метод для заполнения второй формы заказа (данные заказа).
     */
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

    /**
     * Метод для выбора цвета самоката через чекбоксы.
     */
    public void selectScooterColor(String color) {
        if (color.equalsIgnoreCase("чёрный жемчуг")) {
            wait.until(ExpectedConditions.elementToBeClickable(blackCheckbox)).click();
        } else if (color.equalsIgnoreCase("серая безысходность")) {
            wait.until(ExpectedConditions.elementToBeClickable(greyCheckbox)).click();
        }
    }

    /**
     * Метод для нажатия кнопки "Заказать" на второй форме, а затем для подтверждения заказа в модальном окне.
     */
    public void clickOrderButton() {
        wait.until(ExpectedConditions.elementToBeClickable(orderButton)).click();
        // Ожидаем появления модального окна и нажимаем кнопку "Да"
        wait.until(ExpectedConditions.elementToBeClickable(confirmOrderButton)).click();
    }

    /**
     * Метод для проверки отображения сообщения об успешном оформлении заказа.
     */
    public boolean isOrderSuccessMessageDisplayed() {
        try {
            // Ожидаем появления элемента с текстом "Заказ оформлен"
            By successMessageLocator = By.xpath("//*[contains(text(),'Заказ оформлен')]");
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessageLocator));
            String messageText = successMessage.getText();
            // Проверяем, что текст содержит "Заказ оформлен" и "Номер заказа:" (номер заказа может быть любым)
            return messageText.contains("Заказ оформлен") && messageText.contains("Номер заказа:");
        } catch (Exception e) {
            return false;
        }
    }
}