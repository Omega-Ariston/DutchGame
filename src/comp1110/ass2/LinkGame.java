package comp1110.ass2;

import java.util.ArrayList;

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
    public static boolean isPiecePlacementWellFormed(char origin, char piece, char orientation){
        return (origin>='A') && (origin<='X') &&
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
        int appeared_piece = 0;
        for (int i = 0; i < l; i+=3) {
            if(((appeared_piece>>(element[i+1]-'A'))&1)==1 ||
                    !isPiecePlacementWellFormed(element[i], element[i+1], element[i+2]))
                return false;
            appeared_piece |= 1<<(element[i+1]-'A');
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
        Piece p = Piece.getPiece(piecePlacement);
        int origin = piecePlacement.charAt(0)-'A';
        int[] output = new int[3];
        int[] neighbor = getNeighborsOfOrigin(origin);
        output[0] = neighbor[p.node1];
        output[1] = origin;
        output[2] = neighbor[p.node2];
        return output;
    }
    public static int[] getPegsForPiecePlacement(Piece p) {
        int[] output = new int[3];
        int[] neighbor = getNeighborsOfOrigin(p.place);
        output[0] = neighbor[p.node1];
        output[1] = p.place;
        output[2] = neighbor[p.node2];
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

        if(row==0){  //upper bound
            output[0] = -1;
            output[5] = -1;
        }else if(row==3){ //lower bound
            output[2] = -1;
            output[3] = -1;
        }

        if(col==0){ //left bound
            output[4] = -1;
            output[3] = isOdd?output[3]:-1;
        }else if(col==5){   //right bound
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
        if(!isPlacementWellFormed(placement))
            return false;
        char[] p = placement.toCharArray();
        int l = p.length;
        int[] board = new int[24];
        for (int i = 0; i <l ; i+=3) {
            Piece piece = Piece.getPiece(p[i], p[i+1], p[i+2]);
            int[] pegs = getPegsForPiecePlacement(piece);
            for(int j=0; j<3;j++){
                int k = pegs[j];
                if(k==-1) {
                    return false;
                }
                if(board[k]==0)
                    board[k] = piece.nodes[j];
                else{
                    if((board[k]&piece.nodes[j])!=0)
                        return false;
                    board[k] += piece.nodes[j];
                }
            }
        }
        return true;
    }

    public static String getLeft(String s){
        int store = 0;
        for(int i=s.length()-1; i>=0;i--){
            store |= 1<<(s.charAt(i)-65);
        }
        StringBuilder sb = new StringBuilder("");
        for(int i=0; i<26; i++){
            if((store&1)==0)
                sb.append((char)('A'+i));
            store>>=1;
        }
        return sb.toString();
    }

    /**
     * Return an array of all solutions given a starting placement.
     *
     * @param placement  A valid piece placement string.
     * @return An array of strings, each describing a solution to the game given the
     * starting point provided by placement.
     */
    static String[] getSolutions(String placement) {
        // FIXME Task 10: determine all solutions to the game, given a particular starting placement
        int appeared = 0;
        ArrayList<String> output = new ArrayList<>();
        for (int i = placement.length()-2; i >= 1 ; i-=3) {
            appeared |= 1<<(placement.charAt(i)-'A');
        }
        addSolution(appeared,placement,output);
        return output.toArray(new String[0]);
    }
    //H-OR-
    //I-
    //J-
    //K-
    //L-
    static void addSolution(int appeared, String placement, ArrayList<String> solution){
        if(placement.length()==36)
            solution.add(placement);
        char[] p = placement.toCharArray();
        int[] board = boardGenerate(p);

        for (int i = 0; i < 12; i++) {
            if(((appeared>>i)&1)==0){
                char nextPiece = (char)('A' + i);
                //System.out.println(nextPiece);
                int[] legalMove = legalPeg(board, nextPiece);
                int j = 0;
                while(j<24){
                    if(legalMove[j]!=-1) {
                        char bound = nextPiece == 'A' ? 'F' : 'L';
                        for (char k = 'A'; k <= bound; k++) {
                            String piece = "" + (char) ('A' + legalMove[j]) + nextPiece + (char) k;
                            //System.out.println(piece);
                            if (legalNextMove(board, piece)) {
                                //System.out.println(placement+piece);
                                appeared |= 1 << i;
                                addSolution(appeared, placement + piece, solution);
                            }
                        }
                    }
                    j++;
                }
            }
        }
    }

    public static void main(String[] args) {
        String[] output = getSolutions("JACUBACCG");
        for (String s:output
             ) {
            System.out.println(s);
        }
//        int[] board = boardGenerate("JABHBCBCGGDFIEKVFAFGGSHBXIA".toCharArray());
//        System.out.println(Piece.legalOrigin(board['R'-'A'],'H'-'A'));
//        System.out.println(Integer.toBinaryString(Piece.origin['C'-'A']['H'-'A']));
//        int[] legalMove = legalPeg(board, 'H');
//        for (int i:legalMove
//             ) {
//            System.out.println((char)('A'+i));
//        }
    }

    static int[] boardGenerate(char[] p){
        int l = p.length;
        int[] board = new int[24];
        for (int i = 0; i <l ; i+=3) {
            Piece piece = Piece.getPiece(p[i], p[i+1], p[i+2]);
            int[] pegs = getPegsForPiecePlacement(piece);
            for(int j=0; j<3;j++){
                int k = pegs[j];
                if(board[k]==0)
                    board[k] = piece.nodes[j];
                else{
                    board[k] += piece.nodes[j];
                }
            }
        }
        return board;
    }

    static int[] legalPeg(int[] board, char piece){
        int[] output = new int[24];
        int index = 0;
        int origin = piece - 'A';
        for (int i=0; i<24;i++) {
            if(Piece.legalOrigin(board[i],origin))
                output[index++]=i;
            else
                output[index++]=-1;
        }
        return output;
    }

    static boolean legalNextMove(int[] board, String piece){
        Piece p = Piece.getPiece(piece);
        int[] pegs = getPegsForPiecePlacement(p);
        for (int i =0; i<3;i++) {
            if(pegs[i]==-1 || (board[pegs[i]]&p.nodes[i])!=0)
                return false;
        }
        return true;
    }
}
