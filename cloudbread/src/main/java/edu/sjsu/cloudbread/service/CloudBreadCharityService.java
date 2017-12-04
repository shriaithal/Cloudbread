package edu.sjsu.cloudbread.service;

import edu.sjsu.cloudbread.response.FoodDetailsResponse;
import edu.sjsu.cloudbread.response.GenericResponse;

public interface CloudBreadCharityService {


	FoodDetailsResponse getFoodDetailsCharity(String userName) throws Exception;
	void updateStatus(String requestId, String status, String userName) throws Exception;
}
