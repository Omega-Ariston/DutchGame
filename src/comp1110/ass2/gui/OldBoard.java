package comp1110.ass2.gui;

import comp1110.ass2.LinkGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;


import static comp1110.ass2.LinkGame.isPlacementWellFormed;
import static javafx.scene.paint.Color.GREY;

public class OldBoard extends Application {
    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;

    private static final int SQUARE_SIZE = 100;
    private static final double ROW_HEIGHT = SQUARE_SIZE * Math.sqrt(3)*0.5;
    private static final String URI_BASE = "assets/";
    private static final int START_X = 100;
    private static final int START_Y = 70;
    private static final int RADIUS = 30;
    private static String initial;
    //private static String initial = "BAAHBATCJRDKWEBEFD";
    private final Slider difficulty = new Slider(4,9,9);

    /* Loop in public domain CC 0 http://music.163.com/#/m/song?id=22660279&userid=319936486 */
    private static final String LOOP_URI = OldBoard.class.getResource(URI_BASE + "DEPAPEPE.mp3").toString();
    private AudioClip loop;

    /* game variables */
    private boolean loopPlaying = false;

    /* node groups */
    private final Group root = new Group();
    private final Group pieces = new Group();
    private final Group solution = new Group();
    private final Group controls = new Group();
    private final Group initialpieces = new Group();

    /* message on completion */
    private final Text completionText = new Text("Well done!");


    private final Label noSol = new Label("No solution in this situation :(");
    private final Label better = new Label("Come on, you're better than this");

    /* Among other things, the class demonstrates:
     *   - Using inner classes that subclass standard JavaFX classes such as ImageView
     *   - Using JavaFX groups to control properties such as visibility of
     *     a collection of objects
     *   - Using opacity/transparency
     *   - Using mouse events to implement a draggable object
     *   - Making dropped objects snap to legal destinations
     *   - Using a clickable button with an associated event
     *   - Using a slider for user-input
     *   - Using keyboard events to implement toggles controlled by the player
     *   - Using an mp3 audio track (public domain, CC0)
     *   - Using IllegalArgumentExceptions to check for and flag errors*/

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

            setImage(new Image(OldBoard.class.getResource(URI_BASE + piece + ".png").toString()));
            this.piece = piece;
            setFitHeight(210);
            setFitWidth(210);
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
            setFitHeight(210);
            setFitWidth(210);
            if ((pos>=0 && pos<6)||(pos >=12 && pos <18)) {
                setLayoutX(140+70*x);
            }else{setLayoutX(175+70*x);}
            setLayoutY(4.9+(y-1)*ROW_HEIGHT*0.7);

