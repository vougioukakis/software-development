package controller;
import model.*;
import model.Cards.*;
import model.Squares.SafeSquare;
import model.Squares.Square;
import model.Squares.Start;
import view.gameView;

public class GameController {
    private Player[] players;
    private gameView view;
    private Deck deck;
    private Board board;
    public int turnNo = -1;
    public boolean testing = false;

    /**
     * main constructor of the game controller,
     * initializes everything in the game like players, the deck, view, board
     *
     * @post create 2 players and their pawns, the deck, board, view, and starts the game with the yellow
     * player. read the docs of the above objects constructors for more details
     */
    public GameController() {
        this.players = new Player[2];
        this.view = new gameView(this);
        this.deck = new Deck();
        this.board = new Board();

        Player p1 = new Player("YELLOW", board);
        Player p2 = new Player("RED", board);
        players[0] = p1;
        players[1] = p2;

        Slide ys1 = new Slide("YELLOW", 0, board);
        Slide ys2 = new Slide("YELLOW", 1, board);
        Slide rs1 = new Slide("RED", 0, board);
        Slide rs2 = new Slide("RED", 1, board);

        players[0].pawns[0].setSlides(ys1, ys2);
        players[0].pawns[1].setSlides(ys1, ys2);
        players[1].pawns[0].setSlides(rs1, rs2);
        players[1].pawns[1].setSlides(rs1, rs2);

        advance();
    }

    /**
     * constructor used for testing scenarios in junit
     * @param testNo test number
     */
    public GameController(int testNo) {
        this.testing = true;
        this.players = new Player[2];
        this.view = new gameView(this);
        this.deck = new Deck();
        this.board = new Board();

        Player p1 = new Player("YELLOW", board);
        Player p2 = new Player("RED", board);
        players[0] = p1;
        players[1] = p2;

        Slide ys1 = new Slide("YELLOW", 0, board);
        Slide ys2 = new Slide("YELLOW", 1, board);
        Slide rs1 = new Slide("RED", 0, board);
        Slide rs2 = new Slide("RED", 1, board);

        players[0].pawns[0].setSlides(ys1, ys2);
        players[0].pawns[1].setSlides(ys1, ys2);
        players[1].pawns[0].setSlides(rs1, rs2);
        players[1].pawns[1].setSlides(rs1, rs2);
        if (testNo == 1) {
            //move red pawn in front of yellow start
            players[1].pawns[0].setX(160);
            players[1].pawns[0].setY(0);
            players[1].pawns[0].setSquare();

            this.action(0, 0, "class model.Cards.Card_1", "start");

        }
        if (testNo == 2) {
            //move red pawn1  in same spot as red pawn 2
            players[1].pawns[1].setX(160);
            players[1].pawns[1].setY(0);
            players[1].pawns[1].setSquare();
            players[1].pawns[0].setX(160);
            players[1].pawns[0].setY(0);
            players[1].pawns[0].setSquare();

        }
        if (testNo == 3){
            //move red pawn in front of yellow start
            players[1].pawns[0].setX(160);
            players[1].pawns[0].setY(0);
            players[1].pawns[0].setSquare();

            this.action(0, 0, "class model.Cards.Card_2", "start");
        }
        if (testNo == 4){
            //move red pawn in front of yellow start
            players[1].pawns[0].setX(160);
            players[1].pawns[0].setY(0);
            players[1].pawns[0].setSquare();

            this.action(0, 0, "class model.Cards.Sorry", "sorry-1");
        }
        if (testNo == 5){
            players[1].pawns[0].setX(160);
            players[1].pawns[0].setY(40);
            players[1].pawns[0].setSquare();

            this.action(0, 1, "class model.Cards.Sorry", "sorry-1");
        }


    }

