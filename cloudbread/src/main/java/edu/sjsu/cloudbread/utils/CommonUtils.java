package edu.sjsu.cloudbread.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class CommonUtils {
	final static Logger logger = Logger.getLogger(CommonUtils.class);

	public static Map<String, String> FILE_PROPERTIES = new HashMap<String, String>();
	/**
	 * 
 	* @author Nidhi Jamar
 	*
 	*/
	static {
		Properties properties = new Properties();
		File file = null;
		try {
			file = new ClassPathResource("config/application.properties").getFile();
			InputStream input = new FileInputStream(file);
			if (input != null) {
				try {
					properties.load(input);
				} catch (IOException e) {
					// LOG.error("I/O exception", e);
				} catch (IllegalArgumentException e) {
					// LOG.error("Malfunction properties format", e);
				}

				for (String key : properties.stringPropertyNames()) {

					String value = properties.getProperty(key);

					FILE_PROPERTIES.put(key, value);
				}
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
