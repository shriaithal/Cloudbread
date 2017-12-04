package edu.sjsu.cloudbread.logger;

import com.amazonaws.regions.Regions;
/**
 * 
 * @author Anushri Srinath Aithal
 *
 */
@Deprecated
public class AppenderConstants {
  public static final String USER_AGENT_STRING = "kinesis-log4j-appender/1.0.1";
  // Default values
  public static final String DEFAULT_ENCODING = "UTF-8";
  public static final int DEFAULT_MAX_RETRY_COUNT = 3;
  public static final long DEFAULT_BACKOFF_INTERVAL_MSEC = 100;
  public static final int DEFAULT_BUFFER_SIZE = 2000;
  public static final int DEFAULT_THREAD_COUNT = 20;
  public static final int DEFAULT_SHUTDOWN_TIMEOUT_SEC = 30;
  public static final int DEFAULT_THREAD_KEEP_ALIVE_SEC = 30;
  public static final String DEFAULT_REGION = Regions.US_EAST_1.getName();
  public static final String DEFAULT_SERVICE_NAME = "kinesis";
}