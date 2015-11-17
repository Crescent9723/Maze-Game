package ca.cmpt213.as2.model;

import java.util.ArrayList;
import java.util.List;

public class MazeGame {
	private static final int MAZE_SIZE_WIDTH  = 20;
	private static final int MAZE_SIZE_HEIGHT = 15;
	private static final int NUM_COIN_TO_COLLECT = 5;
	
	private static final CellLocation LOCATION_TOP_LEFT     = new CellLocation(1, 1);
	private static final CellLocation LOCATION_TOP_RIGHT    = new CellLocation(MAZE_SIZE_WIDTH - 2, 1);
	private static final CellLocation LOCATION_BOTTOM_LEFT  = new CellLocation(1, MAZE_SIZE_HEIGHT - 2);
	private static final CellLocation LOCATION_BOTTOM_RIGHT = new CellLocation(MAZE_SIZE_WIDTH - 2, MAZE_SIZE_HEIGHT - 2);

	private Maze maze = new Maze(MAZE_SIZE_WIDTH, MAZE_SIZE_HEIGHT);
	
	private CellLocation playerLocation = LOCATION_TOP_LEFT;
	private CellLocation coinLocation;
	private List<Thunder> thunders = new ArrayList<Thunder>();
	private int numCoinCollected;
	
	public MazeGame() {
		placeNewCoinOnBoard();
		placeThundersOnBoard();
		setVisibleAroundPlayerCell();
	}
	
	private void placeThundersOnBoard() {
		thunders.add(new Thunder(this, LOCATION_TOP_RIGHT));
		thunders.add(new Thunder(this, LOCATION_TOP_RIGHT));
		thunders.add(new Thunder(this, LOCATION_BOTTOM_RIGHT));
		thunders.add(new Thunder(this, LOCATION_BOTTOM_RIGHT));
		thunders.add(new Thunder(this, LOCATION_BOTTOM_LEFT));
		thunders.add(new Thunder(this, LOCATION_BOTTOM_LEFT));
	}

	public boolean hasUserWon() {
		boolean collectedEnoughCoin = numCoinCollected >= NUM_COIN_TO_COLLECT;
		return !hasUserLost() && collectedEnoughCoin;
	}
	public boolean hasUserLost() {
		return isThundertAtLocation(playerLocation);
	}
	
	public int getNumberCoinToCollect() {
		return NUM_COIN_TO_COLLECT;
	}
	public int getNumberCoinCollected() {
		return numCoinCollected;
	}

	public boolean isValidPlayerMove(MoveDirection move) {
		CellLocation targetLocation = playerLocation.getMovedLocation(move);
		return maze.isCellOpen(targetLocation);
	}

	public boolean isCellOpen(CellLocation cell) {
		return maze.isCellOpen(cell);
	}

	public void recordPlayerMove(MoveDirection move) {
		if (isValidPlayerMove(move)){
			playerLocation = playerLocation.getMovedLocation(move);
			
			setVisibleAroundPlayerCell();
			
			// Compute goal states achieved
			if (isCoinAtLocation(playerLocation)) {
				numCoinCollected++;
				placeNewCoinOnBoard();
			}
		}
		
		
	}

	private void placeNewCoinOnBoard() {
		// Find a random open location which is connected to where the player is, 
		// but which is not where the player currently is.
		do {
			coinLocation = maze.getRandomLocationInsideMaze();
		} while (isMouseAtLocation(coinLocation)
				|| maze.isCellAWall(coinLocation));
				
	}

	private void setVisibleAroundPlayerCell() {
		CellLocation up = playerLocation.getMovedLocation(MoveDirection.MOVE_UP);
		CellLocation down = playerLocation.getMovedLocation(MoveDirection.MOVE_DOWN);
		CellLocation right = playerLocation.getMovedLocation(MoveDirection.MOVE_RIGHT);
		CellLocation left = playerLocation.getMovedLocation(MoveDirection.MOVE_LEFT);

		// Current cell, Up, Down, Right, Left.
		maze.recordCellVisible(playerLocation);
		maze.recordCellVisible(up);
		maze.recordCellVisible(down);
		maze.recordCellVisible(right);
		maze.recordCellVisible(left);

		// 45' Angles
		maze.recordCellVisible(up.getMovedLocation(MoveDirection.MOVE_RIGHT));
		maze.recordCellVisible(up.getMovedLocation(MoveDirection.MOVE_LEFT));
		maze.recordCellVisible(down.getMovedLocation(MoveDirection.MOVE_RIGHT));
		maze.recordCellVisible(down.getMovedLocation(MoveDirection.MOVE_LEFT));
	}
	
	public CellState getCellState(CellLocation cell) {
		return maze.getCellState(cell);
	}
	public boolean isMouseAtLocation(CellLocation cell) {
		return playerLocation.equals(cell);
	}
	public boolean isThundertAtLocation(CellLocation cell) {
		for (Thunder Thunder : thunders) {
			if (Thunder.getLocation().equals(cell)) {
				return true;
			}
		}
		return false;
	}
	public boolean isCoinAtLocation(CellLocation cell) {
		return coinLocation != null && coinLocation.equals(cell);
	}

	public static int getMazeWidth() {
		return MAZE_SIZE_WIDTH;
	}
	public static int getMazeHeight() {
		return MAZE_SIZE_HEIGHT;
	}
	
	public void doThunderMoves() {
		for (Thunder Thunder : thunders) {
			Thunder.doMove();
		}
	}	
}
