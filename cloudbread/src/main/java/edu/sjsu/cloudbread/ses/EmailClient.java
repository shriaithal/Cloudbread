package edu.sjsu.cloudbread.ses;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;

import edu.sjsu.cloudbread.dao.FoodDetailsDao;
import edu.sjsu.cloudbread.dao.MLDataDao;
import edu.sjsu.cloudbread.dao.UserDao;
import edu.sjsu.cloudbread.kinesis.events.MailerEvent;
import edu.sjsu.cloudbread.model.FoodDetails;
import edu.sjsu.cloudbread.model.MLData;
import edu.sjsu.cloudbread.model.User;
import edu.sjsu.cloudbread.service.WeatherForecastService;

/**
 * 
 * @author Anushri Srinath Aithal
 *
 */
@Service
public class EmailClient {

	@Autowired
	UserDao userDao;

	@Autowired
	FoodDetailsDao foodDetailsDao;

	@Autowired
	WeatherForecastService weatherForecastService;

	@Autowired
	MLDataDao mlDataDao;

	private AmazonSimpleEmailService sesClient;

	Map<Integer, String> daysOfWeekMap = new HashMap<Integer, String>();
	private Map<String, String> charityEmailTemplate = new HashMap<String, String>();
	private Map<String, String> businessEmailTemplate = new HashMap<String, String>();
	private static final Logger LOG = Logger.getLogger(EmailClient.class);
	private static final String FROM = "notification.cloudbread@gmail.com";
	private static final String UTF8 = "UTF-8";

	/**
	 * @author Anushri Srinath Aithal
	 * initialize SES client
	 */
	@PostConstruct
	private void init() {
		AWSCredentialsProvider awsCredentialsProvider = new ClasspathPropertiesFileCredentialsProvider(
				"config/application.properties");
		sesClient = AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(awsCredentialsProvider)
				.withRegion(Regions.US_EAST_1).build();

		loadCharityEmailTemplates();
		loadBusinessEmailTemplates();

		daysOfWeekMap.put(1, "Monday");
		daysOfWeekMap.put(2, "Tuesday");
		daysOfWeekMap.put(3, "Wednesday");
		daysOfWeekMap.put(4, "Thursday");
		daysOfWeekMap.put(5, "Friday");
		daysOfWeekMap.put(6, "Saturday");
		daysOfWeekMap.put(7, "Sunday");
	}

	private void loadBusinessEmailTemplates() {
		Properties properties = new Properties();
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("/config/notify-business-template.properties");
		if (input != null) {
			try {
				properties.load(input);
			} catch (IOException e) {
				LOG.error("I/O exception", e);
			} catch (IllegalArgumentException e) {
				LOG.error("Malfunction properties format", e);
			}
			for (String key : properties.stringPropertyNames()) {
				String value = properties.getProperty(key);
				businessEmailTemplate.put(key, value);
			}
		} else {
			LOG.error("I/O exception");
		}
		
	}

	private void loadCharityEmailTemplates() {
		Properties properties = new Properties();
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("/config/notify-charity-template.properties");
		if (input != null) {
			try {
				properties.load(input);
			} catch (IOException e) {
				LOG.error("I/O exception", e);
			} catch (IllegalArgumentException e) {
				LOG.error("Malfunction properties format", e);
			}
			for (String key : properties.stringPropertyNames()) {
				String value = properties.getProperty(key);
				charityEmailTemplate.put(key, value);
			}
		} else {
			LOG.error("I/O exception");
		}
	}

