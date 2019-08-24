import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;

public class UserInterface {

    private Scanner reader;
    private Random rand;
    private GameLogic logic;

    // Constructor
    public UserInterface(Scanner reader, Random rand){
        this.reader = reader;
        this.rand = rand;
    }

    // Main
    public void start(){
        title();

        int gameState;

        // Setup
        this.logic = new GameLogic(rand);

        do{
            do{
                // Ask for player's bet
                logic.setHandIndex(0);
                playerBet();

                // Give cards to player and dealer
                logic.setup();

                // Display dealer and player cards
                dealerHandOpen();
                playerHand(logic.getHandIndex());

                // Ask for players action
                do{
                    if(logic.getHandIndex() != 0){  // If this isn't the first hand print it.
                        playerHand(logic.getHandIndex());
                    }

                    do {
                        playerTurn();
                        if(logic.getHandInPlay() || logic.getHandIndex() < logic.getPlayerHandsSize()){
                            playerHand(logic.getHandIndex());
                        }
                    }while(logic.getHandInPlay());
                    logic.increaseHandIndex();  // Increment to next hand
                }while(logic.getHandIndex() < logic.getPlayerHandsSize());

                // Dealer Draws
                while(logic.dealerHit()){}

                // Give winnings
                for(int i = 0;  i< 8; i++){
                    System.out.println();
                }
                dealerHandClose();
                for(int i = 0; i < logic.getPlayerHandsSize(); i++){
                    playerHand(i);
                }

                victory(logic.payOut());

                if(0 == logic.getPlayerBank()){
                    quit();
                }

                // Do they want to play again?
                gameState = replay();
                logic.clearHands();
            }while(0 == gameState);
            logic.resetGame();
        }while(2 != gameState);

        quit();
    }

    // Game States
    private int playerBet(){
        // Question
        String money = "$ " + logic.getPlayerBank();
        money = String.format("%31s", money);

        String options = "\n" +
                " ============= Place Bet =============\n" +
                " Bank: " + money + "\n" +
                " Bet: ";

        System.out.println(options);

        // Messages
        ArrayList<String> choices = numberChoices(100);

        String message = "\n" +
                "Place a bet between $5 or $1000.\n" +
                "Type help to see commands.\n" +
                "Type quit to leave.\n";

        // function
        String functionMessage = "\n" +
                "You cannot make that bet.";

        return  interpret(choices, options, message, logic::setBet, functionMessage, 1);
    }

    private void playerTurn(){
        // Question
        String options = "\n" +
                "You can: Stand, Hit, Double, Split, or Insurance.";

        System.out.println(options);

        // Choices
        ArrayList<String> choices = new ArrayList<>();
        choices.add("stand");
        choices.add("hit");
        choices.add("double");
        choices.add("split");
        choices.add("insurance");

        // Messages
        String message = "\n" +
                "Type stand to end your turn." +
                "Type hit to draw another card.\n" +
                "Type double to draw a card, double your bet, and end your turn.\n" +
                "Type split if you have two equal value cards to make two hands with them.\n" +
                "Type insurance to make a side bet with the dealer.\n" +
                "Type help to see commands.\n";

        // Function
        String functionMessage = "\n" +
                "You can not make that move.";

        // Interpret
        interpret(choices, options, message, logic::playerTurn, functionMessage, 0);
    }

    private int replay() {
        // Question
        String options = "\n" +
                "Do you want to play again?\n" +
                "   Yes - Continue playing.\n" +
                "   Reset - Reset the bank and deck.\n" +
                "   Quit - Leave the game.";

        System.out.println(options);

        // Variables
        ArrayList<String> choices = new ArrayList<>();
        choices.add("yes");
        choices.add("reset");
        choices.add("quit");

        String message = "\n" +
                "Type " + choices.get(0) + " to replay\n" +
                "Type " + choices.get(1) + " to play again with a refiled bank.\n" +
                "Type " + choices.get(2) + " to quit the game.\n" +
                "Type help to see commands.";

        // Interpret Choice
        return interpret(choices, options, message);
    }

