package com.nowfloats.chat;

import com.nowfloats.chat.utils.QueueType;

import java.util.Map;

/**
 * Created by tej on 20/10/17.
 */
public class EventLoggingInitParams {
    private QueueType queueType;
    private Map<String, String> optionalParams;
    private String queueName;

    public EventLoggingInitParams(QueueType queueType, Map<String, String> optionalParams) {
        this.queueType = queueType;
        this.optionalParams = optionalParams;
    }

    public Map<String, String> getOptionalParams() {
        return optionalParams;
    }

    public void setOptionalParams(Map<String, String> optionalParams) {
        this.optionalParams = optionalParams;
    }

    public QueueType getQueueType() {
        return queueType;
    }

    public void setQueueType(QueueType queueType) {
        this.queueType = queueType;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
