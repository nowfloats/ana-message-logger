package chat.ana.eventlogs.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chat.ana.eventlogs.utils.Util;

import java.util.Map;


/**
 * Created by root on 16/10/17.
 */

public class EventLoggingAgent<T> {

    private static Logger logger = LoggerFactory.getLogger(EventLoggingAgent.class);

    private final QueueSender queueSender;
    private final boolean isQueueEnabled;

    public boolean isQueueEnabled() {
        return isQueueEnabled;
    }

    public EventLoggingAgent(QueueSender queueSender, boolean isQueueEnabled) {
        this.queueSender = queueSender;
        this.isQueueEnabled = isQueueEnabled;
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
        EventBuilder<T> builder = new EventBuilder<>(eventName, eventSection, epochTimeUtcInSecs);
        if(eventParams != null) {
            builder.setProperties(eventParams);
        }

        String buildMessage = Util.getString(builder.build());
        queueSender.send(buildMessage);

        logger.info("logged event {} {}", eventName, buildMessage);
    }
}
