package sbs.friendbet.data;

import javax.persistence.*;

@Entity
public class FriendTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private User friend;

    private boolean pending = true;

    public FriendTracker(){
    }
    public FriendTracker(User user, User friend) {
        this.user = user;
        this.friend = friend;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}
