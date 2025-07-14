package dev.gotve;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalFactory;

import java.io.IOException;
import java.util.Random;

public class Main {

    static Terminal terminal;
    static Random random = new Random();
    static int findX;
    static int findY;
    static int WIDTH = 10;
    static int HEIGHT = 10;
    static String[][] map = new String[WIDTH][HEIGHT];
    static boolean alreadyUsed;
    static int posX = random.nextInt(map.length);
    static int posY = random.nextInt(map[0].length);

    public static void main(String[] args) {

        while (true) {
            generateMap(map);
            fetchMap(map);
            try {
                terminal = new DefaultTerminalFactory().createTerminal();
                keyboardHandler();
                find(map);
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void generateMap(String[][] map) {
        if (!alreadyUsed) {
            alreadyUsed = true;

            for (int i = 0; i < map.length; i++) {
                for (int x = 0; x < map[i].length; x++) {
                    map[i][x] = "\033[32m░░\033[0m";
                }
            }
            map[posY][posX] = "\033[33m██\033[0m";
        }
    }

    public static void keyboardHandler() {
        try {
            KeyStroke keyStroke = terminal.pollInput();  // ❌ Don't call pollInput() twice
            if (keyStroke != null) {
                if (keyStroke.getKeyType() == KeyType.Character) {
                    char keyChar = Character.toLowerCase(keyStroke.getCharacter());

                    switch (keyChar) {
                        case 'w':
                            moveUp();
                            break;
                        case 'a':
                            moveLeft();
                            break;
                        case 's':
                            moveDown();
                            break;
                        case 'd':
                            moveRight();
                            break;
                    }
                    fetchMap(map); // Draw only if moved
                }

                if (keyStroke.getKeyType() == KeyType.Escape) {
                    terminal.close();
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void moveRight() {
        map[posY][posX] = "\033[32m░░\033[0m";
        posX++;
        if (posX >= WIDTH) {
            posX = 0;
        }
        map[posY][posX] = "\033[33m██\033[0m";
    }

    public static void moveLeft() {
        map[posY][posX] = "\033[32m░░\033[0m";
        posX--;
        if (posX < 0) {
            posX = WIDTH - 1;
        }
        map[posY][posX] = "\033[33m██\033[0m";
    }

    public static void moveUp() {
        map[posY][posX] = "\033[32m░░\033[0m";
        posY--;
        if (posY < 0) {
            posY = HEIGHT - 1;
        }
        map[posY][posX] = "\033[33m██\033[0m";
    }

    public static void moveDown() {
        map[posY][posX] = "\033[32m░░\033[0m";
        posY++;
        if (posY >= HEIGHT) {
            posY = 0;
        }
        map[posY][posX] = "\033[33m██\033[0m";
    }

    public static void fetchMap(String[][] map) {
        System.out.print("\033[H\033[2J");
        for (int i = 0; i < map.length; i++) {
            for (int x = 0; x < map[i].length; x++) {
                System.out.print(map[i][x]);
            }
            System.out.println();
        }
    }

    public static void find(String[][] map) {

        for (findX = 0; findX < map.length; findX++) {
            for (findY = 0; findY < map[findX].length; findY++) {
                if (map[findX][findY].equals("\033[31m██\033[0m")) {
                    System.out.println(findX + ":" + findY);
                }
            }
        }
    }
}
