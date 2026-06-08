package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PuzzleState {
    
    private final byte[] board;

    public PuzzleState(byte[] board) {
        this.board = board.clone();
    }

    public byte[] getBoard() {
        return board.clone();
    }

    public static PuzzleState defaultGoal() {
        return new PuzzleState(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0});
    }
    
    public int getBlankPos() {
        for (int i = 0; i < 16; i++) {
            if (board[i] == 0) return i;
        }
        return -1;
    }
    
    public Map<Move, PuzzleState> getNextStatesWithMoves() {
        Map<Move, PuzzleState> nextStates = new HashMap<>();
        int blankPos = getBlankPos();
        
        // UP: move blank up
        if (blankPos >= 4) {
            nextStates.put(Move.UP, swap(blankPos, blankPos - 4));
        }
        
        // DOWN: move blank down
        if (blankPos < 12) {
            nextStates.put(Move.DOWN, swap(blankPos, blankPos + 4));
        }
        
        // LEFT: move blank left
        if (blankPos % 4 != 0) {
            nextStates.put(Move.LEFT, swap(blankPos, blankPos - 1));
        }
        
        // RIGHT: move blank right
        if (blankPos % 4 != 3) {
            nextStates.put(Move.RIGHT, swap(blankPos, blankPos + 1));
        }
        
        return nextStates;
    }
    
    private PuzzleState swap(int pos1, int pos2) {
        byte[] newBoard = board.clone();
        byte temp = newBoard[pos1];
        newBoard[pos1] = newBoard[pos2];
        newBoard[pos2] = temp;
        return new PuzzleState(newBoard);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PuzzleState)) return false;
        PuzzleState other = (PuzzleState) obj;
        return Arrays.equals(this.board, other.board);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }

}
