package model;

import model.Squares.Square;

import static java.lang.Math.abs;

public class Slide {
    private int endX;
    private int endY;
    private Square[] squares;
    private Square end;
    private Square start;
    private Board board;

    /** @param color the color of the pawn using the slide. yellow pawns use red and vice versa
     * @param slideID 0 or 1, 0 is the small, 1 is the large*/
    public Slide(String color, int slideID, Board board) {
        this.board = board;
        Square[] squares = new Square[4+slideID];

        if (color.equals("RED")){ //these slides  appear yellow on the board
            int startCol = 1 + slideID*8; //1 if id=0, 9 if id=1, column of the first square
            for (int i = 0; i < 4+slideID; i++){
                squares[i] = board.squares[0][startCol];
                startCol++;
            }

            this.squares = squares;
            this.end = squares[squares.length-1];
            this.start = squares[0];
            this.endX = (4+9*slideID)*40;
            this.endY = 0;
        }

        if (color.equals("YELLOW")){ //these slides appear red on the board
            int startCol = 14 - slideID*8;
            for (int i = 0; i < 4+slideID; i++){
                squares[i] = board.squares[15][startCol];
                startCol--;
            }

            this.squares = squares;
            this.end = squares[squares.length-1];
            this.start = squares[0];
            this.endX = (2+9*abs(slideID-1))*40;
            this.endY = 15*40;
        }
    }

    /** @return the x coordinate of the end of the slide*/
    public int getEndX(){
        return endX;
    }
    /** @return the y coordinate of the end of the slide*/
    public int getEndY(){
        return endY;
    }

    /** @return the array that contains all the squares of the slide from start to end in order*/
    public Square[] getSquares(){
        return squares;
    }

    /**
     * @return the square that is the start of the slide
     */
    public Square getStart(){
        return start;
    }
}
