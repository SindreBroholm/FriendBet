package sbs.friendbet.chat;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketChatListener implements MessageListener {


    @Override
    @SendTo("/topic/chat")
    public void onMessage(Message message) {
        System.out.println("Consuming Message - " + new String(message.getBody()));

    }
}
