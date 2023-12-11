package model;

import model.Squares.Home;
import model.Squares.SlideEnd;
import model.Squares.Square;

import static java.lang.Math.abs;

public class Pawn {
    public String color;
    private int x = 0;
    private int y = 0;//current position coordinates on the GUI
    public int row = 0;
    public int column = 0; //current position on the board 2-array
    public Square square; //which square its on
    private final Board board;
    private Slide[] slides = new Slide[2]; //SLIDES YOU CAN SLIDE ON, NOT SAME COLOR
    public SlideEnd entry; //the square in front of the starting position
    protected Home home;

    public boolean atHome = false;
    public boolean atStart = true;

    Pawn(String color, Board board) {
        this.color = color;
        this.board = board;
        if (color.equals("YELLOW")){
            this.x = 160;
            this.y = 40;
            setSquare();
            this.entry = (SlideEnd) board.squares[0][4];
            this.home = (Home) board.squares[6][2];
        }else{
            this.x = 440;
            this.y = 560;
            setSquare();
            this.entry = (SlideEnd) board.squares[15][11];
            this.home = (Home) board.squares[9][13];
        }


        //this.square = findSquare();
    }

    /** @return the square that is the Home (finish) of this pawn */
    public Home home(){
        return home;
    }

    /** @return the array of slides that can be used by this pawn*/
    public Slide[] getSlides(){
        return slides;
    }

    /** set the slides that can be used by this pawn*/
    public void setSlides(Slide slideSmall, Slide slideLarge){
        this.slides[0] = slideSmall;
        this.slides[1] = slideLarge;
    }

    /** set the current square in the board
     * never call between setX and setY, only after both of them
     * */
    public void setSquare() {
        //careful, x gives the column
        this.row = abs(y/40);
        this.column = abs(x/40);
        this.square = board.squares[this.row][this.column];

        if (square == home || square instanceof Home) {
            atHome = true;
        }

        if (color.equals("YELLOW") && !(this.x == 160 && this.y == 40)){
            atStart = false;
        }else if (color.equals("RED") && !(this.x == 440 && this.y == 560)){
            atStart = false;
        }
    }

    /** @post returns the pawn back to start
     */
    public void backToStart() {
        if (color.equals("YELLOW")){
            setX(160);
            setY(40);
            setSquare();
        }else {
            setX(440);
            setY(560);
            setSquare();
        }
        //instant jump to coordinates, will use this when being pushed off
    }

    /** moves to the square in front of the start square*/
    public void start() {
        if (color.equals("YELLOW")){
            setX(160);
            setY(0);
            setSquare();
        }else {
            setX(440);
            setY(600);
            setSquare();
        }
    }

    public int getX(){
        return this.x;
    }

    /** @param x new x coordinate on GUI
     * @post sets the new GUI coordinate and updates the current square in the backend
     * IMPORTANT always setX,setY and then call setSquare() AFTER calling setX and setY
     * */
    public void setX(int x) {
        this.x = x;
    }

    public int getY(){
        return this.y;
    }

    /** @param y new y coordinate on GUI
     * @post sets the new GUI coordinate and updates the current square in the backend
     * IMPORTANT always setX,setY and then call setSquare() AFTER calling setX and setY
     * */
    public void setY(int y) {
        this.y = y;
    }

    /** @param step is the number of steps to perform
     * @return the square that the pawn will land on if it goes step steps forward
     * */
    public Square findNextSquare(int step) {

        int[] nextCoords = findNextCoords(step);
        int nextX = nextCoords[0];
        int nextY = nextCoords[1];

        int nextRow = abs(nextY/40);
        int nextCol = abs(nextX/40);
        return board.squares[nextRow][nextCol];
    }

    /** @return array of the new [x,y] coordinates of the pawn on the GUI*/
    public int[] findNextCoords(int step) {
        int nextX = getX();
        int nextY = getY();
        boolean reachedSZ = false; //reached safety zone entry?
        if ( color.equals("YELLOW") && nextX == 80 && nextY < 240 ){
            reachedSZ = true;
        }if ( color.equals("RED") && nextX == 520 && nextY > 360){
            reachedSZ = true;
        }


        //top row
        while (nextY == 0 && nextX < 600 && step > 0 && !reachedSZ) {
            nextX = nextX + 40;
            step--;

            if ( color.equals("YELLOW") && nextX == 80){
                reachedSZ = true;
            }
        }
        //right column
        while (nextX == 600 && nextY < 600 && step > 0 && !reachedSZ) {
            nextY = nextY + 40;
            step--;
        }
        //bottom row
        while (nextX > 0 && nextY == 600 && step > 0 && !reachedSZ) {
            nextX = nextX - 40;
            step--;

            if ( color.equals("RED") && nextX == 520){
                reachedSZ = true;
            }
        }

        //left column
        while (nextX == 0 && nextY > 0 && step > 0 && !reachedSZ) {
            nextY = nextY - 40;
            step--;
        }

        //top row again to fix the issue where pawns stopped at (0,0) top left
        while (nextY == 0 && nextX < 600 && step > 0 && !reachedSZ) {
            nextX = nextX + 40;
            step--;

            if ( color.equals("YELLOW") && nextX == 80){
                reachedSZ = true;
            }
        }

        // if safety zone entry was reached then start moving in
        if (reachedSZ){
            if (color.equals("YELLOW")){
                while (step > 0){
                    nextY = nextY + 40;
                    step--;
                    if (nextY == 240){
                        break;
                    }
                }

            }if (color.equals("RED")){
                while (step > 0){
                    nextY = nextY - 40;
                    step--;
                    if (nextY == 360){
                        break;
                    }
                }
            }

        }

        int[] res = new int[2];
        res[0] = nextX;
        res[1] = nextY;
        return res;
    }
}
