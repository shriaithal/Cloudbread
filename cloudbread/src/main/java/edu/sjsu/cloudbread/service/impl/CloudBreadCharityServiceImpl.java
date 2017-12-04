package edu.sjsu.cloudbread.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.util.CollectionUtils;

import edu.sjsu.cloudbread.dao.FoodDetailsDao;
import edu.sjsu.cloudbread.dto.FoodDetailsDTO;
import edu.sjsu.cloudbread.kinesis.events.EventType;
import edu.sjsu.cloudbread.kinesis.events.MailerEvent;
import edu.sjsu.cloudbread.model.FoodDetails;
import edu.sjsu.cloudbread.response.FoodDetailsResponse;
import edu.sjsu.cloudbread.service.CloudBreadCharityService;
import edu.sjsu.cloudbread.sqs.SQSEventProducer;
import edu.sjsu.cloudbread.utils.CommonUtils;

@Service
public class CloudBreadCharityServiceImpl implements CloudBreadCharityService {

	@Autowired
	FoodDetailsDao foodDetailsDao;
	
	@Autowired
	SQSEventProducer eventProducer;
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	@Override
	public FoodDetailsResponse getFoodDetailsCharity(String userName) throws Exception {

		FoodDetailsResponse response = new FoodDetailsResponse();
		List<FoodDetailsDTO> foodDetailsDTOList = new ArrayList<FoodDetailsDTO>();
		List<FoodDetails> foodDetailsList = foodDetailsDao.getFoodDetailsCharity();

		String cloudFrontURL = CommonUtils.FILE_PROPERTIES.get("cloudFrontURL");

		if (!CollectionUtils.isNullOrEmpty(foodDetailsList)) {
			for (FoodDetails fd : foodDetailsList) {

				// if charity is already accepted or
				// if charity request is previously rejected by curr user, it is not shown to
				// any charities now
				if ((fd.getReqStatus().equals("Created") && !fd.getRejects().contains(userName))
						|| (fd.getReqStatus().equals("Accepted") && fd.getCharityAccepted().equals(userName))) {
					foodDetailsDTOList.add(new FoodDetailsDTO(fd.getUser(), fd.getRequestId(),
							cloudFrontURL + fd.getUser().getUserName() + "/" + fd.getFileName(), fd.getCreatedDate(),
							fd.getAvailstatus(), fd.getTotalQty(), fd.getWasteQty(), fd.getCategory(),
							fd.getReqStatus(), fd.getRejects(), fd.getCharityAccepted(), fd.getPickUpTime()));

				}

				Collections.sort(foodDetailsDTOList, Collections.reverseOrder());

			}

		}

		response.setFoodDetailsDTOList(foodDetailsDTOList);

		return response;
	}
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	@Override
	public void updateStatus(String requestId, String status, String userName) throws Exception {
		// GenericResponse response= new GenericResponse();
		foodDetailsDao.updateStatus(requestId, status, userName);
		if("Accept".equals(status)) {
			MailerEvent event = new MailerEvent(requestId, EventType.NOTIFY_BUSSINESS);
			eventProducer.pushEvents(event);
		}

	}

}
