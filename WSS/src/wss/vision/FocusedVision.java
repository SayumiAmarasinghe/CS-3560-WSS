package wss.vision;

public class FocusedVision extends Vision {
    public FocusedVision() {
        super("Focused Vision");
    }

    @Override
    public int[][] getVisibleOffsets() {
        return new int[][] {
            {-1, 1}, //northeast
            {0, 1},  //east
            {1, 1}   //southeast
        };
    }
}
