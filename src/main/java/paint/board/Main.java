package paint.board;

import javax.swing.*;

public class Main {

    public static MainWindow mainWindow;

    public static void main(String[] args) {
        try {
            mainWindow = new MainWindow();
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        mainWindow.setLocationRelativeTo(null);
    }
}