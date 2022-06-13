package tests;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.*;

import static org.testng.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pageObjects.MainPage;
import util.TestBase;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainPageTest extends TestBase {
    private MainPage mainPage;

    JSONParser parser = new JSONParser();
    JSONObject config;

    @Parameters({"dataEnv"})
    public MainPageTest(String dataEnv) throws IOException, ParseException {
        config = (JSONObject) parser.parse(new FileReader("src/test/java/testData/" + dataEnv + ".json"));
    }

    @Test
    public void search() throws IOException, InterruptedException {
        JSONObject envs = (JSONObject) config.get("login");
        String valid_username = (String) envs.get("username");
        System.out.println("HI");
        System.out.println(valid_username);
    }

    public void toolsMenu() {
        new Actions(driver)
                .moveToElement(mainPage.toolsMenu)
                .perform();

        WebElement menuPopup = driver.findElement(By.cssSelector("div[data-test='menu-main-popup-content']"));
        assertTrue(menuPopup.isDisplayed());
    }
}