    // Interpreter
    private int interpret(ArrayList<String> choices, String options, String message){
        return interpret(choices, options, message, (Integer)->{return true;}, "");
    }

    private int interpret(ArrayList<String> choices, String options, String message, Function<Integer, Boolean> lambda, String functionMessage){
        return interpret(choices, options, message, lambda, functionMessage, 0);
    }

    private int interpret(ArrayList<String> choices, String options, String message, Function<Integer, Boolean> lambda, String functionMessage, int lambdaModifier) {
        int count = 0;
        int tries = 3;

        while (count < 10) {
            String choice = reader.nextLine().toLowerCase();

            // Default Options
            switch(choice){
                case "help":
                    help();
                    count--;
                    break;
                case "rules":
                    rules();
                    count--;
                    break;
                case "options":
                    System.out.println(options);
                    count--;
                    break;
                case "contact":
                    contact();
                    count--;
                    break;
                case "source":
                    source();
                    count--;
                    break;
                case "quit":
                    quit();
                    count--;
                    break;
            }

            // Options
            if (choices.contains(choice)) {
                int choiceIndex = choices.indexOf((choice)) + lambdaModifier;

                if(lambda.apply(choiceIndex)){
                    return choiceIndex;
                } else {
                    System.out.println(functionMessage);
                }
            }

            // Number of entry tries
            if (tries - 1 == count % tries) {
                System.out.println(message);
            }
            count++;
        }


        quit();
        return -1;
    }

    // Methods
    private ArrayList<String> numberChoices(int amount){

        ArrayList<String> choices = new ArrayList<>();

        for(int i = 1; i <= amount; i++){
            choices.add("" + i);
        }

        return choices;
    }

    // Messages
    private void title() {
        System.out.println(
                "\n" +
                "   , _                                \n" +
                " /|/_)|\\  _,   _  |)  o  _,   _  |)  \n" +
                "  |  \\|/ / |  /   |/) | / |  /   |/) \n" +
                "  |(_/|_/\\/|_/\\__/| \\/|/\\/|_/\\__/| \\/\n" +
                "                     (|   ");
    }

    public void dealerHandOpen(){
        ArrayList<Boolean> side = new ArrayList<>();
        side.add(false);
        side.add(true);

        System.out.println(" =============== Dealer ==============");
        logic.printHand(logic.getDealerHand(), side);
        System.out.println();
    }

    public void dealerHandClose(){
        ArrayList<Boolean> side = new ArrayList<>();

        for(int i = 0; i < logic.getDealerHand().size(); i++){
            side.add(true);
        }

        System.out.println(" =============== Dealer ==============");
        logic.printHand(logic.getDealerHand(), side);
        System.out.println();
    }

    public void playerHand(int handIndex){
        ArrayList<Boolean> side = new ArrayList<>();

        for(int i = 0; i < logic.getPlayerHands(handIndex).size(); i++){
            side.add(true);
        }
        System.out.println(" =========== PLAYER HAND " + (handIndex+1) + " =========== ");
        logic.printHand(logic.getPlayerHands(handIndex), side);
        System.out.println();
    }

