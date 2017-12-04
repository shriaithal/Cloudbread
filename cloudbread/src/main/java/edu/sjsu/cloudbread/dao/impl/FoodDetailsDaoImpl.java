package edu.sjsu.cloudbread.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import edu.sjsu.cloudbread.dao.FoodDetailsDao;
import edu.sjsu.cloudbread.model.FoodDetails;

@Repository
public class FoodDetailsDaoImpl implements FoodDetailsDao {

	@Autowired
	DynamoDbClient dbClient;
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	@Override
	public void upload(FoodDetails foodDetails) throws Exception {
		AmazonDynamoDB dynamoDB = dbClient.getDynamoDB();
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
		mapper.save(foodDetails);
	}
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	public List<FoodDetails> getFoodDetails(String userName) throws Exception {
		AmazonDynamoDB dynamoDB = dbClient.getDynamoDB();
		System.out.println("Start");
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
		Map<String, AttributeValue> userValues = new HashMap<String, AttributeValue>();
		userValues.put(":UserVal", new AttributeValue().withS(userName));

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("userDetails.UserName = :UserVal").withExpressionAttributeValues(userValues);

		List<FoodDetails> scanResultFoodDetails = mapper.scan(FoodDetails.class, scanExpression);

		System.out.println("Done");
		System.out.println(scanResultFoodDetails.size());

		return scanResultFoodDetails;

	}
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	public List<FoodDetails> getFoodDetailsCharity() throws Exception {
		AmazonDynamoDB dynamoDB = dbClient.getDynamoDB();

		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

		List<FoodDetails> scanResultFoodDetails = mapper.scan(FoodDetails.class, scanExpression);

		System.out.println("Done retrieving list");

		return scanResultFoodDetails;

	}

	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	@Override
	public void updateStatus(String requestId, String status, String userName) throws Exception {
		AmazonDynamoDB dynamoDB = dbClient.getDynamoDB();
		DynamoDB d = new DynamoDB(dynamoDB);
		UpdateItemSpec updateItemSpec = null;
		Table table = d.getTable("FoodDetails");
		if (status.equals("Accept")) {
			updateItemSpec = new UpdateItemSpec().withPrimaryKey("RequestId", requestId)
					.withUpdateExpression("set ReqStatus = :r, CharityAccepted = :c")
					.withValueMap(new ValueMap().withString(":r", "Accepted").withString(":c", userName))
					.withReturnValues(ReturnValue.UPDATED_NEW);

		} else {
			FoodDetails fTemp = getFoodDetailsByRequestId(requestId);

			List<String> newRejects = fTemp.getRejects();
			newRejects.add(userName);
			// key, val).withStringSet
			updateItemSpec = new UpdateItemSpec().withPrimaryKey("RequestId", requestId)
					.withUpdateExpression("set Rejects=:prepend_value")
					.withValueMap(new ValueMap().withList(":prepend_value", newRejects))
					.withReturnValues(ReturnValue.UPDATED_NEW);
		}
		try {
			System.out.println("Updating the item...");
			UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
			System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	/**
	 * 
	 * @author Anushri Srinath Aithal 
 	 *
 	**/
	@Override
	public FoodDetails getFoodDetailsByRequestId(String requestId) {
		AmazonDynamoDB dynamoDB = dbClient.getDynamoDB();
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
		FoodDetails foodDetails = mapper.load(FoodDetails.class, requestId);
		return foodDetails;
	}

}
