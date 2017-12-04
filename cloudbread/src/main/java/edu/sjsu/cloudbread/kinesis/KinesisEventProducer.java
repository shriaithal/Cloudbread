package edu.sjsu.cloudbread.kinesis;

import java.nio.ByteBuffer;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.DescribeStreamResult;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.ResourceNotFoundException;
import edu.sjsu.cloudbread.kinesis.events.MailerEvent;

/**
 * 
 * @author Anushri Srinath Aithal
 *
 */
//@Service
@Deprecated
public class KinesisEventProducer {

	private static final Logger LOG = Logger.getLogger(KinesisEventProducer.class);
	private static final String STREAM_NAME = "cloudbreadStream";

	private AmazonKinesis kinesisClient;

	@PostConstruct
	private void init() {
		AWSCredentialsProvider awsCredentialsProvider = new ClasspathPropertiesFileCredentialsProvider(
				"config/application.properties");

		AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();
		clientBuilder.setRegion(Regions.US_EAST_1.getName());
		clientBuilder.setCredentials(awsCredentialsProvider);

		kinesisClient = clientBuilder.build();
	}

	private static boolean validateStream(AmazonKinesis kinesisClient, String streamName) {
		boolean retVal = true;
		try {
			DescribeStreamResult result = kinesisClient.describeStream(streamName);
			if (!"ACTIVE".equals(result.getStreamDescription().getStreamStatus())) {
				LOG.error("Stream " + streamName + " is not active. Please wait a few moments and try again.");
				retVal = false;
			}
		} catch (ResourceNotFoundException e) {
			LOG.error("Stream " + streamName + " does not exist. Please create it in the console.");
			LOG.error(e);
			retVal = false;
		} catch (Exception e) {
			LOG.error("Error found while describing the stream " + streamName);
			LOG.error(e);
			retVal = false;
		}
		return retVal;
	}

	public void sendKinesisEvents(MailerEvent event) {

		boolean isValid = validateStream(kinesisClient, STREAM_NAME);
		if (!isValid) {
			return;
		}

		byte[] bytes = event.toJsonAsBytes();
		if (bytes == null) {
			LOG.error("Could not get JSON bytes for event");
			return;
		}

		LOG.info("Putting event: " + event.toString());
		PutRecordRequest putRecord = new PutRecordRequest();
		putRecord.setStreamName(STREAM_NAME);
		putRecord.setPartitionKey(UUID.randomUUID().toString());
		putRecord.setData(ByteBuffer.wrap(bytes));

		try {
			kinesisClient.putRecord(putRecord);
		} catch (AmazonClientException ex) {
			LOG.error("Error sending record to Amazon Kinesis.", ex);
		}

	}

}
