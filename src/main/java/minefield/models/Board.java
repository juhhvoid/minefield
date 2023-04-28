package minefield.models;

import minefield.exceptions.ExplosionException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Board {

    private int lines;
    private int columns;
    private int mines;

    private final List<Field> fields = new ArrayList<>();

    public Board(int lines, int columns, int mines) {
        this.lines = lines;
        this.columns = columns;
        this.mines = mines;

        generateFields();
        associateNeighbors(); //I know, it is so hard to name variables/methods =(
        undermineField();
    }

    public void openField(int line, int column) { //open a field
        try {
            fields.parallelStream()
                    .filter(f -> f.getFieldColumn() == column && f.getFieldLine() == line)
                    .findFirst()
                    .ifPresent(f -> f.openField());
        } catch (ExplosionException e) {
            fields.forEach(field -> field.setOpenField(true));
            throw e;
        }
    }

    public void toggleFieldMarkup(int line, int column) { //mark a possible field with mine
        fields.parallelStream()
                .filter(f -> f.getFieldColumn() == column && f.getFieldLine() == line)
                .findFirst()
                .ifPresent(f -> f.toggleFieldMarkup());
    }

    private void generateFields() { //defines the board fields
        for (int line = 0; line < this.lines; line++) {
            for (int column = 0; column < this.columns; column++) {
                fields.add(new Field(line, column));
            }
        }
    }

    private void associateNeighbors() { //compares fields (method addNeighbor) and if it is a neighbor, adds as a neighbor
        for (Field field1 : fields) {
            for (Field field2 : fields) {
                field1.addNeighbor(field2);
            }
        }
    }

    private void undermineField() { //logic for define amount of mines and undermine the field
        long armedMines = 0;
        Predicate<Field> mined = field -> field.isMined(); //checks if the field is mined

        do {
            int randomNumber = (int) (Math.random() * fields.size()); //set a random amount of fields to undermined
            fields.get(randomNumber).undermine(); //undermines the field
            armedMines = fields.stream().filter(mined).count(); //checks amount of mines
        } while (armedMines < mines);
    }

    public boolean goalAchieved() { //return if the goal was achieved
        return fields.stream().allMatch(field -> field.goalAchieved());
    }

    public void restartGame() {
        fields.stream().forEach(field -> field.restartGame());
        undermineField();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("  ");
        for (int c = 0; c < columns; c++) {
            stringBuilder.append(" ");
            stringBuilder.append(c);
            stringBuilder.append(" ");
        }

        stringBuilder.append("\n");

        int i = 0;
        for (int line = 0; line < this.lines; line++) { //build the board
            stringBuilder.append(line);
            stringBuilder.append(" ");
            for (int column = 0; column < this.columns; column++) {
                stringBuilder.append(" ");
                stringBuilder.append(fields.get(i));
                stringBuilder.append(" ");
                i++;
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

}
