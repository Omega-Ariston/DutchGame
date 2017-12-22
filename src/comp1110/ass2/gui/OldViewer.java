package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import static comp1110.ass2.LinkGame.isPlacementWellFormed;

import static javafx.scene.paint.Color.GREY;

/**
 * A very simple viewer for piece placements in the link game.
 *
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class OldViewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 100;
    private static final int PIECE_IMAGE_SIZE = 3*SQUARE_SIZE;
    private static final double ROW_HEIGHT = SQUARE_SIZE * Math.sqrt(3)*0.5; // 60 degrees
    private static final int VIEWER_WIDTH = 750;
    private static final int VIEWER_HEIGHT = 500;
    private static final int START_X = 100;
    private static final int START_X2 = START_X + 50;
    private static final int START_Y = 70;
    private static final int RADIUS = 30;

    private static final String URI_BASE = "assets/";

    private final Group pieces = new Group();
    private final Group root = new Group();
    private final Group controls = new Group();
    TextField textField;

    class FXPiece extends ImageView {
        char piece;

        /**
         * Construct a particular playing piece
         * @param piece The letter representing the piece to be created.
         */
        FXPiece(char piece) {
            if (!(piece >= 'A' && piece <= 'L')) {
                throw new IllegalArgumentException("Bad piece: \"" + piece + "\"");
            }

            setImage(new Image(Board.class.getResource(URI_BASE + piece + ".png").toString()));
            this.piece = piece;
            setFitHeight(PIECE_IMAGE_SIZE);
            setFitWidth(PIECE_IMAGE_SIZE);
        }

        /**
         * Construct a particular playing piece at a particular place on the
         * board at a given orientation.
         * @param position A three-character string describing
         *                 - the place the piece is to be located ('A' - 'X'),
         *                 - the piece ('A' - 'L'), and
         *                 - the orientation ('A' - 'L')
         */
        FXPiece(String position) {
            this(position.charAt(1));
            if (position.length() != 3 ||
                    position.charAt(0) < 'A' || position.charAt(0) > 'X' ||
                    position.charAt(2) < 'A' || position.charAt(2) > 'L') {
                throw new IllegalArgumentException("Bad position string: " + position);
            }
            int pos = position.charAt(0) - 'A';
            int o = (position.charAt(2) - 'A');
            int x = (pos % 6);
            int y = (pos / 6);
            setFitHeight(PIECE_IMAGE_SIZE);
            setFitWidth(PIECE_IMAGE_SIZE);
            if ((pos>=0 && pos<6)||(pos >=12 && pos <18)) {
                setLayoutX(START_X+x*SQUARE_SIZE-150);
            }else{setLayoutX(START_X2+x*SQUARE_SIZE-150);}
            setLayoutY(START_Y+y*ROW_HEIGHT-150);

            if (o>=0&&o<6){
                setRotate(60 * o);
            }else if(o>=6&&o<12){
                setRotate(60*(o-6));
                setScaleY(-1);
            }
        }
    }

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement  A valid placement string
     */
    void makePlacement(String placement) {
        pieces.getChildren().clear();
        if (!isPlacementWellFormed(placement)) {
            throw new IllegalArgumentException("Solution incorrect length: " + placement);
        }
        for (int i = 0; i < placement.length() / 3; i++) {
            pieces.getChildren().add(new FXPiece(placement.substring(i * 3, (i + 1) * 3)));
        }

        // FIXME Task 5: implement the simple placement viewer
    }


    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField ();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction((e)->{
            makePlacement(textField.getText());
            textField.clear();
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("LinkGame Viewer");

        for(int i=0; i<6; i++){
            for(int j=0; j<4; j++){
                int x = (j==0||j==2)?START_X:START_X2;
//                Image circleImage = new Image(Viewer.class.getResource(URI_BASE + "Circle.png").toString());
//                ImageView circle = new ImageView();
//                circle.setImage(circleImage);
                Circle circle = new Circle(RADIUS);
                circle.setFill(GREY.brighter());
                circle.setLayoutX(x+i*SQUARE_SIZE);
                circle.setLayoutY(START_Y+j*ROW_HEIGHT);
                root.getChildren().add(circle);
            }
        }
        root.getChildren().add(pieces);




        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
