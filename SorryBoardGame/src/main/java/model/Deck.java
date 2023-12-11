package model;

import model.Cards.*;

import java.util.ArrayList;
import java.util.Random;
public class Deck {
    ArrayList<Card> cards = new ArrayList<Card>();
    ArrayList<Card> used = new ArrayList<Card>();


    public Deck() {
        for(int i = 0; i < 4; i++){
            Card_1 card = new Card_1();
            this.add(card);
        }
        for(int i = 0; i < 4; i++){
            Card_2 card = new Card_2();
            this.add(card);
        }
        for(int i = 0; i < 4; i++){
            Card_3 card = new Card_3();
            this.add(card);
        }
        for(int i = 0; i < 4; i++){
            Card_5 card = new Card_5();
            this.add(card);
        }
        for(int i = 0; i < 4; i++){
            Card_8 card = new Card_8();
            this.add(card);
        }
        for(int i = 0; i < 4; i++){
            Card_12 card = new Card_12();
            this.add(card);
        }
        for(int i = 0; i < 4; i++){
            Sorry card = new Sorry();
            this.add(card);
        }
    }

    private void add(Card card){
        cards.add(card);
    }

    /** @return the card at the top of the deck*/
    public Card pop(){
        Random random = new Random();

        //if deck is empty add back the used
        if (cards.size() == 0){
            int num = used.size();
            for (int i = 0; i < num; i++){
                cards.add(used.remove(0));
            }
        }

        //draw random card
        Card drawn = cards.remove(random.nextInt(cards.size()));
        used.add(drawn);
        return drawn;
    }




}
