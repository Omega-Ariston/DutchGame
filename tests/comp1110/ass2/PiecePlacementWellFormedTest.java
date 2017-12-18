package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Random;

import static comp1110.ass2.TestUtility.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test objective:
 *
 * Determine whether a piece placement is well-formed according to the following:
 * - it consists of exactly three characters
 * - the first character is in the range A .. X
 * - the second character is in the range A .. L
 * - the third character is in the range A .. F if the second character is A, otherwise
 *   in the range A .. L
 */
public class PiecePlacementWellFormedTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    @Test
    public void testSimple() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String p = TestUtility.shufflePlacement(PLACEMENTS[i]);
            for (int j = 0; j < p.length(); j += 3) {
                String test = p.substring(j, j + 3);
                assertTrue("Simple piece placement string '" + test + "', should be OK, but was not", LinkGame.isPiecePlacementWellFormed(test));
            }
        }
    }

    @Test
    public void testUpperCase() {
        for (int i = 0; i < PLACEMENTS[0].length(); i+= 3) {
            String test = PLACEMENTS[0].substring(i, i+3).toLowerCase();
            assertFalse("Simple piece placement string '"+test+"', is lower case, but passed", LinkGame.isPiecePlacementWellFormed(test));
        }
    }

    @Test
    public void testGood() {
        Random r = new Random();
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            char a = (char) ('A' + r.nextInt(PEGS));
            char b = (char) ('A' + r.nextInt(PIECES));
            char c = (char) ('A' + r.nextInt(b == 'A' ? ORIENTATIONS / 2 : ORIENTATIONS));
            String test = ""+a+b+c;
            assertTrue("Well-formed piece placement string '" + test + "' failed", LinkGame.isPiecePlacementWellFormed(test));
        }
    }

    @Test
    public void testA() {
        Random r = new Random();
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            char a = (char) ('A' + r.nextInt(PEGS));
            char b = 'A';
            char c = (char) ('A' + r.nextInt(ORIENTATIONS));
            String test = "" + a + b + c;
            if (c > 'F')
                assertFalse("Badly-formed piece placement string '" + test + "' passed", LinkGame.isPiecePlacementWellFormed(test));
            else
                assertTrue("Well-formed piece placement string '" + test + "' failed", LinkGame.isPiecePlacementWellFormed(test));
        }
    }

    @Test
    public void testBad() {
        Random r = new Random();
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String test = TestUtility.badlyFormedPiecePlacement(r);
            assertFalse("Badly-formed piece placement string '" + test + "' passed", LinkGame.isPiecePlacementWellFormed(test));
        }
    }
}
