package ca.mcmaster.se2aa4.mazerunner;

public class Position {
    private final int row;
    private final int col;
    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public Position move(Direction direction) {
        switch (direction) {
            case NORTH: return new Position(row - 1, col);
            case EAST:  return new Position(row, col + 1);
            case SOUTH: return new Position(row + 1, col);
            case WEST:  return new Position(row, col - 1);
            default:    return this;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.col == other.col;
    }
    
    @Override
    public int hashCode() {
        return 31 * row + col;
    }
    
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
