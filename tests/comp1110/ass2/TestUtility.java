package comp1110.ass2;

import java.util.Random;

public class TestUtility {
    static final int BASE_ITERATIONS = 100;
    static final int PEGS = 24;
    static final int PIECES = 12;
    static final int ORIENTATIONS = 12;
    static final char BAD = Character.MAX_VALUE - 'Z';

    static final String[] PLACEMENTS = {
            "BAARBEUCAGDFWEBFFEIGDSHBDIFPJKJKCHLL",
            "BADPBAWCARDKTEBJFIKGDMHKBILFJFCKKOLH",
            "CAAJBLICFGDFQEKFFGBGESHBUIJXJHRKIHLL",
            "CADTBAWCAQDHKEEFFGRGCMHKCILPJHBKFHLE",
            "DAAGBKWCAQDHKEEPFFRGCFHEBIKSJBHKDOLL",
            "DADTBANCJGDFJEBPFFRGAFHEBIKVJJWKCHLD",
            "EAAIBLVCJRDCKEAPFLWGBTHAAIKCJGGKDMLK",
            "EADTBAQCIGDHBEEIFKHGCLHHWICEJLPKLNLG",
            "GABCBDVCJRDCIEATFIWGBEHLGICPJFQKIMLK",
            "GACIBLVCJKDEQEGFFGRGCSHBNIBCJFEKFPLF",
            "GAETBAJCFCDDBEEMFCDGLOHAXIAFJLPKEKLE",
            "GAFJBBECDNDCUEJSFJVGBCHGXIHLJAPKARLA",
            "IAABBGWCDODDFEEMFKPGLRHIKIHIJBHKFNLE",
            "IABGBBWCJQDLFEGSFJEGEUHIOIABJKKKDJLA",
            "IACUBACCGGDFQEKFFGBGESHBIIKXJHRKIHLL",
            "IADEBJRCKVDAWEBOFIUGISHBAIKLJAGKLCLG",
            "IAEVBADCDJDKBEEOFCGGFSHBFILNJBRKLKLE",
            "IAFUBACCGKDLPEIFFEHGLSHBAIKXJHQKEGLL",
            "JAACBDQCFPDFWEISFBRGKFHEGIIHJEDKKOLL",
            "JABHBCUCAPDEIEFEFLQGCMHKBIFXJHTKHRLI",
            "JACUBACCGKDLIECFFEQGHSHBAIKWJCHKLGLL",
            "JADDBDVCDRDCMECOFBWGBFHEJILNJLHKIGLB",
            "JAEBBGOCDGDFLEFSFJTGBVHIKIIJJGRKEILH",
            "JAFUBAICIKDLCELFFEQGDSHBAIKXJAGKLHLE",
            "KAADBDUCARDKWEBHFCPGGSHBIIKAJKGKLELK",
            "KABQBBUCAPDECELIFLHGHSHBXIAAJKFKEGLL",
            "KACUBAOCJHDFDEGGFFCGESHBKIDXJHQKEILD",
            "KADPBBBCDNDBFEGSFJGGFDHLXIAKJEWKHTLC",
            "KAEHBFTCAODEFEEVFBPGJMHKBIFDJERKLJLD",
            "KAFUBACCGGDFLEFIFCBGESHBOIAEJEQKGRLE"
    };

    final static String[] NOT_COVER ={
            "BAAHBA",
            "TCJKLJ",
            "BAAKLJ",
            "MJJPBA",
            "WEBEFD",
            "HBATCJ",
            "TEBJFI",
            "GAEPKE",
            "RDKQKI",
            "QKGRLE"

    };
    final static String[] COVERED={
            "MJJHAA",
            "MJJOBA",
            "HBAICJ",
            "JAFPKE",
            "QDHKEE",
            "VFBPGJ",
            "IFCQEG",
            "QKIWDB",
            "IAFNBA",
            "TCJTLJ",
    };

    final static String[] GOOD_PAIRS = {
            "OEAHKJ",
            "OEANLH",
            "OEATLA",
            "OEAHIJ",
            "OEAHCJ",
            "OEAHGB",
            "OEAMIK",
            "OEAGJD",
            "BADBIL",
            "BADBJE"
    };

    final static String[] BAD_PAIRS = {
            "OEASIJ",
            "OEAHDE",
            "OEACFE",
            "OEAJBG",
            "OEAHKD",
            "OEAHHB",
            "OEAPGG",
            "OEACJK",
            "OEAGKD"
    };

    final static String[][] SOLUTIONS_ONE = {
            {"KAFCBGUCAGDFLEFPFBBGESHBWIJKJA", "KAFCBGUCAGDFLEFPFBBGESHBWIJKJAHKLJLH"},
            {"KAFCBGUCAGDFLEFPFBBGESHBOIA", "KAFCBGUCAGDFLEFPFBBGESHBOIAKJARKEJLH"},
            {"KAFTBAICFRDCEELWFJJGDMHK", "KAFTBAICFRDCEELWFJJGDMHKCIGNJCPKEBLF"},
            {"JABHBCBCGGDFIEKVFAFGG", "JABHBCBCGGDFIEKVFAFGGSHBXIAJJJUKHKLK"},
            {"JACRBHQCHCDGDELVFJ", "JACRBHQCHCDGDELVFJBGESHBUIAFJEHKLGLL"},
            {"IAFBBDRCEPDEWEB", "IAFBBDRCEPDEWEBSFJTGBFHGGILIJAQKIJLI"},
            {"GAEWBABCDJDA", "GAEWBABCDJDALEFMFCCGLUHBTIAQJCKKBILF"},
    };