            if (o>=0&&o<6){
                setRotate(60 * o);
            }else if(o>=6&&o<12){
                setRotate(60*(o-6));
                setScaleY(-1);
            }
        }
    }
    class DraggableFXPiece extends FXPiece {
        int position;               // the current game position of the piece 0 .. 23 (-1 is off-board)
        int homeX, homeY;           // the position in the window where the piece should be when not on the board
        double mouseX, mouseY;      // the last known mouse positions (used when dragging)

        /**
         * Construct a draggable piece
         * @param piece The piece identifier ('A' - 'L')
         */
        DraggableFXPiece(char piece) {
            super(piece);
            position = -1; // off screen
            homeX = 50 +(210*((piece -'A')%4));
            setLayoutX(homeX);
            homeY = 250 + ((piece - 'A') / 4)*140;
            setLayoutY(homeY);

            /* event handlers */
            setOnScroll(event -> {            // scroll to change orientation
                hideCompletion();
                rotate();
                event.consume();
            });
            setOnMousePressed(event -> {      // mouse press indicates begin of drag
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            });
            setOnMouseClicked(event -> {// mouse click indicates flip of piece
                if (event.getButton() == MouseButton.SECONDARY){
                    flip();
                    setPosition();
                }
            });

            //reference:http://stackoverflow.com/questions/1515547/right-click-in-javafx
            setOnMouseDragged(event -> {      // mouse is being dragged
                hideCompletion();
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
            });
            setOnMouseReleased(event -> {     // drag is complete
                snapToGrid();
            });
        }


        /**
         * Snap the piece to the nearest grid position (if it is over the grid)
         */

        private void snapToGrid() {
            double r = ROW_HEIGHT *0.7;
            if (getLayoutY()>4.9-1.5*r&&getLayoutY()<=4.9-0.5*r){
                setLayoutY(4.9-r);
                setLayoutX(70 * (((int) getLayoutX() + 35)/70));
                // The first row
            }else if(getLayoutY()>4.9-0.5*r&&getLayoutY()<=4.9+0.5*r){
                setLayoutY(4.9);
                setLayoutX(35+70 * ((int) getLayoutX()/70));
                // Second row
            }else if(getLayoutY()>4.9+0.5*r&&getLayoutY()<=4.9+1.5*r){
                setLayoutY(4.9+r);
                setLayoutX(70 * (((int) getLayoutX() + 35)/70));
                // Third row
            }else if(getLayoutY()>4.9+1.5*r&&getLayoutY()<=4.9+2.5*r){
                setLayoutY(4.9+2*r);
                setLayoutX(35+70 * ((int) getLayoutX()/70));
                // Fourth row
            }else{
                snapToHome();
            }
            setPosition();
            if (position != -1) {
                checkMove();
            } else {
                snapToHome();
            }
        }


        /**
         * Snap the piece to its home position (if it is not on the grid)
         */
        public void snapToHome() {
            setLayoutX(homeX);
            setLayoutY(homeY);
            setRotate(0);
            setScaleY(1);
            position = -1;

        }
        /**
         * A move has been made.  Determine whether there are errors,
         * and if so, snap to home, and determine whether the game is
         * complete, and if so, show the completion message.
         */
        public void checkMove() {
            String placement = "";
            for(Node p : pieces.getChildren()) {
                placement += p.toString();
            }
            placement = placement + initial;
            if (!LinkGame.isPlacementValid(placement)) {
                snapToHome();
            } else {
                if (placement.length()==36) {
                    showCompletion();
                }
            }
        }


        /**
         * Rotate the piece by 60 degrees
         */
        private void rotate() {
            setRotate((getRotate() + 60) % 360);
            setPosition();
        }

        //flip the piece when you click the right mouse.
        private void flip(){
            if (piece !='A'){
                setScaleY(getScaleY()*(-1));
                setPosition();
            }
        }

        // FIXME Task 8: Implement a basic playable Link Game in JavaFX that only allows pieces to be placed in valid places
        /**
         * Determine whether the whole piece is on the board, given x and y
         * coordinates representing the top-left corner of the piece in its
         * current rotation.
         * @param x The column that the origin of the piece is on
         * @param y The row that the origin of the piece is on
         * @return True if the entire piece is on the board
         */
        private boolean isOnBoard(int x, int y) {
            if (piece < 'D') { // 'A'~'C'-shaped pieces are simple, because they're basically rectangle.
                switch ((int) getRotate()) {
                    case 0 :
                    case 180:
                        return x >= 1  && x < 5 && y >= 0  && y < 4;
                    case 60:
                    case 120:
                    case 240:
                    case 300:
                        if (y==1) {
                            return x >= 0 && x < 5;
                        }else {
                            return x >=1 && x<=5 && y >= 1 && y < 3;
                        }
                    default:
                        return false;
                }
            }else if (piece <'I'&& piece >='D') {// For 'D'~'H'-shaped pieces it depends on the orientation..
                if (getScaleY() ==-1){
                    switch ((int) getRotate()) {
                        case 0:
                            if (y==0||y==2){
                                return x >=1 && x <= 5;
                            }else{
                                return x>=0 && x < 5 && y == 1;
                            }
                        case 60:
                            if (y==2){
                                return x>=1 && x<=5;
                            }else{
                                return x >= 0  && x <= 5 && y ==1;}
                        case 120:
                            if (y==2){
                                return x>=1 && x<=5;
                            }else{
                                return x >= 1  && x < 5 && y >= 1  && y < 4;}
                        case 180:
                            if (y==2){
                                return x>=1 && x<5;
                            }else{
                                return x >= 0  && x < 5 && y >= 1  && y < 4;}
                        case 240:
                            if (y==2){
                                return x>=0 && x<=5;
                            }else{
                                return x >= 0  && x < 5 && y >= 1  && y < 3;}
                        case 300: default:
                            if (y==0||y==2){
                                return x >=1 && x < 5;
                            }else{
                                return x>=0 && x < 5 && y == 1;
                            }
                    }
                }else{
                    switch ((int) getRotate()) { // Situation when 'D'-'H' are flipped.
                        case 0:
                            if (y==2){
                                return x>=1 && x<=5;
                            }else{
                                return x >= 1  && x < 5 && y >= 1  && y < 4;}
                        case 60:
                            if (y==2){
                                return x>=1 && x<5;
                            }else{
                                return x >= 0  && x < 5 && y >= 1  && y < 4;}
                        case 120:
                            if (y==2){
                                return x>=0 && x<=5;
                            }else{
                                return x >= 0  && x < 5 && y >= 1  && y < 3;}
                        case 180:
                            if (y==0||y==2){
                                return x >=1 && x < 5;
                            }else{
                                return x>=0 && x < 5 && y == 1;
                            }
                        case 240:
                            if (y==0||y==2){
                                return x >=1 && x <= 5;
                            }else{
                                return x>=0 && x < 5 && y == 1;
                            }
                        case 300: default:
                            if (y==2){
                                return x>=1 && x<=5;
                            }else{
                                return x >= 0  && x <= 5 && y ==1;}
                    }
                }
            }else{//For 'I'~'L'-shaped pieces it depends on the orientation.
                if (getScaleY()==-1){
                    switch ((int) getRotate()) {
                        case 0:
                            return x>=1 && x<= 5 && y >= 0 && y < 3;
                        case 60:
                            return x >= 1 && x <= 5 && y >= 1 && y < 4;
                        case 120:
                            if (y == 2){
                                return x>=1 && x<= 5;
                            }else{
                                return x >= 0 && x <= 4 && y >= 1 && y < 4;}
                        case 180:
                            return x >= 0 && x <= 4 && y >= 1 && y < 4;
                        case 240:
                            return x >= 0 && x <= 4 && y >= 0 && y < 3;
                        case 300:
                        default:
                            if (y==1){
                                return x >= 0 && x <= 4;
                            }else{
                                return x >= 1 && x <= 5 && y >= 0 && y < 3;}
                    }
                }else{ // Situation when 'I'-'L' are flipped.
                    switch ((int) getRotate()) {
                        case 0:
                            return x >= 1 && x <= 5 && y >= 1 && y < 4;
                        case 60:
                            if (y == 2) {
                                return x >= 1 && x <= 5;
                            } else {
                                return x >= 0 && x < 5 && y >= 1 && y < 4;
                            }
                        case 120:
                            return x >= 0 && x < 5 && y >= 1 && y < 4;
                        case 180:
                            return x >= 0 && x < 5 && y >= 0 && y < 3;
                        case 240:
                            if (y == 1) {
                                return x >= 0 && x < 5;
                            } else {
                                return x >= 1 && x <= 5 && y >= 0 && y < 3;
                            }
                        case 300:
                        default:
                            return x >= 1 && x <= 5 && y >= 0 && y < 3;
                    }
                }
            }
        }


        /**
         * Determine the grid-position of the origin of the piece (0 .. 23)
         * or -1 if it is off the grid, taking into account its rotation.
         */
        private void setPosition() {

            int y = (int) ((getLayoutY() - 4.9+ ROW_HEIGHT*0.7) / (ROW_HEIGHT*0.7)); //Board_Y
            int x;
            if (y ==1 || y==3) {
                x = (int) (getLayoutX() -35- 140) / 70;
            }else {
                x = (int) (getLayoutX() - 140) / 70;
            }//Board_X
            if (isOnBoard(x,y)) {
                /*  find 'position' (reference point is the middle of *un*rotated piece */

                position = x + y *6;
            } else
                position = -1;
        }


        /** Represent the piece placement as a string */
        public String toString() {
            if (getScaleY() == -1){
                char orientation = (char) ('A'+(int)(getRotate()/60)+6);
                return position == -1 ? "" : "" +(char)('A'+position) +  piece + orientation;
            }else{
                char orientation = (char) ('A' + (int) (getRotate()/ 60));
                return position == -1 ? "" : "" +(char)('A'+position) +  piece + orientation;
            }

        }
    }
    /**
     * Set up event handlers for the main game
     *
     * @param scene  The Scene used by the game.
     */
    private void setUpHandlers(Scene scene) {
        /* create handlers for key press and release events */
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.M) {
                toggleSoundLoop();
                event.consume();
                // M - music on/off
            } else if (event.getCode() == KeyCode.Q) {
                Platform.exit();
                event.consume();
                // Q - quit
            }
            else if (event.getCode() == KeyCode.SLASH) {
                String placement = "";
                for(Node p : pieces.getChildren()) {
                    placement += p.toString();
                }
                if(placement.length()==0&&initial.length()>18){
                    solution.setOpacity(1);
                }else{
                    placement = placement + initial;
                        String[] sol = LinkGame.getSolutions(placement);
                        if (sol.length > 0) {
                            String slo = sol[0];
                            makeSolution(slo);
                            solution.setOpacity(1);
                        } else {
                            showSolution();
                        }
                }
                event.consume();
                // / - hint is visible while you press the key and hold.
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SLASH) {
                solution.setOpacity(0);
                hideSolution();
                hideBetter();
                event.consume();
                // / - hint is invisible when key is released.
            }
        });
    }


    /**
     * Set up the sound loop (to play when the 'M' key is pressed)
     */

    private void setUpSoundLoop() {
        try {
            loop = new AudioClip(LOOP_URI);
            loop.setCycleCount(AudioClip.INDEFINITE);
        } catch (Exception e) {
            System.err.println(":-( something bad happened ("+LOOP_URI+"): "+e);
        }
    }


    /**
     * Turn the sound loop on or off
     */
    private void toggleSoundLoop() {
        if (loopPlaying)
            loop.stop();
        else
            loop.play();
        loopPlaying = !loopPlaying;
    }
    /*make initial placement randomly or according to difficulty */
    void makePlacement(String placement) {
        initialpieces.getChildren().clear();
        if (!isPlacementWellFormed(placement)) {
            throw new IllegalArgumentException("Solution incorrect length: " + placement);
        }
        for (int i = 0; i < placement.length() / 3; i++) {
            initialpieces.getChildren().add(new OldBoard.FXPiece(placement.substring(i * 3, (i + 1) * 3)));
        }

    }

    // FIXME Task 11: Implement hints
