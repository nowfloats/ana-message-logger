package chat.ana.eventlogs.sqssender;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import chat.ana.eventlogs.logger.QueueSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tej on 18/10/17.
 */

public class AwsSqsQueueSender implements QueueSender {
    private static Logger logger = LoggerFactory.getLogger(AwsSqsQueueSender.class);
    public AwsSqsParams awsSqsParams;

    private AmazonSQS amazonSQS;

    public AwsSqsQueueSender(AwsSqsParams awsSqsParams) {
        this.awsSqsParams = awsSqsParams;
    }

    public String send(String message) {

        amazonSQS = AwsClientProvider.getAmazonSQSInstance(awsSqsParams.getQueueName(), awsSqsParams.getRegion());

        ObjectMapper objectMapper = new ObjectMapper();
        String messageJsonStr = null;
        try {
            messageJsonStr = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        SendMessageResult sendMessageResult = amazonSQS.sendMessage(new SendMessageRequest(awsSqsParams.getQueueName(), message));

        if(sendMessageResult != null){
            logger.info("message sent with Id: "+sendMessageResult.getMessageId());
            return sendMessageResult.getMessageId();
        }
        else {
            logger.error("error while sending message to aws sqs "+message);
        }

        return null;

    }
}
