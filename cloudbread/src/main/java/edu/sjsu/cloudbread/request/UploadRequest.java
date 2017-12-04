package edu.sjsu.cloudbread.request;

import java.io.File;
import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class UploadRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private MultipartFile foodFile;
	private String fileName;
	private String userName;
	private String totalFoodCooked;
	private String foodWasteQty;
	private String pickUpTime;

	public UploadRequest() {
	}

	public UploadRequest(MultipartFile foodFile, String fileName, String userName, String totalFoodCooked,
			String foodWasteQty) {
		super();
		this.foodFile = foodFile;
		this.fileName = fileName;
		this.userName = userName;
		this.totalFoodCooked = totalFoodCooked;
		this.foodWasteQty = foodWasteQty;

	}

	public MultipartFile getFoodFile() {
		return foodFile;
	}

	public void setFoodFile(MultipartFile foodFile) {
		this.foodFile = foodFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTotalFoodCooked() {
		return totalFoodCooked;
	}

	public void setTotalFoodCooked(String totalFoodCooked) {
		this.totalFoodCooked = totalFoodCooked;
	}

	public String getFoodWasteQty() {
		return foodWasteQty;
	}

	public void setFoodWasteQty(String foodWasteQty) {
		this.foodWasteQty = foodWasteQty;
	}

	public String getPickUpTime() {
		return pickUpTime;
	}

	public void setPickUpTime(String pickUpTime) {
		this.pickUpTime = pickUpTime;
	}

	@Override
	public String toString() {
		return "SignUpRequest [userName=" + userName + ", totalFoodCooked=" + totalFoodCooked + ", foodWasteQty="
				+ foodWasteQty + "]";
	}

}
