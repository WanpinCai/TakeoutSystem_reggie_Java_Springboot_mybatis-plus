package com.itheima.reggie.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


public class TwilioSMSUtils {

    private static final String ACCOUNT_SID = "AC214a9d9ee5df29273c8a45120cfccbf7";
    private static final String AUTH_TOKEN = "f9b386f4ee6a22d0d7a2947c577990cb";

    /**
     * 发送短信
     * @param from 发送方号码（Twilio提供的号码）
     * @param to 接收方手机号
     * @param messageBody 短信内容
     */
    public static void sendMessage(String from, String to, String messageBody) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(from),
                messageBody
        ).create();

        if (message.getStatus() == Message.Status.FAILED) {
            System.out.println("短信发送失败");
        } else {
            System.out.println("短信发送成功");
        }
    }

//    public static void main(String[] args) {
//        String fromNumber = "your_twilio_phone_number";
//        String toNumber = "recipient_phone_number";
//        String message = "Hello, this is a Twilio test message.";
//
//        sendMessage(fromNumber, toNumber, message);
//    }
}
