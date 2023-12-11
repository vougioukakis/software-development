package view;

import controller.GameController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


//mainframe contains gamePanel, cardPanel, choicePanel
public class gameView {
    JFrame mainFrame;
    boardPanel board;
    sidePanel rightSide;
    cardPanel card;
    buttonPanel buttons;
    GameController controller;
    ImageLoader imageLoader = new ImageLoader();
    String pawnChoice = "none";

    /** constructor that initializes the GUI
     * @param controller is the game controller
     */
    public gameView(GameController controller) {
        this.controller = controller;
        mainFrame = new JFrame("SORRY Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1160, 680); //29x17 game units, game unit = 40, for possible future update
        mainFrame.setBackground(Color.DARK_GRAY);
        mainFrame.setResizable(false);
        mainFrame.setContentPane(new JLabel(new ImageIcon("background.png")));
        mainFrame.setLayout(new FlowLayout());

        board = new boardPanel();
        mainFrame.add(board);

        rightSide = new sidePanel();
        mainFrame.add(rightSide);

        card = new cardPanel(controller);
        rightSide.add(card);

        buttons = new buttonPanel();
        rightSide.add(buttons);

        mainFrame.setVisible(true);
    }

    /** changes the color of the indicator depending on which player is currently playing */
    public void showPlayerName(int turnNo){
        if (turnNo%2 == 0){
            card.playerLabel.setForeground(Color.YELLOW);
        }
        else {
            card.playerLabel.setForeground(Color.RED);
        }

    }

    /** shows the draw button on screen */
    public void drawButton(){

        JButton drawButton = new JButton("draw");
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.draw(controller.turnNo%2);
                //controller.draw(0);
                buttons.remove(drawButton);
                buttons.revalidate();
                buttons.repaint();
            }
        });
        buttons.add(drawButton);
        buttons.revalidate();
        buttons.repaint();
    }

    /** updates the image of the card to the one that was last drawn
     * @param cardClass the class of card that was drawn
     * @param playerIndex the index of the player that drew
     * */
    public void cardDrawn(String cardClass, int playerIndex) {
        card.update(cardClass);
    }

    /** updates the board with new pawn positions
     * @param playerIndex the index of the player that drew
     * @param pawnIndex the index of the pawn gettng updated
     * @param posX the new x position of the pawn
     * @param posY the new y position of the pawn
     * @pre posX and posY are integers in the range [0,600]
     * @post the new position of the pawn can be seen on the board
     * @throws IllegalArgumentException if posX or posX not in [0,600] interval
     * */
    public void update(int playerIndex, int pawnIndex, int posX, int posY){
        if (posX < 0 || posX > 600 || posY < 0 || posY > 600){
            throw new IllegalArgumentException("invalid pawn position");
        }

        if (playerIndex == 0){
            if (pawnIndex == 0){
                board.yellow_pawn1_X = posX;
                board.yellow_pawn1_Y = posY;
            }
            else {
                board.yellow_pawn2_X = posX;
                board.yellow_pawn2_Y = posY;
            }
        } else if (playerIndex == 1) {
            if (pawnIndex == 0){
                board.red_pawn1_X = posX;
                board.red_pawn1_Y = posY;
            }
            else {
                board.red_pawn2_X = posX;
                board.red_pawn2_Y = posY;
            }

        }

        board.repaint();
    }

    /** checks moves of a pawn and shows the buttons of the available moves for card1
     * @param pawnIndex the index of the pawn to check available moves for
     * @pre moves is an array with 2 elements, that are 0 or 1
     * @post shows the available moves in the button panel as clickable buttons
     * */
    public int card1_buttons(int playerIndex, int[] moves, int pawnIndex){
        if (moves[0] == 0 && moves[1] == 0){
            return 1;
            /*
            JButton fold = new JButton("fold");
            fold.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttons.removeAll();
                    buttons.revalidate();
                    buttons.repaint();
                    controller.advance();
                }
            });*/
        }
        if (moves[0] == 1){//can move
            String pawnNo = String.valueOf(pawnIndex+1);
            JButton move = new JButton("Move pawn "+pawnNo);

            move.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pawnChoice = Integer.toString(pawnIndex);
                    controller.action(playerIndex, pawnIndex, "class model.Cards.Card_1","move");
                    //buttons.remove(move);
                    buttons.removeAll();
                    buttons.revalidate();
                    buttons.repaint();
                    controller.advance();

                }
            });

            buttons.add(move);
            buttons.revalidate();
            buttons.repaint();
        }
        if (moves[1] == 1){//can start
            String pawnNo = String.valueOf(pawnIndex+1);
            JButton start = new JButton("Start pawn "+pawnNo);
            buttons.add(start);

            start.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.action(playerIndex, pawnIndex,"class model.Cards.Card_1","start");
                    //buttons.remove(move);
                    buttons.removeAll();
                    buttons.revalidate();
                    buttons.repaint();
                    controller.advance();
                }
            });
        }

        return 0;
    }

    /** checks moves of a pawn and shows the buttons of the available moves for card2
     * @param pawnIndex the index of the pawn to check available moves for
     * @pre moves is an array with 2 elements, that are 0 or 1
     * @post shows the available moves in the button panel as clickable buttons
     * */
    public int card2_buttons(int playerIndex, int[] moves, int pawnIndex){
        if (moves[0] == 0 && moves[1] == 0){
            return 1;
            /*
            JButton fold = new JButton("fold");
            fold.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttons.removeAll();
                    buttons.revalidate();
                    buttons.repaint();

                    //draw again
                    controller.turnNo++;
                    controller.draw(playerIndex);
                    //controller.advance();
                }
            });*/
        }
        if (moves[0] == 1){//can move
            String pawnNo = String.valueOf(pawnIndex+1);
            JButton move = new JButton("Move pawn "+pawnNo);

            move.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pawnChoice = Integer.toString(pawnIndex);
                    controller.action(playerIndex, pawnIndex, "class model.Cards.Card_2","move");
                    //buttons.remove(move);
                    buttons.removeAll();
                    buttons.revalidate();
                    buttons.repaint();


                    //draw again
                    controller.draw(playerIndex);
                    //controller.advance();

                }
            });

            buttons.add(move);
            buttons.revalidate();
            buttons.repaint();
        }
        if (moves[1] == 1){//can start
            String pawnNo = String.valueOf(pawnIndex+1);
            JButton start = new JButton("Start pawn "+pawnNo);
            buttons.add(start);

            start.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.action(playerIndex, pawnIndex,"class model.Cards.Card_2","start");
                    //buttons.remove(move);
                    buttons.removeAll();
                    buttons.revalidate();
                    buttons.repaint();

                    //draw again
                    controller.draw(playerIndex);
                    //controller.advance();
                }
            });
        }

        return 0;
    }

    /** checks moves of a pawn and shows the buttons of the available moves for card3
     * @pre moves is an array with 1 element, that is 0 or 1
     * @post shows the available moves in the button panel as clickable buttons
     * */
    public int card3_buttons(int playerIndex, int[] moves1, int[] moves2){
        if (moves1[0] == 0 && moves2[0] == 0){ //cant do anything with any pawn
            return 1;
        }
        else if (moves1[0] == 1 && moves2[0] == 1){ //can move both pawns
            JButton move = new JButton("Move both pawns");
            buttons.add(move);
            buttons.revalidate();
            buttons.repaint();

            move.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.action(playerIndex, 0, "class model.Cards.Card_3","move");
                    controller.action(playerIndex, 1, "class model.Cards.Card_3","move");
                    //buttons.remove(move);
                    buttons.removeAll();
                    buttons.revalidate();
                    buttons.repaint();
                    controller.advance();

                }
            });

        }
        else if (moves1[0] == 1 && moves2[0] == 0){//can move only pawn 1
            JButton move = new JButton("Move pawn 1");

            move.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.action(playerIndex, 0, "class model.Cards.Card_3","move");
                    //buttons.remove(move);
                    buttons.removeAll();
                    buttons.revalidate();
                    buttons.repaint();
                    controller.advance();

                }
            });

            buttons.add(move);
            buttons.revalidate();
            buttons.repaint();
        }
        else if (moves1[0] == 0&& moves2[0] == 1){//can move only pawn 2
            JButton move = new JButton("Move pawn 2");

            move.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.action(playerIndex, 1, "class model.Cards.Card_3","move");
                    //buttons.remove(move);
                    buttons.removeAll();
                    buttons.revalidate();
                    buttons.repaint();
                    controller.advance();

                }
            });

            buttons.add(move);
            buttons.revalidate();
            buttons.repaint();
        }

        return 0;
    }

    /** checks moves of a pawn and shows the buttons of the available moves for card5
     * @pre moves is an array with 1 element, that is 0 or 1
     * @post shows the available moves in the button panel as clickable buttons
     * */
    public int card5_buttons(int playerIndex, int[] moves1, int[] moves2){
        if (moves1[0] == 0 && moves2[0] == 0){ //cant do anything with any pawn
            return 1;
        }
        else if (moves1[0] == 1 && moves2[0] == 1){ //can move both pawns
            JButton move = new JButton("Move both pawns");
            buttons.add(move);
            buttons.revalidate();
            buttons.repaint();

            move.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.action(playerIndex, 0, "class model.Cards.Card_5","move");
                    controller.action(playerIndex, 1, "class model.Cards.Card_5","move");
                    //buttons.remove(move);
                    buttons.removeAll();
                    buttons.revalidate();
                    buttons.repaint();
                    controller.advance();

                }
            });

        }
        else if (moves1[0] == 1 && moves2[0] == 0){//can move only pawn 1
            JButton move = new JButton("Move pawn 1");

            move.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.action(playerIndex, 0, "class model.Cards.Card_5","move");
                    //buttons.remove(move);
                    buttons.removeAll();
                    buttons.revalidate();
                    buttons.repaint();
                    controller.advance();

                }
            });

            buttons.add(move);
            buttons.revalidate();
            buttons.repaint();
        }
        else if (moves1[0] == 0&& moves2[0] == 1){//can move only pawn 2
            JButton move = new JButton("Move pawn 2");

            move.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.action(playerIndex, 1, "class model.Cards.Card_5","move");
                    //buttons.remove(move);
                    buttons.removeAll();
                    buttons.revalidate();
                    buttons.repaint();
                    controller.advance();

                }
            });

            buttons.add(move);
            buttons.revalidate();
            buttons.repaint();
        }

        return 0;
    }

    public int card8_buttons(int playerIndex, int[] moves, int pawnIndex){
        if (moves[0]==1){ //if can move
            String pawnNo = String.valueOf(pawnIndex+1);
            JButton move = new JButton("Move pawn "+pawnNo);

            move.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttons.removeAll();
                    controller.action(playerIndex, pawnIndex, "class model.Cards.Card_8","move");
                    buttons.revalidate();
                    buttons.repaint();
                    controller.advance();

                }
            });

            buttons.add(move);
            buttons.revalidate();
            buttons.repaint();
        }

        return 0;
    }

    public int card12_buttons(int playerIndex, int[] moves, int pawnIndex){
        if (moves[0]==1){ //if can move
            String pawnNo = String.valueOf(pawnIndex+1);
            JButton move = new JButton("Move pawn "+pawnNo);

            move.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttons.removeAll();
                    controller.action(playerIndex, pawnIndex, "class model.Cards.Card_12","move");
                    buttons.revalidate();
                    buttons.repaint();
                    controller.advance();

                }
            });

            buttons.add(move);
            buttons.revalidate();
            buttons.repaint();
        }

        return 0;
    }

    /** adds the buttons of the available moves of the sorry card */
    public int sorry_buttons(int playerIndex,int[] moves, int pawnIndex){
        if (moves[0] == 0 && moves[1] == 0){
            return 1;
        }

        if (moves[0] ==1 ){
            String pawnNo = String.valueOf(pawnIndex+1);
            JButton button = new JButton("jump to enemy pawn 1 with pawn " + pawnNo);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttons.removeAll();
                    controller.action(playerIndex, pawnIndex, "class model.Cards.Sorry","sorry-1");
                    buttons.revalidate();
                    buttons.repaint();
                    controller.advance();
                }
            });

            buttons.add(button);
            buttons.revalidate();
            buttons.repaint();
        }

        if (moves[1] == 1 ){
            String pawnNo = String.valueOf(pawnIndex+1);
            JButton button = new JButton("jump to enemy pawn 2 with pawn " + pawnNo);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttons.removeAll();
                    controller.action(playerIndex, pawnIndex, "class model.Cards.Sorry","sorry-2");
                    buttons.revalidate();
                    buttons.repaint();
                    controller.advance();
                }
            });

            buttons.add(button);
            buttons.revalidate();
            buttons.repaint();
        }

        return 0;
    }

    /** adds the buttons of the available moves of both pawns of the player*/
    public void addButtons(String cardClass, int playerIndex, int[] pawn1Moves, int[] pawn2Moves){
        //add move buttons

        if (cardClass.equals("class model.Cards.Card_1") || cardClass.equals("class model.Cards.Card_2")){
            int p1 = 0;
            int p2 = 0;//pawn1 and pawn2 variables to check if they can do something
            if(cardClass.equals("class model.Cards.Card_1")){
                p1 = card1_buttons(playerIndex, pawn1Moves, 0);
                p2 = card1_buttons(playerIndex, pawn2Moves, 1);
            }
            if(cardClass.equals("class model.Cards.Card_2")){
                p1 = card2_buttons(playerIndex, pawn1Moves, 0);
                p2 = card2_buttons(playerIndex, pawn2Moves, 1);
            }
            //if no moves were available draw fold button
            if (p1 == 1 && p2 == 1) {
                JButton fold = new JButton("fold");
                buttons.add(fold);
                buttons.revalidate();
                buttons.repaint();
                fold.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        buttons.removeAll();
                        buttons.revalidate();
                        buttons.repaint();
                        controller.advance();
                    }
                });
            }
        }

        if(cardClass.equals("class model.Cards.Card_3") || cardClass.equals("class model.Cards.Card_5")){
            int p = 1; //fold or not
            if(cardClass.equals("class model.Cards.Card_3")){
                p = card3_buttons(playerIndex, pawn1Moves, pawn2Moves);
            }
            if(cardClass.equals("class model.Cards.Card_5")){
                p = card5_buttons(playerIndex, pawn1Moves, pawn2Moves);
            }


            if (p == 1){
                JButton fold = new JButton("fold");
                buttons.add(fold);
                buttons.revalidate();
                buttons.repaint();
                fold.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        buttons.removeAll();
                        buttons.revalidate();
                        buttons.repaint();
                        controller.advance();
                    }
                });
            }

        }

        if(cardClass.equals("class model.Cards.Card_8") || cardClass.equals("class model.Cards.Card_12")){
            //never fold can always draw
            //check move for both pawns
            if(cardClass.equals("class model.Cards.Card_8")){
                card8_buttons(playerIndex, pawn1Moves, 0);
                card8_buttons(playerIndex, pawn2Moves, 1);
            }
            else if(cardClass.equals("class model.Cards.Card_12")){
                card12_buttons(playerIndex, pawn1Moves, 0);
                card12_buttons(playerIndex, pawn2Moves, 1);
            }

            card8_12_draw_btn();
        }

        if(cardClass.equals("class model.Cards.Sorry")){
            int s1 = sorry_buttons(playerIndex, pawn1Moves, 0); //if 1, cant do anything with pawn 1
            int s2 = sorry_buttons(playerIndex, pawn2Moves, 1);
            if (s1 == 1 && s2 == 1){ //if both pawns cant make a move, fold
                JButton fold = new JButton("fold");
                buttons.add(fold);
                buttons.revalidate();
                buttons.repaint();
                fold.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        buttons.removeAll();
                        buttons.revalidate();
                        buttons.repaint();
                        controller.advance();
                    }
                });
            }
        }
    }

    /** show draw option for cards 8 and 12*/
    private void card8_12_draw_btn(){
        JButton drawButton = new JButton("draw");
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttons.removeAll();
                controller.draw(controller.turnNo%2);
                //controller.draw(0);
                buttons.revalidate();
                buttons.repaint();
            }
        });
        buttons.add(drawButton);
        buttons.revalidate();
        buttons.repaint();
    }


}

