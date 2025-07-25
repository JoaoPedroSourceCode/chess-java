package chess;

import boardgame.Position;

public class ChessPosition {

    private char column;
    private int row;

    public ChessPosition(char column, int row) {
        if (column < 'a' || column > 'h' || row < 1 || row > 8) {
            throw new ChessException("Position must be between [a1] and [h8].");
        }
        this.column = column;
        this.row = row;
    }

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    protected Position toPosition () {
        return new Position (8 - row, column - 'a');

    }

    static protected ChessPosition fromPosition(Position position) {
        ChessPosition chessPosition = new ChessPosition((char) ( 'a' - position.getColumn() ), (8 - position.getRow()));
        return chessPosition;
     
    }

    @Override
    public String toString () {
        return "" + column + row;
    }
}
