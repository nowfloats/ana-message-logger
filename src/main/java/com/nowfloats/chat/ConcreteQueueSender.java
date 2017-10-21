package com.nowfloats.chat;

import com.nowfloats.chat.config.AwsClientProvider;
import com.nowfloats.chat.sender.AwsSqsQueueSender;
import com.nowfloats.chat.sender.QueueSender;
import com.nowfloats.chat.utils.QueueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tej on 21/10/17.
 */
public class ConcreteQueueSender {

    private static Logger logger = LoggerFactory.getLogger(EventLoggingAgent.class);
    private QueueSender queueSender;

    public ConcreteQueueSender(EventLoggingInitParams params) {
        queueSender = getConcreteQueueSender(params);
    }

    public QueueSender getConcreteQueueSender(EventLoggingInitParams params) {
        if(QueueType.SQS.equals(params.getQueueType())){
            AwsClientProvider awsClientProvider = new AwsClientProvider();
            awsClientProvider.setQueueName(params.getQueueName());
            awsClientProvider.setRegion(params.getOptionalParams().get("region"));
            AwsSqsQueueSender awsSqsQueueSender = new AwsSqsQueueSender(awsClientProvider);
            return awsSqsQueueSender;
        }
        else{
            logger.info("Incorrect provide type "+params.getQueueType());
        }

        return null;
    }

    public QueueSender getQueueSender() {
        return queueSender;
    }

    public void setQueueSender(QueueSender queueSender) {
        this.queueSender = queueSender;
    }
}
