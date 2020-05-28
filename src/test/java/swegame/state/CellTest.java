package swegame.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CellTest {

    @Test
    void testof(){
        assertThrows(IllegalArgumentException.class, () -> Cell.of(-1));
        assertEquals(Cell.EMPTY, Cell.of(0));
        assertEquals(Cell.RED, Cell.of(1));
        assertEquals(Cell.BLUE, Cell.of(2));
        assertThrows(IllegalArgumentException.class, () -> Cell.of(3));
    }
}
