package edu.sjsu.mldata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class DynamoDbDataGenerator {

	static final String csvFile = "C:\\Users\\Shriaithal\\Desktop\\Final\\Resturant_Data_final.csv";

	public static void main(String[] args) {
		System.out.println("Hello");
		/*
		 * User user = new User(); user.setUserId(UUID.randomUUID().toString());
		 * user.setUserName("Test@test"); user.setRole("Business");
		 * user.setPassword("abc123");
		 */
		try {
			List<User> userList = new ArrayList<>();
			String line = "";
			String cvsSplitBy = ",";
			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			int iteration = 0;
			while ((line = br.readLine()) != null) {

				if (iteration == 0) {
					iteration++;
					continue;
				}
				String[] arr = line.split(",");
				User user = new User();
				user.setUserName("business"+iteration+"@gmail.com");
				user.setPassword("abc123");
				user.setName(arr[0]);
				user.setUserId(arr[3]);
				user.setRatings(new BigDecimal(arr[5]));
				user.setFoodType(arr[6]);
				user.setAddress(arr[7]);
				user.setGoodReviews(Integer.parseInt(arr[8]));
				user.setBadReviews(Integer.parseInt(arr[9]));
				user.setPriceRating(new BigDecimal(arr[10]));
				user.setOccupancy(new BigDecimal(arr[11]));
				user.setDistance(new BigDecimal(arr[12]));
				user.setCapacity(Integer.parseInt(arr[13]));
				user.setRole("Business");
				user.setPredictionEnabled("Y");
				iteration++;
				userList.add(user);
			}
			addUser(userList);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void addUser(List<User> users) {

		AWSCredentialsProvider awsCredentialsProvider = new ClasspathPropertiesFileCredentialsProvider(
				"config/application.properties");
		awsCredentialsProvider.getCredentials();
		AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().withCredentials(awsCredentialsProvider)
				.withRegion(Regions.US_EAST_1.getName()).build();

		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
		for (User user : users) {
			mapper.save(user);
		}
	}
}
