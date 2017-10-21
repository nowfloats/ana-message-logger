package com.nowfloats.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowfloats.chat.config.AwsClientProvider;
import com.nowfloats.chat.config.ClientProvider;
import com.nowfloats.chat.sender.AwsSqsQueueSender;
import com.nowfloats.chat.sender.QueueSender;
import com.nowfloats.chat.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by root on 16/10/17.
 */

public class EventLoggingAgent {

    private static Logger logger = LoggerFactory.getLogger(EventLoggingAgent.class);

    private static AtomicBoolean isInitialized = new AtomicBoolean();
    private static Properties props;
    private static QueueSender queueSender;
    private static final ObjectMapper mapper = new ObjectMapper();


    public EventLoggingAgent() {
    }

    public static synchronized void init(EventLoggingInitParams params) {
        if(params == null) {
            throw new IllegalArgumentException("Init Params cannot be null");
        }

        if(!isInitialized.get()) {
            queueSender = QueueSenderFactory.createQueueSender(params).getQueueSender();
            isInitialized.set(true);
        }
        else {
            logger.info("Already initialized, skipping..");
        }
    }

    public static void logEvent(String eventName,
                                String eventSection,
                                Map<String, Object> eventParams) {

        logger.info("logging event {}, {}", eventName, eventParams);
        long epochTimeUtcInSecs = Util.getCurrentEpochTimeinUTCinMillis() / 1000;
        logEvent(eventName, eventSection, epochTimeUtcInSecs, eventParams);
    }

    public static void logEvent(String eventName,
                                String eventSection,
                                Long epochTimeUtcInSecs,
                                Map<String, Object> eventParams) {
        EventBuilder builder = new EventBuilder(eventName, eventSection, epochTimeUtcInSecs);
        if(eventParams != null) {
            builder.setProperties(eventParams);
        }

        queueSender.send(Util.getString(builder.build()));

        logger.info("logged event {} {}", eventName, eventParams);
    }
}
