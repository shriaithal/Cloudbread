package edu.sjsu.cloudbread.utils;

import java.io.BufferedOutputStream;
import java.io.File;
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

public class S3Utils {
	final static Logger logger = Logger.getLogger(S3Utils.class);
	private static AmazonS3 s3client = null;

	public AmazonS3 getS3Client() {
		try {
			if (s3client == null) {
				AWSCredentialsProvider awsCredentialsProvider = new ClasspathPropertiesFileCredentialsProvider(
						"config/application.properties");
				awsCredentialsProvider.getCredentials();
				s3client = new AmazonS3Client(awsCredentialsProvider);

			}
		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
			return null;
		} catch (AmazonClientException ace) {
			ace.printStackTrace();
			return null;
		}
		return s3client;
	}
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 * Function to upload object at S3 location
	 *
 	**/	
	public int uploadKey(String username, MultipartFile file, String fileName) {
		System.out.println("in UP: " + username);
		AmazonS3 s3client = getS3Client();

		byte[] bytes = null;
		try {
			bytes = file.getBytes();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String serverPath = System.getProperty("catalina.home");
		File dir = new File(serverPath + File.separator + "tmpFiles");
		if (!dir.exists())
			dir.mkdirs();

		File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getName());
		BufferedOutputStream stream = null;
		try {
			stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		try {
			stream.write(bytes);

			stream.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

		logger.info("Server File Location=" + serverFile.getAbsolutePath());
		try {
			s3client.putObject(new PutObjectRequest(CommonUtils.FILE_PROPERTIES.get("basebucket"),
					username + "/" + fileName, serverFile));
		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
			return -1;
		} catch (AmazonClientException ace) {
			ace.printStackTrace();
			return -1;
		}
		serverFile.delete();
		return 1;
	}
	
	/**
	 * 
	 * @author Nidhi Jamar
 	 * Function to create bucket for new registered user
	 *
 	**/	
	public int createBucket(String username) {
		System.out.println("Creating Bucket for User: " + username);
		try {
			getS3Client();

			if (s3client != null) {
				final InputStream inpStr = new InputStream() {
					@Override
					public int read() throws IOException {
						return -1;
					}
				};
				final ObjectMetadata objMeta = new ObjectMetadata();
				objMeta.setContentLength(0L);
				s3client.putObject(new PutObjectRequest(CommonUtils.FILE_PROPERTIES.get("basebucket"), username + "/",
						inpStr, objMeta));
			} else {
				logger.error("Error fetching instance of S3client.");
				return -1;
			}
		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
			return -1;
		} catch (AmazonClientException ace) {
			ace.printStackTrace();
			return -1;
		}
		logger.info("Created Bucket for User: " + username);
		return 1;
	}

}
