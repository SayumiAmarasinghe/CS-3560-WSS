package wss.vision;

public class FarSightVision extends Vision {
    public FarSightVision() {
        super("Far-Sight Vision");
    }

    @Override
    public int[][] getVisibleOffsets() {
        return new int[][] {
            {-2, 0},
            {-1, 0},
            {1, 0},
            {2, 0},

            {-2, 1},
            {-1, 1},
            {0, 1},
            {1, 1},
            {2, 1},

            {-2, 2},
            {-1, 2},
            {0, 2},
            {1, 2},
            {2, 2}
        };
    }
}
