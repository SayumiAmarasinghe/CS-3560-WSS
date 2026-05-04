package wss.vision;

public class KeenEyedVision extends Vision {
    public KeenEyedVision() {
        super("Keen-Eyed Vision");
    }

    @Override
    public int[][] getVisibleOffsets() {
        return new int[][] {
            {-1, 0}, //north
            {1, 0},  //south

            {-1, 1}, //northeast
            {0, 1},  //east
            {1, 1},  //southeast

            {0, 2}   //for two squares east
        };
    }
}
