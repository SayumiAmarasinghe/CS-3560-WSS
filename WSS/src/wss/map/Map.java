package wss.map;

import wss.entities.Player;
import wss.terrain.TerrainSquare;

public class Map 
{
    private int height;
    private int width;
    private Difficulty difficulty;
    private TerrainSquare[][] grid;
    private Player player;

    public Map(int height, int width, Difficulty difficulty) 
    {
        this.width = width;
        this.height = height;
        this.difficulty = difficulty;
        this.grid = new TerrainSquare[height][width];
        // TODO: generate the map based on difficulty
        // generateMap(difficulty);
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
            return grid[y][x];
        else 
            throw new IndexOutOfBoundsException("Coordinates out of bounds");
    }

    public void displayMap()
    {
        // TODO: implement the display logic of the current map state
    }



}
