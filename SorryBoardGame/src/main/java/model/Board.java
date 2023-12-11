package model;

import model.Squares.*;

public class Board {
    public Square[][] squares = new Square[16][16];

    public Board(){
        //fill with null squares and then replace some of them with the appropriate ones from the game
        for (int row = 0; row < 16; row++){
            for (int col = 0; col < 16; col++){
                NullSquare square = new NullSquare();
                squares[row][col] = square;
            }
        }
        for (int col = 0; col < 16; col++){
            RegularSquare square = new RegularSquare();
            squares[0][col] = square;
        }
        for (int col = 0; col < 16; col++){
            RegularSquare square = new RegularSquare();
            squares[15][col] = square;
        }
        for (int row = 0; row < 16; row++){
            RegularSquare square = new RegularSquare();
            squares[row][0] = square;
        }
        for (int row = 0; row < 16; row++){
            RegularSquare square = new RegularSquare();
            squares[row][15] = square;
        }

        for (int row = 1; row < 6; row++){
            squares[row][2] = new SafeSquare();
        }
        for (int row = 10; row <15; row++){
            squares[row][13] = new SafeSquare();
        }

        squares[0][4] = new SlideEnd();
        squares[15][11] = new SlideEnd();
        squares[1][4] = new Start();
        squares[14][11] = new Start();
        squares[9][13] = new Home();
        squares[6][2] = new Home();

    }

}
