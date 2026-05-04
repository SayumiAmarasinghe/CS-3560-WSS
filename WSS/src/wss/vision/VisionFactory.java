package wss.vision;
import java.util.ArrayList;
import java.util.List;
import wss.Difficulty;
public class VisionFactory {
    public static List<String> getAllowedVisionNames(Difficulty difficulty) {
        List<String> visions = new ArrayList<>();

        if (difficulty == Difficulty.EASY) {
            visions.add("Focused");
            visions.add("Cautious");
            visions.add("Keen-Eyed");
            visions.add("Far-Sight");
        } 
        else if (difficulty == Difficulty.MEDIUM) {
            visions.add("Focused");
            visions.add("Cautious");
            visions.add("Keen-Eyed");
        } 
        else if (difficulty == Difficulty.HARD) {
            visions.add("Focused");
            visions.add("Cautious");
        }

        return visions;
    }

    public static Vision createVision(String visionChoice, Difficulty difficulty) {
        List<String> allowedVisions = getAllowedVisionNames(difficulty);

        if (!allowedVisions.contains(visionChoice)) {
            throw new IllegalArgumentException(
                visionChoice + " is not allowed on " + difficulty + " difficulty."
            );
        }
        if (visionChoice.equals("Focused")) {
            return new FocusedVision();
        } 
        else if (visionChoice.equals("Cautious")) {
            return new CautiousVision();
        } 
        else if (visionChoice.equals("Keen-Eyed")) {
            return new KeenEyedVision();
        } 
        else if (visionChoice.equals("Far-Sight")) {
            return new FarSightVision();
        } 
        else {
            throw new IllegalArgumentException("Invalid vision choice.");
        }
    }
}
