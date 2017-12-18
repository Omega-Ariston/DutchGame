package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Random;

import static comp1110.ass2.TestUtility.BASE_ITERATIONS;
import static org.junit.Assert.assertTrue;

/**
 * Test objective:
 *
 * Return a array of peg locations according to which pegs the given piece placement touches.
 * The values in the array should be ordered according to the links that constitute the
 * piece.
 *
 * The code needs to account for the origin of the piece, the piece shape, and the piece
 * orientation.
 */
public class PegsForPiecePlacementTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    void checkPegs(String test, int one, int two, int three) {
        int[] pegs = LinkGame.getPegsForPiecePlacement(test);
        assertTrue("Placement '" + test + "' returned a null pointer", pegs != null);
        assertTrue("Placement '" + test + "', peg 0 is at " + one + " ("+((char) ('A'+one))+")"+", but got " + pegs[0] + " ("+((char) ('A'+pegs[0]))+")", pegs[0] == one);
        assertTrue("Placement '" + test + "', peg 1 is at " + two + " ("+((char) ('A'+two))+")"+", but got " +  pegs[1] +  " ("+((char) ('A'+pegs[1]))+")", pegs[1] == two);
        assertTrue("Placement '" + test + "', peg 2 is at " + three + " ("+((char) ('A'+three))+")"+", but got " + pegs[2]+  " ("+((char) ('A'+pegs[2]))+")", pegs[2] == three);
    }

    @Test
    public void testSimpleUnrotated() {
        Random r = new Random();
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            int row = 1 + r.nextInt(3);
            int col = 1 + r.nextInt(4);
            int origin = (row * 6) + col;
            int piece = r.nextInt(12);
            int first = origin - 1;
            int third = origin;
            if (piece < 3)
                third += 1;
            else if (piece < 8)
                third += (row % 2 == 0) ? -6 : -5;
            else
                third += (row % 2 == 0) ? -7 : -6;

            String test = "" + (char) ('A' + origin) + (char) ('A' + piece) + 'A';
            checkPegs(test, first, origin, third);
        }
    }

    @Test
    public void testSimpleRotated() {
        Random r = new Random();
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            int row = 1+ r.nextInt(2);
            int col = 1 + r.nextInt(4);
            int origin = (row * 6) + col;
            int piece = 1 + r.nextInt(11);
            int first = origin + ((row % 2 == 0) ? 6 : 7);
            int third = origin;
            if (piece < 3)
                third += (row % 2 == 0) ? -7 : -6;
            else if (piece < 8)
                third += (row % 2 == 0) ? -6 : -5;
            else
                third += 1;

            String test = "" + (char) ('A' + origin) + (char) ('A' + piece) + 'K';
            checkPegs(test, first, origin, third);
        }
    }

    @Test
    public void testOffEdgeUnrotated() {
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            Random r = new Random();
            int origin = 1 + r.nextInt(4);
            int piece = 3 + r.nextInt(9);
            String test = "" + (char) ('A' + origin) + (char) ('A' + piece) + 'A';
            checkPegs(test, origin -1, origin, -1);
        }
    }

    @Test
    public void testOffEdgeRotated() {
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            Random r = new Random();
            int origin = 18 + r.nextInt(5);
            int piece = 3 + r.nextInt(9);
            int three = origin + ((piece < 8) ? -5 : 1);
            String test = "" + (char) ('A' + origin) + (char) ('A' + piece) + 'K';
            checkPegs(test, -1, origin, three);
        }
    }
}
