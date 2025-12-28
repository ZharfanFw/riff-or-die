package riffOrDie.config;

/**
 * DATABASECONFIG - Konfigurasi database MySQL
 * 
 * Fungsi:
 * - Simpan koneksi database (URL, user, password)
 * - Nama table dan nama kolom database
 * - Single source of truth untuk database settings
 * 
 * Pattern: Utility class dengan static final fields
 */
public class DatabaseConfig {
    // ===================== DATABASE CONNECTION =====================
    
    /** URL koneksi ke database MySQL */
    public static final String DB_URL = "jdbc:mysql://localhost:3306/riffordie";
    
    /** Username untuk koneksi database */
    public static final String DB_USER = "root";
    
    /** Password untuk koneksi database */
    public static final String DB_PASSWORD = "1234";

    // ===================== TABLE & COLUMN NAMES =====================
    
    /** Nama tabel untuk menyimpan data skor player */
    public static final String DB_TABLE = "tbenefit";

    /** Nama kolom username di database */
    public static final String COL_USERNAME = "username";
    
    /** Nama kolom skor di database */
    public static final String COL_SKOR = "skor";
    
    /** Nama kolom jumlah peluru yang meleset di database */
    public static final String COL_PELURU_MELESET = "peluru_meleset";
    
    /** Nama kolom sisa peluru di database */
    public static final String COL_SISA_PELURU = "sisa_peluru";

    // ===================== CONSTRUCTOR =====================
    
    /**
     * Private constructor - mencegah instansiasi
     * 
     * Pattern: Utility class tidak boleh di-instansiasi
     * Semua fields bersifat static dan diakses langsung via class name
     * Contoh: DatabaseConfig.DB_URL
     */
    private DatabaseConfig() {
    }
}
