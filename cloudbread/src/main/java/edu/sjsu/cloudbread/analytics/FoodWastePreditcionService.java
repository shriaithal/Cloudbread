package edu.sjsu.cloudbread.analytics;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.machinelearning.AmazonMachineLearning;
import com.amazonaws.services.machinelearning.AmazonMachineLearningClientBuilder;
import com.amazonaws.services.machinelearning.model.CreateDataSourceFromS3Request;
import com.amazonaws.services.machinelearning.model.CreateEvaluationRequest;
import com.amazonaws.services.machinelearning.model.CreateMLModelRequest;
import com.amazonaws.services.machinelearning.model.CreateRealtimeEndpointRequest;
import com.amazonaws.services.machinelearning.model.GetMLModelRequest;
import com.amazonaws.services.machinelearning.model.GetMLModelResult;
import com.amazonaws.services.machinelearning.model.MLModelType;
import com.amazonaws.services.machinelearning.model.PredictRequest;
import com.amazonaws.services.machinelearning.model.PredictResult;
import com.amazonaws.services.machinelearning.model.S3DataSpec;

import edu.sjsu.cloudbread.dao.MLDataDao;
import edu.sjsu.cloudbread.dao.ScheduleDataDao;
import edu.sjsu.cloudbread.dao.UserDao;
import edu.sjsu.cloudbread.model.MLData;
import edu.sjsu.cloudbread.model.ScheduleData;
import edu.sjsu.cloudbread.model.User;
import edu.sjsu.cloudbread.service.WeatherForecastService;

/**
 * Class to automate ML predicitions
 * @author Anushri Srinath Aithal
 *
 */
@Configuration
@EnableScheduling
public class FoodWastePreditcionService {

	private static final Logger LOG = Logger.getLogger(FoodWastePreditcionService.class);
	private static final String trainingData = "";
	private static final String dataSchema = "";
	private static Boolean isStarted = Boolean.FALSE;
	AmazonMachineLearning mlClient;

	@Autowired
	ScheduleDataDao scheduleDataDao;

	@Autowired
	MLDataDao mlDataDao;

	@Autowired
	UserDao userDao;

	@Autowired
	WeatherForecastService weatherForecastService;

	Map<Integer, String> daysOfWeekMap = new HashMap<Integer, String>();

	@PostConstruct
	private void init() {
		AWSCredentialsProvider awsCredentialsProvider = new ClasspathPropertiesFileCredentialsProvider(
				"config/application.properties");

		AmazonMachineLearningClientBuilder builder = AmazonMachineLearningClientBuilder.standard()
				.withCredentials(awsCredentialsProvider).withRegion(Regions.US_EAST_1.getName());

		mlClient = builder.build();

		daysOfWeekMap.put(1, "Sunday");
		daysOfWeekMap.put(2, "Monday");
		daysOfWeekMap.put(3, "Tuesday");
		daysOfWeekMap.put(4, "Wednesday");
		daysOfWeekMap.put(5, "Thursday");
		daysOfWeekMap.put(6, "Friday");
		daysOfWeekMap.put(7, "Saturday");
		

	}

