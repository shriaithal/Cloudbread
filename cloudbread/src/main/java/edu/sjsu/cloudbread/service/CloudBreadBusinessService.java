package edu.sjsu.cloudbread.service;

import java.util.List;

import javax.xml.bind.ValidationException;

import edu.sjsu.cloudbread.response.FoodDetailsResponse;
import edu.sjsu.cloudbread.response.GenericResponse;
import edu.sjsu.cloudbread.response.UserResponse;
import edu.sjsu.cloudbread.model.FoodDetails;
import edu.sjsu.cloudbread.request.SignUpRequest;
import edu.sjsu.cloudbread.request.UploadRequest;

public interface CloudBreadBusinessService {
  
	UserResponse login(String userName, String password) throws Exception;
	
	UserResponse signUp(SignUpRequest request) throws ValidationException;
	
	GenericResponse upload(UploadRequest request) throws ValidationException;
	
	FoodDetailsResponse getFoodDetails(String userName) throws Exception;
	
	List<List> getGraphPlots(String userName) throws Exception;
}