/**the game board in its current state, just images*/
class boardPanel extends JPanel {
    ImageLoader imageLoader = new ImageLoader();
    BufferedImage boardImage = imageLoader.load("/board.jpg");
    BufferedImage pawnImageY1 = imageLoader.load("/pawns/yellowPawn1.png");
    BufferedImage pawnImageY2 = imageLoader.load("/pawns/yellowPawn2.png");
    BufferedImage pawnImageR1 = imageLoader.load("/pawns/redPawn1.png");
    BufferedImage pawnImageR2 = imageLoader.load("/pawns/redPawn2.png");
    //yellow
    public int yellow_pawn1_X = 160;
    public int yellow_pawn1_Y = 40;
    public int yellow_pawn2_X = 160;
    public int yellow_pawn2_Y = 40;
    //red
    public int red_pawn1_X = 440;
    public int red_pawn1_Y = 560;
    public int red_pawn2_X = 440;
    public int red_pawn2_Y = 560;

    boardPanel(){
        this.setPreferredSize(new Dimension(640,640));
        //this.setBackground(Color.BLUE);
    }

    //call every time to update the board
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(boardImage, 0, 0, 640, 640, null);
        g.drawImage(pawnImageY1, yellow_pawn1_X,yellow_pawn1_Y, 40, 40, null);
        g.drawImage(pawnImageY2, yellow_pawn2_X,yellow_pawn2_Y, 40, 40, null);
        g.drawImage(pawnImageR1, red_pawn1_X,red_pawn1_Y, 40, 40, null);
        g.drawImage(pawnImageR2, red_pawn2_X,red_pawn2_Y, 40, 40, null);
    }


}

