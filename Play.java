public class Play {
    Integer gameId;
    String team;
    String description;
    String player1;
    String player2;

    int yards;
    String playType;

    int quarter;
    String time;

    Play(Integer gameId, String team, String description, String player1, String player2, int yards, String playType, int quarter, String time) {
        this.gameId = gameId;
        this.team = team;
        this.description = description;
        this.player1 = player1;
        this.player2 = player2;
        this.yards = yards;
        this.playType = playType;
        this.quarter = quarter;
        this.time = time;
    }

    // Running play constructor
    Play(Integer gameId, String team, String description, String player1, int yards, String playType, int quarter, String time) {
        this(gameId, team, description, player1, null, yards, playType, quarter, time);
    }

    public String toString() {
        return this.gameId
            + " | " + this.team
            + " | " + this.playType
            + " | " + this.player1
            + " | " + this.player2
            + " | " + this.yards;
    }
}
