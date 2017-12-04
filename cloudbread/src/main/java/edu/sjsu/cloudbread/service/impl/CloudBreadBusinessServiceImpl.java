package edu.sjsu.cloudbread.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.ValidationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.amazonaws.util.CollectionUtils;

import edu.sjsu.cloudbread.dao.FoodDetailsDao;
import edu.sjsu.cloudbread.dao.MLDataDao;
import edu.sjsu.cloudbread.dao.UserDao;
import edu.sjsu.cloudbread.dto.FoodDetailsDTO;
import edu.sjsu.cloudbread.kinesis.events.EventType;
import edu.sjsu.cloudbread.kinesis.events.MailerEvent;
import edu.sjsu.cloudbread.model.FoodDetails;
import edu.sjsu.cloudbread.model.FoodDetails.CategoryEnum;
import edu.sjsu.cloudbread.model.User;
import edu.sjsu.cloudbread.request.SignUpRequest;
import edu.sjsu.cloudbread.request.UploadRequest;
import edu.sjsu.cloudbread.response.FoodDetailsResponse;
import edu.sjsu.cloudbread.response.GenericResponse;
import edu.sjsu.cloudbread.response.UserResponse;
import edu.sjsu.cloudbread.service.CloudBreadBusinessService;
import edu.sjsu.cloudbread.sqs.SQSEventProducer;
import edu.sjsu.cloudbread.utils.CommonUtils;
import edu.sjsu.cloudbread.utils.RekognitionUtils;
import edu.sjsu.cloudbread.utils.S3Utils;

@Service
public class CloudBreadBusinessServiceImpl implements CloudBreadBusinessService {

	private static final Logger LOG = Logger.getLogger(CloudBreadBusinessServiceImpl.class);

	@Autowired
	UserDao userDao;

	@Autowired
	FoodDetailsDao foodDetailsDao;

	@Autowired
	SQSEventProducer eventProducer;

	@Autowired
	MLDataDao mlDataDao;
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	@Override
	public UserResponse login(String userName, String password) throws Exception {

		UserResponse response = new UserResponse();

		try {
			User user = userDao.getUserByNameAndPassowrd(userName, password);
			if (user == null) {
				throw new UsernameNotFoundException(userName);
			}
			response.setName(user.getName());
			response.setRole(user.getRole());
			response.setAddress(user.getAddress());
			response.setUserId(user.getUserId());
			response.setUserName(user.getUserName());
			response.setCity(user.getCity());
			response.setZipcode(user.getZipcode());
			return response;

		} catch (Exception e) {
			LOG.error(e);
		}
		return null;
	}
	
	/**
	 * 
	 * @author Ashwini Shankar Narayan
 	 *
 	**/
	@Override
	public UserResponse signUp(SignUpRequest request) throws ValidationException {

		try {
			User existinguser = userDao.getUserByNameAndPassowrd(request.getUserName(), request.getPassword());
			if (null != existinguser) {
				throw new ValidationException("User already exists");
			}
			User user = new User();
			user.setUserId(UUID.randomUUID().toString());
			user.setName(request.getName());
			user.setUserName(request.getUserName());
			user.setPassword(request.getPassword());
			user.setRole(request.getRole());
			user.setAddress(request.getAddress());
			user.setCity(request.getCity());
			user.setZipcode(request.getZipcode());
			userDao.signUpUser(user);

			UserResponse response = new UserResponse();
			response.setUserId(user.getUserId());
			response.setName(user.getName());
			response.setUserName(user.getUserName());
			response.setRole(user.getRole());
			response.setAddress(user.getAddress());
			response.setCity(user.getCity());
			response.setZipcode(user.getZipcode());

			// send verify email
			MailerEvent event = new MailerEvent(user.getUserId(), EventType.VALIDATE_EMAIL);
			eventProducer.pushEvents(event);

			return response;
		} catch (Exception e) {
			LOG.error(e);
		}
		return null;
	}
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	@Override
	public GenericResponse upload(UploadRequest request) throws ValidationException {
		try {
			User existinguser = userDao.getUserByUserName(request.getUserName());
			if (null == existinguser) {
				throw new ValidationException("User does not exist");
			}
			S3Utils s3Util = new S3Utils();
			int ret = s3Util.uploadKey(request.getUserName(), request.getFoodFile(), request.getFileName());

			if (ret == -1)
				throw new Exception("Unable to upload image to S3");

			RekognitionUtils rekognitionUtilsUtil = new RekognitionUtils();
			CategoryEnum cat = rekognitionUtilsUtil.getfoodCategory(request.getUserName(), request.getFileName());
			System.out.println("cat: " + cat);
			GenericResponse response = new GenericResponse();
			FoodDetails foodDetails = new FoodDetails();
			foodDetails.setRequestId(UUID.randomUUID().toString());
			foodDetails.setAvailstatus("Available");
			foodDetails.setCategory(cat);
			foodDetails.setCreatedDate(new Date());
			foodDetails.setFileName(request.getFileName());
			foodDetails.setReqStatus("Created");
			foodDetails.setTotalQty(request.getTotalFoodCooked());
			foodDetails.setWasteQty(request.getFoodWasteQty());
			foodDetails.setUser(existinguser);
			foodDetails.setRejects(new ArrayList<String>());
			foodDetails.setCharityAccepted("");
			foodDetails.setPickUpTime(request.getPickUpTime());

			foodDetailsDao.upload(foodDetails);
			
			//send notifications
			MailerEvent event = new MailerEvent(foodDetails.getRequestId(), EventType.NOTIFY_CHARITY);
			eventProducer.pushEvents(event);
			
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	@Override
	public FoodDetailsResponse getFoodDetails(String userName) throws Exception {

		FoodDetailsResponse response = new FoodDetailsResponse();
		List<FoodDetailsDTO> foodDetailsDTOList = new ArrayList<FoodDetailsDTO>();
		List<FoodDetails> foodDetailsList = foodDetailsDao.getFoodDetails(userName);

		String cloudFrontURL = CommonUtils.FILE_PROPERTIES.get("cloudFrontURL");

		if (!CollectionUtils.isNullOrEmpty(foodDetailsList)) {
			for (FoodDetails fd : foodDetailsList) {
				foodDetailsDTOList.add(new FoodDetailsDTO(fd.getUser(), fd.getRequestId(),
						cloudFrontURL + fd.getUser().getUserName() + "/" + fd.getFileName(), fd.getCreatedDate(),
						fd.getAvailstatus(), fd.getTotalQty(), fd.getWasteQty(), fd.getCategory(), fd.getReqStatus(),
						fd.getRejects(), fd.getCharityAccepted(), fd.getPickUpTime()));

			}
			Collections.sort(foodDetailsDTOList, Collections.reverseOrder());
		}
		response.setFoodDetailsDTOList(foodDetailsDTOList);

		return response;
	}
	
	/**
	 * 
	 * @author Ashwini Shankar Narayan
 	 *
 	**/

	@Override
	public List<List> getGraphPlots(String userName) throws Exception {

		try {
			User existinguser = userDao.getUserByUserName(userName);
			if (null == existinguser) {
				throw new ValidationException("User does not exist");
			}
			String name = existinguser.getName();

			List<List> foodWastage = mlDataDao.fetchMLData(name);
			return foodWastage;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
