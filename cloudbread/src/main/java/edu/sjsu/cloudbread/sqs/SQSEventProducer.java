package edu.sjsu.cloudbread.sqs;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import edu.sjsu.cloudbread.kinesis.events.MailerEvent;

/**
 * 
 * @author Anushri Srinath Aithal Anuradha Rajashekar
 *
 */
@Service
public class SQSEventProducer {

	private static final Logger LOG = Logger.getLogger(SQSEventProducer.class);
	private static final String queueUrl = "";

	AmazonSQS sqsClient;

	@PostConstruct
	private void init() {
		AWSCredentialsProvider awsCredentialsProvider = new ClasspathPropertiesFileCredentialsProvider(
				"config/application.properties");

		AmazonSQSClientBuilder clientBuilder = AmazonSQSClientBuilder.standard();
		clientBuilder.setRegion(Regions.US_EAST_1.getName());
		clientBuilder.setCredentials(awsCredentialsProvider);

		sqsClient = clientBuilder.build();
	}

	/**
	 * @author Anuradha Rajashekar Method to write to queue
	 * @param event
	 */
	public void pushEvents(MailerEvent event) {
		LOG.info("Publishing events :" + event);
		try {
			SendMessageRequest request = new SendMessageRequest().withQueueUrl(queueUrl)
					.withMessageBody(event.toJsonString());
			sqsClient.sendMessage(request);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

}
