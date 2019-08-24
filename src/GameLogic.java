import java.util.ArrayList;
import java.util.Random;

public class GameLogic {

    // Constants
    private static final int BANK_START = 200;
    private static final int BET_MAX = 1000;
    private static final int BET_MIN = 5;
    private static final int DEALER_MIN = 16;
    private static final int VALUE_MAX = 21;

    // General variables
    private Random rand;

    // Player
    private ArrayList<Deck> playerHands;
    private ArrayList<Boolean> handsInPlay;
    private ArrayList<Integer> playerBet;
    private int insuranceBet;
    private int playerBank;
    private int handIndex;

    // Dealer
    private Deck dealerHand;
    private Deck deck;

    // Constructor (setup)
    public GameLogic(Random rand){
        this.rand = rand;

        this.playerHands = new ArrayList<>();
        this.playerHands.add(new Deck(0,rand));
        this.handsInPlay = new ArrayList<>();
        this.handsInPlay.add(true);
        this.playerBank = BANK_START;
        this.insuranceBet = 0;
        this.playerBet = new ArrayList<>();

        this.dealerHand = new Deck(0,rand);
        this.deck = new Deck(1, rand);
        this.deck.shuffleAll();
    }

    ///// Sets
    public Boolean setBet(int betAmount){
        if(betAmount >= BET_MIN && betAmount <= BET_MAX){ // Does bet fall into range
            if(this.playerBank - betAmount >= 0){
                this.playerBank -= betAmount;
                this.playerBet.add(0, betAmount);
                return true;
            }
        }

        return false;
    }

    public void setHandIndex(int handIndex){
        this.handIndex = handIndex;
    }

    public void increaseHandIndex(){
        this.handIndex++;
    }

    // Gets
    public int getHandIndex(){
        return this.handIndex;
    }

    public Deck getPlayerHands(int index){
        return this.playerHands.get(index);
    }

    public Boolean getHandInPlay(){
        return this.handsInPlay.get(this.handIndex);
    }

    public int getPlayerHandsSize(){
        return this.playerHands.size();
    }

    public int getPlayerBank(){
        return this.playerBank;
    }

    public int getPlayerBet(int index){
        return this.playerBet.get(index);
    }

    public int getInsuranceBet(){
        return this.insuranceBet;
    }

    public int getPlayerBetSize(){
        return this.playerBet.size();
    }

    public Deck getDealerHand(){
        return this.dealerHand;
    }

    ///// General Methods
    private int handValue(Deck hand){
            ArrayList<Integer> points = new ArrayList<>();
            points.add(0);
            points.add(0);

            for(int cardIndex = 0; cardIndex < hand.size(); cardIndex++){
                if(hand.get(cardIndex).getRanki() == 1){
                    points.set(0, points.get(0) + 1);
                    if(points.get(1) + 11 > VALUE_MAX){
                        points.set(1, points.get(1) + 1);
                    } else {
                        points.set(1, points.get(1) + 11);
                    }
                } else if(hand.get(cardIndex).getRanki() > 9){
                    points.set(0, points.get(0) + 10);
                    points.set(1, points.get(1) + 10);
                } else {
                    points.set(0, points.get(0) + hand.get(cardIndex).getRanki());
                    points.set(1, points.get(1) + hand.get(cardIndex).getRanki());
                }
            }

            // Which value is the best?
            if(points.get(1) > VALUE_MAX){
                return points.get(0);
            }

            return points.get(1);
    }

    public void clearHands(){
        // Player
        this.playerHands.clear();
        this.playerHands.add(new Deck(0,this.rand));
        this.playerBet.clear();
        this.playerBet.add(0);
        this.insuranceBet = 0;
        this.handsInPlay.clear();
        this.handsInPlay.add(true);

        // Dealer
        this.dealerHand.clear();
    }

    public void resetGame(){
        // Hands
        clearHands();
        // Deck
        this.deck = new Deck(2, this.rand);
        this.deck.shuffleAll();
        // Bank
        this.playerBank = BANK_START;
    }

    ///// Turns
    public void setup(){
        dealerHit();
        playerHit();
        dealerHit();
        playerHit();
    }

    public Boolean playerTurn(int choice){
        Boolean output;

        switch(choice){
            case 0: // Stand
                output = playerStand();
                break;
            case 1: // Hit
                output = playerHit();
                break;
            case 2: // Double
                output = playerDouble();
                break;
            case 3: // Split
                output = playerSplit();
                break;
            case 4: // Insurance
                output = playerInsurance();
                break;
            default:
                output = false;
        }

        return output;
    }

