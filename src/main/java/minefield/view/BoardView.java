package minefield.view;

import minefield.exceptions.CloseGameException;
import minefield.exceptions.ExplosionException;
import minefield.models.Board;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class BoardView {

    private Board board;
    private Scanner scanner = new Scanner(System.in);
    private String response;

    public BoardView(Board board) {
        this.board = board;

        executeGame();
    }

    private void executeGame() {
        try {
            boolean continueGame = true;

            while (continueGame) {
                gameCycle();
                
                System.out.println("Another Match? (Y/n)");
                String response = scanner.nextLine();
                if ("n".equalsIgnoreCase(response)) {
                    continueGame = false;
                } else {
                    board.restartGame();
                }
            }

        } catch (CloseGameException e) {
            System.out.println("Bye!!!");
        } finally {
            scanner.close();
        }
    }

    private void gameCycle() {
        try {

            while (!board.goalAchieved()) {
                System.out.println(board);

                String typedText = captureTypedValue("Type line and column numbers (x, y): ");

                Iterator<Integer> xy = Arrays.stream(typedText.split(","))
                        .map(e -> Integer.parseInt(e.trim())).iterator();

                typedText = captureTypedValue("1 - Open or 2 - (Un)Mark: ");

                if("1".equals(typedText)) {
                    board.openField(xy.next(), xy.next());
                } else if ("2".equals(typedText)) {
                    board.toggleFieldMarkup(xy.next(), xy.next());
                }

            }
            System.out.println(board);
            System.out.println("You Win!!!");
        } catch (ExplosionException e) {
            System.out.println(board);
            System.out.println("You Lose!!!");
        }
    }

    private String captureTypedValue(String text) {
        System.out.print(text);
        String typedText = scanner.nextLine();

        if("exit".equalsIgnoreCase(typedText)) {
            throw new CloseGameException();
        }
        return typedText;
    }

}
