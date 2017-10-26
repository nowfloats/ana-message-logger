package com.nowfloats.chat.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowfloats.chat.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by root on 16/10/17.
 */

public class EventLoggingAgent<T> {

    private static Logger logger = LoggerFactory.getLogger(EventLoggingAgent.class);

    private static AtomicBoolean isInitialized = new AtomicBoolean();
    private static Properties props;
    private static QueueSender queueSender;
    private static final ObjectMapper mapper = new ObjectMapper();


    public EventLoggingAgent() {
    }

    public static synchronized void initWithQueueSender(QueueSender queueSenderImpl) {
        if(!isInitialized.get()) {
            queueSender = queueSenderImpl;
            isInitialized.set(true);
        }
        else {
            logger.info("Already initialized, skipping..");
        }
    }

    public void logEvent(String eventName,
                                String eventSection,
                                Map<String, T> eventParams) {

        logger.info("logging event {}, {}", eventName, eventParams);
        long epochTimeUtcInSecs = Util.getCurrentEpochTimeinUTCinMillis() / 1000;
        logEvent(eventName, eventSection, epochTimeUtcInSecs, eventParams);
    }

    public void logEvent(String eventName,
                                String eventSection,
                                Long epochTimeUtcInSecs,
                                Map<String, T> eventParams) {
        EventBuilder builder = new EventBuilder(eventName, eventSection, epochTimeUtcInSecs);
        if(eventParams != null) {
            builder.setProperties(eventParams);
        }

        queueSender.send(Util.getString(builder.build()));

        logger.info("logged event {} {}", eventName, eventParams);
    }
}
