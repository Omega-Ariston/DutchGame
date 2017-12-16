package comp1110.ass2;

public class Piece {
    int[] nodes = new int[3]; //[origin, node1, node2]
    int peg;
    public Piece(String p){
        char piece = p.charAt(1);
        char orientation = p.charAt(2);
        switch(piece){
            case 'A':
                peg = (1<<4) + (1<<1);
                nodes[0] = 0b011110;
                nodes[1] = 1<<1;
                nodes[2] = 1<<4;
                break;
            case 'B':
                peg = (1<<4) + (1<<1);
                nodes[0] = 0b111111;
                nodes[1] = 1<<1;
                nodes[2] = 0b111110;
                break;
            case 'C':
                peg = (1<<4) + (1<<1);
                nodes[0] = 0b111111;
                nodes[1] = 1<<1;
                nodes[2] = 0b011111;
                break;
            case 'D':
                peg = (1<<4) + 1;
                nodes[0] = 0b111111;
                nodes[1] = 1<<1;
                nodes[2] = 0b111011;
                break;
            case 'E':
                peg = (1<<4) + 1;
                nodes[0] = 0b111111;
                nodes[1] = 1<<1;
                nodes[2] = 0b101111;
                break;
            case 'F':
                peg = (1<<4) + 1;
                nodes[0] = 0b111111;
                nodes[1] = 1<<1;
                nodes[2] = 0b011111;
                break;
            case 'G':
                peg = (1<<4) + 1;
                nodes[0] = 0b110111;
                nodes[1] = 1<<1;
                nodes[2] = 1<<3;
                break;
            case 'H':
                peg = (1<<4) + 1;
                nodes[0] = 0b111111;
                nodes[1] = 0b011111;
                nodes[2] = 0b111110;
                break;
            case 'I':
                peg = (1<<4) + (1<<5);
                nodes[0] = (1<<4) + (1<<5);
                nodes[1] = 1<<1;
                nodes[2] = 0b011111;
                break;
            case 'J':
                peg = (1<<4) + (1<<5);
                nodes[0] = (1<<4) + (1<<5);
                nodes[1] = 1<<1;
                nodes[2] = 0b101111;
                break;
            case 'K':
                peg = (1<<4) + (1<<5);
                nodes[0] = 0b111011;
                nodes[1] = 1<<1;
                nodes[2] = 1<<2;
                break;
            case 'L':
                peg = (1<<4) + (1<<5);
                nodes[0] = 0b111011;
                nodes[1] = 1<<1;
                nodes[2] = 0b111101;
                break;
        }
        if(orientation>='G'){
            peg = flip(peg);
            nodes[0] = flip(nodes[0]);
            nodes[1] = flip(nodes[1]);
            nodes[2] = flip(nodes[2]);
        }
        int rotation = (orientation - 'A')%6;
        peg = rotate_clockwise(peg, rotation);
        nodes[0] = rotate_clockwise(nodes[0], rotation);
        nodes[1] = rotate_clockwise(nodes[1], rotation);
        nodes[2] = rotate_clockwise(nodes[2], rotation);
    }
    final int flip(int i){
        i = swap(i, 0, 2);
        i = swap(i, 3, 5);
        return i;
    }
    final int swap(int i, int x, int y){
        return i & (~(1<<x)) & (~(1<<y)) | (((i>>y)&1)<<x) | (((i>>x)&1)<<y);
    }

    final int rotate_clockwise(int goal, int n){
        return (goal<<n | goal>>>(6-n))&0b111111;
    }

    @Override
    public String toString(){
        return "Peg: " + Integer.toBinaryString(peg) +
                " origin: " + Integer.toBinaryString(nodes[0]) +
                " node1: " + Integer.toBinaryString(nodes[1]) +
                " node2: " + Integer.toBinaryString(nodes[2]);
    }

}
