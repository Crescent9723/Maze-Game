package ca.cmpt213.as2.textui;

import ca.cmpt213.as2.model.MazeGame;

/**
 * Launch the Maze Game with a text UI.
 */
public class MazeGameText {

	public static void main(String[] args) {
		MazeGame game = new MazeGame();
		MazeTextUI ui = new MazeTextUI(game);
		ui.playGame();
	}

}
