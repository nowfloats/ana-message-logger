package com.nowfloats.chat;

import com.nowfloats.chat.config.AwsClientProvider;
import com.nowfloats.chat.config.ClientProvider;
import com.nowfloats.chat.sender.AwsSqsQueueSender;
import com.nowfloats.chat.sender.QueueSender;
import com.nowfloats.chat.utils.ProviderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by root on 16/10/17.
 */

public class AsyncMessaging {

    private static Logger logger = LoggerFactory.getLogger(AsyncMessaging.class);

    private QueueSender queueSender;
    private static boolean isInitialized = false;

    public void init(ClientProvider clientProvider, ProviderType providerType){

        if(ProviderType.SQS.equals(providerType)){
            AwsClientProvider awsClientProvider = (AwsClientProvider) clientProvider;
            queueSender = new AwsSqsQueueSender(awsClientProvider);
        }
        else{
            logger.info("Incorrect provide type "+providerType.getQueueType());
        }
    }

    public void send(String message){
        if (isInitialized){
            queueSender.send(message);
        }
        else{
            logger.error("Queue sender not initialized, please do init");
        }
    }
}
