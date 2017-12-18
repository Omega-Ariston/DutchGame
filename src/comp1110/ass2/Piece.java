package comp1110.ass2;

public class Piece {
    int[] nodes = new int[3]; //[node1, origin, node2]
    int node1;
    int node2;
    int peg;
    int place;
    public Piece(String piece) {
        place = piece.charAt(0)-'A';
        initializePiece(piece.charAt(1),piece.charAt(2));
    }
    public Piece(char place, char piece, char orientation){
        this.place = place-'A';
        initializePiece(piece, orientation);
    }
    public void initializePiece(char piece, char orientation){
        switch(piece){
            case 'A':
                peg = (1<<4) + (1<<1);
                nodes[1] = 0b011110;
                nodes[0] = (1<<1) + (1<<6);
                nodes[2] = (1<<4) + (1<<6);
                node1 = 4;
                node2 = 1;
                break;
            case 'B':
                peg = (1<<4) + (1<<1);
                nodes[1] = 0b111111;
                nodes[0] = (1<<1) + (1<<6);
                nodes[2] = 0b111110;
                node1 = 4;
                node2 = 1;
                break;
            case 'C':
                peg = (1<<4) + (1<<1);
                nodes[1] = 0b111111;
                nodes[0] = (1<<1) + (1<<6);
                nodes[2] = 0b011111;
                node1 = 4;
                node2 = 1;
                break;
            case 'D':
                peg = (1<<4) + 1;
                nodes[1] = 0b111111;
                nodes[0] = (1<<1) + (1<<6);
                nodes[2] = 0b111011;
                node1 = 4;
                node2 = 0;
                break;
            case 'E':
                peg = (1<<4) + 1;
                nodes[1] = 0b111111;
                nodes[0] = (1<<1) + (1<<6);
                nodes[2] = 0b101111;
                node1 = 4;
                node2 = 0;
                break;
            case 'F':
                peg = (1<<4) + 1;
                nodes[1] = 0b111111;
                nodes[0] = (1<<1) + (1<<6);
                nodes[2] = 0b011111;
                node1 = 4;
                node2 = 0;
                break;
            case 'G':
                peg = (1<<4) + 1;
                nodes[1] = 0b110111;
                nodes[0] = (1<<1) + (1<<6);
                nodes[2] = (1<<3) + (1<<6);
                node1 = 4;
                node2 = 0;
                break;
            case 'H':
                peg = (1<<4) + 1;
                nodes[1] = 0b111111;
                nodes[0] = 0b011111;
                nodes[2] = 0b111110;
                node1 = 4;
                node2 = 0;
                break;
            case 'I':
                peg = (1<<4) + (1<<5);
                nodes[1] = (1<<4) + (1<<5) + (1<<6);
                nodes[0] = (1<<1) + (1<<6);
                nodes[2] = 0b011111;
                node1 = 4;
                node2 = 5;
                break;
            case 'J':
                peg = (1<<4) + (1<<5);
                nodes[1] = (1<<4) + (1<<5)+ (1<<6);
                nodes[0] = (1<<1) + (1<<6);
                nodes[2] = 0b101111;
                node1 = 4;
                node2 = 5;
                break;
            case 'K':
                peg = (1<<4) + (1<<5);
                nodes[1] = 0b111011;
                nodes[0] = (1<<1) + (1<<6);
                nodes[2] = (1<<2)+ (1<<6);
                node1 = 4;
                node2 = 5;
                break;
            case 'L':
                peg = (1<<4) + (1<<5);
                nodes[1] = 0b111011;
                nodes[0] = (1<<1) + (1<<6);
                nodes[2] = 0b111101;
                node1 = 4;
                node2 = 5;
                break;
        }
        if(orientation>='G'){
            peg = flip(peg);
            nodes[1] = flip(nodes[1]);
            nodes[0] = flip(nodes[0]);
            nodes[2] = flip(nodes[2]);
            node1 = flip_node(node1);
            node2 = flip_node(node2);
        }
        int rotation = (orientation - 'A')%6;
        peg = rotate_clockwise(peg, rotation);
        nodes[1] = rotate_clockwise(nodes[1], rotation);
        nodes[0] = rotate_clockwise(nodes[0], rotation);
        nodes[2] = rotate_clockwise(nodes[2], rotation);
        node1 = rotate_node(node1, rotation);
        node2 = rotate_node(node2, rotation);
    }
    final int flip(int i){
        i = swap(i, 0, 2);
        i = swap(i, 3, 5);
        return i;
    }
    final int flip_node(int i){
        switch(i){
            case 1:
            case 4:
                return i;
            case 3:
                return 5;
            case 5:
                return 3;
            case 0:
                return 2;
            case 2:
                return 0;
            default:
                System.out.println("Something wrong happened in flip_node");
                return -1;
        }
    }
    public static void main(String[] args) {
        Piece p1 = new Piece("HKJ");
        Piece p2 = new Piece("OEA");
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(Integer.toBinaryString(rotate_clockwise(0b1110100, 3)));
    }
    final int swap(int i, int x, int y){
        return i & (~(1<<x)) & (~(1<<y)) | (((i>>y)&1)<<x) | (((i>>x)&1)<<y);
    }

    //第七位不变，后六位旋转位移
    final static int rotate_clockwise(int goal, int n){
        int sign = goal&0b1000000;
        goal &= 0b111111;
        goal <<= n;
        goal |= (goal>>>6);
        goal &= 0b111111;
        goal |= sign;
        return goal;
    }

    final int rotate_node(int goal, int n){return (goal+n)%6;}

    @Override
    public String toString(){
        return "Peg: " + Integer.toBinaryString(peg) +
                " node1: " + Integer.toBinaryString(nodes[0]) +
                " origin: " + Integer.toBinaryString(nodes[1]) +
                " node2: " + Integer.toBinaryString(nodes[2]);
    }

}
