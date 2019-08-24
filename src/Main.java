import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        Random rand = new Random(System.currentTimeMillis());

        UserInterface ui = new UserInterface(reader, rand);
        ui.start();
    }






}
