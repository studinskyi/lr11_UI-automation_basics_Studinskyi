package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.WebAssert;
import utils.WebUtils;
import utils.WebWait;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UpdateIssuePage {
    public String issueKey = "";

    private WebDriver driver;
    public WebUtils wUtil;
    public WebWait wWait;
    public WebAssert webAssert;
    private CreateIssuePage createIssuePage;


    public UpdateIssuePage(WebDriver driver) {
        this.driver = driver;
        this.wUtil = new WebUtils(this.driver);
        this.wWait = new WebWait(this.driver);
        this.webAssert = new WebAssert(wUtil);
        this.createIssuePage = new CreateIssuePage(this.driver);
    }

    public void updateReporterInIssue(String newReporterLogin, String newReporterName) {
        // 1. create issue
        createIssuePage.createIssue();
        issueKey = createIssuePage.issueKey;
        // 2. обновление поля Reporter в Issue
        setNewReporterInIssue(newReporterLogin);

        // ожидание появления на странице текста с именем нового пользователя-репортера
        wWait.waitForTextPresent(newReporterName);
    }

    public void setNewReporterInIssue(String newReporterLogin) {
        // обновление поля Reporter в Issue
        driver.get("http://soft.it-hillel.com.ua:8080/browse/" + issueKey);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wUtil.findByXpath("//*[@id='issue_summary_reporter_studinskyi']").click();
        //driver.findElement(By.xpath("//*[@id='issue_summary_reporter_studinskyi']")).click();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        wUtil.findByXpath("//*[@id='reporter-field']").sendKeys(Keys.DELETE);
        //driver.findElement(By.xpath("//*[@id='reporter-field']")).sendKeys(Keys.DELETE);
        wUtil.findByXpath("//*[@id='reporter-field']").sendKeys(newReporterLogin, Keys.ENTER);
        //driver.findElement(By.xpath("//*[@id='reporter-field']")).sendKeys("a.a.piluck", Keys.ENTER);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteIssue() {
        // deleteting new test issue
        createIssuePage.deleteIssue();
        //        try {
        //            Thread.sleep(1000);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
    }

    public void addCommentToIssue(String textNewComment) {
        // 1. create issue
        createIssuePage.createIssue();
        issueKey = createIssuePage.issueKey;
        // 2. add comment to issue
        driver.get("http://soft.it-hillel.com.ua:8080/browse/" + issueKey);
        WebElement buttonComment = wUtil.findByXpath("//*[@id='comment-issue']/span[1]");
        //WebElement comment = driver.findElement(By.xpath("//*[@id='comment-issue']/span[1]"));
        buttonComment.click();
        WebElement commentTextArea = wWait.waitWebElement("//*[@id='comment']", 10);
        commentTextArea.sendKeys(textNewComment);
        //addComment.sendKeys(textNewComment, Keys.CONTROL, Keys.ENTER);
        WebElement buttonAdd = wWait.waitWebElement("//*[@id='issue-comment-add-submit']", 10);
        buttonAdd.click();

        // ожидание появления на странице текста textNewComment после успешного добавления комментария в issue
        wWait.waitForTextPresent(textNewComment);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updatePriorityInIssue(String newTextPriority) {
        // 1. create issue
        createIssuePage.createIssue();
        issueKey = createIssuePage.issueKey;
        // 2. update priority
        driver.get("http://soft.it-hillel.com.ua:8080/browse/" + issueKey);
        WebElement fieldPriority = wWait.waitWebElement("//*[@id='priority-val']", 10);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fieldPriority.click();
        WebElement priorityChange = wWait.waitWebElement("//*[@id='priority-field']", 10);
        priorityChange.clear();
        priorityChange.sendKeys(newTextPriority, Keys.ENTER, Keys.ENTER);
        //priorityChange.sendKeys("Lowest", Keys.ENTER, Keys.ENTER);

        // ожидание появления на странице текста textNewComment после успешного добавления комментария в issue
        wWait.waitForTextPresent(newTextPriority);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getIssueKey() {
        createIssuePage.getIssueKeyOfNewIssue();
        //        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //        issueKey = driver
        //                .findElement(By.xpath("//*[@id='aui-flag-container']/div/div/a"))
        //                .getAttribute("data-issue-key");
        //        System.out.println(issueKey);
    }


    public void updateIssueTitle() {
        driver.get("http://soft.it-hillel.com.ua:8080/browse/" + issueKey);
        WebElement changeIssueTitle = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='summary-val']")));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        changeIssueTitle.click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // TODO add assert text bar
        WebElement changeIssueSummary = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='summary']")));
        changeIssueSummary.clear();

        changeIssueSummary.sendKeys(" This title was changed via WebDriver", Keys.ENTER);

    }


    //    public String getCurrentDateTimeString() {
    //        Date d = new Date();
    //        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    //        //FileManager.executedOperations.put(formatDate.format(d), FileManager.currentCommand);
    //        return formatDate.format(d);
    //    }


}