/** panel on the right of the board, containing the buttons and the cards*/
class sidePanel extends JPanel {
    ImageLoader imageLoader = new ImageLoader();
    BufferedImage bg = imageLoader.load("/background.png");
    sidePanel(){
        //this.setBackground(Color.CYAN);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(500,640));
        this.setVisible(true);
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, 500, 640, null);

    }

}

/** panel that displays the card you drew*/
class cardPanel extends JPanel {
    ImageLoader imageLoader = new ImageLoader();
    BufferedImage cardImage = imageLoader.load("/cards/backCard.png");
    BufferedImage bg = imageLoader.load("/background.png");
    JLabel playerLabel = new JLabel("---------------------------------");
    GameController controller;
    cardPanel(){
        //this.setBackground(Color.ORANGE);
        this.setPreferredSize(new Dimension(100,400));
        this.setVisible(true);
    }
    cardPanel(GameController controller){
        //this.setBackground(Color.ORANGE);
        this.controller = controller;
        playerLabel.setForeground(Color.YELLOW); //first turn
        this.setPreferredSize(new Dimension(100,400));
        this.setVisible(true);
    }

    /** call to update card panel*/
    public void update(String cardClass){
        this.removeAll();
        this.add(playerLabel);
        String cardName = toName(cardClass);
        cardImage = imageLoader.load("/cards/"+cardName+".png");
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, 500, 640, null);
        g.drawImage(cardImage, 150, 40, 200, 300, null);

    }

    /** convert card class name to a simple string */
    public String toName(String cardClass){
        switch (cardClass) {
            case "class model.Cards.Card_1":
                return "card1";
            case "class model.Cards.Card_2":
                return "card2";
            case "class model.Cards.Card_3":
                return "card3";
            case "class model.Cards.Card_5":
                return "card5";
            case "class model.Cards.Card_8":
                return "card8";
            case "class model.Cards.Card_12":
                return "card12";
            case "class model.Cards.Sorry":
                return "cardSorry";
            default:
                return "backCard";

        }

    }

}

/** panel that shows the buttons for the available moves after you draw a card*/
class buttonPanel extends JPanel {
    ImageLoader imageLoader = new ImageLoader();
    BufferedImage bg = imageLoader.load("/background.png");
    buttonPanel(){
        this.setPreferredSize(new Dimension(300,200));
        this.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(bg, 0, -300, 500, 660, null);
    }
}

/** helper class that handles the loading of images*/
class ImageLoader {
    public ImageLoader(){}
    public BufferedImage load(String s){
        BufferedImage result = null;
        try{
            result = ImageIO.read(Objects.requireNonNull(getClass().getResource(s)));
        } catch(IOException e){
            System.err.println("imagenot found");
        }
        return result;

    }
}