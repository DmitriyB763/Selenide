import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.conditions.Visible;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;


public class CardDelivery {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setUp(){
        open("http://localhost:9999");
    }

    @Test
    public void shouldCorrectData(){
        Configuration.holdBrowserOpen=true;
        String planningDate = generateDate(4);
        $x("//*[@placeholder=\"Город\"]").setValue("Москва"); //ввод города
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);//удаление данных из поля
        $("[type=\"tel\"]").setValue(planningDate); //дата
        $("[name=\"name\"]").sendKeys("Иван Иванов"); // ввод имени и фамилии
        $("[name=\"phone\"]").sendKeys("+79171416421"); //ввод телефона
        $("[data-test-id=\"agreement\"]").click();  // Клик по чекбоксу
       // $x("//*[@data-test-id=\"agreement\"]").click();
        $("[class=\"button__text\"]").click(); //клик по кнопке забронировать
        $(withText("Встреча успешно забронирована на")).should(visible, Duration.ofSeconds(16));//подверждение о встречи + установка времени ожидания
        $("[class=\"notification__content\"]").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate),Duration.ofSeconds(11));

    }

    @Test
    public void shouldInvalidCity(){
        String planningDate = generateDate(4);
        $x("//*[@placeholder=\"Город\"]").setValue("Тольятти"); //ввод города
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[type=\"tel\"]").setValue(planningDate); //дата
        $("[name=\"name\"]").sendKeys("Иван Иванов"); // ввод имени и фамилии
        $("[name=\"phone\"]").sendKeys("+79171416421"); //ввод телефона
        $("[data-test-id=\"agreement\"]").click();  // Клик по чекбоксу
        // $x("//*[@data-test-id=\"agreement\"]").click();
        $("[class=\"button__text\"]").click(); //клик по кнопке забронировать
        $(byText("Доставка в выбранный город недоступна")).shouldHave(visible);
    }

    @Test
    public void shouldInvalidTel(){
        Configuration.holdBrowserOpen=true;
        String planningDate = generateDate(4);
        $x("//*[@data-test-id='city']//input").sendKeys("Москва"); //ввод города
        $x("//span[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//input").sendKeys(planningDate);
        $x("//input[@name='name']").setValue("Иван Петров");
        $x("//input[@name='phone']").setValue("+7927761513");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $x("//*[@data-test-id='phone']//span[@class='input__sub']").shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldInvalidTelWith8(){
        Configuration.holdBrowserOpen=true;
        String planningDate = generateDate(4);
        $x("//*[@data-test-id='city']//input").sendKeys("Москва"); //ввод города
        $x("//span[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//input").sendKeys(planningDate);
        $x("//input[@name='name']").setValue("Иван Петров");
        $x("//input[@name='phone']").setValue("89277615138");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $x("//*[@data-test-id='phone']//span[@class='input__sub']").shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldMissingTel(){
        Configuration.holdBrowserOpen=true;
        String planningDate = generateDate(4);
        $x("//*[@data-test-id='city']//input").sendKeys("Москва"); //ввод города
        $x("//span[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//input").sendKeys(planningDate);
        $x("//input[@name='name']").setValue("Иван Петров");
        $x("//input[@name='phone']").setValue("");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $x("//*[@data-test-id='phone']//span[@class='input__sub']").shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldInvalidName(){
        Configuration.holdBrowserOpen=true;
        String planningDate=generateDate(6);
        $x("//*[@data-test-id='city']//input").sendKeys("Москва"); //ввод города
        $x("//span[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//input").sendKeys(planningDate);
        $x("//input[@name='name']").setValue("Mick Vazovski");
        $x("//input[@name='phone']").setValue("+79277615138");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $x("//*[@data-test-id='name']//span[@class='input__sub']").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldInvalidNameWithLetter_ё_(){
        Configuration.holdBrowserOpen=true;
        String planningDate=generateDate(6);
        $x("//*[@data-test-id='city']//input").sendKeys("Москва"); //ввод города
        $x("//span[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//input").sendKeys(planningDate);
        $x("//input[@name='name']").setValue("Семён Калёнов");
        $x("//input[@name='phone']").setValue("+79277615813");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $(withText("Встреча успешно забронирована на")).should(visible, Duration.ofSeconds(16));//подверждение о встречи + установка времени ожидания
        $("[class=\"notification__content\"]").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate),Duration.ofSeconds(11));
    }

    @Test
    public void shouldMissingName(){
        Configuration.holdBrowserOpen=true;
        String planningDate=generateDate(6);
        $x("//*[@data-test-id='city']//input").sendKeys("Москва"); //ввод города
        $x("//span[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//input").sendKeys(planningDate);
        $x("//input[@name='name']").setValue("");
        $x("//input[@name='phone']").setValue("+79277615183");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $x("//*[@data-test-id='name']//span[@class='input__sub']").shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldInvalidDate(){ //исходя из требований встреча с курьеров возможна через 3 дня
        Configuration.holdBrowserOpen=true;
        String planningDate=generateDate(2);
        $x("//*[@data-test-id='city']//input").sendKeys("Москва"); //ввод города
        $x("//span[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//input").sendKeys(planningDate);
        $x("//input[@name='name']").setValue("Иван Иванов-Петров");
        $x("//input[@name='phone']").setValue("+79277615137");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $x("//*[@data-test-id='date']//span[@class='input__sub']").shouldHave(Condition.text("Заказ на выбранную дату невозможен"));
    }

    /*@Test
    public void shouldNotClickCheckBox(){
        Configuration.holdBrowserOpen=true;
        String planningDate=generateDate(4);
        $x("//*[@data-test-id='city']//input").sendKeys("Москва"); //ввод города
        $x("//span[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//input").sendKeys(planningDate);
        $x("//input[@name='name']").setValue("Иван Иванов-Петров");
        $x("//input[@name='phone']").setValue("+79277615135");
        $x("//*[@data-test-id='agreement']"); // не кликаем по чек боксу
        $x("//*[text()='Забронировать']").click();//нажимаем забронировать
        //$("[data-test-id='agreement'] [class='checkbox__text']").getCssValue("color");*/

    }

