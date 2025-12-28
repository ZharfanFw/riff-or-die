package riffOrDie.model;

/**
 * DATABASEMANAGER - Database CRUD operations
 * 
 * Fungsi:
 * - saveScore() - Insert/update ke database
 * - getPlayerScore() - Ambil score player tertentu
 * - getAllScores() - Ambil semua untuk scoreboard
 * - Connection management (open/close)
 * 
 * Penting: PreparedStatement untuk prevent SQL injection!
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import riffOrDie.model.GameScore;
import riffOrDie.config.DatabaseConfig;

/**
 * DatabaseManager - handle semua database operations
 * PENTING: Jangan pernah close connection dalam try-with-resources
 * karena bisa membuat connection closed untuk operasi berikutnya
 */
public class DatabaseManager {

    /**
     * Static block - Load MySQL Driver saat class pertama kali dimuat
     * Penting untuk register JDBC driver sebelum koneksi
     */
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver tidak ditemukan!");
            e.printStackTrace();
        }
    }

    /**
     * Get fresh database connection
     * Membuka koneksi baru ke database MySQL
     * 
     * @return Connection object yang aktif
     * @throws SQLException Jika koneksi gagal
     * 
     * PENTING: JANGAN di-close dalam try-with-resources
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.DB_URL,
                DatabaseConfig.DB_USER,
                DatabaseConfig.DB_PASSWORD);
    }

    /**
     * Simpan score player ke database
     * Jika user sudah ada: UPDATE data
     * Jika user belum ada: INSERT data baru
     * 
     * @param score GameScore object yang akan disimpan
     * @return true jika berhasil, false jika gagal
     */
    public static boolean saveScore(GameScore score) {
        // Validasi input
        if (score == null || score.getUsername().isEmpty()) {
            System.err.println("Invalid GameScore! username tidak boleh kosong");
            return false;
        }

        Connection conn = null;
        try {
            conn = getConnection();

            if (userExists(score.getUsername())) {
                // User sudah ada - UPDATE existing record
                String updateSQL = "UPDATE " + DatabaseConfig.DB_TABLE +
                        " SET " + DatabaseConfig.COL_SKOR + " = ?, " +
                        DatabaseConfig.COL_PELURU_MELESET + " = ?, " +
                        DatabaseConfig.COL_SISA_PELURU + " = ? " +
                        "WHERE " + DatabaseConfig.COL_USERNAME + " = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                    pstmt.setInt(1, score.getSkor());
                    pstmt.setInt(2, score.getPeluruMeleset());
                    pstmt.setInt(3, score.getSisaPeluru());
                    pstmt.setString(4, score.getUsername());

                    int rowsAffected = pstmt.executeUpdate();
                    return rowsAffected > 0;
                }
            } else {
                // User belum ada - INSERT new record
                String insertSQL = "INSERT INTO " + DatabaseConfig.DB_TABLE +
                        " (" + DatabaseConfig.COL_USERNAME + ", " +
                        DatabaseConfig.COL_SKOR + ", " +
                        DatabaseConfig.COL_PELURU_MELESET + ", " +
                        DatabaseConfig.COL_SISA_PELURU + ") " +
                        "VALUES (?, ?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setString(1, score.getUsername());
                    pstmt.setInt(2, score.getSkor());
                    pstmt.setInt(3, score.getPeluruMeleset());
                    pstmt.setInt(4, score.getSisaPeluru());

                    int rowsAffected = pstmt.executeUpdate();
                    return rowsAffected > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error menyimpan score: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Close connection di finally block
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Ambil data score player tertentu dari database
     * 
     * @param username Username player yang dicari
     * @return GameScore object jika ditemukan, null jika tidak
     */
    public static GameScore getPlayerScore(String username) {
        // Validasi input
        if (username == null || username.isEmpty()) {
            System.err.println("Username tidak boleh kosong");
            return null;
        }

        String selectSQL = "SELECT * FROM " + DatabaseConfig.DB_TABLE +
                " WHERE " + DatabaseConfig.COL_USERNAME + " = ?";

        Connection conn = null;
        try {
            conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                pstmt.setString(1, username);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        GameScore score = new GameScore();
                        score.setUsername(rs.getString(DatabaseConfig.COL_USERNAME));
                        score.setSkor(rs.getInt(DatabaseConfig.COL_SKOR));
                        score.setPeluruMeleset(rs.getInt(DatabaseConfig.COL_PELURU_MELESET));
                        score.setSisaPeluru(rs.getInt(DatabaseConfig.COL_SISA_PELURU));
                        return score;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil score: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }

        return null;
    }

    /**
     * Ambil semua data score dari database untuk scoreboard
     * Data diurutkan berdasarkan skor (descending)
     * 
     * @return List<GameScore> berisi semua skor, kosong jika gagal
     */
    public static List<GameScore> getAllScores() {
        List<GameScore> scores = new ArrayList<>();

        String selectSQL = "SELECT * FROM " + DatabaseConfig.DB_TABLE +
                " ORDER BY " + DatabaseConfig.COL_SKOR + " DESC";

        Connection conn = null;
        try {
            conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
                    ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GameScore score = new GameScore();
                    score.setUsername(rs.getString(DatabaseConfig.COL_USERNAME));
                    score.setSkor(rs.getInt(DatabaseConfig.COL_SKOR));
                    score.setPeluruMeleset(rs.getInt(DatabaseConfig.COL_PELURU_MELESET));
                    score.setSisaPeluru(rs.getInt(DatabaseConfig.COL_SISA_PELURU));
                    scores.add(score);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil semua scores: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }

        return scores;
    }

    /**
     * Cek apakah user sudah ada di database
     * Method private, hanya digunakan secara internal oleh saveScore()
     * 
     * @param username Username yang dicek
     * @return true jika user sudah ada, false jika belum
     */
    private static boolean userExists(String username) {
        String selectSQL = "SELECT COUNT(*) FROM " + DatabaseConfig.DB_TABLE +
                " WHERE " + DatabaseConfig.COL_USERNAME + " = ?";

        Connection conn = null;
        try {
            conn = getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                pstmt.setString(1, username);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengecek user existence: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }

        return false;
    }
}
