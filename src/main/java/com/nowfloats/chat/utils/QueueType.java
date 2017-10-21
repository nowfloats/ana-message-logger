package com.nowfloats.chat.utils;

/**
 * Created by tej on 18/10/17.
 */
public enum QueueType {
    SQS(0), KAFKA(1), RABBIT_MQ(2);

    private int queueType;

    QueueType(int queueType) {
        this.queueType = queueType;
    }

    public int getQueueType() {
        return queueType;
    }

    public void setQueueType(int queueType) {
        this.queueType = queueType;
    }
}
