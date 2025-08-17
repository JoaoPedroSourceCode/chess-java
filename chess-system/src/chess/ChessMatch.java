package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chesspieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {

    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;


    List<Piece> piecesOnTheBoard = new ArrayList<>();
    List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public boolean getCheck() {
        return check;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece[][] getChessPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        check = (testCheck(opponent(currentPlayer))) ? true : false;

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;

        } else {
            nextTurn();
        }

        return (ChessPiece) capturedPiece;
    }

    public Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        //specialMoveCastling_KingSideRook
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT1 = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT1 = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece t1 = (ChessPiece) board.removePiece(sourceT1);
            board.placePiece(t1, targetT1);
            t1.increaseMoveCount();
        }

        //specialMoveCastling_QueenSideRook
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT2 = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT2 = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece t2 = (ChessPiece) board.removePiece(sourceT2);
            board.placePiece(t2, targetT2);
            t2.increaseMoveCount();
        }

        return capturedPiece;
    }

    public void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        //specialMoveCastling_KingSideRook
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT1 = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT1 = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece t1 = (ChessPiece) board.removePiece(targetT1);
            board.placePiece(t1, sourceT1);
            t1.decreaseMoveCount();
        }

        //specialMoveCastling_QueenSideRook
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT2 = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT2 = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece t2 = (ChessPiece) board.removePiece(targetT2);
            board.placePiece(t2, sourceT2);
            t2.decreaseMoveCount();
        }
    }

    public void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position");
        }

        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
        }

        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    public void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can not be moved towards target position");
        }
        ;
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (getCurrentPlayer() == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> filteredList = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());

        for (Piece p : filteredList) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }

        throw new IllegalStateException("There is no" + color + "King on the board");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() != color).collect(Collectors.toList());
        for (Piece p : opponentPieces) {
            boolean mat[][] = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }

        List<Piece> filteredList = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) (x)).getColor() == color).collect(Collectors.toList());
        for (Piece p : filteredList) {
            boolean mat[][] = p.possibleMoves();

            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);

                        if (!testCheck(color)) {
                            return false;
                        }
                        undoMove(source, target, capturedPiece);
                    }
                }
            }
        }

        return true;
    }


    private void placeNewPiece(char column, int row, ChessPiece chesspiece) {
        board.placePiece(chesspiece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(chesspiece);
    }

    private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));

    }
}
