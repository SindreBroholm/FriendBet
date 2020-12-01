package sbs.friendbet.data;

public enum BetType {
    OneVsOne("OneVsOne"),
    OneVsMany("OneVsMany"),
    ManyVsMany("ManyVsMany");

    public final String type;

    BetType(String type) {
        this.type = type;
    }


    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}
