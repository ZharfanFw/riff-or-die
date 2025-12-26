package riffOrDie.config;

public class DatabaseConfig {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/riffordie";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "1234";

    public static final String DB_TABLE = "tbenefit";

    public static final String COL_USERNAME = "username";
    public static final String COL_SKOR = "skor";
    public static final String COL_PELURU_MELESET = "peluru_meleset";
    public static final String COL_SISA_PELURU = "sisa_peluru";

    private DatabaseConfig() {
    }
}
