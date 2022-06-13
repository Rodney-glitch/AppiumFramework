package util;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TestBase {
    public static AndroidDriver<AndroidElement> driver;
    public static AppiumDriverLocalService service;

    @BeforeSuite(dependsOnMethods = {"killAllNodes"})
    public void startServer(){
        if (!Utilities.checkIfServerIsRunning(4723)){
            service = AppiumDriverLocalService.buildDefaultService();
            service.start();
        } else {
            System.out.println("Server port in use");
            //taskkill /F /IM node.exe
        }
    }

    @BeforeSuite
    public void killAllNodes() throws IOException, InterruptedException {
        Runtime.getRuntime().exec("killall -s KILL node");
        Thread.sleep(3000);
    }

    @BeforeSuite(dependsOnMethods = {"startServer"})
    public void startEmulator() throws IOException, InterruptedException {
        if (!Utilities.getProperty("deviceName").equals("real")){
            String osName = System.getProperty("os.name");
            if (osName.startsWith("Mac")){
                Runtime.getRuntime().exec(System.getProperty("user.dir")+"/src/test/resources/startEmulator.sh");
            } else if (osName.startsWith("Windows")){
                Runtime.getRuntime().exec(System.getProperty("user.dir")+"/src/test/resources/startEmulator.bat");
            }
            Thread.sleep(6000);
        }
    }

    @BeforeSuite(dependsOnMethods = {"startEmulator"})
    public void setUp() throws IOException {
        try {
            File appDir = new File("src/test/resources");
            File app = new File(appDir, "renmoney.apk");
            DesiredCapabilities cap = new DesiredCapabilities();
            String deviceName = Utilities.getProperty("deviceName");

            if (deviceName.equals("real")){
                cap.setCapability("deviceName", "Android Device");
                try {
                    String devices = Utilities.executeAdbCommand("adb devices");
                    devices = devices.replaceAll("List of devices attached", " ");
                    devices = devices.replaceAll("device", " ").trim();
                    String udid = devices.split(" ")[0];
                    cap.setCapability("udid", udid);
                } catch (IOException e) {
                    System.out.println("No devices found: " + e.toString());
                }
            } else {
                cap.setCapability("deviceName", deviceName);
            }
            cap.setCapability("platformName", "Android");
            cap.setCapability("automationName", "uiautomator2");
            cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());

            driver= new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"),cap);
            System.out.println("Application started");
        } catch(Exception exp) {
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
    }

    @AfterSuite
    public void tearDown() {
        driver.quit();
    }

    @AfterSuite(dependsOnMethods = {"tearDown"}, alwaysRun = true)
    public void stopServer(){
        service.stop();
    }
}
