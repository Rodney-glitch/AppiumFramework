package util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.*;
import java.net.ServerSocket;
import java.util.Properties;

public class Utilities extends TestBase{
    public static void scrollIntoText(String element){
        driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\""+element+"\"))");
    }

    public static boolean checkIfServerIsRunning(int port){
        boolean isServerRunning = false;
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.close();
        } catch (IOException e) {
            isServerRunning = true;
        } finally {
            serverSocket = null;
        }
        return isServerRunning;
    }

    public static String executeAdbCommand(String command) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
            builder.append(line);
        }
        return builder.toString();
    }

    public static String getProperty(String key) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(System.getProperty("user.dir")+"/global.properties");
        Properties properties = new Properties();
        properties.load(fileInputStream);

        return properties.getProperty(key);
    }

    public static void getScreenshot(String testName) throws IOException {
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"/screenshots/"+testName+".png"));
    }
}
