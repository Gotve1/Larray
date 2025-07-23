package dev.gotve;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    static Path path = Paths.get("/home/gotve/Documents/Java/RandomArray/src/main/resources/maps/");

    static Terminal terminal;

    static int WIDTH = 30;
    static int HEIGHT = 30;
    static String[][] map = new String[HEIGHT][WIDTH];
    static boolean alreadyUsed;

    static Random random = new Random();
    static int posX = random.nextInt(map.length);
    static int posY = random.nextInt(map[0].length);

    static Random random1 = new Random();
    static int posX1 = random1.nextInt(map.length);
    static int posY1 = random1.nextInt(map[0].length);

    static String cube = "\u001B[32m██\033[0m";
    static String player = "\u001B[33m██\033[0m";
    static String stone = "\u001B[34m██\033[0m";

    public static void main(String[] args) throws IOException {
        try {
            terminal = new DefaultTerminalFactory().createTerminal();

            if (Files.list(path).findAny().isEmpty()) {
                System.out.println("Looking for files in: " + path.toAbsolutePath());
                generateMap(map);
            } else {
                loadMap();
            }

            fetchMap(map);

            while (true) {
                keyboardHandler();
                Thread.sleep(50);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void generateMap(String[][] map) {
        if (!alreadyUsed) {
            alreadyUsed = true;

            for (int i = 0; i < map.length; i++) {
                for (int x = 0; x < map[i].length; x++) {
                    map[i][x] = cube;
                }
            }
            for (int i = 0; i < 1; i++) {
                Random random2 = new Random();
                int posX2 = random2.nextInt(map.length);
                int posY2 = random2.nextInt(map[0].length);
                map[posY2][posX2 + 1] = stone;
                map[posY2][posX2 + 2] = stone;
                map[posY2 - 1][posX2 + 2] = stone;
            }
            map[posY1][posX1] = stone;
            map[posY][posX] = player;
        }
    }

    public static void loadMap() throws IOException {
        Path mapFile = Files.list(path).findFirst().get();

        var lines = Files.readAllLines(mapFile);

        boolean playerFound = false;

        for (int i = 0; i < map.length; i++) {
            Arrays.fill(map[i], cube);
        }

        for (int y = 0; y < HEIGHT && y < lines.toArray().length; y++) {
            String line = lines.get(y);
            for (int x = 0; x < WIDTH && x < line.length(); x++) {
                char ch = line.charAt(x);
                switch (ch) {
                    case '.':
                        map[y][x] = cube;
                        break;
                    case 'S':
                        map[y][x] = stone;
                        break;
                    case 'P':
                        map[y][x] = player;
                        posX = x;
                        posY = y;
                        playerFound = true;
                        break;
                    default:
                        map[y][x] = cube;
                        break;
                }
            }
        }
        if (!playerFound) {
            System.out.println("Warning: no player found on map.txt");
            //generateMap(map);
        } else {
            alreadyUsed = true;
        }
    }

    public static void keyboardHandler() {
        try {
            KeyStroke keyStroke = terminal.pollInput();
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
                    fetchMap(map);
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
        if (map[posY][posX + 1].equals(stone)) {
            return;
        }
        map[posY][posX] = cube;
        if (posX >= WIDTH) {
            posX = 0;
        }
        posX++;
        map[posY][posX] = player;
    }

    public static void moveLeft() {
        if (map[posY][posX - 1].equals(stone)) {
            return;
        }
        map[posY][posX] = cube;
        if (posX < 0) {
            posX = WIDTH - 1;
        }
        posX--;
        map[posY][posX] = player;
    }

    public static void moveUp() {
        if (map[posY - 1][posX].equals(stone)) {
            return;
        }
        map[posY][posX] = cube;
        if (posY < 0) {
            posY = HEIGHT - 1;
        }
        posY--;
        map[posY][posX] = player;
    }

    public static void moveDown() {
        if (map[posY + 1][posX].equals(stone)) {
            return;
        }
        map[posY][posX] = cube;
        if (posY >= HEIGHT) {
            posY = 0;
        }
        posY++;
        map[posY][posX] = player;
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
}
