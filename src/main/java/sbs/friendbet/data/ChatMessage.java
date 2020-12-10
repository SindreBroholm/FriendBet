package sbs.friendbet.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String chatId;

    private int senderId;

    private int recipientId;

    private String senderName;

    private String recipientName;

    private String content;


    public ChatMessage() {
    }

    public ChatMessage(User sender, User recipient, String content) {

        if (sender.getId() > recipient.getId()){
            this.chatId = recipient.getId() + "_" + sender.getId();
        } else {
            this.chatId = sender.getId() + "_" + recipient.getId();
        }
        this.senderId = sender.getId();
        this.recipientId = recipient.getId();
        this.senderName = sender.getUsername();
        this.recipientName = recipient.getUsername();
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getChatId() {
        return chatId;
    }


    public int getSenderId() {
        return senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id='" + id + '\'' +
                ", chatId='" + chatId + '\'' +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", senderName='" + senderName + '\'' +
                ", recipientName='" + recipientName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
