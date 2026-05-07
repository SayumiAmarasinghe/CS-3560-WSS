package wss;
import wss.gui.EntryScreen;
import wss.gui.InstrScreen;
import wss.gui.GameScreen;

//Vision testing
import wss.vision.Vision;
import wss.vision.VisionFactory;

public class Main {
    public static void main(String[] args) 
    {
        EntryScreen entryScreen = new EntryScreen();


        //Vision test
        Difficulty difficulty = Difficulty.EASY;
        System.out.println("Allowed visions on " + difficulty + ":");
        System.out.println(VisionFactory.getAllowedVisionNames(difficulty));
        Vision vision = VisionFactory.createVision("Cautious", difficulty);
        System.out.println("Chosen vision: " + vision.getName());
        vision.printVisionScope();
    }
}
