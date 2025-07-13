package boardgame;

public class Board {

    private Integer rows;
    private Integer columns;
    protected Piece pieces[][];

    public Board() {
    }

    public Board(int rows, Integer columns) {

        if (rows < 1 || columns < 1) {
            throw new BoardException("Board must have at least 1 column and 1 row");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public Piece piece(int rows, int columns) {
        if (!positionExists(rows, columns)) {
            throw new BoardException("Position not on the board.");
        }
        return pieces[rows][columns];
    }

    public Piece piece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Position not on the board.");
        }
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position) {
        if (thereIsAPiece(position)) {
            throw new BoardException("Position [" + position + "] occupied");
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    public boolean positionExists(int row, int column) {
        return row < rows && 0 <= row && column < columns && 0 <= column;
    }

    public Piece removePiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Position not on the board.");
        }

        if (piece(position) == null) {
            return null;
        }

        Piece aux = piece(position);
        aux.position = null;
        pieces[position.getRow()][position.getColumn()] = null;
        return aux;
    }

    public boolean positionExists(Position position) {
        return (positionExists(position.getRow(), position.getColumn()));
    }

    public boolean thereIsAPiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Position not on the board.");
        }
        return piece(position) != null;
    }
}
