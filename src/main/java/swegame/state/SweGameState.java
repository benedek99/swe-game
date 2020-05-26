package swegame.state;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Class representing the state of the game.
 */
@Data
@Slf4j
public class SweGameState implements Cloneable{

    /**
     * The array representing the initial configuration of the board.
     */
    public static final int[][] INITIAL = {
            {2, 1, 2, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 2, 1, 2}
    };

    /**
     * The array storing the current configuration of the board.
     */
    @Setter(AccessLevel.NONE)
    private Cell[][] board;

    /**
     * Creates a {@code SweGameState} object representing the (original)
     * initial state of the game.
     */
    public SweGameState() {
        this(INITIAL);
    }

    /**
     * Creates a {@code SweGameState} object that is initialized it with
     * the specified array.
     *
     * @param a an array of size 5;4 representing the initial configuration
     *          of the tray
     * @throws IllegalArgumentException if the array does not represent a valid
     *                                  configuration of the tray
     */
    public SweGameState(int[][] a) {
        if (!isValidBoard(a)) {
            throw new IllegalArgumentException();
        }
        initBoard(a);
    }

    private boolean isValidBoard(int[][] a) {
        if (a == null || a.length != 5) {
            return false;
        }
        int red = 0, blue = 0;
        for (int[] row : a) {
            if (row == null || row.length != 4) {
                return false;
            }
            for (int space : row) {
                if (space < 0 || space >= Cell.values().length) {
                    return false;
                }
                if (space == Cell.RED.getValue()) {
                    red++;
                }
                if (space == Cell.BLUE.getValue()) {
                    blue++;
                }
            }
        }
        return red == 4 && blue == 4;

    }

    private void initBoard(int[][] a) {
        this.board = new Cell[5][4];
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 4; ++j) {
                this.board[i][j] = Cell.of(a[i][j]);
            }
        }
    }


    /**
     * Checks whether the game is over.
     *
     * @return {@code true} if a goal state is reached, {@code false} otherwise
     */
    public boolean isGoal() {
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (board[i][j].getValue()>0){

                if ((i==0 && j==0) || (i==0 && j==3) || (i==4 && j==0) || (i==4 && j==3)) {
                    continue;
                }
                if (i == 0 || i == 4){
                    if ((board[i][j] == board[i][j+1]) && (board[i][j] == board[i][j-1])){
                        return true;
                    }
                }
                else if (j == 0 || j == 3){
                    if ((board[i][j] == board[i+1][j]) && (board[i][j] == board[i-1][j])){
                        return true;
                    }
                }
                else if (((board[i][j] == board[i+1][j]) && (board[i][j] == board[i-1][j])) ||
                        ((board[i][j] == board[i][j+1]) && (board[i][j] == board[i][j-1])) ||
                        ((board[i][j] == board[i-1][j+1]) && (board[i][j] == board[i+1][j-1])) ||
                        ((board[i][j] == board[i-1][j-1]) && (board[i][j] == board[i+1][j+1])))
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns whether a disk at the specified position can be moved to an other specified position.
     *
     * @param fromRow the row of the disk to be moved
     * @param fromCol the column of the disk to be moved
     * @param toRow the row we move the disk to
     * @param toCol the column we move the disk to
     * @param player the number of the player moving
     * @return {@code true} if the disk at the specified position can be moved
     * to an other specified position, {@code false} otherwise
     */

    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, int player){
        if (fromRow<0 || fromRow>4 || toRow<0 || toRow>4 || fromCol<0 || fromCol>3 || toCol<0 || toCol>3){
            return false;
        }
        if (board[fromRow][fromCol].getValue()==player && board[toRow][toCol].getValue()==0){
            return (Math.abs(fromRow - toRow) == 1 && Math.abs(fromCol - toCol) == 0) || (Math.abs(fromRow - toRow) == 0 && Math.abs(fromCol - toCol) == 1);
        }
        return false;
    }

    /**
     * Moves the disk at the specified position to an other specified position.
     *
     * @param fromRow the row of the disk to be moved
     * @param fromCol the column of the disk to be moved
     * @param toRow the row we move the disk to
     * @param toCol the column we move the disk to
     * @param player the number of the player moving
     * @throws IllegalArgumentException if the disk at the specified position
     * can not be moved to the other specified position
     */
    public void move(int fromRow, int fromCol, int toRow, int toCol, int player){
        if (canMoveTo(fromRow,fromCol,toRow,toCol, player)){
            //log.info("disk at ({},{}) is rolled to ({},{})", fromRow, fromCol, toRow, toCol);
            board[toRow][toCol] = Cell.of(board[fromRow][fromCol].getValue());
            board[fromRow][fromCol] = Cell.of(0);
            System.out.println("Can move");
        }
        else {System.out.println("Cannot move");
            throw new IllegalArgumentException();

        }

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Cell[] row : board) {
            for (Cell cell : row) {
                sb.append(cell).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SweGameState state = new SweGameState();
        System.out.println(state);
        if (state.isGoal()){
            System.out.println("Player won!");
        }
        if (state.canMoveTo(0,0,3,1,1)){
            System.out.println("true");
        }
        state.move(0,0,1,0, 2);
        System.out.println(state);
    }

}
