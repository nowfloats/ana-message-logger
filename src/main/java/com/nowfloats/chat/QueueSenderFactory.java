package com.nowfloats.chat;

/**
 * Created by tej on 21/10/17.
 */
public class QueueSenderFactory {

    public static ConcreteQueueSender createQueueSender(EventLoggingInitParams params) {
        return new ConcreteQueueSender(params);
    }
}
