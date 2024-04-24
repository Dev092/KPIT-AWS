package com.kpit;

import java.util.List;
import java.util.Scanner;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ListQueuesRequest;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;


import software.amazon.awssdk.services.sqs.model.Message;

public class App 
{
    private static final String URL = "https://sqs.us-east-1.amazonaws.com/891377117529/devqueue7";

    public static final String TOPIC_ARN="arn:aws:sns:eu-west-2:891377117529:devtopic";

    public static void main( String[] args )
    {
         int random = (int)(Math.random() * 50 + 1);

        SnsClient client = SnsClient.builder().region(Region.EU_WEST_2).build();

        System.out.println("Sending an OTP to your registered email address !");

        PublishRequest req = PublishRequest.builder()
                                    .targetArn(TOPIC_ARN)
                                    .subject("Your OTP")
                                    .message("Your OTP is "+random+" . Kindly do not share this OTP with anyone !")
                                    .build();
        
        client.publish(req);
        Scanner sc = new Scanner(System.in);
        System.out.println("Kindly enter your OTP: ");
        String otp = sc.nextLine();

        int otpFromMail = Integer.parseInt(otp);
        if(random == otpFromMail){
            System.out.println("Authentication is successful !");
        }else{
            System.out.println("Invalid OTP ?");
        }


       

       String msg="DEV ka file";
        SqsClient client2 = SqsClient.builder()
                .region(Region.US_EAST_1)
                .build();

        // List all Queues
        listQueues(client2);
        sendMessage(client2, msg);
        System.out.println("Start reading messages ...");
        receiveMessage(client2);

    }


     static void listQueues(SqsClient client) {
        ListQueuesRequest req = ListQueuesRequest.builder().build();
        ListQueuesResponse resp = client.listQueues(req);

        List<String> queueUrls = resp.queueUrls();
        System.out.println("You have " + queueUrls.size() + " queues");
        queueUrls.forEach(qUrl -> System.out.println("Queue Url : " + qUrl));

    }

    static void sendMessage(SqsClient client, String message){
        SendMessageRequest req = SendMessageRequest.builder()
                                            .queueUrl(URL)
                                            .messageBody(message)
                                            .delaySeconds(2)
                                            .build();
        client.sendMessage(req);
        System.out.println("Message sent successfully !");        
    }

    static void receiveMessage(SqsClient client){
        ReceiveMessageRequest req = ReceiveMessageRequest.builder()
                                            .queueUrl(URL) 
                                            .maxNumberOfMessages(5)
                                            .waitTimeSeconds(10)
                                            .build();
        while(true){
        ReceiveMessageResponse resp = client.receiveMessage(req);
        List<Message> messages =  resp.messages();
        System.out.println("Found "+messages.size()+" messages");
        for(Message msg : messages){
            System.out.println("Reading the message....");
            System.out.println("  "+msg.body());
            System.out.println("Mark as delivered (Delete from the Queue)");
            // DeleteMessageRequest del = DeleteMessageRequest.builder()
            //                             .queueUrl(URL)
            //                             .receiptHandle(msg.receiptHandle())
            //                             .build();
            // client.deleteMessage(del);
        }
        if(messages.size()<1)
        break;
    }
    }


}
