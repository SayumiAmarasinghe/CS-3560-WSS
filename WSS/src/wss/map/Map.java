package wss.map;

import wss.entities.Player;
import wss.terrain.TerrainSquare;
import wss.terrain.TerrainType;

public class Map 
{
    // attributes
    private int height;
    private int width;
    private Difficulty difficulty;
    private TerrainSquare[][] grid;
    private Player player;

    // constructor
    public Map(int height, int width, Difficulty difficulty) 
    {
        this.height = height;
        this.width = width;
        this.difficulty = difficulty;
        this.grid = generateMap(height, width, difficulty);
    }

    // helper method to generate the map 
    private TerrainSquare[][] generateMap(int height, int width, Difficulty difficulty)
    {
        // probabilities to generate a terrain type
        double p_plains = 0.0, p_forest = 0.0; 
        double p_mountain = 0.0, p_swamp = 0.0;
        double p_desert = 0.0, p_tundra = 0.0;
        // random generator to the map based on difficulty
        if (difficulty == Difficulty.EASY) 
        {
            p_plains = 0.5;
            p_forest = 0.4;
            p_mountain = 0.05;
            p_swamp = 0.05;
        } 
        else if (difficulty == Difficulty.MEDIUM) 
        {
            p_plains = 0.3;
            p_forest = 0.3;
            p_mountain = 0.2;
            p_swamp = 0.1;
            p_desert = 0.05;
            p_tundra = 0.05;
        } 
        else if (difficulty == Difficulty.HARD) 
        {
            p_plains = 0.1;
            p_forest = 0.1;
            p_mountain = 0.3;
            p_swamp = 0.2;
            p_desert = 0.2;
            p_tundra = 0.1;
        }
        else 
        {
            throw new IllegalArgumentException("Invalid difficulty level set!");
        }

        TerrainSquare[][] mapGrid = new TerrainSquare[height][width];
        for (int y = 0; y < height; y++) 
        {
            for (int x = 0; x < width; x++) 
            {
                double rand = Math.random();
                if (rand < p_plains) 
                    mapGrid[y][x] = new TerrainSquare(TerrainType.PLAINS, null, null);
                else if (rand < p_plains + p_forest) 
                    mapGrid[y][x] = new TerrainSquare(TerrainType.FOREST, null, null);
                else if (rand < p_plains + p_forest + p_mountain) 
                    mapGrid[y][x] = new TerrainSquare(TerrainType.MOUNTAIN, null, null);
                else if (rand < p_plains + p_forest + p_mountain + p_swamp) 
                    mapGrid[y][x] = new TerrainSquare(TerrainType.SWAMP, null, null);
                else if (rand < p_plains + p_forest + p_mountain + p_swamp + p_desert) 
                    mapGrid[y][x] = new TerrainSquare(TerrainType.DESERT, null, null);
                else 
                    mapGrid[y][x] = new TerrainSquare(TerrainType.TUNDRA, null, null);
            }
        }
        return mapGrid;

    }
    public int getHeight() 
    {
        return height;
    }

    public int getWidth() 
    {
        return width;
    }


    public TerrainSquare getTerrainAt(int y, int x)
    {
        if (x >= 0 && x < width && y >= 0 && y < height) 
            return this.grid[y][x];
        else 
            throw new IndexOutOfBoundsException("Coordinates out of bounds");
    }

    public void displayMap()
    {
        final int cellWidth = 18;
        System.out.println("Current Map State: ");
        for (int y = 0; y < height; y++)
        {
            printRowBorder(cellWidth);
            for (int line = 0; line < 4; line++)
            {
                printRowLine(y, line, cellWidth);
            }
        }
        printRowBorder(cellWidth);
        System.out.println();
    }

    // helper methods to generate the map grid and its metadata

    private void printRowBorder(int cellWidth)
    {
        StringBuilder border = new StringBuilder();
        for (int x = 0; x < width; x++)
        {
            border.append('+');
            for (int i = 0; i < cellWidth; i++)
            {
                border.append('-');
            }
        }
        border.append('+');
        System.out.println(border);
    }

    private void printRowLine(int rowIndex, int lineIndex, int cellWidth)
    {
        for (int x = 0; x < width; x++)
        {
            System.out.print("|");
            System.out.print(formatCellLine(this.grid[rowIndex][x], lineIndex, cellWidth));
        }
        System.out.println("|");
    }

    private String formatCellLine(TerrainSquare square, int lineIndex, int cellWidth)
    {
        String cellText;

        switch (lineIndex)
        {
            case 0:
                cellText = "Terrain: " + square.getTerrainType().name();
                break;
            case 1:
                cellText = "Food Cost: " + square.getFoodCost();
                break;
            case 2:
                cellText = "Water Cost: " + square.getWaterCost();
                break;
            default:
                cellText = "Movement Cost: " + square.getMovementCost();
                break;
        }

        return String.format("%-" + cellWidth + "s", cellText);
    }



}
