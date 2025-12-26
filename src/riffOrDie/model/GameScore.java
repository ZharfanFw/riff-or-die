package riffOrDie.model;

public class GameScore {
    private String username;
    private int skor;
    private int peluruMeleset;
    private int sisaPeluru;

    // Constructor
    public GameScore() {
    }

    // Constructor with param
    public GameScore(String username, int skor, int peluruMeleset, int sisaPeluru) {
        this.username = username;
        this.skor = skor;
        this.peluruMeleset = peluruMeleset;
        this.sisaPeluru = sisaPeluru;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public int getSkor() {
        return skor;
    }

    public int getPeluruMeleset() {
        return peluruMeleset;
    }

    public int getSisaPeluru() {
        return sisaPeluru;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setSkor(int skor) {
        this.skor = skor;
    }

    public void setPeluruMeleset(int peluruMeleset) {
        this.peluruMeleset = peluruMeleset;
    }

    public void setSisaPeluru(int sisaPeluru) {
        this.sisaPeluru = sisaPeluru;
    }

    @Override
    public String toString() {
        return "GameScore{" +
                "username='" + username + '\'' +
                ", skor=" + skor +
                ", peluruMeleset=" + peluruMeleset +
                ", sisaPeluru=" + sisaPeluru +
                '}';
    }
}
