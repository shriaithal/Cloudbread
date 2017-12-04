package edu.sjsu.cloudbread.dao;

import java.util.List;

import edu.sjsu.cloudbread.model.FoodDetails;

public interface FoodDetailsDao {

	void upload(FoodDetails foodDetails) throws Exception;

	List<FoodDetails> getFoodDetails(String userName) throws Exception;

	List<FoodDetails> getFoodDetailsCharity() throws Exception;

	public void updateStatus(String requestId, String status, String userName) throws Exception;

	FoodDetails getFoodDetailsByRequestId(String requestId);
}
