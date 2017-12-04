package edu.sjsu.cloudbread.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.sjsu.cloudbread.request.LoginRequest;
import edu.sjsu.cloudbread.request.SignUpRequest;
import edu.sjsu.cloudbread.request.UploadRequest;
import edu.sjsu.cloudbread.response.FoodDetailsResponse;
import edu.sjsu.cloudbread.response.GenericResponse;
import edu.sjsu.cloudbread.response.GraphPlotResponse;
import edu.sjsu.cloudbread.response.UserResponse;
import edu.sjsu.cloudbread.service.CloudBreadBusinessService;

@Controller
public class CloudBreadBusinessPortalController {

	private static final Logger LOG = Logger.getLogger(CloudBreadBusinessPortalController.class);

	@Autowired
	CloudBreadBusinessService apiService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String startPage(ModelMap model) {
		return "index";
	}
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
		ResponseEntity<UserResponse> responseEntity = null;
		UserResponse response = new UserResponse();
		try {
			response = apiService.login(request.getUserName(), request.getPassword());
			response.setStatusCode(HttpStatus.OK.toString());
			responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			// response.setMessage("Invalid User Name and Password");
			responseEntity = new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return responseEntity;
	}
	
	/**
	 * 
	 * @author Ashwini Shankar Narayan
 	 *
 	**/
	@RequestMapping(value = "/signup", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseEntity<UserResponse> signup(@RequestBody SignUpRequest request) {
		ResponseEntity<UserResponse> responseEntity = null;
		UserResponse response = new UserResponse();
		try {
			response = apiService.signUp(request);
			response.setStatusCode(HttpStatus.OK.toString());
			responseEntity = new ResponseEntity<UserResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			// response.setMessage("User already Exists");
			responseEntity = new ResponseEntity<UserResponse>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return responseEntity;
	}
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseEntity<GenericResponse> upload(
	//		 @RequestBody UploadRequest request
			@RequestParam(value = "foodFile", required = true) MultipartFile foodFile,
			@RequestParam(value = "fileName", required = true) String fileName,
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "totalFoodCooked", required = false) String totalFoodCooked,
			@RequestParam(value = "foodWasteQty", required = true) String foodWasteQty,
			@RequestParam(value = "pickUpTime", required = true) String pickUpTime
			) {

		UploadRequest request = new UploadRequest();
		request.setFoodFile(foodFile);
		request.setFileName(fileName);
		request.setUserName(userName);
		request.setTotalFoodCooked(totalFoodCooked);
		request.setFoodWasteQty(foodWasteQty);
		request.setPickUpTime(pickUpTime);
		
		ResponseEntity<GenericResponse> responseEntity = null;
		GenericResponse response = new GenericResponse();
		try {
			response = apiService.upload(request);
			response.setMessage("Upload Successfull!!");
			response.setStatusCode(HttpStatus.OK.toString());
			responseEntity = new ResponseEntity<GenericResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			 response.setMessage("Upload: something went wrong");
			 responseEntity = new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return responseEntity;

	}
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	@RequestMapping(value = "/businessList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<FoodDetailsResponse> getFoodDetails(
			@RequestParam(value = "userName", required = true) String userName) {
		ResponseEntity<FoodDetailsResponse> responseEntity = null;
		FoodDetailsResponse response = new FoodDetailsResponse();
		try {
			System.out.println("Controller Usrname:" + userName);
			response = apiService.getFoodDetails(userName);
			response.setStatusCode(HttpStatus.OK.toString());
			responseEntity = new ResponseEntity<FoodDetailsResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			System.out.println(e.getMessage());
			 response.setMessage("Business List: Something went wrong");
			responseEntity = new ResponseEntity<FoodDetailsResponse>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return responseEntity;
	}
	
	/**
	 * 
	 * @author Ashwini Shankar Narayan
 	 *
 	**/
	
	@RequestMapping(value = "/graphPlots", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<GraphPlotResponse> getGraphPlots(
			@RequestParam(value = "userName", required = true) String userName) {
		ResponseEntity<GraphPlotResponse> responseEntity = null;
		GraphPlotResponse response = new GraphPlotResponse();
		try {
			System.out.println("Username:" + userName);
			@SuppressWarnings("rawtypes")
			List<List> graphResult= new ArrayList<List>();
			graphResult = apiService.getGraphPlots(userName);
			response = new GraphPlotResponse(graphResult.get(0), graphResult.get(1), graphResult.get(2), graphResult.get(3));
//			response.setSeries1(graphResult.get(0));
//			response.setSeries2(graphResult.get(1));
			response.setStatusCode(HttpStatus.OK.toString());
			responseEntity = new ResponseEntity<GraphPlotResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			System.out.println(e.getMessage());
			responseEntity = new ResponseEntity<GraphPlotResponse>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return responseEntity;
	}

	/*@RequestMapping(value = "/testKinesis", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String testKinesis() {
		String resp = null;
		
		 * for (int i = 0; i < 100; i++) { KinesisEvent event = new KinesisEvent(i + "",
		 * EventType.VALIDATE_EMAIL); kinesisProducer.sendKinesisEvents(event); }
		 
		emailClient.sendEmail();
		return resp;
	}*/
}
