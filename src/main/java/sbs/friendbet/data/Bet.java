package sbs.friendbet.data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Size(min = 2, max = 150)
    private String name;

    @NotEmpty
    @Size(min = 2, max = 100)
    private String oddsName;

    @NotEmpty
    @Size(min = 1, max = 100)
    private String odds;

    private String betType = "OneVsOne";

    @Size(max = 5000)
    private String description;
    public Bet(){}

    public Bet(String name, String oddsName, String odds,
               String description) {
        this.name = name;
        this.oddsName = oddsName;
        this.odds = odds;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOddsName() {
        return oddsName;
    }

    public void setOddsName(String oddsName) {
        this.oddsName = oddsName;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public String getBetType() {
        return betType;
    }

    public void setBetType(String betType) {
        this.betType = betType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
