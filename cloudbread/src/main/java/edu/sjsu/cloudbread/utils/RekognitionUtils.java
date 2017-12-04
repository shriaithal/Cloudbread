package edu.sjsu.cloudbread.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.S3Object;

import edu.sjsu.cloudbread.model.FoodDetails;

public class RekognitionUtils {
	final static Logger logger = Logger.getLogger(RekognitionUtils.class);
	private static AmazonRekognition rekognitionClient = null;

	private static Map<String, FoodDetails.CategoryEnum> categoryLabels = new HashMap<String, FoodDetails.CategoryEnum>();
	private static Set<String> raw = new HashSet<String>(Arrays.asList("Flora", "Salad", "Vegetable"));
	private static Set<String> processed = new HashSet<String>(
			Arrays.asList("Beverage", "Food", "Buffet", "Chocolate", "Dessert", "Grain"));
	private static Set<String> canned = new HashSet<String>(Arrays.asList("Water", "Tin", "Aluminium"));

	public RekognitionUtils() {
		categoryLabels.put("Beverage", FoodDetails.CategoryEnum.Processed);
		categoryLabels.put("Flora", FoodDetails.CategoryEnum.Raw);
		categoryLabels.put("Water", FoodDetails.CategoryEnum.Canned);
		categoryLabels.put("Food", FoodDetails.CategoryEnum.Processed);
		categoryLabels.put("Buffet", FoodDetails.CategoryEnum.Processed);
		categoryLabels.put("Salad", FoodDetails.CategoryEnum.Raw);

	}
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	public AmazonRekognition getRekognitionClient() {
		try {
			if (rekognitionClient == null) {
				AWSCredentialsProvider awsCredentialsProvider = new ClasspathPropertiesFileCredentialsProvider(
						"config/application.properties");
				awsCredentialsProvider.getCredentials();
				rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_EAST_1)
						.withCredentials(awsCredentialsProvider).build();

			}

		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
			return null;
		} catch (AmazonClientException ace) {
			ace.printStackTrace();
			return null;
		}
		return rekognitionClient;
	}
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
	public FoodDetails.CategoryEnum getfoodCategory(String username, String fileName) {
		if (rekognitionClient == null)
			getRekognitionClient();

		String key = username + "/" + fileName;
		String bucketName = CommonUtils.FILE_PROPERTIES.get("basebucket");

		DetectLabelsRequest request = new DetectLabelsRequest()
				.withImage(new Image().withS3Object(new S3Object().withName(key).withBucket(bucketName)))
				.withMaxLabels(10).withMinConfidence(75F);

		logger.info("key ::  " + key);
		logger.info("bucketName :: " + bucketName);

		FoodDetails.CategoryEnum ret = null;
		try {
			DetectLabelsResult result = rekognitionClient.detectLabels(request);
			List<Label> labels = result.getLabels();
			Set<String> categoriesIdentified = new HashSet<String>();
			System.out.println("Detected labels for " + key);
			System.out.println("labels " + labels);
			logger.info("Detected labels for " + key);
			logger.info("labels " + labels);

			if (labels != null) {

				logger.info(labels.get(0).getName() + ": " + labels.get(0).getConfidence().toString());

				for (Label label : labels) {
					categoriesIdentified.add(label.getName());
				}

				if (CollectionUtils.containsAny(categoriesIdentified, raw)) {
					ret = FoodDetails.CategoryEnum.Raw;
				} else if (CollectionUtils.containsAny(categoriesIdentified, canned)) {
					ret = FoodDetails.CategoryEnum.Canned;
				} else {
					ret = FoodDetails.CategoryEnum.Processed;
				}
				/*
				 * if (categoryLabels.containsKey(labels.get(0).getName())) { ret =
				 * categoryLabels.get(labels.get(0).getName()); logger.info("lbl: " +
				 * labels.get(0).getName()); } else ret = FoodDetails.CategoryEnum.Processed; }
				 * else { ret = FoodDetails.CategoryEnum.Processed; }
				 */
				System.out.println(ret);
				logger.info("ret: " + ret.toString());
			}

		} catch (AmazonRekognitionException e) {
			logger.error(e);
			return ret;
		}
		return ret;
	}

}