    public void victory(ArrayList<Integer> state){

        System.out.println(" ============== Earnings =============");

        int prizeTotal = 0;

        for(int betIndex = 0; betIndex < state.size(); betIndex++){
            String prizeStr = new String();

            switch(state.get(betIndex)){
                case -1: // Lose
                    prizeStr = "\u001B[31m" + "-$ " + logic.getPlayerBet(betIndex) + "\u001B[0m";
                    prizeTotal -= logic.getPlayerBet(betIndex);
                    break;
                case 0: // Tie
                    prizeStr = "\u001B[33m" + "$ " + 0 + "\u001B[0m";
                    break;
                case 1: // Win
                    prizeStr = "\u001B[32m" + "$ " + logic.getPlayerBet(betIndex) + "\u001B[0m";
                    prizeTotal += logic.getPlayerBet(betIndex);
                    break;
                case 2: // Insurance Won
                    prizeStr = "\u001B[32m" + "$ " + logic.getInsuranceBet() + "\u001B[0m";
                    prizeTotal += logic.getInsuranceBet();
                case -2: // Insurance Lost
                    prizeStr = "\u001B[31m" + "-$ " + logic.getInsuranceBet() + "\u001B[0m";
                    prizeTotal -= logic.getInsuranceBet();
            }

            if(state.get(betIndex) == 2 || state.get(betIndex) == -2){
                System.out.print(" Insurance: ");
                prizeStr = String.format("%35s", prizeStr);
            } else {
                System.out.print(" Hand " + (betIndex+1) + ": ");
                prizeStr = String.format("%38s", prizeStr);
            }

            System.out.println(prizeStr);

        }

        System.out.print(" Bank: ");

        if(prizeTotal > 0) {
            System.out.println(String.format("%40s", "\u001B[32m" + "$ " + logic.getPlayerBank() + "\u001B[0m"));
        } else if(prizeTotal == 0){
            System.out.println(String.format("%40s", "\u001B[33m" + "$ " + logic.getPlayerBank() + "\u001B[0m"));
        } else {
            System.out.println(String.format("%40s", "\u001B[31m" + "$ " + logic.getPlayerBank() + "\u001B[0m"));
        }
    }

    private void help() {
        System.out.println("\n" +
                "Help\n" +
                "   rules   - Prints the rules for Blackjack.\n" +
                "   options - Prints the current input options.\n" +
                "   contact - How to reach the creator of this game.\n" +
                "   source  - Where the source code for this game can be obtained.\n" +
                "   quit    - Leave the game.");
    }

    private void rules() {
        System.out.println("\n" +
                "Goal:\n" +
                "   Accumulate a hand of cards whose value is larger than the dealer’s hand but not over 21 points. \n" +
                "\n" +
                "Counting:\n" +
                "   1) Aces can be counted as 1 or 11 points. \n" +
                "   2) The two to nine cards are according to their pip value.\n" +
                "   3) Tens and face cards are counted as 10 points.\n" +
                "\n" +
                "Rules:\n" +
                "   1) The player makes a bet between the minimum and maximum limit on his chance of winning. \n" +
                "   2) The dealer two cards to the player and himself. The dealer then reveals one of his cards.\n" +
                "   3) The player chooses one of the following moves until he stands or goes over 21 points.\n" +
                "       Stand: The player ends his turn for a given hand.\n" +
                "       Hit: player draws another card.\n" +
                "       Double: The player doubles his bet and draws only one more card.\n" +
                "       Split: If the player has a pair of two equal value cards on his first turn, he may then double his bet and separate his cards into two individual hands. The dealer will then will automatically give each hand a second card.\n" +
                "   4) After the player’s turn is over, the dealer will turn his cards over. If the dealer has 16 points or less, he will draw cards until he has more than 16 points.\n" +
                "\n" +
                "Win Conditions:\n" +
                "   1) If the player went over 21 points, then the player losses.\n" +
                "   2) If the player has more points then the dealer, then the player wins.\n" +
                "   3) If the player and dealer have the same points, then it is a tie.\n" +
                "   4) If the dealer went over 21 points, then the player wins.\n" +
                "   5) If the player has less points than the dealer, then the player losses.\n");
    }

    private void contact() {
        System.out.println("\n" +
                "Contact\n" +
                " Name: Joshua de Jong\n" +
                " Email: JoshuaKdeJong@gmail.com\n" +
                " LinkedIn: https://www.linkedin.com/in/joshua-de-jong/" +
                " GitHub: https://github.com/ManVanMaan");
    }

    private void source() {
        System.out.println("\n" +
                "Source\n" +
                " Dropbox: https://www.dropbox.com/sh/kn70x4rh1mkpuh7/AABL__rf7nepJ_wuCJR2JRXAa?dl=0\n" +
                " Github: https://github.com/ManVanMaan/Blackjack");
    }

    private void dots(int speed) {
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(speed);
                System.out.print("  .");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // State Changer
    private void quit() {
        System.out.print("\n" + "Quitting");
        dots(350);
        System.out.println();
        System.exit(0);
    }
}