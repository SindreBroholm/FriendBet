package sbs.friendbet.data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
public class BetCollector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private final LocalDateTime created = LocalDateTime.now();
    private int userId;

    private int againstUser;
    @ManyToOne
    private Bet bet;

    public BetCollector() {
    }

    public BetCollector(User user, Bet bet, User againstUser) {
        this.userId = user.getId();
        this.againstUser = againstUser.getId();
        this.bet = bet;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public int getAgainstUser() {
        return againstUser;
    }

    public Bet getBet() {
        return bet;
    }
}
