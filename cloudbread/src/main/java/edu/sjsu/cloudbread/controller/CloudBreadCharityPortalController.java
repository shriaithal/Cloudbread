package edu.sjsu.cloudbread.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.sjsu.cloudbread.request.CharityRequest;
import edu.sjsu.cloudbread.response.FoodDetailsResponse;
import edu.sjsu.cloudbread.service.CloudBreadCharityService;

@Controller
public class CloudBreadCharityPortalController {

	private static final Logger LOGGER = Logger.getLogger(CloudBreadCharityPortalController.class);

	@Autowired
	CloudBreadCharityService service;
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	@RequestMapping(value = "/charityList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<FoodDetailsResponse> getFoodDetailsCharity(
			//@RequestBody CharityRequest request
			@RequestParam(value = "userName", required = true) String userName
			) {
		ResponseEntity<FoodDetailsResponse> responseEntity = null;
		FoodDetailsResponse response = new FoodDetailsResponse();
		try {

			response = service.getFoodDetailsCharity(userName);
			System.out.println("charity: "+userName);
			response.setStatusCode(HttpStatus.OK.toString());
			responseEntity = new ResponseEntity<FoodDetailsResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			// LOG.error(e.getMessage());
			System.out.println(e.getMessage());
			 response.setMessage("Charity List: Something went wrong");
			responseEntity = new ResponseEntity<FoodDetailsResponse>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return responseEntity;
	}

	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseEntity<FoodDetailsResponse> updateStatus(
			@RequestBody CharityRequest request) {
		ResponseEntity<FoodDetailsResponse> responseEntity = null;
		FoodDetailsResponse response = new FoodDetailsResponse();
		try {
			System.out.println(request.getRequestId() + " --  " + request.getStatus());
			service.updateStatus(request.getRequestId(), request.getStatus(), request.getUserName());
			response = service.getFoodDetailsCharity(request.getUserName());
			response.setStatusCode(HttpStatus.OK.toString());
			responseEntity = new ResponseEntity<FoodDetailsResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			//LOG.error(e.getMessage());
			System.out.println(e.getMessage());
			response.setMessage("Something went wrong");
			responseEntity = new ResponseEntity<FoodDetailsResponse>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return responseEntity;
	}
}