    private Boolean playerStand(){
        this.handsInPlay.set(this.handIndex, false);
        return true;
    }

    private Boolean playerHit(){
        if(getHandInPlay()){
            this.playerHands.get(this.handIndex).add(this.deck.deal());
            playerContinue();
            return true;
        }
        return false;
    }

    private Boolean playerDouble(){
        if(this.playerBank - 2*this.playerBet.get(this.handIndex) >= 0){
            this.playerBank -= this.playerBet.get(this.handIndex);
            this.playerBet.set(this.handIndex, 2*this.playerBet.get(this.handIndex));
            playerHit();
            this.handsInPlay.set(this.handIndex, false);

            return true;
        }

        return false;
    }

    private Boolean playerSplit(){
        if(this.playerHands.get(this.handIndex).size() == 2){ // Is it the first turn?
            // Are the cards equal value?
            if(this.playerHands.get(this.handIndex).get(0).getRanki() == this.playerHands.get(this.handIndex).get(1).getRanki()
                || (this.playerHands.get(this.handIndex).get(0).getRanki() > 9 && this.playerHands.get(this.handIndex).get(1).getRanki() > 9)){

                if(this.playerBet.get(this.handIndex) <= this.playerBank){   // Can the player Double his bet

                    // New Bet
                    this.playerBank -= this.playerBet.get(this.handIndex);
                    this.playerBet.add(this.playerBet.get(this.handIndex));

                    // New Hand
                    Deck newDeck = new Deck(0, this.rand);
                    newDeck.add(this.playerHands.get(this.handIndex).deal());
                    newDeck.add(this.deck.deal());

                    this.playerHands.add(newDeck);
                    this.handsInPlay.add(true);

                    // Old Hand
                    this.playerHands.get(this.handIndex).add(this.deck.deal());

                    return true;
                }
            }
        }

        return false;
    }

    private Boolean playerInsurance(){
        if(this.insuranceBet == 0) {    // Player hans't made an insurance bet yet
            if (this.handIndex == 0) { // It is the first turn of the round
                if (this.dealerHand.get(1).getRanki() == 1) { // The second card of the dealer is an ACE
                    if (this.playerBank - 2 * this.getPlayerBet(0) > 0) { // The player has enough in his bank
                        this.playerBank -= 2 * this.getPlayerBet(0);
                        this.insuranceBet = 2 * this.getPlayerBet(0);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private Boolean playerContinue(){
        if(handValue(this.playerHands.get(this.handIndex)) <= VALUE_MAX){
            return true;
        }

        this.handsInPlay.set(this.handIndex, false);
        return false;
    }

    ///// Dealer
    public Boolean dealerHit(){
        if(handValue(dealerHand) < DEALER_MIN){
            dealerHand.add(deck.deal());
            return true;
        }

        return false;
    }

    // Bank
    public ArrayList<Integer> payOut(){
        ArrayList<Integer> state = new ArrayList<>();

        for(int handIndex = 0; handIndex < this.playerHands.size(); handIndex++){

            // Who won?
            if(handValue(this.playerHands.get(handIndex)) > VALUE_MAX){ // Player > 21
                state.add(-1); // Loss
            } else if(handValue(this.playerHands.get(handIndex)) > handValue(this.dealerHand)){ // Player > Dealer
                state.add(1); // Win
            } else if(handValue(this.playerHands.get(handIndex)) == handValue(this.dealerHand)){ // Player == Dealer
                state.add(0); // Tie
            } else if (handValue(this.dealerHand) > VALUE_MAX){ // Dealer > 21
                state.add(1); // Win
            } else {    // player < dealer
                state.add(-1); // Loss
            }

            // Bank
            switch(state.get(handIndex)){
                case -1:
                    // Do nothing (Bet is cleared in a few lines)
                    break;
                case 0:
                    this.playerBank += this.playerBet.get(handIndex);
                    break;
                case 1:
                    this.playerBank += 2*this.playerBet.get(handIndex);
                    break;
                default:
            }
        }

        if(this.insuranceBet != 0){ // Insurance Bet was made
            if(this.dealerHand.get(0).getRanki() == 1 && this.dealerHand.get(1).getRanki() > 9){ // Bet Won
                this.playerBank += 2*this.insuranceBet;
                state.add(2);
            } else {
                state.add(-2);
            }
        }

        return state;
    }

    // Print
    public void printHand(Deck hand, ArrayList<Boolean> side){
        ArrayList<String> art = hand.deckArt(side);

        for(int i = 0; i < art.size(); i++){
            System.out.println(art.get(i));
        }
    }
}
