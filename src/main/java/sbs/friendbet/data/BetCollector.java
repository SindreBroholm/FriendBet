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
    private int betId;
    private int againstUser;

    public BetCollector() {
    }

    public BetCollector(User user, Bet bet, User againstUser) {
        this.userId = user.getId();
        this.betId = bet.getId();
        this.againstUser = againstUser.getId();
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

    public int getBetId() {
        return betId;
    }

    public void setBetId(int betId) {
        this.betId = betId;
    }
}
