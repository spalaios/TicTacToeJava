public class Player {
    private int moves;
    boolean isBot = false;

    public int getWeapon() {
        return weapon;
    }

    public void setWeapon(int weapon) {
        this.weapon = weapon;
    }

    private int weapon = 0;

    public Player(String user, int weapon) {
        if(user.equalsIgnoreCase("user")) {
            isBot = false;
        }else {
            isBot = true;
        }
        this.weapon = weapon;
        setMoves(0);
    }
    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public String toString() {
        String playerDetails = "Weapon-> "+this.weapon+" type-> "+((this.isBot) ? "computer": "user");
        return playerDetails;
    }

    public void increaseMoves() {
        this.moves = this.moves + 1;
    }
}
