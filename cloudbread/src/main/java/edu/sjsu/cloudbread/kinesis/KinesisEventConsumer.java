package edu.sjsu.cloudbread.kinesis;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.GetRecordsRequest;
import com.amazonaws.services.kinesis.model.GetRecordsResult;
import com.amazonaws.services.kinesis.model.GetShardIteratorRequest;
import com.amazonaws.services.kinesis.model.Shard;
import com.amazonaws.services.kinesis.model.ShardIteratorType;

import edu.sjsu.cloudbread.kinesis.events.MailerEvent;
import edu.sjsu.cloudbread.ses.EmailClient;

/**
 * 
 * @author Anushri Srinath Aithal
 *
 */
//@Configuration
//@EnableScheduling
@Deprecated
public class KinesisEventConsumer {

	private static final Logger LOG = Logger.getLogger(KinesisEventConsumer.class);
	private static final String STREAM_NAME = "cloudbreadStream";

	private AmazonKinesis kinesisClient;
	
	@Autowired
	EmailClient emailClient;

	@PostConstruct
	private void init() {
		AWSCredentialsProvider awsCredentialsProvider = new ClasspathPropertiesFileCredentialsProvider(
				"config/application.properties");

		AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();
		clientBuilder.setRegion(Regions.US_EAST_1.getName());
		clientBuilder.setCredentials(awsCredentialsProvider);

		kinesisClient = clientBuilder.build();
	}

	//@Scheduled(cron = "0 0/45 * * * ?")
	public void receiveKinesisEvent() {

		List<Shard> initialShardData = kinesisClient.describeStream(STREAM_NAME).getStreamDescription().getShards();

		LOG.info("\nlist of shards:");
		initialShardData.forEach(d -> LOG.info(d.toString()));

		// Getting shardIterators (at beginning sequence number) for reach shard
		List<String> initialShardIterators = initialShardData
				.stream().map(
						s -> kinesisClient
								.getShardIterator(new GetShardIteratorRequest().withStreamName(STREAM_NAME)
										.withShardId(s.getShardId())
										.withStartingSequenceNumber(
												s.getSequenceNumberRange().getStartingSequenceNumber())
										.withShardIteratorType(ShardIteratorType.AT_SEQUENCE_NUMBER))
								.getShardIterator())
				.collect(Collectors.toList());

		LOG.info("\nlist of ShardIterators:");
		initialShardIterators.forEach(i -> LOG.info(i));
		LOG.info("\nwaiting for messages....");

		String shardIterator = initialShardIterators.get(0);
		long recordNum = 0;
		final Integer INTERVAL = 2000;

		while (true) {

			GetRecordsRequest getRecordsRequest = new GetRecordsRequest();
			getRecordsRequest.setShardIterator(shardIterator);
			getRecordsRequest.setLimit(25);

			GetRecordsResult recordResult = kinesisClient.getRecords(getRecordsRequest);

			recordResult.getRecords().forEach(record -> {
				try {
					MailerEvent event = MailerEvent.fromJsonAsBytes(record.getData().array());
					LOG.info(event.toString());
					emailClient.sendEmails(event);
				} catch (Exception e) {
					LOG.error("Could not decode message from Kinesis stream result" + e);
				}
			});

			recordNum += recordResult.getRecords().size();
			LOG.info("\nReceived " + recordNum + " records. sleep for " + INTERVAL / 1000 + "s ...");
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException exception) {
				LOG.error("Receving InterruptedException. Exiting ...");
				return;
			}
			shardIterator = recordResult.getNextShardIterator();
		}
	}
}
