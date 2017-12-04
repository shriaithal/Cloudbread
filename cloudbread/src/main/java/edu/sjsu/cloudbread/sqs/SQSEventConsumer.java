package edu.sjsu.cloudbread.sqs;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;

import edu.sjsu.cloudbread.kinesis.events.MailerEvent;
import edu.sjsu.cloudbread.ses.EmailClient;

/**
 * 
 * @author Anushri Srinath Aithal Anuradha Rajashekar
 *
 */
@Configuration
@EnableScheduling
public class SQSEventConsumer {

	private static final Logger LOG = Logger.getLogger(SQSEventProducer.class);
	private static final String queueUrl = "";

	@Autowired
	EmailClient emailClient;

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
	 * Scheduler continuously polling to read from queue
	 * 
	 * @author Anushri Srinath Aithal Anuradha Rajashekar
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void receiveEvents() {
		// LOG.info("SQS Consumer Running");
		try {
			List<Message> messages = sqsClient.receiveMessage(queueUrl).getMessages();
			if (!CollectionUtils.isEmpty(messages)) {
				for (Message msg : messages) {
					String msgBody = msg.getBody();
					MailerEvent event = MailerEvent.fromJsonString(msgBody);

					LOG.info("SQS Event ID :" + event.getId() + " type : " + event.getEventType());

					emailClient.sendEmails(event);
					sqsClient.deleteMessage(queueUrl, msg.getReceiptHandle());
				}
			}
		} catch (Exception e) {
			LOG.error(e);
		}
	}

}
