package sbs.friendbet.chat;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;


import java.util.Objects;


@Component
public class WebSocketChatEventListener {

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Received a new web socket connection from: "+ Objects.requireNonNull(event.getUser()).getName());
    }

}
