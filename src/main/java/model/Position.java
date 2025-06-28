package model;

public class Position {
    private int i;
    private int j;

    public Position(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Position) {
            Position other = (Position) obj;
            return this.i == other.i && this.j == other.j;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(i) * 31 + Integer.hashCode(j);
    }
}
