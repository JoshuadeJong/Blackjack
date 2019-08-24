import java.util.ArrayList;
import java.util.Random;

public class Deck {

    // Constants
    private static final int RANK_MAX_INDEX = 13;
    private static final int SUIT_MAX_INDEX = 4;
    private static final int VALUE_MAX = 21;

    // Variables
    private ArrayList<Card> deck;
    private Random rand;

    // Constructor
    public Deck(){
        this(1);
    }

    public Deck(int numberDecks){
        this(numberDecks, new Random());
    }

    public Deck(int numberDecks, Random rand){
        make(numberDecks);
        this.rand = rand;
    }

    // Make Deck
    public void make(int numberDecks){
        this.deck = new ArrayList<>();

        for(int i = 0; i < numberDecks; i++){
            for(int rankIndex = 1; rankIndex <= RANK_MAX_INDEX; rankIndex++){
                for(int suitIndex = 1; suitIndex <= SUIT_MAX_INDEX; suitIndex++){
                    this.deck.add(new Card(rankIndex, suitIndex));
                }
            }
        }
    }

    // Shuffle
    public void shuffleAll(){
        shuffle(0, this.deck.size());
    }

    public void shuffle(int start, int end){
        for(int i = start; i < end; i++){
            int j = this.rand.nextInt(end) + start;

            // swap
            Card temp = this.deck.get(i);
            this.deck.set(i, this.deck.get(j));
            this.deck.set(j, temp);
        }
    }

    // Deal Card
    public Card deal(){
        return deal(0);
    }

    public Card dealRandom(){
        return deal(this.rand.nextInt()%this.deck.size());
    }

    private Card deal(int index){
        if(this.deck.size() == 0){
            make(1);
            shuffleAll();
        }

        Card temp = deck.get(index);
        this.deck.remove(index);
        return temp;
    }

    // Place Card
    public void add(Card card){
        add(this.deck.size(), card);
    }

    public void addRandom(Card card){
        add(this.rand.nextInt()%this.deck.size(), card);
    }

    private void add(int index, Card card){
        this.deck.add(index, card);
    }

    // Gets
    public Card get(int index){
        return this.deck.get(index);
    }

    public ArrayList<Card> getAll(){
        return this.deck;
    }

    public int size(){
        return this.deck.size();
    }

    // Methods
    public void clear(){
        this.deck.clear();
    }

    // String
    public ArrayList<String> deckArt(ArrayList<Boolean> side){
        ArrayList<String> art = new ArrayList<>();

        for(int i = 0; i < 8; i++){
            art.add("");
        }

        for(int cardIndex = 0; cardIndex < this.deck.size(); cardIndex++){
            ArrayList<String> temp;

            if(side.get(cardIndex)){ // True = Front
                temp = this.deck.get(cardIndex).cardFront();
            } else {
                temp = this.deck.get(cardIndex).cardBack();
            }

            for(int lineIndex = 0; lineIndex < temp.size(); lineIndex++){
                art.set(lineIndex, art.get(lineIndex) + " " + temp.get(lineIndex));
            }
        }

        return art;
    }

}
