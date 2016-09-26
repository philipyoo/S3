package com.scality;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before ;
import org.junit.BeforeClass ;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Bucket;
public class StreamingAuthV4Test {
	protected static String accessKey;
	public static  String getAccessKey() { return accessKey; }
	protected static String secretKey;
	public static String getSecretKey() { return secretKey; }
	protected static String transport;
	public static String getTransport() { return transport; }
	protected static String ipAddress;
	public static String getIpAddress() { return ipAddress; }
	protected static AmazonS3 s3client;
	public AmazonS3 getS3Client() { return this.s3client; }

        //run once before all the tests
	@BeforeClass public static void initConfig() throws Exception {
		JSONParser parser = new JSONParser();
		String path = Paths.get("../config.json").toAbsolutePath().toString();
		System.out.println(path);
    	JSONObject obj = (JSONObject) parser.parse(new FileReader(path));
    	StreamingAuthV4Test.accessKey = (String) obj.get("accessKey");
    	StreamingAuthV4Test.secretKey = (String) obj.get("secretKey");
		StreamingAuthV4Test.transport = (String) obj.get("transport");
		StreamingAuthV4Test.ipAddress = (String) obj.get("ipAddress");

		BasicAWSCredentials awsCreds =
			new BasicAWSCredentials(getAccessKey(), getSecretKey());
		s3client = new AmazonS3Client(awsCreds);
		s3client.setEndpoint(getTransport() + "://" + getIpAddress() +
			":8000");
		s3client.setS3ClientOptions(new S3ClientOptions()
			.withPathStyleAccess(true));
	}

 	@Test public void testBucket() throws Exception {
        final String bucketName = "somebucket" ;
 		getS3Client().createBucket(bucketName);
  		Object[] buckets=getS3Client().listBuckets().toArray();
		Assert.assertEquals(buckets.length,1);
		Bucket bucket = (Bucket) buckets[0];
		Assert.assertEquals(bucketName,bucket.getName());
    }
}