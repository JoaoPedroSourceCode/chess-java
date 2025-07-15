package chesspieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
    public King() {
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean [][] mat = new boolean [board.getRows()][board.getColumns()];
        return mat;
    }

    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString () {
        return "K";
    }
}
