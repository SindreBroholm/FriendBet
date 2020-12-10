package sbs.friendbet.data;

import javax.persistence.*;

@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String chatId;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

    public ChatRoom() {
    }

    public ChatRoom(User sender, User recipient) {
        this.sender = sender;
        this.recipient = recipient;

        if (sender.getId() > recipient.getId()){
            this.chatId = recipient.getId() + "_" + sender.getId();
        } else {
            this.chatId = sender.getId() + "_" + recipient.getId();
        }
    }

    public int getId() {
        return id;
    }

    public String getChatId() {
        return chatId;
    }

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }
}
