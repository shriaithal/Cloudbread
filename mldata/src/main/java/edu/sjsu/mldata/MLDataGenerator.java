package edu.sjsu.mldata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;

public class MLDataGenerator {

	public AmazonRDS client;
	final static Logger LOG = Logger.getLogger(MLDataGenerator.class);

	public MLDataGenerator() {
		AWSCredentialsProvider awsCredentialsProvider = new ClasspathPropertiesFileCredentialsProvider(
				"config/application.properties");

		AmazonRDSClientBuilder clientBuilder = AmazonRDSClientBuilder.standard();
		clientBuilder.setRegion(Regions.US_EAST_1.getName());
		clientBuilder.setCredentials(awsCredentialsProvider);

		client = clientBuilder.build();
	}

	public static void main(String[] args) throws IOException {
		try {
			MLDataGenerator mlDataGenerator = new MLDataGenerator();
			mlDataGenerator.insertMLData("C:\\Users\\Shriaithal\\Desktop\\Final\\Cloudbread_MLData.csv");
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	private void insertMLData(String csvFile) {
		BufferedReader br = null;
		Connection dbConnection = null;

		try {
			
			Properties prop = new Properties();
			InputStream input = this.getClass().getResourceAsStream("/config/application.properties");
			prop.load(input);
			String db_hostname = prop.getProperty("db_hostname");
			String db_username = prop.getProperty("db_username");
			String db_password = prop.getProperty("db_password");
			String db_database = prop.getProperty("db_database");

			Class.forName("com.mysql.jdbc.Driver");
			String jdbc_url = "jdbc:mysql://" + db_hostname + "/" + db_database + "?user=" + db_username + "&password="
					+ db_password;

			String insertsql = " INSERT INTO mldata ( NAME, RATINGS, DISTANCE, DAY, OCCUPANCY, CAPACITY, WEATHER, FOODTYPE, GOODREVIEWS, BADREVIEWS, AMOUNTCOOKED, PRICERATING, WASTAGE, PREDICTED, CREATEDTIME, ACTUALTIME ) VALUES "
					+ " ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

			String line = "";
			String cvsSplitBy = ",";
			br = new BufferedReader(new FileReader(csvFile));
			dbConnection = DriverManager.getConnection(jdbc_url);
			int iteration = 0;
			int i = 1;
			
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, -7);
			while ((line = br.readLine()) != null) {
				
				if(iteration == 0) {
			        iteration++;  
			        continue;
			    }
				String[] mldata = line.split(cvsSplitBy);
				
				System.out.println(mldata[0] + mldata[1] + " iteration :" + i++);
				
				PreparedStatement statement = dbConnection.prepareStatement(insertsql);
				statement.setString(1, mldata[0]);
				statement.setBigDecimal(2, new BigDecimal(mldata[1]));
				statement.setBigDecimal(3, new BigDecimal(mldata[2]));
				statement.setString(4, mldata[3]);
				statement.setBigDecimal(5, new BigDecimal(mldata[4]));
				statement.setInt(6, Integer.parseInt(mldata[5]));
				statement.setString(7, mldata[6]);
				statement.setString(8, mldata[7]);
				statement.setInt(9, Integer.parseInt(mldata[8]));
				statement.setInt(10, Integer.parseInt(mldata[9]));
				statement.setBigDecimal(11, new BigDecimal(mldata[10]));
				statement.setBigDecimal(12, new BigDecimal(mldata[11]));
				statement.setBigDecimal(13, new BigDecimal(mldata[12]));
				statement.setString(14, "N");
				
				statement.setTimestamp(15, new Timestamp(date.getTimeInMillis()));
				statement.setTimestamp(16, new Timestamp(date.getTimeInMillis()));

				statement.executeUpdate();
			}

		} catch (Exception e) {
			LOG.error(e);

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (Exception e) {
					LOG.error(e);
				}
			}
		}

	}
}
