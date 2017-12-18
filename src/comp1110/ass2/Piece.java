package comp1110.ass2;

public class Piece {

    //用一个7位二进制码表示一个node的开口情况，0到5位表示其周围6个方向的开闭口，第6位表示其自身是否为环状

    int[] nodes = new int[3]; //nodes中存放每个node的开口情况，1为闭口，0为开口
                              // 顺序为[node1, origin, node2]
    int node1;  //node1的开口情况
    int node2;  //node2的开口情况
    int place;  //棋子位置
    int peg;    //piece的形状，1为该位置有node，0为该位置无node

    static int[][] origin = new int[12][12];
    static {
        origin[0] = new int[]{
                (1<<4) + (1<<1), (1<<4) + (1<<1), (1<<4) + (1<<1),
                (1<<4) + 1, (1<<4) + 1, (1<<4) + 1, (1<<4) + 1, (1<<4) + 1,
                (1<<4) + (1<<5), (1<<4) + (1<<5), (1<<4) + (1<<5), (1<<4) + (1<<5)
        };
        for (int i = 1; i < 12; i++) {
            for (int j = 0; j < 6; j++) {
                origin[i][j] = rotate_clockwise(origin[0][j],i);
            }
            for(int j=6; j<12;j++){
                origin[i][j] = rotate_clockwise(flip(origin[0][j]),i%6);
            }
        }
    }

    public static boolean legalOrigin(int board, int o){
        for (int i = 0; i < 12; i++) {
            if((origin[i][o]&board)==0)
                return true;
        }
        return false;
    }

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

    //将棋子的节点附着情况上下翻转
    final static int flip(int i){
        i = swap(i, 0, 2);
        i = swap(i, 3, 5);
        return i;
    }

    //将node以水平中心线为轴上下翻转
    final static int flip_node(int i){
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

    //交换二进制码中的任意两位上的值
    final static int swap(int i, int x, int y){
        return i & (~(1<<x)) & (~(1<<y)) | (((i>>y)&1)<<x) | (((i>>x)&1)<<y);
    }

    //单个节点的第6位不变，1到5位旋转位移
    final static int rotate_clockwise(int goal, int n){
        int sign = goal&0b1000000;
        goal &= 0b111111;
        goal <<= n;
        goal |= (goal>>>6);
        goal &= 0b111111;
        goal |= sign;
        return goal;
    }

    //将节点附着情况旋转
    final static int rotate_node(int goal, int n){return (goal+n)%6;}

    @Override
    public String toString(){
        return "Peg: " + Integer.toBinaryString(peg) +
                " node1: " + Integer.toBinaryString(nodes[0]) +
                " origin: " + Integer.toBinaryString(nodes[1]) +
                " node2: " + Integer.toBinaryString(nodes[2]);
    }

}