	/**
	 * Scheduler running every 5th day to predict for next 5 days
	 */
	@Scheduled(cron = "0 0 0 0/5 * ?")
	public void generatePredictions() {
		LOG.info("Runing scheduler");
		try {
			ScheduleData scheduleData = scheduleDataDao.getLatestData();
			List<MLData> results = getPredictions(scheduleData.getModelId());
			insertPredictedData(results);
			insertNewScheduleData(scheduleData);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	private void insertPredictedData(List<MLData> results) {
		mlDataDao.insertMLData(results);
	}

	private void insertNewScheduleData(ScheduleData scheduleData) {
		ScheduleData newScheduleData = new ScheduleData();
		newScheduleData.setModelCreationDate(scheduleData.getModelCreationDate());
		newScheduleData.setModelId(scheduleData.getModelId());
		newScheduleData.setNewModelCreated("N");
		newScheduleData.setSchedulerLastRunDate(new Timestamp(System.currentTimeMillis()));
		scheduleDataDao.insert(newScheduleData);
	}

	private List<MLData> getPredictions(String mlModelId) {
		LOG.info("Prediction started");

		String predictionEndpoint = lookupEndpoint(mlModelId);
		LOG.info("Prediction End point :" + predictionEndpoint);

		List<String> weatherList = weatherForecastService.getForecast();
		LOG.info("Weather for the following week : " + weatherList);

		List<MLData> predictionResult = new ArrayList<MLData>();
		List<User> users = userDao.getPredictionEnabledUsers();
		LOG.info("Predicting for users : " + users.size());

		for (User user : users) {

			Map<String, String> attributeMap = new HashMap<String, String>();
			attributeMap.put("Name", user.getName());
			attributeMap.put("Ratings", user.getRatings().toString());
			attributeMap.put("Distance", user.getDistance().toString());
			attributeMap.put("Occupency", user.getOccupancy().toString());
			attributeMap.put("Capacity", user.getCapacity().toString());
			attributeMap.put("foodType", user.getFoodType());
			attributeMap.put("goodReviews", user.getGoodReviews().toString());
			attributeMap.put("badReviews", user.getBadReviews().toString());
			attributeMap.put("Price_rating", user.getPriceRating().toString());
			attributeMap.put("AmountofFoodCookedfor", "0");
			attributeMap.put("Wastage", "0");

			for (int i = 0; i < 5; i++) {
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, i + 1);
				Integer dayOfWeekNum = calendar.get(Calendar.DAY_OF_WEEK);
				calendar.get(Calendar.DATE);
				String day = daysOfWeekMap.get(dayOfWeekNum);
				String weather = weatherList.get(i);

				attributeMap.put("Day", day);
				attributeMap.put("Weather", weather);

				PredictResult result = predict(mlModelId, predictionEndpoint, attributeMap);
				MLData mlData = new MLData();
				mlData.setName(user.getName());
				mlData.setRatings(user.getRatings());
				mlData.setDistance(user.getDistance());
				mlData.setOccupancy(user.getOccupancy());
				mlData.setCapacity(user.getCapacity());
				mlData.setFoodType(user.getFoodType());
				mlData.setGoodReviews(user.getGoodReviews());
				mlData.setBadReviews(user.getBadReviews());
				mlData.setAmountCooked(new BigDecimal("0"));
				mlData.setPriceRating(user.getPriceRating());
				mlData.setWeather(weather);
				mlData.setDay(day);
				mlData.setPredicted("Y");
				mlData.setCreatedTime(new Timestamp(System.currentTimeMillis()));
				mlData.setWastage(new BigDecimal(result.getPrediction().getPredictedValue()));

				mlData.setActualTime(new Timestamp(calendar.getTimeInMillis()));

				predictionResult.add(mlData);

			}
		}
		return predictionResult;
	}

	private PredictResult predict(String mlModelId, String predictionEndpoint, Map<String, String> record) {
		PredictRequest request = new PredictRequest().withMLModelId(mlModelId).withPredictEndpoint(predictionEndpoint)
				.withRecord(record);
		PredictResult result = mlClient.predict(request);
		return result;
	}

	private String lookupEndpoint(String mlModelId) {
		GetMLModelRequest request = new GetMLModelRequest().withMLModelId(mlModelId);
		GetMLModelResult model = mlClient.getMLModel(request);
		String predictionEndpoint = model.getEndpointInfo().getEndpointUrl();
		return predictionEndpoint;
	}

	public void buildPrediticionModel() {
		try {
			while (!isStarted) {
				isStarted = Boolean.TRUE;
				Map<String, String> resultMap = createDataSources();
				String mlModelId = createModel(resultMap.get("trainingDataSourceId"),
						resultMap.get("trainingDataSourceName"));
				createEvaluation(resultMap.get("testDataSourceId"), resultMap.get("testDataSourceName"), mlModelId);
				createRealTimeEndPoint(mlModelId);
				getPredictions(mlModelId);
				isStarted = Boolean.FALSE;
			}

		} catch (Exception e) {
			LOG.error(e);
			isStarted = Boolean.FALSE;
		}
	}

	private void createEvaluation(String testDataSourceId, String testDataSourceName, String mlModelId) {
		CreateEvaluationRequest request = new CreateEvaluationRequest().withEvaluationDataSourceId(testDataSourceId)
				.withEvaluationName(testDataSourceName + " evaluation").withMLModelId(mlModelId);

		mlClient.createEvaluation(request);
	}

	private Map<String, String> createDataSources() {
		Map<String, String> resultMap = new HashMap<String, String>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
		String date = simpleDateFormat.format(new Date());
		String trainingEntityId = UUID.randomUUID().toString();
		String trainingEntityName = "Wastage_0_70" + date;
		resultMap.put("trainingDataSourceId", trainingEntityId);
		resultMap.put("trainingDataSourceName", trainingEntityName);

		createDataSource(trainingEntityId, trainingEntityName, 0, 70);

		String testEntityId = UUID.randomUUID().toString();
		String testEntityName = "Wastage_70_100" + date;
		resultMap.put("testDataSourceId", testEntityId);
		resultMap.put("testDataSourceName", testEntityName);

		createDataSource(testEntityId, testEntityName, 70, 100);

		return resultMap;
	}

	private String createModel(String entityId, String entityName) {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("sgd.maxPasses", "100");
		parameters.put("sgd.maxMLModelSizeInBytes", "104857600"); // 100 MiB
		parameters.put("sgd.l2RegularizationAmount", "1e-4");

		String mlModelId = UUID.randomUUID().toString();
		CreateMLModelRequest request = new CreateMLModelRequest().withMLModelId(mlModelId)
				.withMLModelName(entityName + " model").withMLModelType(MLModelType.REGRESSION)
				.withParameters(parameters).withTrainingDataSourceId(entityId);

		mlClient.createMLModel(request);
		return mlModelId;
	}

	private void createDataSource(String entityId, String entityName, Integer percentBegin, Integer percentEnd) {
		String dataRearrangement = "{\"splitting\":{\"percentBegin\":" + percentBegin + ",\"percentEnd\":" + percentEnd
				+ "}}";

		CreateDataSourceFromS3Request request = new CreateDataSourceFromS3Request().withDataSourceId(entityId)
				.withDataSourceName(entityName).withComputeStatistics(true);
		S3DataSpec dataSpec = new S3DataSpec().withDataLocationS3(trainingData).withDataRearrangement(dataRearrangement)
				.withDataSchemaLocationS3(dataSchema);
		request.setDataSpec(dataSpec);

		mlClient.createDataSourceFromS3(request);

		/*
		 * RDSDatabase databaseInformation = new
		 * RDSDatabase().withInstanceIdentifier(properties.get("db_hostname"))
		 * .withDatabaseName("db_database"); RDSDatabaseCredentials
		 * awsCredentialsProvider = new RDSDatabaseCredentials()
		 * .withUsername(properties.get("db_username")).withPassword(properties.get(
		 * "db_password"));
		 * 
		 * RDSDataSpec rDSData = new
		 * RDSDataSpec().withDatabaseInformation(databaseInformation)
		 * .withDatabaseCredentials(awsCredentialsProvider).withDataSchema(dataSchema)
		 * .withSelectSqlQuery(selectQuery).withDataRearrangement(dataRearrangement);
		 * CreateDataSourceFromRDSRequest request = new CreateDataSourceFromRDSRequest()
		 * .withDataSourceId(UUID.randomUUID().toString()).
		 * withDataSourceName("Wastage Prediction")
		 * .withRDSData(rDSData).withComputeStatistics(true).withRoleARN(
		 * "arn:aws:iam::948121461290:role/ML_Role");
		 * 
		 * mlClient.createDataSourceFromRDS(request);
		 */
	}

	private void createRealTimeEndPoint(String mlModelId) {
		waitForModel(mlModelId);
		CreateRealtimeEndpointRequest request = new CreateRealtimeEndpointRequest().withMLModelId(mlModelId);
		mlClient.createRealtimeEndpoint(request);
	}

	private void waitForModel(String mlModelId) {
		long delay = 2000;
		Random random = new Random();
		while (true) {
			GetMLModelRequest request = new GetMLModelRequest().withMLModelId(mlModelId);
			GetMLModelResult model = mlClient.getMLModel(request);
			LOG.info(" Model : " + mlModelId + " Status :" + model.getStatus() + " Time : " + (new Date()).toString());
			switch (model.getStatus()) {
			case "COMPLETED":
			case "FAILED":
			case "INVALID":
				// These are terminal states
				return;
			}

			// exponential backoff with Jitter
			delay *= 1.1 + random.nextFloat();
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				LOG.error(e);
				return;
			}
		}

	}
}
