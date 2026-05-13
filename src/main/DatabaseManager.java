package main;

import java.sql.*;
import java.time.Duration;
import java.util.Locale;

public class DatabaseManager {
    private Connection conn;
    private static int playerCounter = 1;

    public DatabaseManager() {
        // Constructor gol
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:rpg3_game.db");
            conn.setAutoCommit(false);
            createTables();
            System.out.println("Database connected");
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database disconnected");
            }
        } catch (SQLException e) {
            System.err.println("Error disconnecting database: " + e.getMessage());
        }
    }

    private void createTables() {
        try (Statement stmt = conn.createStatement()) {
            String sqlScore = "CREATE TABLE IF NOT EXISTS score (" +
                    "username TEXT, " +
                    "time TEXT, " +
                    "life INTEGER, " +
                    "coin INTEGER)";
            stmt.executeUpdate(sqlScore);

            String sqlSave = "CREATE TABLE IF NOT EXISTS save (" +
                    "username TEXT PRIMARY KEY, " +
                    "map INTEGER, " +
                    "posX INTEGER, " +
                    "posY INTEGER, " +
                    "life INTEGER, " +
                    "coin INTEGER, " +
                    "playTime INTEGER)";
            stmt.executeUpdate(sqlSave);

            conn.commit();
            System.out.println("Score table created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    public void saveGameState(String username, int map, int posX, int posY, int life, int coin, double playTime) {
        try (PreparedStatement pstmt = conn.prepareStatement(
                "INSERT OR REPLACE INTO save (username, map, posX, posY, life, coin, playTime) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setInt(2, map);
            pstmt.setInt(3, posX);
            pstmt.setInt(4, posY);
            pstmt.setInt(5, life);
            pstmt.setInt(6, coin);
            pstmt.setDouble(7, playTime);
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Game saved for " + username);
        } catch (SQLException e) {
            System.err.println("Error saving game state: " + e.getMessage());
        }
    }

    public java.util.List<String> getSavedUsers() {
        java.util.List<String> users = new java.util.ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username FROM save")) {
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving saved users: " + e.getMessage());
        }
        return users;
    }

    public GameState loadGameState(String username) {
        try (PreparedStatement pstmt = conn.prepareStatement(
                "SELECT map, posX, posY, life, coin, playTime FROM save WHERE username = ?")) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new GameState(
                        username,
                        rs.getInt("map"),
                        rs.getInt("posX"),
                        rs.getInt("posY"),
                        rs.getInt("life"),
                        rs.getInt("coin"),
                        rs.getDouble("playTime")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error loading game state: " + e.getMessage());
        }

        return null;
    }

    public void saveGameStats(String username, Duration duration, int life, int coin) {
        String formattedTime = String.format(Locale.US, "%02d:%02d:%02d",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart());

        try (PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO score (username, time, life, coin) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, formattedTime);
            pstmt.setInt(3, life);
            pstmt.setInt(4, coin);
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Game stats saved for " + username +
                    " | Time: " + formattedTime +
                    " | Life: " + life +
                    " | Coin: " + coin);
        } catch (SQLException e) {
            System.err.println("Error saving game stats: " + e.getMessage());
        }
    }

    public void saveGameStatsAuto(Duration duration, int life, int coin) {
        String username = "Player" + playerCounter++;
        saveGameStats(username, duration, life, coin);
    }

    public void printAllGameTimes() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username, time, life, coin FROM score")) {

            System.out.println("=== Game Completion Times ===");
            while (rs.next()) {
                String user = rs.getString("username");
                String time = rs.getString("time");
                int life = rs.getInt("life");
                int coin = rs.getInt("coin");
                System.out.println("Player: " + user +
                        " | Time: " + time +
                        " | Life: " + life +
                        " | Coin: " + coin);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving game times: " + e.getMessage());
        }
    }

    public String getTopScores(int limit) {
        StringBuilder sb = new StringBuilder();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT username, time, life, coin FROM score ORDER BY time ASC LIMIT " + limit)) {

            int rank = 1;
            while (rs.next()) {
                String user = rs.getString("username");
                String time = rs.getString("time");
                int life = rs.getInt("life");
                int coin = rs.getInt("coin");

                sb.append(rank++)
                        .append(". ").append(user)
                        .append(" | Time: ").append(time)
                        .append(" | Life: ").append(life)
                        .append(" | Coin: ").append(coin)
                        .append("\n");
            }
        } catch (SQLException e) {
            sb.append("Eroare la încărcarea scorurilor: ").append(e.getMessage());
        }
        return sb.toString();
    }

    public void commit() throws SQLException {
        if (conn != null) {
            conn.commit();
        }
    }
}
