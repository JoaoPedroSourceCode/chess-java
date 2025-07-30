package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {

    private Color color;

    public ChessPiece() {
    }

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public ChessPosition getChessPosition () {
        return ChessPosition.fromPosition(position);
    }

    public boolean isThereOpponentPiece (Position position) {
        ChessPiece p = (ChessPiece) board.piece(position);
         return p != null && p.getColor() != color;
    }
}
