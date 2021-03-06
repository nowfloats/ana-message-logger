package chat.ana.eventlogs.sqssender;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.*;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.QueueDeletedRecentlyException;

import chat.ana.eventlogs.logger.ClientProvider;

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

        logger.debug("using default aws credentials provider chain");
        AWSCredentialsProvider provider = new DefaultAWSCredentialsProviderChain();
        ClientConfiguration cc = new ClientConfiguration();

        AmazonSQS sqs = new AmazonSQSAsyncClient(provider, cc);
        sqs.setRegion(Region.getRegion(Regions.fromName(region)));
        Map<String, String> queueAttributes = new HashMap<>();
        logger.info("Creating Queue " + queueName );
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
