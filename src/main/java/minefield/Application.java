package minefield;

import minefield.models.Board;
import minefield.view.BoardView;

public class Application {
    public static void main(String[] args) {

        Board board = new Board(6, 6, 3);
        new BoardView(board);
    }
}
