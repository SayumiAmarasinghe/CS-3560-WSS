package wss.vision;

public abstract class Vision {
    private String name;

    public Vision(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    //Each vision type will decide what squares it can see
    //Each row is {rowChange, colChange}
    public abstract int[][] getVisibleOffsets();
    public boolean canSee(int playerRow, int playerCol, int targetRow, int targetCol) {
        int rowDifference = targetRow - playerRow;
        int colDifference = targetCol - playerCol;
        for (int[] offset : getVisibleOffsets()) {
            if (offset[0] == rowDifference && offset[1] == colDifference) {
                return true;
            }
        }
        return false;
    }
    public void printVisionScope() {
        System.out.println(name + " can see these relative squares:");

        for (int[] offset : getVisibleOffsets()) {
            System.out.println("Row change: " + offset[0] + ", Column change: " + offset[1]);
        }
    }
} 
