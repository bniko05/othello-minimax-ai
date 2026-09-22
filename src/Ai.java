import java.util.ArrayList;

public class Ai {

    private int maxdepth;
    private boolean turn;

    Ai(int d, boolean t){
        maxdepth = d;
        turn = t;
    }

    public Move MiniMax(Board board){
        //an turn == true, paizei prwta to ai kai gi auto kaloume th max, pou antistoixiei sta maura pou auta paizoun prwta
        if (turn){

            return max(new Board(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        }else{
            
            return min(new Board(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        }
    }


    private Move max(Board board, int depth, int a, int b) {
        //an eimaste se termatikh katastash epistrefoume thn teleutaia kinhsh
        if(board.isTerminal() || (depth == maxdepth)){
            return new Move(board.getLastMove().getRow(), board.getLastMove().getColumn(), board.heuristic(1));
        }

        ArrayList<Board> children = board.getChildren(1);  //pairnoume oles tis pithanes kinhseis

        //an o max den exei kinhsh (alla to paixnidi den exei teleiwsei) xanei th seira tou kai paizei o min
        if (children.isEmpty()) {
            return min(board, depth + 1, a, b);
        }
        
        Move maxmove = new Move(0, 0, Integer.MIN_VALUE); 
        int maxval = Integer.MIN_VALUE;

        //gia kathe pithanh kinhsh aksiologoume tis kinhseis tou antipalou
        for(Board child: children)
        {
            Move move = min(child, depth + 1, a, b);

            //enhmerwsh kaluterhs kinhshs
            if(move.getValue() > maxval)
            {       
                maxmove.setRow(child.getLastMove().getRow());
                maxmove.setColumn(child.getLastMove().getColumn());
                maxmove.setValue(move.getValue());
                maxval = move.getValue();
            }

            //pruning me bash dialekseis thewrias
            if (maxval > a) {
                a = maxval;
            }

            if (maxval >= b) {
                break;
            }
        }
        return maxmove;
    }

    public Move min(Board board, int depth, int a, int b) {
        //an eimaste se termatikh katastash epistrefoume thn teleutaia kinhsh
        if(board.isTerminal() || (depth == maxdepth)){
            return new Move(board.getLastMove().getRow(), board.getLastMove().getColumn(), board.heuristic(-1));
        }

        ArrayList<Board> children = board.getChildren(-1); //pairnoume oles tis pithanes kinhseis

        //an o min den exei kinhsh (alla to paixnidi den exei teleiwsei) xanei th seira tou kai paizei o max
        if (children.isEmpty()) {
            return max(board, depth + 1, a, b);
        }

        Move minmove = new Move(0, 0, Integer.MAX_VALUE); 
        int minval = Integer.MAX_VALUE;

        //gia kathe pithanh kinhsh aksiologoume tis kinhseis tou antipalou
        for(Board child: children)
        {
            Move move = max(child, depth + 1, a, b);
            
            if(move.getValue() < minval)
            {       
                minmove.setRow(child.getLastMove().getRow());
                minmove.setColumn(child.getLastMove().getColumn());
                minmove.setValue(move.getValue());
                minval = move.getValue();
            }

            //pruning me bash dialekseis thewrias
            if (minval < b) {
                b = minval;
            }

            if (minval <= a) {
                break;
            }
        }
        return minmove;
    }

}
