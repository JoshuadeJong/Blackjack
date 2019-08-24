import java.util.ArrayList;

public class Card {

    // Constants
    private static final int RANK_MAX_INDEX = 13;
    private static final int SUIT_MAX_INDEX = 4;

    // Variables
    private int rank;
    private int suit;

    // Constructor
    public Card(int rank, int suit){
        this.rank = rank;
        this.suit = suit;
    }       // Test 0

    public Card(String rank, String suit){
        this.rank = str2int(rank);
        this.suit = str2int(rank);
    }   // Test 0

    // Gets
    public int getRanki(){
        return this.rank;
    }   // Test 0

    public String getRankc(){
        return rank2str();
    }   // Test 0

    public int getSuiti(){
        return this.suit;
}   // Test 0

    public String getSuitc(){
        return suit2str();
    }   // Test 0

    public ArrayList<Integer> getValue(){ //
        ArrayList<Integer> values = new ArrayList<>();

        values.add((this.rank - 1)*SUIT_MAX_INDEX + (this.suit - 1) + 1); // Lower Value

        if(this.rank == 1){ // Higher Value, If an Ace
            values.add((this.rank - 1 + RANK_MAX_INDEX)*SUIT_MAX_INDEX + (this.suit - 1) + 1);
        } else {
            values.add((this.rank - 1)*SUIT_MAX_INDEX + (this.suit - 1) + 1);
        }

        return values;
    }   // Test 1

    // Sets
    public void setRank(int rank){
        this.rank = rank;
    }   // Test 2

    public void setRank(String rank){
        this.rank = str2int(rank);
    }   // Test 2

    public void setSuit(int suit){
        this.suit = suit;
    }   // Test 2

    public void setSuit(String suit){
        this.suit = str2int(suit);
    }   // Test 2

    // Methods
    private int str2int(String str) {
        int output;

        switch(str.toLowerCase()){
            // Ranks
            case "one": output = 1; break;
            case "two": output = 2; break;
            case "three": output = 3; break;
            case "four": output = 4; break;
            case "five": output = 5; break;
            case "six": output = 6; break;
            case "seven": output = 7; break;
            case "eight": output = 8; break;
            case "nine": output = 9; break;
            case "ten": output = 10; break;
            case "jack": output = 11; break;
            case "queen": output = 12; break;
            case "king": output = 13; break;
            case "ace": output = 1; break;
            // Suits
            case "diamonds": output = 1; break;
            case "clubs": output = 2; break;
            case "hearts": output = 3; break;
            case "spades": output = 4; break;
            // default
            default: output = 0;
        }

        return output;
    }

    private String rank2str() {
        String str;

        switch(this.rank) {
            case 1: str = "ace"; break;
            case 2: str = "two"; break;
            case 3: str = "three"; break;
            case 4: str = "four"; break;
            case 5: str = "five"; break;
            case 6: str = "six"; break;
            case 7: str = "seven"; break;
            case 8: str = "eight"; break;
            case 9: str = "nine"; break;
            case 10: str = "ten"; break;
            case 11: str = "jack"; break;
            case 12: str = "queen"; break;
            case 13: str = "king"; break;
            default: str = "Not Found";
        }

        return str;
    }

    private String suit2str() {
        String str;

        switch(this.suit){
            case 1: str = "diamonds"; break;
            case 2: str = "clubs"; break;
            case 3: str = "hearts"; break;
            case 4: str = "spades"; break;
            default: str = "Not Found";
        }

        return str;
    }

    // String
    public ArrayList<String> cardFront(){
        // Symbols
        String rS;

        switch(this.rank){
            case 1: rS = " A"; break;
            case 2: rS = " 2"; break;
            case 3: rS = " 3"; break;
            case 4: rS = " 4"; break;
            case 5: rS = " 5"; break;
            case 6: rS = " 6"; break;
            case 7: rS = " 7"; break;
            case 8: rS = " 8"; break;
            case 9: rS = " 9"; break;
            case 10: rS = "10"; break;
            case 11: rS = " J"; break;
            case 12: rS = " Q"; break;
            case 13: rS = " K"; break;
            default: rS = "" + this.rank;
        }

        String line_1, line_2, line_3, line_4;

        switch(this.suit){
            case 1:

                line_1 = "│"+"\u001B[31m"+"   .I.   "+"\u001B[0m"+"│";
                line_2 = "│"+"\u001B[31m"+" .I888I. "+"\u001B[0m"+"│";
                line_3 = "│"+"\u001B[31m"+" `Y888Y' "+"\u001B[0m"+"│";
                line_4 = "│"+"\u001B[31m"+"   `Y'   "+"\u001B[0m"+"│";
                break;
            case 2:
                line_1 = "│"+"\u001B[34m"+"   / \\   "+"\u001B[0m"+"│";
                line_2 = "│"+"\u001B[34m"+" __\\_/__ "+"\u001B[0m"+"│";
                line_3 = "│"+"\u001B[34m"+" \\__X__/ "+"\u001B[0m"+"│";
                line_4 = "│"+"\u001B[34m"+"    Y    "+"\u001B[0m"+"│";
                break;
            case 3:
                line_1 = "│"+"\u001B[31m"+" ,m. .m, "+"\u001B[0m"+"│";
                line_2 = "│"+"\u001B[31m"+" 888Y888 "+"\u001B[0m"+"│";
                line_3 = "│"+"\u001B[31m"+" `Y888Y' "+"\u001B[0m"+"│";
                line_4 = "│"+"\u001B[31m"+"   `Y'   "+"\u001B[0m"+"│";
                break;
            case 4:
                line_1 = "│"+"\u001B[34m"+"   / \\   "+"\u001B[0m"+"│";
                line_2 = "│"+"\u001B[34m"+"  /   \\  "+"\u001B[0m"+"│";
                line_3 = "│"+"\u001B[34m"+" (__ __) "+"\u001B[0m"+"│";
                line_4 = "│"+"\u001B[34m"+"    Y    "+"\u001B[0m"+"│";
                break;
            default:
                line_1 = "│         │";
                line_2 = "│         │";
                line_3 = "│         │";
                line_4 = "│         │";
        }

        // Card Build
        ArrayList<String> art = new ArrayList<>();
        art.add("┌─────────┐");
        art.add("│"+rS+"       │");
        art.add(line_1);
        art.add(line_2);
        art.add(line_3);
        art.add(line_4);
        art.add("│      "+rS+" │");
        art.add("└─────────┘");

        return art;
    }

    public ArrayList<String> cardBack() {
        ArrayList<String> art = new ArrayList<>();
        art.add("┌─────────┐");
        art.add("│         │");
        art.add("│---__o   │");
        art.add("│--_\\ <,_ │");
        art.add("│-(_)/ (_)│");
        art.add("│ Bicycle │");
        art.add("│         │");
        art.add("└─────────┘");

        return art;
    }
}