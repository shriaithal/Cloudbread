package edu.sjsu.mldata;

import java.math.BigDecimal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "User")
public class User {
	private String userId;
	private String name;
	private String userName;
	private String password;
	private String role;
	private String address;
	private String city;
	private String zipcode;
	private String predictionEnabled;
	private BigDecimal ratings;
	private BigDecimal distance;
	private BigDecimal occupancy;
	private Integer capacity;
	private String foodType;
	private Integer goodReviews;
	private Integer badReviews;
	private BigDecimal priceRating;

	@DynamoDBHashKey(attributeName = "UserId")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@DynamoDBAttribute(attributeName = "Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@DynamoDBAttribute(attributeName = "UserName")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@DynamoDBAttribute(attributeName = "Password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@DynamoDBAttribute(attributeName = "UserRole")
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@DynamoDBAttribute(attributeName = "Address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@DynamoDBAttribute(attributeName = "City")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@DynamoDBAttribute(attributeName = "ZipCode")
	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@DynamoDBAttribute(attributeName = "FoodType")
	public String getFoodType() {
		return foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}

	@DynamoDBAttribute(attributeName = "PriceRating")
	public BigDecimal getPriceRating() {
		return priceRating;
	}

	public void setPriceRating(BigDecimal priceRating) {
		this.priceRating = priceRating;
	}

	@DynamoDBAttribute(attributeName = "Distance")
	public BigDecimal getDistance() {
		return distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	@DynamoDBAttribute(attributeName = "Ratings")
	public BigDecimal getRatings() {
		return ratings;
	}

	public void setRatings(BigDecimal ratings) {
		this.ratings = ratings;
	}

	@DynamoDBAttribute(attributeName = "Occupancy")
	public BigDecimal getOccupancy() {
		return occupancy;
	}

	public void setOccupancy(BigDecimal occupancy) {
		this.occupancy = occupancy;
	}

	@DynamoDBAttribute(attributeName = "Capacity")
	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	@DynamoDBAttribute(attributeName = "GoodReviews")
	public Integer getGoodReviews() {
		return goodReviews;
	}

	public void setGoodReviews(Integer goodReviews) {
		this.goodReviews = goodReviews;
	}

	@DynamoDBAttribute(attributeName = "BadReviews")
	public Integer getBadReviews() {
		return badReviews;
	}

	public void setBadReviews(Integer badReviews) {
		this.badReviews = badReviews;
	}

	@DynamoDBAttribute(attributeName = "PredictionEnabled")
	public String isPredictionEnabled() {
		return predictionEnabled;
	}

	public void setPredictionEnabled(String predictionEnabled) {
		this.predictionEnabled = predictionEnabled;
	}

}
