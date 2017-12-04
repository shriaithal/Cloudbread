package edu.sjsu.shield;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class LambdaFunctionHandler implements RequestHandler<S3Event, String> {

    private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();

    public LambdaFunctionHandler() {}

    // Test purpose only.
    LambdaFunctionHandler(AmazonS3 s3) {
        this.s3 = s3;
    }
    
    /**
	 * 
	 * @author Nidhi Jamar
 	 *
 	**/
    @Override
    public String handleRequest(S3Event event, Context context) {
        context.getLogger().log("Received event: " + event);

        // Get the object from the event and show its content type
        String bucket = event.getRecords().get(0).getS3().getBucket().getName();
        String key = event.getRecords().get(0).getS3().getObject().getKey();
        try {
            S3Object response = s3.getObject(new GetObjectRequest(bucket, key));
            String contentType = response.getObjectMetadata().getContentType();
            context.getLogger().log("CONTENT TYPE: " + contentType);
            
            String dbURL = "";
            String MasterUsername = "";
            String MasterUserPassword = "";

                       Connection conn = null;
                        Statement stmt = null;
                        try{
                           //Dynamically load postgresql driver at runtime.
                           Class.forName("org.postgresql.Driver");


                           System.out.println("Connecting to database...");
                           Properties props = new Properties();


                           props.setProperty("user", MasterUsername);
                           props.setProperty("password", MasterUserPassword);
                           conn = DriverManager.getConnection(dbURL, props);
                           stmt = conn.createStatement();
                           String sql="copy mldatatable from 's3://"+ bucket+ "/"+ key+"' credentials 'aws_access_key_id=;"
                           		+ "aws_secret_access_key='"
                           		+ " DELIMITER ',' IGNOREHEADER 1";
                            int j = stmt.executeUpdate(sql);

                            stmt.close();
                           conn.close();
                        }catch(Exception ex){
                           //For convenience, handle all errors here.
                           ex.printStackTrace();
                        }
            
            
            
            
            return contentType;
        } catch (Exception e) {
            e.printStackTrace();
            context.getLogger().log(String.format(
                "Error getting object %s from bucket %s. Make sure they exist and"
                + " your bucket is in the same region as this function.", key, bucket));
            throw e;
        }
        
        
        
        
    }
}
