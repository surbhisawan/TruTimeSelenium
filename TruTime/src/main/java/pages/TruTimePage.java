package pages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import baseClass.BaseClass;

//Page class for the TruTme page of the website
public class TruTimePage extends BaseClass {
	// Webpage elements of the current page used for running the test

	// Getting current Date
	@FindBy(xpath = "//*[contains(@class,'dayHeadr active')]")
	WebElement currentDate;
	// List Containg dates
	@FindBy(xpath = "//*[contains(@class,'dayHeadr')]")
	List<WebElement> listOfDates;
	@FindBy(id = "appFrame")
	WebElement iframe;
	@FindBy(linkText = "1C")
	WebElement menuOptions;
	@FindBy(xpath = "//*[@class='ui-datepicker-month']")
	WebElement month;
	@FindBy(xpath = "//*[@class='ui-datepicker-year']")
	WebElement year;
	@FindBy(xpath = "//*[contains(@class, 'topupavailablefromDate')]")
	WebElement topup;

	// Constructor to instantiate the Page Object
	public TruTimePage() {
		PageFactory.initElements(driver, this);
	}

	// Variables defined to get the system time in the required format
	public static SimpleDateFormat dateParser = new SimpleDateFormat("E, dd MMM");
	public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E, dd MMM");
	public static LocalDate now = LocalDate.now();
	public static Calendar calendar = Calendar.getInstance();

	// Variable declared to get the dates from the website
	List<String> dateList = new ArrayList<>();

	// Method to get the displayed dates on the TruTime Page
	public void displayedDates() throws InterruptedException {

		eWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));
		eWait.until(ExpectedConditions.visibilityOfAllElements(listOfDates));

		for (int i = 0; i < listOfDates.size(); i++) {
			dateList.add(listOfDates.get(i).getText());
		}

		System.out.println(
				"_______________________________________________________________________________________________________");
		dateList.forEach(System.out::println);
		System.out.println(
				"_______________________________________________________________________________________________________");
	}

	// Method to check the displayed dates against the current system dates
	public void verifyDisplayedDates() {
		List<String> list = new ArrayList<>();

		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

		for (int i = 0; i < 7; i++) {
			list.add(dateParser.format(calendar.getTime()));
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		assertTrue(dateList.containsAll(list));

	}

	// Method to check the active date is of today
	public void verifyPageCurrentDate() {
		String presentPageDate = currentDate.getText();
//		calendar.setTime(new Date());
		assertEquals(presentPageDate, dtf.format(now), "Current Date is different");
	}

	// Verifying Displayed Month and Year
	public void verifyMonth() {
		assertEquals(month.getText().toLowerCase(), now.getMonth().toString().toLowerCase());
		assertEquals(Integer.parseInt(year.getText()), now.getYear());
	}

	// Verfifying Topup Logic
	public void topUpLogic() throws ParseException {
		Date displayedDate = dateParser.parse(topup.getText());

		Date systemDate = dateParser.parse(dtf.format(now));
		System.out.println(
				"_______________________________________________________________________________________________________");

		System.out.println("Topup Logic :"
				+ (systemDate.getTime() - displayedDate.getTime()) / ((1000 * 60 * 60 * 24)) % 365 + " Days");
		System.out.println(
				"_______________________________________________________________________________________________________");

	}

	// Method to verify the page title
	public void verifyPage() {
		eWait.until(ExpectedConditions.visibilityOf(menuOptions));
		check = driver.getTitle();
		assertEquals(check, "OneCognizant");
	}

}
