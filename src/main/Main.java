package main;

import javax.swing.*;

public class Main {
    public static JFrame gameFrame;

    // ✅ METODA MODIFICATĂ: primește numele jucătorului
    public static void startGame(String playerName) {
        gameFrame = new JFrame("RPG Game");
        GamePanel gamePanel = new GamePanel(playerName); // ✅ transmite numele
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.add(gamePanel);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        gamePanel.setupGame(true);
        gamePanel.startGameThread();
    }

    public static void main(String[] args) {
        new GameTitleMenu();
    }
}
