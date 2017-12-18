package comp1110.ass2;

import java.util.HashSet;
import java.util.Set;

/**
 * This class provides the text interface for the Link Game
 *
 * The game is based directly on Smart Games' IQ-Link game
 * (http://www.smartgames.eu/en/smartgames/iq-link)
 */
public class LinkGame {

    /**
     * Determine whether a piece placement is well-formed according to the following:
     * - it consists of exactly three characters
     * - the first character is in the range A .. X
     * - the second character is in the range A .. L
     * - the third character is in the range A .. L
     *
     * @param piecePlacement A string describing a piece placement
     * @return True if the piece placement is well-formed
     */
    public static boolean isPiecePlacementWellFormed(String piecePlacement) {
        // FIXME Task 3: determine whether a piece placement is well-formed
        char origin = piecePlacement.charAt(0);
        char piece = piecePlacement.charAt(1);
        char orientation = piecePlacement.charAt(2);
        return (piecePlacement.length()==3) &&
                (origin>='A') && (origin<='X') &&
                (piece>='A') && (piece<='L') &&
                (piece=='A'? (orientation>='A') && (orientation<='F')
                : (orientation>='A') && (orientation<='L'));
    }

    /**
     * Determine whether a placement string is well-formed:
     *  - it consists of exactly N three-character piece placements (where N = 1 .. 12);
     *  - each piece placement is well-formed
     *  - no piece appears more than once in the placement
     *
     * @param placement A string describing a placement of one or more pieces
     * @return True if the placement is well-formed
     */
    public static boolean isPlacementWellFormed(String placement) {
        // FIXME Task 4: determine whether a placement is well-formed
        if(placement==null)
            return false;
        int l = placement.length();
        if(l%3!=0 || l>36 || l<3)
            return false;
        char[] element = placement.toCharArray();
        Set<Character> appeared = new HashSet<>();
        for (int i = 0; i < l; i+=3) {
            if(appeared.contains(element[i+1]) || !isPiecePlacementWellFormed(placement.substring(i, i+3)))
                return false;
            appeared.add(element[i+1]);
        }
        return true;
    }

    /**
     * Return a array of peg locations according to which pegs the given piece placement touches.
     * The values in the array should be ordered according to the links that constitute the
     * piece.
     * The code needs to account for the origin of the piece, the piece shape, and the piece
     * orientation.
     * @param piecePlacement A valid string describing a piece placement
     * @return An array of integers corresponding to the pegs which the piece placement touches,
     * listed in the normal order of links for that piece.
     */
    public static int[] getPegsForPiecePlacement(String piecePlacement) {
        // FIXME Task 6: determine the pegs touched by a piece placement
        Piece p = new Piece(piecePlacement);
        int origin = piecePlacement.charAt(0)-'A';
        int[] output = new int[3];
        int[] neibor = getNeighborsOfOrigin(origin);
        output[0] = neibor[p.node1];
        output[1] = origin;
        output[2] = neibor[p.node2];
        return output;
    }

    private static int[] getNeighborsOfOrigin(int origin){
        int[] output = new int[]{origin, origin, origin, origin, origin, origin};
        int row = origin/6;
        int col = origin%6;

        //normal case
        boolean isOdd = (row&1)==1;
        output[0] -= isOdd?5:6;
        output[1] += 1;
        output[2] += isOdd?7:6;
        output[3] = output[2]-1;
        output[4] -= 1;
        output[5] = output[0]-1;

        //upper bound
        if(row==0){
            output[0] = -1;
            output[5] = -1;
        }
        //lower bound
        if(row==3){
            output[2] = -1;
            output[3] = -1;
        }
        //left bound
        if(col==0){
            output[4] = -1;
            output[3] = isOdd?output[3]:-1;
        }
        //right bound
        if(col==5){
            output[1] = -1;
            output[0] = isOdd?-1:output[0];
        }
        return output;
    }

    /**
     * Determine whether a placement is valid.  To be valid, the placement must be well-formed
     * and each piece must correctly connect with each other.
     *
     * @param placement A placement string
     * @return True if the placement is valid
     */
    static boolean isPlacementValid(String placement) {
        // FIXME Task 7: determine whether a placement is valid
        return false;
    }

    /**
     * Return an array of all solutions given a starting placement.
     *
     * @param placement  A valid piece placement string.
     * @return An array of strings, each describing a solution to the game given the
     * starting point provied by placement.
     */
    static String[] getSolutions(String placement) {
        // FIXME Task 10: determine all solutions to the game, given a particular starting placement
        return null;
    }
}