    /**
     * @return player array
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Draws a card and returns the action the player chose as well as what card they pulled
     *
     * @param playerIndex index of player that draws the card
     * @post one card is drawn from the deck and shown on screen,
     * possible moves are checked based on card, buttons are shown
     */
    public void draw(int playerIndex) {
        try {
            checkBoard();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            //System.exit(1);
        }

        checkWin();
        Card drawn = deck.pop();
        view.cardDrawn(drawn.getClass().toString(), playerIndex);
        int[] pawn1Moves = getMoves(drawn.getClass().toString(), playerIndex, 0);
        int[] pawn2Moves = getMoves(drawn.getClass().toString(), playerIndex, 1);
        view.addButtons(drawn.getClass().toString(), playerIndex, pawn1Moves, pawn2Moves);
    }

    /**
     * get the possible moves given the card that was drawn, in an array with 1 or 0
     *
     * @param cardClass
     * @param playerIndex
     * @param pawnIndex
     * @return array with 1 or 0 in the position of the corresponding move. 1=can, 0=cannot.
     * for example, the card 1 has [move, start ]. array [1,0] means the pawn can only move (forward)
     * and cant start because it has already left the start. [0,0] means there are no available moves for this pawn
     */
    public int[] getMoves(String cardClass, int playerIndex, int pawnIndex) {
        int[] result = new int[0];
        Pawn pawn = players[playerIndex].pawns[pawnIndex];

        //card cases
        if (cardClass.equals("class model.Cards.Card_1")) {
            result = new int[2]; //[move, start]
            int step = 1;

            //if not at start
            if (!pawn.square.getClass().toString().equals("class model.Squares.Start")) {
                Object nextSquare = pawn.findNextSquare(step);

                //square of the other pawn of same player
                Square other = players[playerIndex].pawns[(pawnIndex + 1) % 2].square;
                if (other != nextSquare) {
                    result[0] = 1;
                }

                if (nextSquare == pawn.home()) {
                    result[0] = 1;
                }

                if (pawn.atHome) {
                    result[0] = 0;
                }
            } else {
                if (pawn.entry != players[playerIndex].pawns[(pawnIndex + 1) % 2].square) {
                    result[1] = 1;
                }
            }

            return result;
        } else if (cardClass.equals("class model.Cards.Card_2")) {
            result = new int[2]; //[move, start]
            int step = 2;

            //if not at start
            if (!pawn.square.getClass().toString().equals("class model.Squares.Start")) {
                Object nextSquare = pawn.findNextSquare(step);

                //square of the other pawn of same player
                Square other = players[playerIndex].pawns[(pawnIndex + 1) % 2].square;
                if (other != nextSquare) {
                    result[0] = 1;
                }

                if (nextSquare == pawn.home()) {
                    result[0] = 1;
                }

                if (pawn.atHome) {
                    result[0] = 0;
                }
            } else {
                if (pawn.entry != players[playerIndex].pawns[(pawnIndex + 1) % 2].square) {
                    result[1] = 1;
                }
            }

            return result;
        }
        else if (cardClass.equals("class model.Cards.Card_3") || cardClass.equals("class model.Cards.Card_5")) {
            result = new int[1]; //[move]
            int step = 0;
            switch (cardClass) {
                case "class model.Cards.Card_3":
                    step = 3;
                    break;
                case "class model.Cards.Card_5":
                    step = 5;
                    break;
            }


            //if not at start
            if (!pawn.square.getClass().toString().equals("class model.Squares.Start")) {
                Object nextSquare = pawn.findNextSquare(step);

                //square of the other pawn of same player
                /*
                Square other = players[playerIndex].pawns[(pawnIndex+1)%2].square;
                if (other != nextSquare) {
                    result[0] = 1;
                }*/

                result[0] = 1;

                if (pawn.atHome) {
                    result[0] = 0;
                }
            } else {
                result[0] = 0; //cant move a pawn if its at start
            }

            return result;
        } else if (cardClass.equals("class model.Cards.Card_8") || cardClass.equals("class model.Cards.Card_12")) {
            result = new int[1]; //[move]
            int step = 0;
            switch (cardClass) {
                case "class model.Cards.Card_8":
                    step = 8;
                    break;
                case "class model.Cards.Card_12":
                    step = 12;
                    break;
            }


            //if not at start
            if (!pawn.square.getClass().toString().equals("class model.Squares.Start")) {
                Object nextSquare = pawn.findNextSquare(step);

                //square of the other pawn of same player

                Square other = players[playerIndex].pawns[(pawnIndex + 1) % 2].square;
                if (other != nextSquare) {
                    result[0] = 1;
                }

                if (nextSquare == pawn.home()) {
                    result[0] = 1;
                }

                if (pawn.atHome) {
                    result[0] = 0;
                }
            } else {
                result[0] = 0; //cant move a pawn if its at start
            }

            return result;

        }
        else if (cardClass.equals("class model.Cards.Sorry"))
        {//sorry
            result = new int[2]; //returns [ can jump to enemy 1, can jump to enemy 2 ]
            if (!(pawn.square instanceof Start)){
                result[0] = 0;
                result[1] = 0;
            }
            else {
                Player enemy = players[(playerIndex + 1) % 2];
                Pawn enemyPawn1 = enemy.pawns[0];

                //check if i can jump to enemy pawn 1
                if (enemyPawn1.square instanceof SafeSquare || enemyPawn1.square instanceof Start || enemyPawn1.atHome) {
                    result[0] = 0;
                } else {
                    result[0] = 1;
                }

                //check if i can jump to enemy pawn 2
                Pawn enemyPawn2 = enemy.pawns[1];
                if (enemyPawn2.square instanceof SafeSquare || enemyPawn2.square instanceof Start || enemyPawn2.atHome) {
                    result[1] = 0;
                } else {
                    result[1] = 1;
                }
            }
        }
        //should never reach here
        return result;
    }

