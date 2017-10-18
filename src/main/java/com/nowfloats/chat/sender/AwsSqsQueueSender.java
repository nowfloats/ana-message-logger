package com.nowfloats.chat.sender;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowfloats.chat.config.AwsClientProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tej on 18/10/17.
 */

public class AwsSqsQueueSender implements QueueSender {
    private static Logger logger = LoggerFactory.getLogger(AwsSqsQueueSender.class);

    public AwsClientProvider awsClientProvider;

    public AwsSqsQueueSender(AwsClientProvider awsClientProvider) {
        this.awsClientProvider = awsClientProvider;
    }

    public boolean send(String message) {

        ObjectMapper objectMapper = new ObjectMapper();
        String messageJsonStr = null;
        try {
            messageJsonStr = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        logger.info("creating async sqs client for "+awsClientProvider.getQueueName());
        logger.info(" for region "+awsClientProvider.getRegion());

        AmazonSQS amazonSQS = awsClientProvider.createAsyncSQSClient();

        SendMessageRequest sendMessageRequest = new SendMessageRequest();
        sendMessageRequest.withMessageBody(messageJsonStr);

        GetQueueUrlResult result = amazonSQS.getQueueUrl(awsClientProvider.getQueueName());
        String queURL = result.getQueueUrl();
        sendMessageRequest.withQueueUrl(queURL);

        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        sendMessageRequest.withMessageAttributes(messageAttributes);

        logger.info("sending message to queue"+awsClientProvider.getQueueName());

        SendMessageResult sendMessageResult = amazonSQS.sendMessage(new SendMessageRequest(awsClientProvider.getQueueName(), "Hello World!"));
        return true;
    }
}
