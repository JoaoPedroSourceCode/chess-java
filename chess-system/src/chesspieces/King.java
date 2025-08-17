package chesspieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.ChessMatch;
import chess.Color;


public class King extends ChessPiece {

    private ChessMatch chessMatch;

    public King() {
    }

    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[board.getRows()][board.getColumns()];

        Position p = new Position(0, 0);

        //above
        p.setValues(position.getRow() - 1, position.getColumn());
        if (board.positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //left
        p.setValues(position.getRow(), position.getColumn() - 1);
        if (board.positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //right
        p.setValues(position.getRow(), position.getColumn() + 1);
        if (board.positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //below
        p.setValues(position.getRow() + 1, position.getColumn());
        if (board.positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //nw

        p.setValues(position.getRow() - 1, position.getColumn() - 1);
        if (board.positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //ne
        p.setValues(position.getRow() - 1, position.getColumn() + 1);
        if (board.positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //sw
        p.setValues(position.getRow() + 1, position.getColumn() - 1);
        if (board.positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //se
        p.setValues(position.getRow() + 1, position.getColumn() + 1);
        if (board.positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //specialMoveCastling
        if (getMoveCount() == 0 && !chessMatch.getCheck()) {

            //specialMoveCastling_KingSideRook
            Position pt1 = new Position(position.getRow(), position.getColumn() + 3);
            if (testRookCastling(pt1)) {
                Position p1 = new Position(position.getRow(), position.getColumn() + 1);
                Position p2 = new Position(position.getRow(), position.getColumn() + 2);
                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
                    mat[position.getRow()][position.getColumn() + 2] = true;
                }
            }

            //specialMoveCastling_QueenSideRook
            Position pt2 = new Position(position.getRow(), position.getColumn() - 4);
            if (testRookCastling(pt2)) {
                Position p3 = new Position(position.getRow(), position.getColumn() - 1);
                Position p4 = new Position(position.getRow(), position.getColumn() - 2);
                Position p5 = new Position(position.getRow(), position.getColumn() - 3);
                if (getBoard().piece(p3) == null && getBoard().piece(p4) == null && getBoard().piece(p5) == null) {
                    mat[position.getRow()][position.getColumn() - 2] = true;
                }
            }
        }
        return mat;
    }

    public boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();

    }

    public boolean testRookCastling(Position position) {
        ChessPiece p = (ChessPiece) board.piece(position);
        return p != null && p.getColor() == getColor() && p instanceof Rook && p.getMoveCount() == 0;
    }

    @Override
    public String toString() {
        return "K";
    }
}
