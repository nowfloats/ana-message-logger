package com.nowfloats.chat.config;

/**
 * Created by tej on 26/10/17.
 */
public class AwsSqsParams {
    String queueName;
    String region;

    public AwsSqsParams(String queueName, String region) {
        this.queueName = queueName;
        this.region = region;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
