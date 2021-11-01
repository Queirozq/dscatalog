package com.devsuperior.dscatalog.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${AWS_KEY}")
    private String awsId;

    @Value("${AWS_SECRET}")
    private String awsKey;

    @Value("${AWS_S3_REGION}")
    private String region;

    @Bean
    public AmazonS3 s3client() {
        BasicAWSCredentials awsCred = new BasicAWSCredentials(awsId, awsKey);
        AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCred)).build();
        return s3client;
    }
}
