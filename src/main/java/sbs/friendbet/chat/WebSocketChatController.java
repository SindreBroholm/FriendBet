package sbs.friendbet.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sbs.friendbet.data.User;
import sbs.friendbet.notification.Notification;
import sbs.friendbet.repositories.ChatMessageRepo;
import sbs.friendbet.repositories.ChatRoomRepo;
import sbs.friendbet.repositories.NotificationRepo;
import sbs.friendbet.repositories.UserRepo;
import java.security.Principal;
import java.util.List;


@Controller
public class WebSocketChatController {

    private ChatMessageRepo chatMessageRepo;
    private ChatRoomRepo chatRoomRepo;
    private UserRepo userRepo;
    private NotificationRepo notificationRepo;

    public WebSocketChatController(ChatMessageRepo chatMessageRepo, ChatRoomRepo chatRoomRepo, UserRepo userRepo, NotificationRepo notificationRepo) {
        this.chatMessageRepo = chatMessageRepo;
        this.chatRoomRepo = chatRoomRepo;
        this.userRepo = userRepo;
        this.notificationRepo = notificationRepo;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chat")
    public ChatMessage sendMessage(@Payload ChatMessage message, Principal principal) {
        User sender = userRepo.findByUsername(principal.getName());
        User recipient = userRepo.findById(message.getRecipientId());
        ChatMessage chatMessage = new ChatMessage(sender, recipient, message.getContent());
        chatMessageRepo.save(chatMessage);
        return chatMessage;
    }

    @GetMapping("/chat/{recipientId}")
    public String showChat(Principal principal, @PathVariable int recipientId) {
        User sender = userRepo.findByUsername(principal.getName());
        User recipient = userRepo.findById(recipientId);
        ChatRoom room = getChatRoom(sender, recipient);
        return "redirect:/room/" + room.getChatId();
    }
    private ChatRoom getChatRoom(User sender, User recipient) {
        String chatRoom;

        if (sender.getId() > recipient.getId()) {
            chatRoom = recipient.getId() + "_" + sender.getId();
        } else {
            chatRoom = sender.getId() + "_" + recipient.getId();
        }
        ChatRoom room = chatRoomRepo.findByChatId(chatRoom);

        if (room == null) {
            room = chatRoomRepo.save(new ChatRoom(sender, recipient));
        }
        return room;
    }

    @GetMapping("/room/{chatRoom}")
    public String getChatRoom(@PathVariable String chatRoom, Model model, Principal principal) {
        User sender = userRepo.findByUsername(principal.getName());
        ChatRoom room = chatRoomRepo.findByChatId(chatRoom);
        List<ChatMessage> conversation = chatMessageRepo.findAllByChatId(room.getChatId());
        User recipient = getRecipient(chatRoom, sender);

        model.addAttribute("conversation", conversation);
        model.addAttribute("recipient", recipient);
        model.addAttribute("sender", sender);
        model.addAttribute("room", room);
        return "chat";
    }
    private User getRecipient(String chatRoom, User sender) {
        User recipient = null;
        String[] findRecipient = chatRoom.split("_");
        for (String s : findRecipient) {
            if (Integer.parseInt(s) != sender.getId()) {
                recipient = userRepo.findById(Integer.parseInt(s));
            }
        }
        return recipient;
    }

    @MessageMapping("/notification.sendNotification")
    @SendTo("/topic/notification")
    public Notification sendNotification(@Payload Notification payload, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        User recipient = userRepo.findById(payload.getRecipientId());
        String type = payload.getType();

        Notification notification = new Notification(user, recipient, type);
        if (type.equals("chatMessage")){
            notification.setContent(user.getName()+" sent you a chat message");
        } else if (type.equals("friendRequest")){
            notification.setContent(user.getName()+" asked to become your friend");
        } else if (type.equals("acceptFriend")){
            notification.setContent(recipient.getName()+" is now your friend");
        }
        notificationRepo.save(notification);
        return notification;
    }
}
