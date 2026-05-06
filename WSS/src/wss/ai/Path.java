package wss.ai;
import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<String> moves;
    private int totalMovementCost;
    private int totalWaterCost;
    private int totalFoodCost;
    private int targetRow;
    private int targetCol;

    public Path() {
        moves = new ArrayList<>();
        totalMovementCost = 0;
        totalWaterCost = 0;
        totalFoodCost = 0;
        targetRow = -1;
        targetCol = -1;
    }
    public void addMove(String move) {
        moves.add(move);
    }
    public void addCosts(int movementCost, int waterCost, int foodCost) {
        totalMovementCost += movementCost;
        totalWaterCost += waterCost;
        totalFoodCost += foodCost;
    }
    public List<String> getMoves() {
        return moves;
    }
    public int getTotalMovementCost() {
        return totalMovementCost;
    }
    public int getTotalWaterCost() {
        return totalWaterCost;
    }
    public int getTotalFoodCost() {
        return totalFoodCost;
    }
    public int getTotalCost() {
        return totalMovementCost + totalWaterCost + totalFoodCost;
    }
    public int getLength() {
        return moves.size();
    }
    public void setTargetLocation(int targetRow, int targetCol) {
        this.targetRow = targetRow;
        this.targetCol = targetCol;
    }
    public int getTargetRow() {
        return targetRow;
    }
    public int getTargetCol() {
        return targetCol;
    }
    @Override
    public String toString() {
        return "Path{" +
                "moves=" + moves +
                ", movementCost=" + totalMovementCost +
                ", waterCost=" + totalWaterCost +
                ", foodCost=" + totalFoodCost +
                ", totalCost=" + getTotalCost() +
                ", target=(" + targetRow + ", " + targetCol + ")" +
                '}';
    }
}
