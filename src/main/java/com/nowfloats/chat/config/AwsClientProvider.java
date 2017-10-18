package com.nowfloats.chat.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.QueueDeletedRecentlyException;
import com.nowfloats.chat.sender.QueueSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 16/10/17.
 */
public class AwsClientProvider implements ClientProvider {


    private String queueName;
    private String region;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    private static Logger logger = LoggerFactory.getLogger(QueueSender.class);

    public AmazonSQS createAsyncSQSClient() {

        AWSCredentialsProvider provider = new ClasspathPropertiesFileCredentialsProvider();
        ClientConfiguration cc = new ClientConfiguration();
        Region REGION = Region.getRegion(Regions.fromName("ap-southeast-1"));

        AmazonSQS sqs = new AmazonSQSAsyncClient(provider, cc);
        sqs.setRegion(Region.getRegion(Regions.fromName(region)));
        sqs.setRegion(REGION);
        Map<String, String> queueAttributes = new HashMap<>();
        queueAttributes.put("VisibilityTimeout", "120");

        logger.info("Creating Queue " + queueName + " with VisibilityTimeout of 120s.");
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName).withAttributes(queueAttributes);
        String myQueueUrl;
        try {
            myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
        } catch (QueueDeletedRecentlyException e) {
            logger.info("Queue was deleted recently. Waiting for 65s before we continue ...");
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(65));
            } catch (InterruptedException e1) {
                logger.error("Thread inturrupted exception while sleeping..");
            }
            myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
        }

        logger.info("Queue url "+myQueueUrl);

        return sqs;
    }
}
