package com.spdrtr.nklcb.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class Crawling {
    public static WebDriver driver;

    public static void process(String url) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/minha/util/chromedriver");
        driver = new ChromeDriver();
        driver.get(url);    //브라우저에서 url로 이동한다.
        Thread.sleep(3000); //브라우저 로딩될때까지 잠시 기다린다.
    }

    /**
     * class 코드 서칭
     * @param elmName
     * @return class타입 인자를 받으면 그에 대한 코드를 모두 받아서 리스트로 반환
     */
    public static List<String> getData(String elmName) {
        List<String> list = new ArrayList<>();

        List<WebElement> elements = driver.findElements(By.className(elmName));
        for (WebElement element : elements) {
            list.add(element.getText());
        }
        return list;
    }

    /**
     * 버튼 서칭
     * @param elmName
     * @return 버튼 class명을 받아서 WebElement로 반환
     */
    public static WebElement findElement(String elmName) {
        WebElement element = driver.findElement(By.className(elmName));
        return element;
    }

    /**
     * 다수의 버튼 서칭
     * @param elmName
     * @return 버튼 class명을 받앗 WebElement 리스트로 반환
     */
    public static List<WebElement> findElements(String elmName) {
        List<WebElement> element = driver.findElements(By.className(elmName));
        return element;
    }

    public static void logIn() throws InterruptedException {
        findElement("signUpButton").click();

        findElement("css-wq6t17").sendKeys("100minha@naver.com");  //아이디 입력
        findElement("css-c61xw1").click();
        Thread.sleep(3000);
        findElement("css-wq6t17").sendKeys("rapael4785!");  //비밀번호 입력
        findElement("css-c61xw1").click();
    }

}
