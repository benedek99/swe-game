package swegame.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SweGameStateTest {

    @Test
    void testOneArgConstructor_InvalidArg(){
        assertThrows(IllegalArgumentException.class, () -> new SweGameState(null));
        assertThrows(IllegalArgumentException.class, () -> new SweGameState(new int[][] {
                {1, 1},
                {1, 0}})
        );
        assertThrows(IllegalArgumentException.class, () -> new SweGameState(new int[][] {
                {0},
                {1, 2},
                {3, 4, 5}})
        );
        assertThrows(IllegalArgumentException.class, () -> new SweGameState(new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {1, 2, 3},
                {1, 2, 3},})
        );
        assertThrows(IllegalArgumentException.class, () -> new SweGameState(new int[][] {
                {1, 1, 5, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}})
        );
        assertThrows(IllegalArgumentException.class, () -> new SweGameState(new int[][] {
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}})
        );
        assertThrows(IllegalArgumentException.class, () -> new SweGameState(new int[][] {
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {2, 0, 2, 0},
                {0, 2, 0, 0},
                {0, 2, 1, 0}})
        );
    }

    @Test
    void testOneArgConstructor_ValidArg() {
        int[][] a = new int[][] {
                {2, 1, 2, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {1, 2, 1, 2}
        };
        SweGameState state = new SweGameState(a);
        assertArrayEquals(new Cell[][] {
                {Cell.BLUE,Cell.RED,Cell.BLUE,Cell.RED},
                {Cell.EMPTY,Cell.EMPTY,Cell.EMPTY,Cell.EMPTY},
                {Cell.EMPTY,Cell.EMPTY,Cell.EMPTY,Cell.EMPTY},
                {Cell.EMPTY,Cell.EMPTY,Cell.EMPTY,Cell.EMPTY},
                {Cell.RED,Cell.BLUE,Cell.RED,Cell.BLUE}
        }, state.getBoard());
    }

    @Test
    void testIsGoal(){
        assertFalse(new SweGameState().isGoal());
        assertTrue(new SweGameState(new int[][] {
                {2, 2, 2, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {1, 2, 1, 1}
        }).isGoal());
        assertTrue(new SweGameState(new int[][] {
                {2, 0, 0, 1},
                {2, 0, 0, 0},
                {2, 0, 0, 0},
                {0, 0, 0, 0},
                {1, 2, 1, 1}
        }).isGoal());
        assertTrue(new SweGameState(new int[][] {
                {2, 0, 2, 1},
                {0, 0, 1, 0},
                {0, 1, 0, 0},
                {0, 0, 0, 0},
                {1, 2, 2, 0}
        }).isGoal());
    }

    @Test
    void testCanMoveTo(){
        SweGameState state = new SweGameState();
        assertFalse(state.canMoveTo(-1,0,3 ,5));
        assertFalse(state.canMoveTo(6,0,3 ,5));
        assertFalse(state.canMoveTo(0,0,5 ,5));
        assertFalse(state.canMoveTo(0,0,-4 ,5));
        assertFalse(state.canMoveTo(0,0,1 ,0));
        assertFalse(state.canMoveTo(0,0,0 ,1));
        assertFalse(state.canMoveTo(0,0,0 ,0));
        assertTrue(state.canMoveTo(0,0,1,0));
        assertTrue(state.canMoveTo(4,0,3,0));
    }

    @Test
    void testMove(){
        SweGameState state = new SweGameState();
        state.move(0,0,1,0);
        assertArrayEquals(new Cell[][] {
                {Cell.EMPTY,Cell.RED,Cell.BLUE,Cell.RED},
                {Cell.BLUE,Cell.EMPTY,Cell.EMPTY,Cell.EMPTY},
                {Cell.EMPTY,Cell.EMPTY,Cell.EMPTY,Cell.EMPTY},
                {Cell.EMPTY,Cell.EMPTY,Cell.EMPTY,Cell.EMPTY},
                {Cell.RED,Cell.BLUE,Cell.RED,Cell.BLUE}
        }, state.getBoard());
        state.move(4,0,3,0);
        assertArrayEquals(new Cell[][] {
                {Cell.EMPTY,Cell.RED,Cell.BLUE,Cell.RED},
                {Cell.BLUE,Cell.EMPTY,Cell.EMPTY,Cell.EMPTY},
                {Cell.EMPTY,Cell.EMPTY,Cell.EMPTY,Cell.EMPTY},
                {Cell.RED,Cell.EMPTY,Cell.EMPTY,Cell.EMPTY},
                {Cell.EMPTY,Cell.BLUE,Cell.RED,Cell.BLUE}
        }, state.getBoard());
    }

    @Test
    void testToString() {
        SweGameState state = new SweGameState();
        assertEquals("2 1 2 1 \n"
                + "0 0 0 0 \n"
                + "0 0 0 0 \n"
                + "0 0 0 0 \n"
                + "1 2 1 2 \n", state.toString());
    }
}
