package com.kpit;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

/**
 * Hello world!
 *
 */
public class App {
    public static final String topicURL = "arn:aws:sns:eu-west-2:891377117529:devTopic";

    public static void main(String[] args) {

        SnsClient client = SnsClient.builder().region(Region.EU_WEST_2).build();

        System.out.println("Sending offer messages to the registered email address !");

        PublishRequest req = PublishRequest.builder()
                .targetArn(topicURL)
                .subject("New Offers")
                .message("You have new offers in your account!")
                .build();
        client.publish(req);  
    }
}
