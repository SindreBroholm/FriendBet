package sbs.friendbet.notification;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
public class WebSocketNotificationListener implements MessageListener {

    @Override
    @SendTo("/topic/notification")
    public void onMessage(Message message) {
        System.out.println("notification - " + new String(message.getBody()));

    }
}
