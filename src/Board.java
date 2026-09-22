import java.util.ArrayList;

public class Board {

    public static final int BLACK = 1;
    public static final int EMPTY = 0;
    public static final int WHITE = -1;

    private int gameBoard[][] = new int [8][8];
    private Move lastMove;

    public Board() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gameBoard[i][j] = EMPTY;
            }
        }
        //topothetisi twn arxikwn pouliwn sto kentro tiw skakieras opws ginetai sto klasiko othello
        gameBoard[3][3] = WHITE;
        gameBoard[4][3] = BLACK;
        gameBoard[4][4] = WHITE;
        gameBoard[3][4] = BLACK;

        lastMove = new Move(-1, -1, 0);
    }

    public Board(Board board){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gameBoard[i][j] = board.gameBoard[i][j];
            }
        }

        lastMove = board.getLastMove();
    }

    public int[][] getBoard()
    {
        return gameBoard;
    }
    public Move getLastMove(){

        return lastMove;
    }

    public void showBoard() {

        int k = 0;
        System.out.println("    A   B   C   D   E   F   G   H");
        for (int i = 0; i < 17; i++) {
            if (i%2 == 0) {
                  System.out.println("  ---------------------------------");
            } else {
                k++;
                System.out.print(i-k+1 + " |");
                for (int j = 0; j < 8; j++) {
                    if (gameBoard[i-k][j] == EMPTY )
                    {
                        System.out.print("   |");
                    }else if (gameBoard[i-k][j] == WHITE )
                    {
                        System.out.print(" o |");
                    }else if (gameBoard[i-k][j] == BLACK )
                    {
                        System.out.print(" * |");
                    }    
                }
                System.out.println(" " + (i-k+1));
            }
        }
        System.out.println("    A   B   C   D   E   F   G   H");
    }


     public int heuristic(int turn) {
        int f1 = f1();
        int f2 = f2();
        int f3 = f3();
        int f4 = f4(turn);

        int heuristic = f1 + 6*f2 + 4*f3 + 5*f4; //oi topothetisi twn varwn egine me diaisthisi
        return heuristic;
    }

    private int f1() { //diafora metaksi maurwn kai asprwn
        int blacks = 0;
        int whites = 0;

        for  (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameBoard[i][j] == BLACK) {
                    blacks++;
                } else if (gameBoard[i][j] == WHITE) {
                    whites++;
                }
            }
        }
        return blacks - whites;
    }

    private int f2() { //poios exei tis pio polles gwnies 
        return gameBoard[0][0] + gameBoard[0][7] + gameBoard[7][0] + gameBoard[7][7];
    }

    private int f3() { //poios exei ta perissotera poulia se akraies theseis ektos apo tis gwnies;

        int result = 0;
        for (int i = 1; i < 7; i++) {
            result += gameBoard[0][i] + gameBoard[7][i] + gameBoard[i][0] + gameBoard[i][7]; 
        }
        return result;
    }

    private int f4(int turn) { //an se epomeni kinisi mporei na parei gwnia
        int result = 0;

        if (isValidMove(1, 1, turn) || isValidMove(8, 8, turn) || isValidMove(1, 8, turn) || isValidMove(8, 1, turn))
        {
            result = 1;
        }
        return result;
    }

    public void makeMove(int x, int y, int slotType) {
        x--; //euthigrammisi me tis syntetagmenes tou pinaka
        y--;

        gameBoard[x][y] = slotType;

        turnPieces(x, y, slotType);

        lastMove = new Move(x, y, 0);
    }

    private boolean outofbounds(int x)
    {
        return x < 0 || x >= 8; 
    }

    public boolean isValidMove(int x, int y, int slotType) {
        if (x < 1 || x > 8 || y < 1 || y > 8)
            return false;

        //euthigrammisi twn syntetagmenwn me ton pinaka 
        x--;
        y--;

        if(gameBoard[x][y] != EMPTY)
        {
            return false;
        }

        //psakse pros oles tis kateuthinsis gia katallili akolouthia apo poulia
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

                //arxikopoiisi twn metritwn
                int k = x + i;
                int l = y + j;

                boolean found = false;
                
                if(i== 0 && j == 0) continue;
                //oso den exeis vgei ektos oriwn kai petyxeneis pouli tou antipalou synexise
                while(!outofbounds(k) && !outofbounds(l) && gameBoard[k][l] == -slotType)
                {
                    found = true;
                    k += i;
                    l += j;
                }
                //an vrikes estw kai ena pouli antipalou kai den eisai ektos oriwn kai exeis stamatisei se diko sou tote i kinisi einai valid 
                if(found && !outofbounds(k) && !outofbounds(l) && gameBoard[k][l] == slotType)
                {
                    return true;
                }
            }
        } 
        return false;
    }

    public ArrayList<Board> getChildren(int turn) {
        //gia kathe keli ths skakieras elegxoume an h kinhsh auth einai nomimh kai tote thn prosthetoume stis pithanes 
        ArrayList<Board> children = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                if (isValidMove(i, j, turn)) {
                    Board child = new Board(this);
                    child.makeMove(i, j, turn);
                    children.add(child);
                }
            }
        }
        return children;
    }


    public boolean isTerminal() {
        //an kaneis den exei kinhsh, to paixnidi teleiwse
        ArrayList<Board> movesBlack = getChildren(Board.BLACK);
        ArrayList<Board> movesWhite = getChildren(Board.WHITE);
        return movesBlack.isEmpty() && movesWhite.isEmpty();
    }

    private void turnPieces(int x, int y, int slotType)
    {
        //idia logiki me tin isValidMove opou i moni diafora einai oti den elexoume an vrikame pouli tou allou 
        // xrwmatos alla aplws kratame se enan pinaka auta pou prepei na allaksoume sto telos tis epanalipsis
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
          
                if(i== 0 && j == 0) continue;
            
                //arxikopoiisi twn metritwn
                int k = x + i;
                int l = y + j;
                ArrayList<int[]> toturnpieces = new ArrayList<int[]>();
            
                while(!outofbounds(k) && !outofbounds(l) && gameBoard[k][l] == -slotType)
                {
                    toturnpieces.add(new int[]{k,l});
                    k += i;
                    l += j;
                }
            
                if(!toturnpieces.isEmpty() && !outofbounds(k) && !outofbounds(l) && gameBoard[k][l] == slotType)
                {
                    for(int t = 0 ; t < toturnpieces.size(); t++)
                    {
                        gameBoard[toturnpieces.get(t)[0]][toturnpieces.get(t)[1]] = slotType;
                    }
                }
            }
        } 
    }
}