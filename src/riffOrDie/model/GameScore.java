package riffOrDie.model;

/**
 * GAMESCORE - Data Transfer Object (DTO)
 * 
 * Fungsi:
 * - Container untuk data score
 * - Fields: username, skor, peluru_meleset, sisa_peluru
 * - Getter/setter methods
 * 
 * Digunakan: Pass data ke/dari database
 */
public class GameScore {
    /** Username pemain */
    private String username;
    
    /** Skor pemain */
    private int skor;
    
    /** Jumlah peluru yang meleset (mengenai amplifier) */
    private int peluruMeleset;
    
    /** Sisa peluru pemain */
    private int sisaPeluru;

    /**
     * Constructor default - buat GameScore kosong
     * Berguna saat ingin set field secara manual
     */
    public GameScore() {
    }

    /**
     * Constructor dengan parameter - buat GameScore lengkap
     * 
     * @param username Username pemain
     * @param skor Skor pemain
     * @param peluruMeleset Jumlah peluru meleset
     * @param sisaPeluru Sisa peluru
     */
    public GameScore(String username, int skor, int peluruMeleset, int sisaPeluru) {
        this.username = username;
        this.skor = skor;
        this.peluruMeleset = peluruMeleset;
        this.sisaPeluru = sisaPeluru;
    }

    /**
     * Ambil username pemain
     * 
     * @return Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Ambil skor pemain
     * 
     * @return Skor
     */
    public int getSkor() {
        return skor;
    }

    /**
     * Ambil jumlah peluru meleset
     * 
     * @return Jumlah peluru meleset
     */
    public int getPeluruMeleset() {
        return peluruMeleset;
    }

    /**
     * Ambil sisa peluru
     * 
     * @return Sisa peluru
     */
    public int getSisaPeluru() {
        return sisaPeluru;
    }

    /**
     * Set username pemain
     * 
     * @param username Username baru
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set skor pemain
     * 
     * @param skor Skor baru
     */
    public void setSkor(int skor) {
        this.skor = skor;
    }

    /**
     * Set jumlah peluru meleset
     * 
     * @param peluruMeleset Jumlah peluru meleset baru
     */
    public void setPeluruMeleset(int peluruMeleset) {
        this.peluruMeleset = peluruMeleset;
    }

    /**
     * Set sisa peluru
     * 
     * @param sisaPeluru Sisa peluru baru
     */
    public void setSisaPeluru(int sisaPeluru) {
        this.sisaPeluru = sisaPeluru;
    }

    /**
     * Representasi string dari GameScore untuk debugging
     * 
     * @return String representation
     */
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
