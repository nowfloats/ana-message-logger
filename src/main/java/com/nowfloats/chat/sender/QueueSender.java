package com.nowfloats.chat.sender;

/**
 * Created by root on 17/10/17.
 */


public interface QueueSender {
    public boolean send(String message);
}