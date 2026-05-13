package main;

import java.awt.*;
import javax.swing.*;

public class GameTitleMenu extends JFrame {
    public GameTitleMenu() {
        this.setTitle("regatul luminii");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = new ImageIcon("res/Background/regatul luminii.jpg");
                g.drawImage(bg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        panel.setLayout(null);

        JButton playBtn = createStyledButton("Play", 300, 110);
        JButton loadBtn = createStyledButton("Load", 300, 180);
        JButton highScoresBtn = createStyledButton("High Scores", 300, 250);
        JButton exitBtn = createStyledButton("Exit", 300, 320);

        // ✅ CERE NUMELE JUCĂTORULUI ȘI ÎL TRANSMITE
        playBtn.addActionListener((e) -> {
            String playerName = JOptionPane.showInputDialog(this, "Enter your name:", "Player Name", JOptionPane.QUESTION_MESSAGE);

            if (playerName == null || playerName.trim().isEmpty()) {
                playerName = "Player" + System.currentTimeMillis(); // fallback default
            }

            this.dispose();
            Main.startGame(playerName);
        });

        // ✅ Afișează top scoruri din baza de date
        highScoresBtn.addActionListener((e) -> {
            DatabaseManager db = new DatabaseManager();
            db.connect();
            String topScores = db.getTopScores(10); // Top 10 scoruri
            db.disconnect();

            JTextArea textArea = new JTextArea(topScores);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
            textArea.setEditable(false);
            textArea.setBackground(new Color(240, 240, 240));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Clasament High Scores", JOptionPane.PLAIN_MESSAGE);
        });

        loadBtn.addActionListener((e) -> {
            DatabaseManager db = new DatabaseManager();
            db.connect();
            java.util.List<String> savedUsers = db.getSavedUsers();

            if (savedUsers.isEmpty()) {
                db.disconnect();
                JOptionPane.showMessageDialog(this, "Nu există salvări disponibile.");
                return;
            }

            String selectedUser = (String) JOptionPane.showInputDialog(
                    this,
                    "Selectează un jucător:",
                    "Load Game",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    savedUsers.toArray(),
                    savedUsers.get(0)
            );

            if (selectedUser != null) {
                GameState state = db.loadGameState(selectedUser);
                db.disconnect();

                if (state != null) {
                    this.dispose();
                    GamePanel gp = new GamePanel(state.username);
                    gp.currentMap = state.map;
                    gp.player.worldx = state.posX;
                    gp.player.worldy = state.posY;
                    gp.player.life = state.life;
                    gp.player.hasCoin = state.coin;
                    gp.ui.playTime=state.playTime;

                    Main.gameFrame = new JFrame("RPG Game");
                    Main.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Main.gameFrame.setResizable(false);
                    Main.gameFrame.add(gp);
                    Main.gameFrame.pack();
                    Main.gameFrame.setLocationRelativeTo(null);
                    Main.gameFrame.setVisible(true);

                    gp.setupGame(false);
                    gp.startGameThread();
                } else {
                    JOptionPane.showMessageDialog(this, "Eroare la încărcarea jocului.");
                }
            } else {
                db.disconnect(); // Dacă a apăsat Cancel
            }
        });
        exitBtn.addActionListener((e) -> System.exit(0));

        panel.add(playBtn);
        panel.add(loadBtn);
        panel.add(highScoresBtn);
        panel.add(exitBtn);
        this.add(panel);
        this.setVisible(true);
    }

    private JButton createStyledButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setBounds(x, y, 200, 50);
        button.setFocusPainted(false);
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(Color.WHITE);
        return button;
    }
}
