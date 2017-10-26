package com.nowfloats.chat.sqssender;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.QueueDeletedRecentlyException;
import com.nowfloats.chat.logger.ClientProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 16/10/17.
 */
public class AwsClientProvider {


    private static Logger logger = LoggerFactory.getLogger(AwsClientProvider.class);
    private static volatile AmazonSQS amazonSQS;


    public static AmazonSQS getAmazonSQSInstance(String queueName, String region) {

        if(amazonSQS == null){
            synchronized (AwsSqsQueueSender.class){
                if (amazonSQS == null){
                    amazonSQS = createAsyncSQSClient(queueName, region);
                    logger.info("created async sqs client for "+queueName);
                    logger.info(" for region "+region);
                }
            }
        }
        return amazonSQS;
    }

    public static AmazonSQS createAsyncSQSClient(String queueName, String region) {

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
