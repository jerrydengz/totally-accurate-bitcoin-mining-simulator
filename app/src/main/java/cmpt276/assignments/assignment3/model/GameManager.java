package cmpt276.assignments.assignment3.model;

import java.util.Random;

// Handles Game Functionality like:
// (1) Handles which cells are bombs (sets the bomb grids).
// (2) Getting number of mine vertically/horizontally (scan).
// (3) Dynamically updating mine counts in row/col when a new mine  is found.
public class GameManager {
    private final int NUM_MINES;
    private final int NUM_ROWS;
    private final int NUM_COLS;
    private static GridCell[][] gridCells;
    private int numOfScansDone = 0;
    private int numOfMinesFound = 0;

    public void IncrementNumOfScansDone() {
        numOfScansDone++;
    }

    public void IncrementNumOfMinesFound() {
        numOfMinesFound++;
    }

    public int getNumOfScansDone() {
        return numOfScansDone;
    }

    public int getNumOfMinesFound() {
        return numOfMinesFound;
    }

    public GridCell[][] getGridCells() {
        return gridCells;
    }

    public GameManager(int dimX, int dimY, int numMines) {
        NUM_MINES = numMines;
        NUM_ROWS = dimX;
        NUM_COLS = dimY;
        gridCells = new GridCell[NUM_ROWS][NUM_COLS];

        fillGrid();
        setMineCords();
    }

    // Instantiate grid cell objects.
    private void fillGrid(){
        for (int row = 0; row < NUM_ROWS; ++row) {
            for (int col = 0; col < NUM_COLS; ++col) {
                gridCells[row][col] = new GridCell(false, false, false);
            }
        }
    }

    public void setMineCords() {
        int numGeneratedMines = 0;

        while (numGeneratedMines < NUM_MINES) {
            int row = getRandomValue(NUM_ROWS);
            int col = getRandomValue(NUM_COLS);

            GridCell grid = gridCells[row][col];

            // Only set the mine if it is not already a mine.
            if (!grid.isMine()) {
                grid.setMine(true);
                numGeneratedMines++;
            }
        }
    }

    // For generating mine locations.
    public int getRandomValue(int upperBound) {
        Random rand = new Random();
        return rand.nextInt(upperBound);
    }

    // Scans for bombs in a specific grid entry.
    public void scan(int row, int col) {
        GridCell gridToScan = gridCells[row][col];

        // Guard statement if scanned already.
        if (gridToScan.isScanned()) {
            return;
        }

        int numOfMine = 0;
        IncrementNumOfScansDone();

        // Scans all grids in the row for num of bombs.
        for (int gridInCol = 0; gridInCol < NUM_COLS; ++gridInCol) {
            GridCell scannedGrid = gridCells[row][gridInCol];

            // Guard to avoid counting the clicked entry
            if (gridInCol == col) {
                continue;
            }

            // Update number of mines found.
            if (scannedGrid.isMine() && !scannedGrid.isMineFound()) {
                numOfMine++;
            }

            // Update mineCounter in its row.
            if (gridToScan.isMine()) {
                scannedGrid.decrementLocalMineCounter();
            }
        }

        // Scans all grids in the col for num of bombs.
        for (int gridInRow = 0; gridInRow < NUM_ROWS; ++gridInRow) {
            GridCell scannedGrid = gridCells[gridInRow][col];

            // Guard to avoid counting the clicked entry
            if (gridInRow == row) {
                continue;
            }

            // Update number of mines found.
            if (scannedGrid.isMine() && !scannedGrid.isMineFound()) {
                numOfMine++;
            }

            // Update mineCounter in its row.
            if (gridToScan.isMine()) {
                scannedGrid.decrementLocalMineCounter();
            }
        }

        if (!gridToScan.isMine()) {
            gridToScan.setScanned(true);
        }

        gridToScan.setLocalMineCounter(numOfMine);
    }

    public void updateRowColValues(int row, int col) {
        GridCell gridToScan = gridCells[row][col];

        // Scans all grids in the row for num of bombs.
        for (int gridInCol = 0; gridInCol < NUM_COLS; ++gridInCol) {
            GridCell scannedGrid = gridCells[row][gridInCol];

            // Guard to avoid counting the clicked entry
            if (gridInCol == col) {
                continue;
            }

            // Update mineCounter in its row.
            if (gridToScan.isMine()) {
                scannedGrid.decrementLocalMineCounter();
            }
        }

        // Scans all grids in the col for num of bombs.
        for (int gridInRow = 0; gridInRow < NUM_ROWS; ++gridInRow) {
            GridCell scannedGrid = gridCells[gridInRow][col];

            // Guard to avoid counting the clicked entry
            if (gridInRow == row) {
                continue;
            }

            // Update mineCounter in its row.
            if (gridToScan.isMine()) {
                scannedGrid.decrementLocalMineCounter();
            }
        }

    }

}