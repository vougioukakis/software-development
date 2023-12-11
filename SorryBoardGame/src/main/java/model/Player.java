package model;

import model.Cards.Card;

public class Player {
    public Pawn[] pawns = new Pawn[2];
    String color = "";
    private Board board;

    public Player(String color, Board board) {
        this.color = color;
        Pawn yp1 = new Pawn(color, board);
        Pawn yp2 = new Pawn(color, board);
        pawns[0] = yp1;
        pawns[1] = yp2;
    }


}
