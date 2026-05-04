package wss.vision;

public class CautiousVision extends Vision {
    public CautiousVision() {
        super("Cautious Vision");
    }

    @Override
    public int[][] getVisibleOffsets() {
        return new int[][] {
            {-1, 0}, //north
            {1, 0},  //south
            {0, 1}   //east
        };
    }
}