	public void sendEmails(MailerEvent event) throws Exception {
		try {
			switch (event.getEventType().toString()) {
			case "VALIDATE_EMAIL":
				sendValidationEmail(event);
				break;
			case "NOTIFY_CHARITY":
				notifyCharirtyEmail(event);
				break;
			case "NOTIFY_BUSSINESS":
				notifyBusinessEmail(event);
				break;
			}
		} catch (Exception e) {
			LOG.error(e);
			throw e;
		}
	}
/**
 * 
 * @author Anuradha Rajashekar
 *
 */
	private void notifyBusinessEmail(MailerEvent event) {
		LOG.info("Sending business emails");
		try {
			FoodDetails foodDetails = foodDetailsDao.getFoodDetailsByRequestId(event.getId());
			User user = foodDetails.getUser();

			Body body = new Body().withHtml(new Content().withCharset(UTF8).withData(businessEmailTemplate.get("text.body")))
					.withText(new Content().withCharset(UTF8).withData(businessEmailTemplate.get("text.body")));
			Message message = new Message();
			message.withBody(body).withSubject(new Content().withCharset(UTF8).withData(businessEmailTemplate.get("subject")));
			Destination destination = new Destination().withToAddresses(user.getUserName());
			SendEmailRequest request = new SendEmailRequest().withDestination(destination).withMessage(message)
					.withSource(FROM);
			sesClient.sendEmail(request);

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
		}
	}
/**
 * 
 * @author Anuradha Rajashekar
 *
 */

	private void notifyCharirtyEmail(MailerEvent event) throws Exception {
		LOG.info("Sending charity emails");
		try {
			FoodDetails foodDetails = foodDetailsDao.getFoodDetailsByRequestId(event.getId());
			List<User> users = userDao.getUserByRole("Charity");

			if (CollectionUtils.isEmpty(users)) {
				generateMLData(foodDetails);
				return;
			}
			for (User user : users) {
				Body body = new Body().withHtml(new Content().withCharset(UTF8).withData(charityEmailTemplate.get("text.body")))
						.withText(new Content().withCharset(UTF8).withData(charityEmailTemplate.get("text.body")));
				Message message = new Message();
				message.withBody(body).withSubject(new Content().withCharset(UTF8).withData(charityEmailTemplate.get("subject")));
				Destination destination = new Destination().withToAddresses(user.getUserName());
				SendEmailRequest request = new SendEmailRequest().withDestination(destination).withMessage(message)
						.withSource(FROM);
				sesClient.sendEmail(request);
			}
			generateMLData(foodDetails);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
		}
	}

	private void generateMLData(FoodDetails foodDetails) {
		try {
			LOG.info("RDS entry for :" + foodDetails.getRequestId());
			User user = foodDetails.getUser();
			if ("N".equals(user.isPredictionEnabled())) {
				return;
			}
			MLData mlData = new MLData();
			mlData.setName(user.getName());
			mlData.setRatings(user.getRatings());
			mlData.setDistance(user.getDistance());
			mlData.setOccupancy(user.getOccupancy());
			mlData.setCapacity(user.getCapacity());
			mlData.setFoodType(user.getFoodType());
			mlData.setGoodReviews(user.getGoodReviews());
			mlData.setBadReviews(user.getBadReviews());
			mlData.setPriceRating(user.getPriceRating());
			mlData.setPredicted("N");
			mlData.setCreatedTime(new Timestamp(System.currentTimeMillis()));
			mlData.setActualTime(new Timestamp(System.currentTimeMillis()));

			Double wastage = Double.parseDouble(foodDetails.getWasteQty());
			wastage = wastage / 120;
			mlData.setWastage(new BigDecimal(String.valueOf(wastage)));

			BigDecimal amountCooked = new BigDecimal(foodDetails.getTotalQty());
			mlData.setAmountCooked(amountCooked);

			String weather = weatherForecastService.getTodaysWeather();
			mlData.setWeather(weather);

			Calendar calendar = Calendar.getInstance();
			Integer dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			mlData.setDay(daysOfWeekMap.get(dayOfWeek));

			List<MLData> mlDataList = new ArrayList<>();
			mlDataList.add(mlData);

			mlDataDao.insertMLData(mlDataList);
			LOG.info("Exiting ml data insertion");
		} catch (Exception e) {
			LOG.error(e);
		}
	}
/**
 * 
 * @author Anuradha Rajashekar
 *
 */

	private void sendValidationEmail(MailerEvent event) {
		User user = userDao.getUserById(event.getId());
		VerifyEmailAddressRequest request = new VerifyEmailAddressRequest().withEmailAddress(user.getUserName());
		sesClient.verifyEmailAddress(request);
	}
}
