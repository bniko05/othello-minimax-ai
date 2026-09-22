public class Move {
    //oi syntetagmenes tis kathe kinisis einai eythigammismenes me tis syntetagmenes tou pinaka
    private int x;
    private int y;
    private int value;

    public Move(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public void setRow(int row){
        x = row;
    }

    public int getRow() {
        return x;
    }

    public void setColumn(int col){
        y = col;
    }

    public int getColumn() {
        return y;
    }

    public void setValue(int v){
        value = v;
    }

    public int getValue(){
        return value;
    }

    public String toString() {
        return "(" + (x+1) + ", " + (y+1) + ")";
    }
}