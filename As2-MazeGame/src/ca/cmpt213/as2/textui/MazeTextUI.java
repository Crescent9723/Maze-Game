package ca.cmpt213.as2.textui;

import java.util.Scanner;

import ca.cmpt213.as2.model.CellLocation;
import ca.cmpt213.as2.model.CellState;
import ca.cmpt213.as2.model.MazeGame;
import ca.cmpt213.as2.model.MoveDirection;

/**
 * Text UI for the maze game. Handles all input/output with user. 
 * Displays the maze when needed.
 */
public class MazeTextUI {	
	private MazeGame game;
		
	private static final char SYMBOL_FOG    = '.';
	private static final char SYMBOL_CAT    = '!';
	private static final char SYMBOL_DEAD   = 'X';
	private static final char SYMBOL_MOUSE  = '@';
	private static final char SYMBOL_WALL   = '#';
	private static final char SYMBOL_CHEESE = '$';
	private static final char SYMBOL_SPACE  = ' ';
	
	public MazeTextUI(MazeGame game) {
		this.game = game; 
	}

	public void playGame() {
		displayWelcome();
		displayDirections();
		displayBoard(false);
		while (gameNotWonOrLost()) {
			doPlayerMove();

			// Handle if player moves into cat's space and dies.
			if (!gameNotWonOrLost()){
				break;
			}
			
			doCatMoves();
			displayBoard(false);
		}
		doWonOrLost();
	} 
	
	private void displayWelcome() {
		System.out.println("----------------------------------------");
		System.out.println("Welcome to Cat and Mouse Maze Adventure!");
		System.out.println("by Brian Fraser");
		System.out.println("----------------------------------------");
	}

	private void displayDirections() {
		System.out.printf("%n");
		System.out.printf("DIRECTIONS:%n");
		System.out.printf("	Find %d cheese before a cat eats you!%n", 
				game.getNumberCoinToCollect());
		System.out.printf("LEGEND:%n");
		System.out.printf("	%c: Wall%n", SYMBOL_WALL);
		System.out.printf("	%c: You (a mouse)%n", SYMBOL_MOUSE);
		System.out.printf("	%c: Cat%n", SYMBOL_CAT);
		System.out.printf("	%c: Cheese%n", SYMBOL_CHEESE);
		System.out.printf("	%c: Unexplored space%n", SYMBOL_FOG);
		System.out.printf("MOVES:%n");
		System.out.printf("	Use W (up), A (left), S (down) and D (right) to move.%n");
		System.out.printf("	(You must press enter after each move).%n");
	}

	private boolean gameNotWonOrLost() {
		return !game.hasUserWon() && !game.hasUserLost();
	}

	private void displayBoard(boolean revealBoard) {
		System.out.println();
		System.out.println("Maze:");
		
		for (int y = 0; y < MazeGame.getMazeHeight(); y++) {
			for (int x = 0; x < MazeGame.getMazeWidth(); x++) {
				
				CellLocation cell = new CellLocation(x, y);
				char symbol = getSymbolForCell(cell, revealBoard);
				System.out.printf("%c", symbol);

			}
			System.out.println();
		}
		System.out.printf("Cheese collected: %d of %d%n",
				game.getNumberCoinCollected(),
				game.getNumberCoinToCollect());
		
	}

	private char getSymbolForCell(CellLocation cell, boolean revealBoard) {
		CellState state = game.getCellState(cell);
		
		if (game.isMouseAtLocation(cell) && game.isThundertAtLocation(cell)) {
			return SYMBOL_DEAD;
		} else if (game.isMouseAtLocation(cell)) {
			return SYMBOL_MOUSE;
		} else if (game.isThundertAtLocation(cell)) {
			return SYMBOL_CAT;
		} else if (game.isCoinAtLocation(cell)) {
			return SYMBOL_CHEESE;
		} else if (state.isHidden() && !revealBoard) {
			return SYMBOL_FOG;
		} else if (state.isWall()) {
			return SYMBOL_WALL;
		} else {
			return SYMBOL_SPACE;
		} 
	}

	private void doPlayerMove() {
		MoveDirection move = MoveDirection.MOVE_NONE;
		do {
			move = getPlayerMove();
			if (!game.isValidPlayerMove(move)) {
				System.out.println("Invalid move: you cannot move through walls!");
			}
		} while (!game.isValidPlayerMove(move));
		game.recordPlayerMove(move);
	}

	private MoveDirection getPlayerMove() {
		while (true) {
			System.out.print("Enter your move [WASD?]: ");

			// Don't warn about un-closed System.in Scanner.
			// Don't close System.in or it won't read user input.
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			
			if (input.length() > 0) {
				switch (input.toLowerCase().charAt(0)) {
				case 'a': return MoveDirection.MOVE_LEFT;
				case 's': return MoveDirection.MOVE_DOWN;
				case 'd': return MoveDirection.MOVE_RIGHT;
				case 'w': return MoveDirection.MOVE_UP;
				
				// Dvorak support ('A' is the same on both keyboards) 
				case 'o': return MoveDirection.MOVE_DOWN;
				case 'e': return MoveDirection.MOVE_RIGHT;
				case ',': return MoveDirection.MOVE_UP;
				
				// Help
				case '?':
					displayDirections();
					continue;
				// Debug support
				case 'm':
					revealBoard();
					continue;
				case 'r':
					game = new MazeGame();
					playGame();
				}
			}
			System.out.println("Invalid move. Please enter just A (left), S (down), D (right), or W (up).");
		}
	}

	private void doCatMoves() {
		game.doThunderMoves();
	}

	private void doWonOrLost() {
		if (game.hasUserWon()) {
			System.out.println("Congratulations! You won!");
			revealBoard();
		} else if (game.hasUserLost()) {
			System.out.println("I'm sorry, you have been eaten!");
			revealBoard();
			System.out.println("GAME OVER; please try again.");
		} else {
			assert false;
		}
	}
	
	public void revealBoard() {
		displayBoard(true);
	}	
}