    /**
     * @param slide  , the slide object we are on
     * @param except , the pawn that entered the slide, will be ignored for the clearing
     * @post clears the slide of all pawns and sends them back to start, except for except pawn
     */
    public void clear(Slide slide, Pawn except) {
        //clear all pawns on slide except for the one that landed
        for (int pl = 0; pl < 2; pl++) {
            for (int pn = 0; pn < 2; pn++) {
                for (int sq = 0; sq < slide.getSquares().length; sq++) {
                    if (players[pl].pawns[pn] != except && players[pl].pawns[pn].square == slide.getSquares()[sq]) {
                        players[pl].pawns[pn].backToStart();
                        view.update(pl, pn, players[pl].pawns[pn].getX(), players[pl].pawns[pn].getY());
                    }
                }

            }
        }
    }

    /**
     * slide the pawn that jumped at a slide start by using the SOrry card
     * @param playerIndex player who used sorry
     * @param pawnIndex pawn that used sorry
     * @param enemyIndex enemy pawn at start of slide
     * @post player pawn is at the end of the slide and every other pawn in slide back to start
     */
    public void slideSorry(int playerIndex, int pawnIndex, int enemyIndex){
        Pawn currPawn = players[playerIndex].pawns[pawnIndex];
        Pawn enemyPawn = players[(playerIndex+1)%2].pawns[enemyIndex];
        Square nextSquare = enemyPawn.square;
        Slide smallSlide = currPawn.getSlides()[0];
        Slide largeSlide = currPawn.getSlides()[1];

        //slide the pawn and clear the slides removing the other pawns
        if (nextSquare == largeSlide.getStart()) {
            currPawn.setX(largeSlide.getEndX());
            currPawn.setY(largeSlide.getEndY());
            view.update(playerIndex, pawnIndex, currPawn.getX(), currPawn.getY());
            clear(largeSlide, currPawn);
        } else if (nextSquare == smallSlide.getStart()) {
            currPawn.setX(smallSlide.getEndX());
            currPawn.setY(smallSlide.getEndY());
            view.update(playerIndex, pawnIndex, currPawn.getX(), currPawn.getY());
            clear(smallSlide, currPawn);
        }

    }