//This function is used to make invisible hint.
    private void makeSolution(String solution) {
        this.solution.getChildren().clear();
        if (solution.length() != 36) {
            throw new IllegalArgumentException("Solution incorrect length: " + solution);
        }
        for (int i = 0; i < solution.length() / 3; i++) {
            this.solution.getChildren().add(new OldBoard.FXPiece(solution.substring(i * 3, (i + 1) * 3)));
        }
        this.solution.setOpacity(0);
    }


    private void makeControls() {

        Button button = new Button("Reset");
        Button game = new Button("New Game");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                resetPieces();
            }
        });
        game.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                newGame();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().add(button);
        hb.setLayoutX(750);
        hb.setLayoutY(70);
        HBox gg = new HBox();
        gg.getChildren().add(game);
        gg.setLayoutX(750);
        gg.setLayoutY(110);
        controls.getChildren().add(hb);
        controls.getChildren().add(gg);

        difficulty.setShowTickLabels(true);
        difficulty.setShowTickMarks(true);
        difficulty.setMajorTickUnit(1);
        difficulty.setMinorTickCount((int) 1);
        difficulty.setSnapToTicks(true);

        difficulty.setLayoutX(750);
        difficulty.setLayoutY(170);
        controls.getChildren().add(difficulty);

        final Label sound = new Label("Press M - Sound on/off");
        sound.setTextFill(Color.BLUE);
        sound.setLayoutX(30);
        sound.setLayoutY(70);
        final Label hint = new Label("Hold  / - Hint");
        hint.setTextFill(Color.BLUE);
        hint.setLayoutX(30);
        hint.setLayoutY(110);

        final Label difficultyCaption = new Label("Number of Initial pieces:");
        difficultyCaption.setTextFill(Color.GREY);
        difficultyCaption.setLayoutX(750);
        difficultyCaption.setLayoutY(150);
        controls.getChildren().addAll(sound,hint, difficultyCaption);
    }

    /* private void makePieces() {
         pieces.getChildren().clear();
         for (char p = 'A'; p <= 'L'; p++) {
             pieces.getChildren().add(new DraggableFXPiece(p));
         }
         now makePieces has been merged with newGame()
     }*/
    private void resetPieces() {
        solution.setOpacity(0);
        pieces.toFront();
        for (Node n : pieces.getChildren()) {
            ((DraggableFXPiece) n).snapToHome();
        }
        hideCompletion();
    }
    // FIXME Task 9: Implement starting placements.
    /*add new initial placement on the board*/
    private void newGame() {
        try {
            hideCompletion();
            Double d = difficulty.getValue();
            int t = d.intValue();
            // source: http://stackoverflow.com/questions/5404149/how-to-convert-double-to-int-directly
            initial = "JAFXJAGKLHLE";
            makePlacement(initial);
            //get solution by method of LinkGame
            if(t>6){String[] sol =LinkGame.getSolutions(initial) ;
                String slo =sol[0];
                makeSolution(slo);
            }else{
                this.solution.getChildren().clear();
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Uh oh. "+ e);
            Thread.dumpStack();
            Platform.exit();
        }

// This function ensures that the initialplacement will not display again by draggableFXPiece.
        pieces.getChildren().clear();
        for (char p = 'A'; p <= 'L'; p++) {
            for (int i = 1 ; i < initial.length(); i += 3){
                if (p==initial.charAt(i)){
                    break;
                }else if (p!= initial.charAt(i)&&i==initial.length()-2){
                    pieces.getChildren().add(new DraggableFXPiece(p));
                }
            }
        }
        pieces.toFront();
    }

    //A sign of Completion "Well Done" will be shown once completed.
    private void makeCompletion() {
        completionText.setFill(Color.BLACK);
        completionText.setFont(Font.font("Arial", 50));
        completionText.setLayoutX(300);
        completionText.setLayoutY(4.9+ROW_HEIGHT*0.7*5);
        completionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(completionText);
    }

    private void makeShowSolution(){
        noSol.setTextFill(Color.RED);
        noSol.setLayoutX(180);
        noSol.setLayoutY(280);
        noSol.setFont(Font.font(40));
        noSol.toFront();
        noSol.setOpacity(0);
        root.getChildren().add(noSol);
    }

    private void showSolution() {
        noSol.toFront();
        noSol.setOpacity(1);
    }

    private void hideSolution() {
        noSol.toBack();
        noSol.setOpacity(0);
    }

    private void makeBetter(){
        better.setTextFill(Color.RED);
        better.setLayoutX(100);
        better.setLayoutY(280);
        better.setFont(Font.font(40));
        better.toFront();
        better.setOpacity(0);
        root.getChildren().add(better);
    }

    private void showBetter() {
        better.toFront();
        better.setOpacity(1);
    }

    private void hideBetter() {
        better.toBack();
        better.setOpacity(0);
    }


    /**
     * Show the completion message
     */
    private void showCompletion() {
        completionText.toFront();
        completionText.setOpacity(1);
    }


    /**
     * Hide the completion message
     */
    private void hideCompletion() {
        completionText.toBack();
        completionText.setOpacity(0);
    }




    // FIXME Task 12: Generate interesting starting placements

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Link Game");
        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 0 || j == 2) {
//                Image circleImage = new Image(Viewer.class.getResource(URI_BASE + "Circle.png").toString());
//                ImageView circle = new ImageView();
//                circle.setImage(circleImage);
                    Circle circle = new Circle(RADIUS*0.7);
                    circle.setFill(GREY.brighter());
                    circle.setLayoutX((350 + i * SQUARE_SIZE)*0.7);
                    circle.setLayoutY((START_Y + j * ROW_HEIGHT)*0.7);
                    root.getChildren().add(circle);
                } else {
//                    Image circleImage = new Image(Viewer.class.getResource(URI_BASE + "Circle.png").toString());
//                    ImageView circle = new ImageView();
//                    circle.setImage(circleImage);
                    Circle circle = new Circle(RADIUS*0.7);
                    circle.setFill(GREY.brighter());
                    circle.setLayoutX((400 + i * SQUARE_SIZE)*0.7);
                    circle.setLayoutY((START_Y + j * ROW_HEIGHT)*0.7);
                    root.getChildren().add(circle);
                }
            }



            //makePieces();
            //newGame();*/
            setUpHandlers(scene);
            setUpSoundLoop();
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        //put groups together
        makeControls();
        makeCompletion();
        makeShowSolution();
        makeBetter();
        root.getChildren().add(pieces);
        root.getChildren().add(controls);
        root.getChildren().add(initialpieces);
        newGame();
        root.getChildren().add(solution);
    }
}
