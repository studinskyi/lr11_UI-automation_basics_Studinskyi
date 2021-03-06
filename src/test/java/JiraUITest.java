import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.CreateIssuePage;
import pages.LoginPage;
import pages.LogoutPage;
import pages.UpdateIssuePage;
import utils.WebDate;

import static org.testng.Assert.assertEquals;

public class JiraUITest {

    private WebDriver driver;
    private LoginPage loginPage;
    private LogoutPage logoutPage;
    private CreateIssuePage createIssuePage;
    private UpdateIssuePage updateIssuePage;


    private final static String loginUser = "studinskyi";
    private final static String passwordUser = "dima_st";
    private final static String newReporterLogin = "a.a.piluck";
    private final static String newReporterName = "Artur Pilyuk";

    @BeforeTest(groups = {"login", "issue", "update"})
    public void beforeStartTests() {
        //driver = new ChromeDriver(); // new ChromeDriver() или new FirefoxDriver();
        initWebDriver();
        this.loginPage = new LoginPage(driver);
        this.logoutPage = new LogoutPage(driver);
        this.createIssuePage = new CreateIssuePage(driver);
        this.updateIssuePage = new UpdateIssuePage(driver);
    }

    public void initWebDriver() {
        // System.setProperty("webdriver.chrome.driver", "geckodriver.exe"); // для случая с Firefox
        this.driver = new ChromeDriver(); // new ChromeDriver() или new FirefoxDriver();
    }

    @Test(groups = {"login", "issue", "update"})
    public void loginTest() {
        // 1. log in to jira
        loginPage.login(loginUser, passwordUser);
        // 2.  проверка Assert-тов
        System.out.println("wUtil.elementIsEnabled(\"//*[@id='header-details-user-fullname']\", 10) = " + loginPage.wWait.isElementEnabled("//*[@id='header-details-user-fullname']", 10));
        loginPage.webAssert.assertIsElementEnabled("//*[@id='header-details-user-fullname']", 10);
        loginPage.webAssert.assertTitleContainsText("System Dashboard"); //проверка наличия в заголовке подстроки
        loginPage.webAssert.assertPageContainsText("Welcome to JIRA");

        System.out.println("loginTest " + WebDate.getCurrentDateTimeString());
        System.out.println("loginTest - thread id: " + Thread.currentThread().getId());
    }

    @Test(groups = "login", dependsOnMethods = "loginTest")
    public void logoutTest() {
        // 1. log in to jira
        logoutPage.logout();
        // 2.  проверка Assert-тов
        loginPage.webAssert.assertTitleContainsText("Logout"); //проверка наличия в заголовке подстроки
        loginPage.webAssert.assertPageContainsText("You are now logged out");

        System.out.println("logoutTest " + WebDate.getCurrentDateTimeString());
        System.out.println("logoutTest - thread id: " + Thread.currentThread().getId());
    }

    //    @TestCaseId("TMS-1")
//    @Features("Issue")
//    @Stories({"CRUDIssue"})
    @Test(groups = "issue", dependsOnMethods = "loginTest")
    public void createIssue() {
        // 1. create new testing issue
        createIssuePage.createIssue();
        // 2.  проверка Assert-тов
        System.out.println("wUtil.elementIsEnabled(\"//*[@id='key-val']\", 10) = " + loginPage.wWait.isElementEnabled("//*[@id='key-val']", 10));
        createIssuePage.webAssert.assertIsElementEnabled("//*[@id='key-val']", 10);
        createIssuePage.webAssert.assertIsElementEnabled("//*[@data-issue-key='" + createIssuePage.issueKey + "']", 10);
        createIssuePage.webAssert.assertTitleContainsText(updateIssuePage.issueKey); //проверка наличия в заголовке подстроки ключа issueKey "QAAUT-1676"
        createIssuePage.webAssert.assertTitleContainsText(createIssuePage.textSummaryIssue);
        createIssuePage.webAssert.assertTextPresent("//*[@id='summary-val']", 10, createIssuePage.textSummaryIssue); // наличие на странице строки

        // 3. deleteting new test issue
        createIssuePage.deleteIssue();

        System.out.println("createIssue " + WebDate.getCurrentDateTimeString());
        System.out.println("createIssue - thread id: " + Thread.currentThread().getId());
    }

    @Test(groups = "issue", dependsOnMethods = "loginTest")
    public void deleteIssue() {
        // 1. create new testing issue
        createIssuePage.createIssue();

        // 3. deleteting new test issue
        createIssuePage.deleteIssue();

        // 2.  проверка Assert-тов
//        createIssuePage.webAssert.assertIsElementEnabled("//*[@id='key-val']", 10);
//        createIssuePage.webAssert.assertIsElementEnabled("//*[@data-issue-key='" + createIssuePage.issueKey + "']", 10);
//        createIssuePage.webAssert.assertTitleContainsText(updateIssuePage.issueKey); //проверка наличия в заголовке подстроки ключа issueKey "QAAUT-1676"
//        createIssuePage.webAssert.assertTitleContainsText(createIssuePage.textSummaryIssue);
//        createIssuePage.webAssert.assertTextPresent("//*[@id='summary-val']", 10, createIssuePage.textSummaryIssue); // наличие на странице строки
        updateIssuePage.webAssert.assertPageContainsText("has been deleted");

        //

        System.out.println("createIssue " + WebDate.getCurrentDateTimeString());
        System.out.println("createIssue - thread id: " + Thread.currentThread().getId());
    }

