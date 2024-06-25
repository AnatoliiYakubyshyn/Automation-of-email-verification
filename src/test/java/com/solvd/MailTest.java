package com.solvd;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import jakarta.mail.MessagingException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MailTest {

    @Test
    public void test() throws IOException, MessagingException {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.get("https://sendtestemail.com/");
        String topic = "SendTestEmail";
        int cntBefore = CheckMailService.cntOfTestMailsInbox(topic);
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/data.properties"));
        driver.findElement(By.id("ea2t")).sendKeys((String)properties.get("email"));
        driver.findElement(By.xpath("//form")).submit();
        int cntAfter = CheckMailService.cntOfTestMailsInbox(topic);
        Assert.assertEquals(cntAfter, cntBefore+1);
        driver.close();
    }
}