    /**
     * @param playerIndex player that owns the sliding pawn
     * @param pawnIndex   index of the sliding pawn
     * @pre pawn pawnIndex must have landed on a slide of different color
     * @post pawn slides to the end of the slide and pushes every other pawn back to the start
     */
    public void slide(int playerIndex, int pawnIndex, String cardClass) {
        Pawn currPawn = players[playerIndex].pawns[pawnIndex];
        //find step number
        int step = 0;
        switch (cardClass) {
            case "class model.Cards.Card_1":
                step = 1;
                break;
            case "class model.Cards.Card_2":
                step = 2;
                break;
            case "class model.Cards.Card_3":
                step = 3;
                break;
            case "class model.Cards.Card_5":
                step = 5;
                break;
            case "class model.Cards.Card_8":
                step = 8;
                break;
            case "class model.Cards.Card_12":
                step = 12;
                break;
        }

        Square nextSquare = currPawn.findNextSquare(step);
        Slide smallSlide = currPawn.getSlides()[0];
        Slide largeSlide = currPawn.getSlides()[1];

        //slide the pawn and clear the slides removing the other pawns
        if (nextSquare == largeSlide.getStart()) {
            currPawn.setX(largeSlide.getEndX());
            currPawn.setY(largeSlide.getEndY());
            view.update(playerIndex, pawnIndex, currPawn.getX(), currPawn.getY());
            clear(largeSlide, currPawn);
        } else if (nextSquare == smallSlide.getStart()) {
            currPawn.setX(smallSlide.getEndX());
            currPawn.setY(smallSlide.getEndY());
            view.update(playerIndex, pawnIndex, currPawn.getX(), currPawn.getY());
            clear(smallSlide, currPawn);
        }


    }

    /**
     * @param playerIndex index of player
     * @param pawnIndex   index of the pawn for the action
     * @param cardClass   is the type of card leading to the action
     * @param action      is the action the player chose to do with the card. possible values: "move", "start", "sorry-1", "sorry-2"
     * @throws IllegalArgumentException if it takes an invalid action argument
     * @post does the action the player chose (move, start, ...) and updates the view
     */
    public void action(int playerIndex, int pawnIndex, String cardClass, String action) {
        Pawn currPawn = players[playerIndex].pawns[pawnIndex];
        //must add action "move all" for cards 3 and 5
        if (action.equals("move")) {
            int step = 0;
            if (cardClass.equals("class model.Cards.Card_1")) step = 1;
            if (cardClass.equals("class model.Cards.Card_2")) step = 2;
            if (cardClass.equals("class model.Cards.Card_3")) step = 3;
            if (cardClass.equals("class model.Cards.Card_5")) step = 5;
            if (cardClass.equals("class model.Cards.Card_8")) step = 8;
            if (cardClass.equals("class model.Cards.Card_12")) step = 12;

            Square nextSquare = currPawn.findNextSquare(step);
            Slide smallSlide = currPawn.getSlides()[0];
            Slide largeSlide = currPawn.getSlides()[1];

            if (nextSquare == largeSlide.getStart() || nextSquare == smallSlide.getStart()) { //IF LANDING ON SLIDE
                slide(playerIndex, pawnIndex, cardClass);
            } else { // IF NOT LANDING ON SLIDE
                int[] newPos = currPawn.findNextCoords(step);
                currPawn.setX(newPos[0]);
                currPawn.setY(newPos[1]);
                currPawn.setSquare();
                view.update(playerIndex, pawnIndex, newPos[0], newPos[1]);

                //check if enemy pawn is on square
                Pawn enemy1 = players[(playerIndex + 1) % 2].pawns[0];
                Pawn enemy2 = players[(playerIndex + 1) % 2].pawns[1];
                if (enemy1.square == currPawn.square) {
                    enemy1.backToStart();
                    view.update((playerIndex + 1) % 2, 0, enemy1.getX(), enemy1.getY());
                }
                if (enemy2.square == currPawn.square) {
                    enemy2.backToStart();
                    view.update((playerIndex + 1) % 2, 1, enemy2.getX(), enemy2.getY());
                }
            }
        } else if (action.equals("start")) {
            currPawn.start();
            view.update(playerIndex, pawnIndex, currPawn.getX(), currPawn.getY());

            //check if enemy pawn is on square
            Pawn enemy1 = players[(playerIndex + 1) % 2].pawns[0];
            Pawn enemy2 = players[(playerIndex + 1) % 2].pawns[1];
            if (enemy1.square == currPawn.square) {
                enemy1.backToStart();
                enemy1.setSquare();
                view.update((playerIndex + 1) % 2, 0, enemy1.getX(), enemy1.getY());
            }
            if (enemy2.square == currPawn.square) {
                enemy2.backToStart();
                enemy1.setSquare();
                view.update((playerIndex + 1) % 2, 1, enemy2.getX(), enemy2.getY());
            }
        } else if (action.equals("sorry-1")) {
            Pawn enemy = players[(playerIndex + 1) % 2].pawns[0];
            Square nextSquare = enemy.square;

            //if at slide start slide
            Slide smallSlide = currPawn.getSlides()[0];
            Slide largeSlide = currPawn.getSlides()[1];
            if (nextSquare == largeSlide.getStart() || nextSquare == smallSlide.getStart()) { //IF LANDING ON SLIDE
                slideSorry(playerIndex, pawnIndex, 0);
            }
            else {//not at slide
                currPawn.setX(enemy.getX());
                currPawn.setY(enemy.getY());
                currPawn.setSquare();
                enemy.backToStart();
                view.update(playerIndex, pawnIndex, currPawn.getX(), currPawn.getY());
                view.update((playerIndex+1)%2, 0, enemy.getX(), enemy.getY());
            }

        }
        else if (action.equals("sorry-2")) {
            Pawn enemy = players[(playerIndex + 1) % 2].pawns[1];
            Square nextSquare = enemy.square;

            //if at slide start slide
            Slide smallSlide = currPawn.getSlides()[0];
            Slide largeSlide = currPawn.getSlides()[1];
            if (nextSquare == largeSlide.getStart() || nextSquare == smallSlide.getStart()) { //IF LANDING ON SLIDE
                slideSorry(playerIndex, pawnIndex, 1);
            } else {//not at slide
                currPawn.setX(enemy.getX());
                currPawn.setY(enemy.getY());
                currPawn.setSquare();
                enemy.backToStart();
                enemy.setSquare();
                view.update(playerIndex, pawnIndex, currPawn.getX(), currPawn.getY());
                view.update((playerIndex + 1) % 2, 1, enemy.getX(), enemy.getY());
            }
        }
        else {
                throw new IllegalArgumentException("invalid action");
        }

            checkWin();
    }

