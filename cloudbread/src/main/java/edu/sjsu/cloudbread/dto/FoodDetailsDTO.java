package edu.sjsu.cloudbread.dto;

import java.util.Comparator;
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
import edu.sjsu.cloudbread.model.FoodDetails.CategoryEnum;

public class FoodDetailsDTO  implements Comparable {
	private static final long serialVersionUID = 1L;
	private User user;
	private String requestId;
	private String fileName;
	private Date createdDate;
	private String availstatus;
	private String totalQty;
	private String wasteQty;
	private CategoryEnum category;
	private String reqStatus;
	private List<String> rejects;
	private String charityAccepted;
	private String pickUpTime;
	
	

	public FoodDetailsDTO() {
	}

	public FoodDetailsDTO(User user, String requestId, String fileName, Date createdDate, String availstatus,
			String totalQty, String wasteQty, CategoryEnum category, String reqStatus, List<String> rejects,
			String charityAccepted, String pickUpTime) {
		super();
		user.setPassword(""); //no password to display on front end
		this.user = user;
		this.requestId = requestId; 
		this.fileName = fileName;
		this.createdDate = createdDate;
		this.availstatus = availstatus;
		this.totalQty = totalQty;
		this.wasteQty = wasteQty;
		this.category = category;
		this.reqStatus = reqStatus;
		this.rejects = rejects;
		this.charityAccepted = charityAccepted;
		this.pickUpTime =  pickUpTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getAvailstatus() {
		return availstatus;
	}

	public void setAvailstatus(String availstatus) {
		this.availstatus = availstatus;
	}

	public String getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(String totalQty) {
		this.totalQty = totalQty;
	}

	public String getWasteQty() {
		return wasteQty;
	}

	public void setWasteQty(String wasteQty) {
		this.wasteQty = wasteQty;
	}

	public CategoryEnum getCategory() {
		return category;
	}

	public void setCategory(CategoryEnum category) {
		this.category = category;
	}

	public String getReqStatus() {
		return reqStatus;
	}

	public void setReqStatus(String reqStatus) {
		this.reqStatus = reqStatus;
	}

	public List<String> getRejects() {
		return rejects;
	}

	public void setRejects(List<String> rejects) {
		this.rejects = rejects;
	}

	public String getCharityAccepted() {
		return charityAccepted;
	}

	public void setCharityAccepted(String charityAccepted) {
		this.charityAccepted = charityAccepted;
	}
	
	public String getPickUpTime() {
		return pickUpTime;
	}

	public void setPickUpTime(String pickUpTime) {
		this.pickUpTime = pickUpTime;
	}

	


	

	@Override
	public int compareTo(Object o) {
		
				if (this.getCreatedDate().compareTo(((FoodDetailsDTO)o).getCreatedDate()) > 0)
		    			return 1;
				else
		    			return -1;
	}
}
