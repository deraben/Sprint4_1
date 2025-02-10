package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Класс, представляющий главную страницу приложения.
 * Содержит методы для взаимодействия с основными элементами страницы:
 * - принятие куки,
 * - выбор кнопок заказа (в шапке и внизу страницы),
 * - работу с блоком FAQ.
 */
public class HomePage {
    private WebDriver driver;

    // Локатор кнопки для принятия куки
    private By cookieAcceptButton = By.id("rcc-confirm-button");

    // Локатор кнопки «Заказать» в шапке страницы
    private By headerOrderButton = By.xpath("//div[contains(@class,'Header_Nav')]//button[text()='Заказать']");

    // Локатор кнопки «Заказать» внизу страницы (в секции «Как это работает»)
    private By footerOrderButton = By.xpath("//*[@id=\"root\"]/div/div/div[4]/div[2]/div[5]/button");

    /**
     * Конструктор класса HomePage.
     *
     * @param driver WebDriver для взаимодействия с браузером.
     */
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Возвращает кнопку вопроса FAQ по заданному индексу.
     *
     * @param index Индекс вопроса.
     * @return WebElement кнопки вопроса.
     */
    public WebElement getFAQQuestionButton(int index) {
        return driver.findElement(By.id("accordion__heading-" + index));
    }

    /**
     * Возвращает панель ответа FAQ по заданному индексу.
     *
     * @param index Индекс ответа.
     * @return WebElement панели с ответом.
     */
    public WebElement getFAQAnswerPanel(int index) {
        return driver.findElement(By.id("accordion__panel-" + index));
    }

    /**
     * Метод для принятия куки.
     * Находит кнопку принятия куки и кликает по ней.
     */
    public void acceptCookies() {
        driver.findElement(cookieAcceptButton).click();
    }

    /**
     * Метод для клика по кнопке «Заказать» в шапке страницы.
     */
    public void clickHeaderOrderButton() {
        driver.findElement(headerOrderButton).click();
    }

    /**
     * Метод для клика по кнопке «Заказать» внизу страницы.
     * Перед кликом осуществляется прокрутка страницы до видимости кнопки.
     */
    public void clickFooterOrderButton() {
        WebElement footerButton = driver.findElement(footerOrderButton);
        // Прокручиваем элемент в область видимости
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", footerButton);
        footerButton.click();
    }
}