    /**
     * @throws IllegalStateException if 2 pawns are on the same square that are not home, start
     */
    public void checkBoard() throws IllegalStateException {
        Square p1p1 = players[0].pawns[0].square;
        Square p1p2 = players[0].pawns[1].square;
        Square p2p2 = players[1].pawns[1].square;
        Square p2p1 = players[1].pawns[0].square;

        if (p1p1 == p1p2 && !players[0].pawns[0].atHome && !(players[0].pawns[0].square instanceof Start)) {
            throw new IllegalStateException("yellow pawns on same square");
        }
        if (p2p1 == p2p2 && !players[0].pawns[0].atHome && !(players[1].pawns[0].square instanceof Start)) {
            throw new IllegalStateException("red pawns on same square");
        }

        if (p2p1 == p1p1 || p2p1 == p1p2 || p2p2 == p1p1 || p2p2 == p1p2) {
            throw new IllegalStateException("pawns on same square");
        }

    }

    /**
     * checks the win conditions: both pawns of one player have to be at home
     *
     * @post if conditions are met, finish game function runs. otherwise game goes on
     */
    public void checkWin() {
        if (players[0].pawns[0].atHome && players[0].pawns[1].atHome) {
            finishGame(0);
        } else if (players[1].pawns[0].atHome && players[1].pawns[1].atHome) {
            finishGame(1);
        }
    }

    /**
     * starts the next turn/ round of the game.
     * @post shows the button to draw a card
     */
    public void advance() {
        turnNo++;
        checkWin();
        view.showPlayerName(turnNo);
        view.drawButton();
    }

    /**
     * for now, this method ends the program
     */
    private void finishGame(int playerIndex) {
        if (playerIndex == 0) {
            System.out.println("game finished, the winner is Yellow");
        } else if (playerIndex == 1) {
            System.out.println("game finished, the winner is Red");
        }

        System.exit(0);
    }
}
