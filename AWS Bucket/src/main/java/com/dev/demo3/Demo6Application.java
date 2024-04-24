package com.dev.demo3;

import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@SpringBootApplication
public class Demo6Application implements CommandLineRunner {
    private String bucketName = "dev8989";

    public static void main(String[] args) {
        SpringApplication.run(Demo6Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String filepath = null;
        if (args.length < 1) {
            System.out.println("ERROR! Need file path as argument.");
            filepath = "D:\\AWSJava\\demo6\\Readme.md";
        } else {
            filepath = args[0];
        }

        System.out.println("File to be uploaded to bucket: " + filepath);
        S3Client client = S3Client.builder().region(Region.US_EAST_1).build();
        File file = new File(filepath);

        if (file.isFile()) {
            PutObjectRequest req = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(file.getName())
                    .build();

            client.putObject(req, RequestBody.fromFile(file));
            System.out.println("File " + filepath + " uploaded successfully");
        } else {
            System.out.println("File " + filepath + " does not exist!");
        }
    }
}
