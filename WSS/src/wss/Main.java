package wss;
import java.util.Scanner;
import wss.map.Difficulty;
import wss.map.Map;

// client program to run the WSS game
public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the World Survival Simulator (WSS) Game!");
        System.out.println("Your goal: get to the bottom right corner of the map while managing your resources and surviving the terrain");
        // get user input for map configurations
        Scanner keyboard = new Scanner(System.in);
        System.out.println("\nNow, let's start configuring the game!");
        System.out.println("Enter map height (# squares on the y-axis). Make sure to keep it <= 5: ");
        int mapHeight = keyboard.nextInt();
        if (mapHeight > 5 || mapHeight < 0) {
            System.out.println("Invalid map height! Setting to default value of 5.");
            mapHeight = 5;
        }

        System.out.println("Enter map width (# squares on the x-axis). Make sure to keep it <= 5: ");
        int mapWidth = keyboard.nextInt();
        if (mapWidth > 5 || mapWidth < 0) {
            System.out.println("Invalid map width! Setting to default value of 5.");
            mapWidth = 5;
        }

        System.out.println("Enter difficulty level (either easy, medium, or hard): ");
        Difficulty difficulty = Difficulty.valueOf(keyboard.next().toUpperCase());

        // create the map
        Map gameMap = new Map(mapHeight, mapWidth, difficulty);
        gameMap.displayMap();

        // close the scanner
        keyboard.close();
    }
}
