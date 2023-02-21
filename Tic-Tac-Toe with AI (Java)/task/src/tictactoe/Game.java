package tictactoe;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Game {

    int[][] field;
    private static final String BAD_PARAMETRS = "Bad parameters!";
    private static final String CELL_IS_OCCUPIED = "This cell is occupied! Choose another one!";
    private static final String INPUT_COMMAND = "Input command:";
    private static final String ENTER_NUMBER = "You should enter numbers!";
    private static final String WRONG_COORDINATES = "Coordinates should be from 1 to 3!";
    private static final String MAKING_EASY_MOVE = "Making move level \"easy\"";
    private static final String MAKING_MEDIUM_MOVE = "Making move level \"medium\"";
    private static final String MAKING_HARD_MOVE = "Making move level \"hard\"";
    private static final String DRAW = "Draw";
    private static final String X_WINS = "X wins";
    private static final String O_WINS = "O wins";
    private static final String ENTER_THE_CELLS = "Enter the coordinates:";
    private static final String FIRST_LAST_STRING = "---------";
    private static boolean gameLoop;
    private static boolean movePlayerX;
    private static int Player1Mode = 0;
    private static int Player2Mode = 0;

    int moveCount = 0;


    Game() {
        field = new int[][] {{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}};
    }


    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println(INPUT_COMMAND);
            String userInput = scanner.nextLine().trim();

            if (Objects.equals(userInput, "exit")) {
                return;
            }

            String[] userInputArray = userInput.split(" ");
            if (userInputArray.length != 3) {
                System.out.println(BAD_PARAMETRS);
                continue;
            }

            switch (userInputArray[1]) {
                case "user" -> Player1Mode = 0;
                case "easy" -> Player1Mode = 1;
                case "medium" -> Player1Mode = 2;
                case "hard" -> Player1Mode = 3;
            }

            switch (userInputArray[2]) {
                case "user" -> Player2Mode = 0;
                case "easy" -> Player2Mode = 1;
                case "medium" -> Player2Mode = 2;
                case "hard" -> Player2Mode = 3;
            }

            movePlayerX = true;
            gameLoop = true;
            moveCount = 0;
            field = new int[][] {{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}};
            while (gameLoop) {
                gameLoop(scanner, Player1Mode, Player2Mode);
            }
        }
    }

    private void gameLoop(Scanner scanner, int playerXMode, int playerYMode) {
        printField();
        moveCount++;
        if (movePlayerX) {
            switch (playerXMode) {
                case 0 -> makeHumanMove(scanner, 1);
                case 1 -> {
                    System.out.println(MAKING_EASY_MOVE);
                    makeMoveAiEasy(1);
                }
                case 2 -> {
                    makeMoveAiMedium(1);
                    System.out.println(MAKING_MEDIUM_MOVE);
                }
                case 3 -> {
                    makeMoveAiHard(1);
                    System.out.println(MAKING_HARD_MOVE);
                }
            }
            movePlayerX = false;
        } else {
            switch (playerYMode) {
                case 0 -> makeHumanMove(scanner, 0);
                case 1 -> {
                    System.out.println(MAKING_EASY_MOVE);
                    makeMoveAiEasy(0);
                }
                case 2 -> {
                    makeMoveAiMedium(0);
                    System.out.println(MAKING_MEDIUM_MOVE);
                }
                case 3 -> {
                    makeMoveAiHard(0);
                    System.out.println(MAKING_HARD_MOVE);
                }
            }
            movePlayerX = true;
        }

        if (moveCount == 9) {
            System.out.println(DRAW);
            printField();
            gameLoop = false;
        } else {
            int gameStatus = checkWhoIsWin();
            if (gameStatus == 1) {
                System.out.println(X_WINS);
                printField();
                gameLoop = false;
            } else if (gameStatus == 0) {
                System.out.println(O_WINS);
                printField();
                gameLoop = false;
            }
        }
    }

    private void makeMoveAiHard(int player) {
        int anotherPlayer = Math.abs(player - 1);
        switch (moveCount) {
            case 1 -> {
                field[1][1] = player;
            }
            case 2 -> {
                makeMove2AiHard(player);
            }
            case 3 -> {
                makeMove3AiHard(player);
            }
            default -> {
                makeMoveAiMedium(player);
            }
        }
    }

    private void makeMove3AiHard(int player) {
        makeMove2AiHard(player);
    }

    private void makeMove2AiHard(int player) {
        if (field[1][1] == -1) {
            field[1][1] = player;
        } else {
            if (field[0][0] == -1) {
                field[0][0] = player;
            } else {
                field[0][2] = player;
            }
        }
    }

    private void makeMoveAiMedium(int player) {
        int anotherPlayer = Math.abs(player - 1);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (field[y][x] == -1) {
                    field[y][x] = player;
                    if (checkWhoIsWin() == player) {
                        return;
                    } else {
                        field[y][x] = -1;
                    }
                }
            }
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (field[y][x] == -1) {
                    field[y][x] = anotherPlayer;
                    if (checkWhoIsWin() == anotherPlayer) {
                        field[y][x] = player;
                        return;
                    } else {
                        field[y][x] = -1;
                    }
                }
            }
        }
        makeMoveAiEasy(player);
    }

    private void makeHumanMove(Scanner scanner, int player) {
        while (true) {
            int[] coor = enterCoordinates(scanner);
            if (insertCoor(coor, player)) {
                return;
            } else {
                System.out.println(CELL_IS_OCCUPIED);
            }
        }
    }

    private void makeMoveAiEasy(int player) {
        Random random = new Random();
        while (true) {
            int rndX = random.nextInt(3);
            int rndY = random.nextInt(3);
            if (field[rndY][rndX] == -1) {
                field[rndY][rndX] = player;
                return;
            }
        }
    }

    private int checkWhoIsWin() {
        for (int y = 0; y < 3; y++) {
           if (field[y][0] == 1 && field[y][1] == 1 && field[y][2] == 1 ) {
               return 1;
           }
           if (field[0][y] == 1 && field[1][y] == 1 && field[2][y] == 1 ) {
                return 1;
           }

           if (field[y][0] == 0 && field[y][1] == 0 && field[y][2] == 0 ) {
               return 0;
           }
           if (field[0][y] == 0 && field[1][y] == 0 && field[2][y] == 0 ) {
               return 0;
           }

        }
        if (field[0][0] == 1 && field[1][1] == 1 && field[2][2] == 1 ) {
            return 1;
        }
        if (field[2][0] == 1 && field[1][1] == 1 && field[0][2] == 1 ) {
            return 1;
        }

        if (field[0][0] == 0 && field[1][1] == 0 && field[2][2] == 0 ) {
            return 0;
        }
        if (field[2][0] == 0 && field[1][1] == 0 && field[0][2] == 0 ) {
            return 0;
        }
        return -1;
    }


    private boolean insertCoor(int[] coor, int player) {
        int y = coor[0] - 1;
        int x = coor[1] - 1;
        if (field[y][x] > -1) {
            return false;
        }
        field[y][x] = player;
        return true;
    }


    private int[] enterCoordinates(Scanner scanner) {
        while (true) {
            System.out.println(ENTER_THE_CELLS);
            String userInput = scanner.nextLine().trim();
            if (userInput.length() != 3) {
                System.out.println(ENTER_NUMBER);
                continue;
            }
            String[] userInputArray = userInput.split(" ");
            if (userInputArray.length != 2) {
                System.out.println(ENTER_NUMBER);
                continue;
            }

            if (!Objects.equals(userInputArray[0], userInputArray[0].replaceAll("\\D",""))) {
                System.out.println(ENTER_NUMBER);
                continue;
            }

            if (!Objects.equals(userInputArray[1], userInputArray[1].replaceAll("\\D",""))) {
                System.out.println(ENTER_NUMBER);
                continue;
            }

            int y = Integer.parseInt(userInputArray[0]);
            int x = Integer.parseInt(userInputArray[1]);

            if (x < 1 || x > 3 || y < 1 || y > 3) {
                System.out.println(WRONG_COORDINATES);
                continue;
            }
            return new int[]{y, x};
        }
    }

    private void printField() {
        System.out.println(FIRST_LAST_STRING);
        for (int y = 0; y < 3; y++) {
            System.out.print("|");
            for (int x = 0; x < 3; x++) {
                if (field[y][x] == 0) {
                    System.out.print(" O");
                } else if (field[y][x] == 1) {
                    System.out.print(" X");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.print(" |\n");
        }
        System.out.println(FIRST_LAST_STRING);
    }
}
