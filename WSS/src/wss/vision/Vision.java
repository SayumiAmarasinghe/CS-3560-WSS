package wss.vision;
import java.util.ArrayList;
import java.util.List;
import wss.ai.Path;
import wss.items.FoodBonus;
import wss.items.GoldBonus;
import wss.items.Item;
import wss.items.WaterBonus;
import wss.map.Map;
import wss.terrain.TerrainSquare;
public abstract class Vision {
    private String name;
    public Vision(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    //Each vision type will decide what squares it can see.
    //Each row is {rowChange, colChange}.
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
    private boolean isInsideMap(Map map, int row, int col) {
        return row >= 0 && row < map.getHeight()
            && col >= 0 && col < map.getWidth();
    }
    private String getMoveName(int rowStep, int colStep) {
        if (rowStep == -1 && colStep == 0) return "MoveNorth";
        if (rowStep == 1 && colStep == 0) return "MoveSouth";
        if (rowStep == 0 && colStep == 1) return "MoveEast";
        if (rowStep == 0 && colStep == -1) return "MoveWest";

        if (rowStep == -1 && colStep == 1) return "MoveNorthEast";
        if (rowStep == -1 && colStep == -1) return "MoveNorthWest";
        if (rowStep == 1 && colStep == 1) return "MoveSouthEast";
        if (rowStep == 1 && colStep == -1) return "MoveSouthWest";

        return "Stay";
    }
    public Path buildPathToOffset(Map map, int playerRow, int playerCol, int rowOffset, int colOffset) {
        Path path = new Path();
        int currentRow = playerRow;
        int currentCol = playerCol;
        int remainingRow = rowOffset;
        int remainingCol = colOffset;
        while (remainingRow != 0 || remainingCol != 0) {
            int rowStep = Integer.compare(remainingRow, 0);
            int colStep = Integer.compare(remainingCol, 0);
            currentRow += rowStep;
            currentCol += colStep;
            if (!isInsideMap(map, currentRow, currentCol)) {
                return null;
            }
            TerrainSquare square = map.getTerrainAt(currentRow, currentCol);
            path.addMove(getMoveName(rowStep, colStep));
            path.addCosts(
                square.getMovementCost(),
                square.getWaterCost(),
                square.getFoodCost()
            );
            remainingRow -= rowStep;
            remainingCol -= colStep;
        }
        path.setTargetLocation(currentRow, currentCol);
        return path;
    }
    public Path easiestPath(Map map, int playerRow, int playerCol) {
        Path bestPath = null;
        for (int[] offset : getVisibleOffsets()) {
            int targetRow = playerRow + offset[0];
            int targetCol = playerCol + offset[1];
            if (!isInsideMap(map, targetRow, targetCol)) {
                continue;
            }
            Path currentPath = buildPathToOffset(map, playerRow, playerCol, offset[0], offset[1]);
            if (currentPath == null) {
                continue;
            }
            if (bestPath == null || currentPath.getTotalCost() < bestPath.getTotalCost()) {
                bestPath = currentPath;
            }
        }
        return bestPath;
    }
    public Path closestFood(Map map, int playerRow, int playerCol) {
        return closestPathToItemType(map, playerRow, playerCol, FoodBonus.class);
    }
    public Path closestWater(Map map, int playerRow, int playerCol) {
        return closestPathToItemType(map, playerRow, playerCol, WaterBonus.class);
    }
    public Path closestGold(Map map, int playerRow, int playerCol) {
        return closestPathToItemType(map, playerRow, playerCol, GoldBonus.class);
    }
    public Path closestTrader(Map map, int playerRow, int playerCol) {
        Path closestPath = null;
        for (int[] offset : getVisibleOffsets()) {
            int targetRow = playerRow + offset[0];
            int targetCol = playerCol + offset[1];
            if (!isInsideMap(map, targetRow, targetCol)) {
                continue;
            }
            TerrainSquare square = map.getTerrainAt(targetRow, targetCol);
            if (square.hasTrader()) {
                Path path = buildPathToOffset(map, playerRow, playerCol, offset[0], offset[1]);
                if (path != null && isCloser(path, closestPath)) {
                    closestPath = path;
                }
            }
        }
        return closestPath;
    }
    public Path secondClosestFood(Map map, int playerRow, int playerCol) {
        return secondClosestPathToItemType(map, playerRow, playerCol, FoodBonus.class);
    }
    public Path secondClosestWater(Map map, int playerRow, int playerCol) {
        return secondClosestPathToItemType(map, playerRow, playerCol, WaterBonus.class);
    }
    public Path secondClosestGold(Map map, int playerRow, int playerCol) {
        return secondClosestPathToItemType(map, playerRow, playerCol, GoldBonus.class);
    }
    public Path secondClosestTrader(Map map, int playerRow, int playerCol) {
        List<Path> traderPaths = new ArrayList<>();
        for (int[] offset : getVisibleOffsets()) {
            int targetRow = playerRow + offset[0];
            int targetCol = playerCol + offset[1];
            if (!isInsideMap(map, targetRow, targetCol)) {
                continue;
            }
            TerrainSquare square = map.getTerrainAt(targetRow, targetCol);
            if (square.hasTrader()) {
                Path path = buildPathToOffset(map, playerRow, playerCol, offset[0], offset[1]);
                if (path != null) {
                    traderPaths.add(path);
                }
            }
        }
        return getSecondClosestPath(traderPaths);
    }
    private Path closestPathToItemType(Map map, int playerRow, int playerCol, Class<?> itemType) {
        Path closestPath = null;
        for (int[] offset : getVisibleOffsets()) {
            int targetRow = playerRow + offset[0];
            int targetCol = playerCol + offset[1];
            if (!isInsideMap(map, targetRow, targetCol)) {
                continue;
            }
            TerrainSquare square = map.getTerrainAt(targetRow, targetCol);
            if (containsItemType(square, itemType)) {
                Path path = buildPathToOffset(map, playerRow, playerCol, offset[0], offset[1]);
                if (path != null && isCloser(path, closestPath)) {
                    closestPath = path;
                }
            }
        }
        return closestPath;
    }
    private Path secondClosestPathToItemType(Map map, int playerRow, int playerCol, Class<?> itemType) {
        List<Path> matchingPaths = new ArrayList<>();
        for (int[] offset : getVisibleOffsets()) {
            int targetRow = playerRow + offset[0];
            int targetCol = playerCol + offset[1];
            if (!isInsideMap(map, targetRow, targetCol)) {
                continue;
            }
            TerrainSquare square = map.getTerrainAt(targetRow, targetCol);
            if (containsItemType(square, itemType)) {
                Path path = buildPathToOffset(map, playerRow, playerCol, offset[0], offset[1]);

                if (path != null) {
                    matchingPaths.add(path);
                }
            }
        }
        return getSecondClosestPath(matchingPaths);
    }
    private boolean containsItemType(TerrainSquare square, Class<?> itemType) {
        if (!square.hasItems()) {
            return false;
        }
        for (Item item : square.getItems()) {
            if (itemType.isInstance(item)) {
                return true;
            }
        }
        return false;
    }
    private boolean isCloser(Path currentPath, Path bestPath) {
        if (bestPath == null) {
            return true;
        }
        if (currentPath.getLength() < bestPath.getLength()) {
            return true;
        }
        if (currentPath.getLength() == bestPath.getLength()
                && currentPath.getTotalCost() < bestPath.getTotalCost()) {
            return true;
        }
        return false;
    }
    private Path getSecondClosestPath(List<Path> paths) {
        Path closest = null;
        Path secondClosest = null;
        for (Path path : paths) {
            if (isCloser(path, closest)) {
                secondClosest = closest;
                closest = path;
            } else if (isCloser(path, secondClosest)) {
                secondClosest = path;
            }
        }
        return secondClosest;
    }
}