    //    @TestCaseId("TMS-2")
//    @Issue("CEV-9933") // Это ссылка на баг
    @Test(groups = "update", dependsOnMethods = "loginTest")
    public void updateReporterInIssue() {
        // 1. обновление поля Reporter в Issue
        updateIssuePage.updateReporterInIssue(newReporterLogin, newReporterName);
        // 2. проверка Assert-тов
        System.out.println("driver.findElement(By.xpath(\"//*[@id='issue_summary_reporter_a.a.piluck']\")).isEnabled() = " + driver.findElement(By.xpath("//*[@id='issue_summary_reporter_a.a.piluck']")).isEnabled());
        System.out.println("driver.findElement(By.xpath(\"//*[@id='issue_summary_reporter_a.a.piluck']\")).getText() = " + driver.findElement(By.xpath("//*[@id='issue_summary_reporter_a.a.piluck']")).getText());
        System.out.println("driver.getTitle() = " + driver.getTitle());
        updateIssuePage.webAssert.assertTitleContainsText(updateIssuePage.issueKey); //проверка наличия в заголовке подстроки ключа issueKey "QAAUT-1676"
        updateIssuePage.webAssert.assertIsElementEnabled("//*[@id='issue_summary_reporter_a.a.piluck']", 10);
        updateIssuePage.webAssert.assertPageContainsText(newReporterName);
        updateIssuePage.webAssert.assertPageContainsText(newReporterLogin);

        // 3. deleteting new test issue
        updateIssuePage.deleteIssue();

        System.out.println("updateReporterInIssue " + WebDate.getCurrentDateTimeString());
        System.out.println("updateReporterInIssue - thread id: " + Thread.currentThread().getId());
    }

    //    @TestCaseId("TMS-2")
//    @Features("Issue")
//    @Stories({"CRUDIssue"})
    @Test(groups = "update", dependsOnMethods = "loginTest")
    public void addCommentToIssue() {
        // 1. добавление комментария в Issue
        String textNewComment = "add comment UI for lr11 " + WebDate.getCurrentDateTimeString();
        updateIssuePage.addCommentToIssue(textNewComment);

        // 2. проверка Assert-тов
        String xPath_newComment = "//div[@id='issue_actions_container']/div[1]/div[1]/div[2]";
        updateIssuePage.webAssert.assertTitleContainsText(updateIssuePage.issueKey); //проверка наличия в заголовке подстроки ключа issueKey "QAAUT-1676"
        updateIssuePage.webAssert.assertIsElementEnabled(xPath_newComment, 10); // наличие элемента контейнера комментариев
        updateIssuePage.webAssert.assertTextPresent(xPath_newComment, 10, textNewComment); // равенство текста добавленного коментария, исходной формулировке
        updateIssuePage.webAssert.assertPageContainsText(textNewComment);

        // 3. deleteting new test issue
        updateIssuePage.deleteIssue();

        System.out.println("addCommentToIssue " + WebDate.getCurrentDateTimeString());
        System.out.println("addCommentToIssue - thread id: " + Thread.currentThread().getId());
    }

    @Test(groups = "update", dependsOnMethods = "loginTest")
    public void updatePriorityInIssue() {
        // 1. добавление комментария в Issue
        String newTextPriority = "Lowest";
        updateIssuePage.updatePriorityInIssue(newTextPriority);

        // 2. проверка Assert-тов
        String xPath_priority_val = ".//*[@id='priority-val']";
        //String xPath_priority_val_text = ".//*[@id='priority-val']";
        //String xPath_priority_val_text = ".//*[@id='priority-val']/text()[2]";
        updateIssuePage.webAssert.assertIsElementEnabled(xPath_priority_val, 10); // наличие элемента по адресу xPath на форме
        updateIssuePage.webAssert.assertTextPresent(xPath_priority_val, 10, newTextPriority); // соответствие текста в элементе по адресу xPath на форме
        updateIssuePage.webAssert.assertPageContainsText(newTextPriority);

        // 3. deleteting new test issue
        updateIssuePage.deleteIssue();

        System.out.println("updatePriorityInIssue " + WebDate.getCurrentDateTimeString());
        System.out.println("updatePriorityInIssue - thread id: " + Thread.currentThread().getId());
    }


    @AfterTest
    public void afterEndTests() {
        driver.close();
        driver.quit();
    }

    //    public String getCurrentDateTimeString() {
    //        Date d = new Date();
    //        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    //        return formatDate.format(d);
    //    }

}

