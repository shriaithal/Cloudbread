package edu.sjsu.cloudbread.model;

import java.util.Date;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import edu.sjsu.cloudbread.model.User;

@DynamoDBTable(tableName = "FoodDetails")
public class FoodDetails {
	private User user;
	private String requestId;
	private String fileName;
	private Date createdDate;
	private String availstatus;
	private String totalQty;
	private String wasteQty;
	private CategoryEnum category;
	private String reqStatus;
	private String weather;
	private List<String> rejects;
	private String charityAccepted;
	private String pickUpTime;

	public static enum CategoryEnum {
		Raw, Processed, Canned
	};

	@DynamoDBHashKey(attributeName = "RequestId")
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@DynamoDBTyped(DynamoDBAttributeType.M)
	@DynamoDBAttribute(attributeName = "userDetails")
	// @DynamoDBTypeConverted(converter = User.class)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@DynamoDBAttribute(attributeName = "FileName")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@DynamoDBAttribute(attributeName = "CreatedDate")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@DynamoDBAttribute(attributeName = "AvailStatus")
	public String getAvailstatus() {
		return availstatus;
	}

	public void setAvailstatus(String availstatus) {
		this.availstatus = availstatus;
	}

	@DynamoDBAttribute(attributeName = "TotalQty")
	public String getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(String string) {
		this.totalQty = string;
	}

	@DynamoDBAttribute(attributeName = "WasteQty")
	public String getWasteQty() {
		return wasteQty;
	}

	public void setWasteQty(String wasteQty) {
		this.wasteQty = wasteQty;
	}

	// @DynamoDBAttribute(attributeName="Category")
	@DynamoDBTyped(DynamoDBAttributeType.S)
	public CategoryEnum getCategory() {
		return category;
	}

	public void setCategory(CategoryEnum category) {
		this.category = category;
	}

	@DynamoDBAttribute(attributeName = "ReqStatus")
	public String getReqStatus() {
		return reqStatus;
	}

	public void setReqStatus(String reqStatus) {
		this.reqStatus = reqStatus;
	}

	@DynamoDBAttribute(attributeName = "Weather")
	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	@DynamoDBTyped(DynamoDBAttributeType.L)
	@DynamoDBAttribute(attributeName = "Rejects")
	public List<String> getRejects() {
		return rejects;
	}

	public void setRejects(List<String> rejects) {
		this.rejects = rejects;
	}

	@DynamoDBAttribute(attributeName = "CharityAccepted")
	public String getCharityAccepted() {
		return charityAccepted;
	}

	public void setCharityAccepted(String charityAccepted) {
		this.charityAccepted = charityAccepted;
	}

	@DynamoDBAttribute(attributeName = "PickUpTime")
	public String getPickUpTime() {
		return pickUpTime;
	}

	public void setPickUpTime(String pickUpTime) {
		this.pickUpTime = pickUpTime;
	}

}
