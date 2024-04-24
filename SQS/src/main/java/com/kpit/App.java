package com.kpit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ListQueuesRequest;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

/**
 * Hello world!
 *
 */
public class App {
    private static final String queueURL = "https://sqs.eu-west-2.amazonaws.com/891377117529/devsqs";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String foodItem;
        System.out.println("Enter the Food item :");
        foodItem = sc.nextLine();
        int quantity;
        System.out.println("Enter the quantity of Food :");
        quantity = sc.nextInt();
        int table;
        System.out.println("Enter the table number :");
        table = sc.nextInt();

        String msg = foodItem + " " + quantity + " " + table;
        //System.out.println(msg);

        SqsClient client = SqsClient.builder()
                .region(Region.EU_WEST_2)
                .build();

        // List all Queues
        listQueues(client);
        sendMessage(client, msg);
        System.out.println("Start reading messages ...");
        receiveMessage(client);

    }


    static void listQueues(SqsClient client) {
        ListQueuesRequest req = ListQueuesRequest.builder().build();
        ListQueuesResponse resp = client.listQueues(req);

        List<String> queueUrls = resp.queueUrls();
        System.out.println("You have " + queueUrls.size() + " queues");
        queueUrls.forEach(qUrl -> System.out.println("Queue Url : " + qUrl));

    }

    static void sendMessage(SqsClient client, String message){
         LocalDateTime now=LocalDateTime.now();
        String key=now.format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
        Map<String,MessageAttributeValue> attributes=new HashMap<>();
        attributes.put("Object key", MessageAttributeValue.builder().stringValue(key).build());

        SendMessageRequest req = SendMessageRequest.builder()
                                            .queueUrl(queueURL)
                                            .messageBody(message+" : "+key)
                                            .delaySeconds(2)
                                            .build();
        client.sendMessage(req);
        System.out.println("Message sent successfully !");        
    }

    static void receiveMessage(SqsClient client){
        ReceiveMessageRequest req = ReceiveMessageRequest.builder()
                                            .queueUrl(queueURL) 
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
            //                             .queueUrl(queueURL)
            //                             .receiptHandle(msg.receiptHandle())
            //                             .build();
            // client.deleteMessage(del);
        }
        if(messages.size()<1)
        break;
    }
    }
}

