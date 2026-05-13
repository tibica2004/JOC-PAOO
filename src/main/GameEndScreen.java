package main;

import javax.swing.*;
import java.awt.*;

public class GameEndScreen extends JFrame {

    private final String playerName; // ➤ nou câmp pentru numele jucătorului

    public GameEndScreen(boolean victory, String playerName) {
        this.playerName = playerName; // ➤ setăm numele jucătorului

        this.setTitle("Sfârșitul jocului");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                String imagePath = victory ? "res/Background/gameWon.png" : "res/Background/gameLost.png";
                ImageIcon bg = new ImageIcon(imagePath);
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(null);

        JButton restartBtn = createStyledButton("Restart", 300, 300);
        JButton exitBtn = createStyledButton("Exit", 300, 380);

        restartBtn.addActionListener(e -> {
            dispose();
            Main.startGame(playerName); // ✅ transmite numele
        });

        exitBtn.addActionListener(e -> {
            System.exit(0);
        });

        panel.add(restartBtn);
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
