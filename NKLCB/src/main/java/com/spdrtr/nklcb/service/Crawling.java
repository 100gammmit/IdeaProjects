package com.spdrtr.nklcb.service;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

public class Crawling {
    private static WebDriver driver;

    public static void process(String url) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/minha/util/chromedriver");    // Window 운영체제에선 경로 끝에 .exe 붙이기
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        // options.addArguments("headless"); // test해봐야됨

        driver = new ChromeDriver(options);
        driver.get(url);    //브라우저에서 url로 이동한다.
        Thread.sleep(3000); //브라우저 로딩될때까지 잠시 기다린다.
    }

    /**
     * class 코드 서칭
     * @param elmName
     * @return class타입 인자를 받으면 그에 대한 getText()를 모두 받아서 리스트로 반환
     */
    public static List<String> getTextsByElement(String elmName) {
        List<String> list = new ArrayList<>();

        List<WebElement> elements = driver.findElements(By.className(elmName));
        for (WebElement element : elements) {
            list.add(element.getText());
        }
        return list;
    }

    /**
     * findElement
     * @param elmName
     * @return WebDriver.Element(By.className) 수행 함수
     */
    public static WebElement findElement(String elmName) {
        WebElement element = driver.findElement(By.className(elmName));
        return element;
    }

    /**
     * findElements
     * @param elmName
     * @return WebDriver.Elements 수행 함수
     */
    public static List<WebElement> findElements(String elmName) {
        List<WebElement> element = driver.findElements(By.className(elmName));
        return element;
    }

    /**
     * 로그인 진행 함수
     */
    public static void logIn() throws InterruptedException {
        findElement("signUpButton").click();

        findElement("css-wq6t17").sendKeys("100minha@naver.com");  //아이디 입력
        findElement("css-c61xw1").click();
        Thread.sleep(3000);
        findElement("css-wq6t17").sendKeys("rapael4785!");  //비밀번호 입력
        findElement("css-c61xw1").click();

        System.out.println("===============================");
        System.out.println("로그인 완료");
        System.out.println("===============================");
    }

    public static void scrollTo(WebElement scrollElm){
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView()", scrollElm);
    }
    public static void scrollBottom() {
        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }
    public static void scrollTop(){
        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, 0)");
    }
    public static void quitDriver() { driver.quit(); }
}