    final static String[][] SOLUTIONS_MULTI = {
            {"KAFUBAICCPDALEFEFEQGHSHBNIB", "KAFUBAICCPDALEFEFEQGHSHBNIBCJFGKIRLE", "KAFUBAICCPDALEFEFEQGHSHBNIBCJFRKEGLI"},
            {"KAFCBGUCAGDFLEFPFBBGESHB", "KAFCBGUCAGDFLEFPFBBGESHBOIAKJARKEJLH", "KAFCBGUCAGDFLEFPFBBGESHBWIJKJAHKLJLH"},
            {"IAFBBGVCAJDJGEDQFEUGI", "IAFBBGVCAJDJGEDQFEUGIRHCIIHFJGNKFOLG", "IAFBBGVCAJDJGEDQFEUGIRHKIIHFJGNKFOLG"},
            {"KAAHBLTCAODEFEGMFC", "KAAHBLTCAODEFEGMFCEGERHGBIGVJCDKFJLF", "KAAHBLTCAODEFEGMFCEGERHGBIGVJIDKFJLF", "KAAHBLTCAODEFEGMFCEGEQHDBIGXJHDKFJLF", "KAAHBLTCAODEFEGMFCEGEVHBBIGXJADKFJLF"},//"KAAHBLTCAODEFEGMFCEGEBIGDKFJLF QHD XJH"
            {"JADVBJBCJRDCDED", "JADVBJBCJRDCDEDHFEWGBFHEJILSJCOKLMLC", "JADVBJBCJRDCDEDSFBWGBFHEJILGJEOKLHLE"},
            {"JAAPBGVCJRDC", "JAAPBGVCJRDCDEDSFBWGBFHECIFAJDHKGOLF", "JAAPBGVCJRDCDEDSFBWGBFHECIFAJDOKFHLG", "JAAPBGVCJRDCHEFSFBWGBFHEGIICJDDKKOLF"},
         // {"KAFUBAHCI", "KAFUBAHCIPDALEFEFEQGHSHBWIJAJKGKLILI", "KAFUBAHCIPDALEFEFEQGHSHBWIJBJFGKEILI"}
    };


    static final char[][] PIECE_SHAPES = {
            {'A', 'B', 'C'},
            {'D', 'E', 'F', 'G', 'H'},
            {'I', 'J', 'K', 'L'}
    };

    static String shufflePlacement(String placement) {
        Random r = new Random();
        int pieces = placement.length() / 3;
        if (pieces == 1) return placement;

        int order[] = new int[pieces];
        for (int i = 1; i < pieces; i++) {
            int slot = r.nextInt(pieces - 1);
            while (order[slot] != 0) slot = (slot + 1) % pieces;
            order[slot] = i;
        }

        String shuffled = "";
        for (int i = 0; i < pieces; i++) {
            shuffled += placement.substring(3 * order[i], 3 * (order[i] + 1));
        }
        return shuffled;
    }

    static String badlyFormedPiecePlacement(Random r) {
        char a = (char) ('A' + r.nextInt(PEGS));
        char bada = (char) ('A' + PEGS + r.nextInt(r.nextInt(BAD)));
        char b = (char) ('A' + r.nextInt(PIECES));
        char badb = (char) ('A' + PIECES + r.nextInt(BAD));
        char c = (char) ('A' + r.nextInt(b == 'A' ? ORIENTATIONS / 2 : ORIENTATIONS));
        char badc = (char) ('A' + (b == 'A' ? ORIENTATIONS / 2 : ORIENTATIONS) + r.nextInt(BAD));
        String test = "";
        switch (r.nextInt(4)) {
            case 0:
                test += "" + bada + b + c;
                break;
            case 1:
                test += "" + a + badb + c;
                break;
            case 2:
                test += "" + a + b + badc;
                break;
            default:
                test += "" + bada + b + badc;
        }
        return test;
    }

    static String invalidPiecePlacement(Random r) {
        int peg = 0;
        int piece = 0;
        int orientation = 0;
        switch (r.nextInt(2)) {
            case 0:
                boolean left = r.nextBoolean();
                peg = 6 * r.nextInt(4) + (left ? 0 : 5);
                piece = r.nextInt(12);
                orientation = 6 * r.nextInt(2) + (left ? 0 : 3);
                break;
            default:
                boolean top = r.nextBoolean();
                peg = r.nextInt(6) + (top ? 0 : 18);
                piece = r.nextInt(12);
                orientation = (top ? 1 : 4) + r.nextInt(2) + (r.nextBoolean() ? 0 : 6);
        }
        if (piece == 0 && orientation >= 6) orientation = (orientation + 3) % 6;
        return "" + (char) ('A' + peg) + (char) ('A' + piece) + (char) ('A' + orientation);
    }


    static String normalize(String placement) {
        String[] pp = new String[12];
        boolean flip = false;
        for (int i = 0; i < placement.length(); i += 3) {

            int idx = placement.charAt(i + 1) - 'A';
            pp[idx] = placement.substring(i, i + 3);
            if (idx == 0) flip = (placement.charAt(i) - 'A') > 11;
        }
        String norm = "";
        for (int i = 0; i < pp.length; i++) {
            if (pp[i] != null) norm += pp[i];
        }
        if (flip) norm = flipPlacement(norm);
        return norm;
    }

    static String flipPlacement(String placement) {
        String flipped = "";
        for (int i = 0; i < placement.length(); i += 3) {
            int origin = placement.charAt(i) - 'A';
            char piece = placement.charAt(i + 1);
            int orientation = placement.charAt(i + 2) - 'A';

            origin = 23 - origin;
            orientation = (orientation < 6) ? (orientation + 3) % 6 : 6 + ((orientation + 3) % 6);

            flipped += "" + (char) (origin + 'A') + piece + (char) (orientation + 'A');
        }
        return flipped;
    }
}
