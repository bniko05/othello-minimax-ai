import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Game {

    private HashMap<String, Integer> symbols = new HashMap<>(); //hashmap etsi wste o paiktis na vazei syntetagmenes skakieras                                                                    
    private Board board;                                     // p.x. 3D kai na metatrepontai se syntetagmenes kinisis
    private Ai opponent;
    private int turn;
    boolean playerplays = false;
    boolean pass = false;
    Scanner in = new Scanner(System.in);

    public Game() {
        board = new Board();
    }

    public void setup() {

        symbols.put("A", 1);
        symbols.put("B", 2);
        symbols.put("C", 3);
        symbols.put("D", 4);
        symbols.put("E", 5);
        symbols.put("F", 6);
        symbols.put("G", 7);
        symbols.put("H", 8);

        turn = Board.BLACK; //o mauros paizei prwtos aneksartita apo to poios tha ksekinisei
        int depth = readInt("Insert Maximum Search Depth (1-12, recommended 5): ", 1, 12);

        System.out.println("Who wants to play first? Type P for Player and A for AI: ");
        String ans = in.nextLine().trim();

        while(!ans.equalsIgnoreCase("p") && !ans.equalsIgnoreCase("a"))
        {
            System.out.println("Invalid input!!Please Try Again: ");
            System.out.println("Who wants to play first? Type P for Player and A for AI: ");
            ans = in.nextLine().trim();
        }

        if (ans.equalsIgnoreCase("p")) 
        {
            playerplays = true;
            opponent = new Ai(depth, false); // to ai paizei me ta aspra gia auto kai pername false
        }else
        {
            opponent = new Ai(depth, true); // to ai paizei me ta maura gia auto kai pername false
        }
        
    }

    private void playerTurn() {

        boolean valid = false;
        ArrayList<Board> moves = board.getChildren(turn);

        if (moves.isEmpty()) {
            System.out.println("No Moves Available: You Lose your Turn: ");
            pass = true;
            return;
        }

        while (!valid) { //oso den vazei egkyri kinisi o paiktis tou ksanazitame

            int x = readInt("Insert X coordinate of Move (1-8): ", 1, 8);
            System.out.println("Insert Y coordinate of Move (A-H): ");
            String letter = in.nextLine().trim().toUpperCase();

            while (!symbols.containsKey(letter)) {
               System.out.println("Invalid column! Provide a letter from A to H: ");
               letter = in.nextLine().trim().toUpperCase();
            }

            int y = symbols.get(letter);

            if (board.isValidMove(x, y, turn)) {
                pass = false;
                board.makeMove(x, y, turn);
                valid = true;
            } else {
                System.out.println("Invalid Move: Try Again");
            }
        }
    }

    private void aiTurn() {

        ArrayList<Board> moves = board.getChildren(turn);

        if (moves.isEmpty()) {
            System.out.println("No Moves Available: Ai Loses its Turn: ");
            pass = true;
            return;
        }

        Move move = opponent.MiniMax(board);

        //sto ai den elegxoume an kinisi tou einai valid giati h minimax exei ftiaxtei me tetoio tropo wste na min ginete

        int x = move.getRow() + 1; //euthigrammisi twn syntetagmenwn 
        int y = move.getColumn() + 1;

        board.makeMove(x, y, turn);
    }

    public void score() { //ypologismos tou score apo to plithos twn pouliwn tou kathe xrwmatos
        int black = 0;
        int white = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getBoard()[i][j] == Board.BLACK) {
                    black++;
                } else if (board.getBoard()[i][j] == Board.WHITE) {
                    white++;
                }
            }
        }
        System.out.println("Black : " + black + " , White : " + white);
        if (black == white) {
            System.out.println("It's a Draw");
        } else {
            System.out.println(black > white ? "Black Wins" : "White Wins");
        }
    }  

    private int readInt(String prompt, int min, int max) { //zitame arithmo mexri na dothei egkyros
        while (true) {
            System.out.println(prompt);
            try {
                int value = Integer.parseInt(in.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                //mh egkyrh eisodos, ksanarwtame
            }
            System.out.println("Invalid input! Please enter a number from " + min + " to " + max + ".");
        }
    }

    public void gameloop() {
        while (!board.isTerminal()) {
            board.showBoard();

            if (playerplays) {
                playerTurn();
            }else {
                aiTurn();
            }

            turn = -turn; 
            playerplays = !playerplays; //allagi seiras
            System.out.println(); 
        }
        board.showBoard();
        score();
    }
}