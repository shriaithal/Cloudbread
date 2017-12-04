package edu.sjsu.cloudbread.logger;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClient;
import com.amazonaws.services.kinesis.model.PutRecordRequest;

/**
 * 
 * @author Anushri Srinath Aithal
 *
 */
@Deprecated
public class KinesisAppender extends AppenderSkeleton {

	private static final Logger LOGGER = Logger.getLogger(KinesisAppender.class);
	private BlockingQueue<Runnable> taskBuffer;
	private AmazonKinesisAsyncClient kinesisClient;

	@Override
	public void close() {
		ThreadPoolExecutor threadpool = (ThreadPoolExecutor) kinesisClient.getExecutorService();
		threadpool.shutdown();
		BlockingQueue<Runnable> taskQueue = threadpool.getQueue();
		int bufferSizeBeforeShutdown = threadpool.getQueue().size();
		boolean gracefulShutdown = true;
		try {
			gracefulShutdown = threadpool.awaitTermination(AppenderConstants.DEFAULT_SHUTDOWN_TIMEOUT_SEC, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// we are anyways cleaning up
		} finally {
			int bufferSizeAfterShutdown = taskQueue.size();
			if (!gracefulShutdown || bufferSizeAfterShutdown > 0) {
				String errorMsg = "Kinesis Log4J Appender (" + name + ") waited for " + AppenderConstants.DEFAULT_SHUTDOWN_TIMEOUT_SEC
						+ " seconds before terminating but could send only "
						+ (bufferSizeAfterShutdown - bufferSizeBeforeShutdown) + " logevents, it failed to send "
						+ bufferSizeAfterShutdown + " pending log events from it's processing queue";
				LOGGER.error(errorMsg);
				errorHandler.error(errorMsg, null, ErrorCode.WRITE_FAILURE);
			}
		}
		kinesisClient.shutdown();
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	public void activateOptions() {

		taskBuffer = new LinkedBlockingDeque<Runnable>(AppenderConstants.DEFAULT_BUFFER_SIZE);
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(AppenderConstants.DEFAULT_THREAD_COUNT,
				AppenderConstants.DEFAULT_THREAD_COUNT, AppenderConstants.DEFAULT_THREAD_KEEP_ALIVE_SEC,
				TimeUnit.SECONDS, taskBuffer);
		threadPoolExecutor.prestartAllCoreThreads();
		AWSCredentials awsCredentials = new BasicAWSCredentials("",
				"");
		kinesisClient = new AmazonKinesisAsyncClient(awsCredentials, threadPoolExecutor);
	}

	@Override
	protected void append(LoggingEvent event) {
		String streamName = "testStream";
		try {
			String message = layout.format(event);
			ByteBuffer data = ByteBuffer.wrap(message.getBytes(AppenderConstants.DEFAULT_ENCODING));
			kinesisClient.putRecordAsync(new PutRecordRequest().withPartitionKey(UUID.randomUUID().toString())
					.withStreamName(streamName).withData(data));
		} catch (Exception e) {
			LOGGER.error("Failed to schedule log entry for publishing into Kinesis stream: " + streamName);
			errorHandler.error("Failed to schedule log entry for publishing into Kinesis stream: " + streamName, e,
					ErrorCode.WRITE_FAILURE, event);
		}
	}

}
