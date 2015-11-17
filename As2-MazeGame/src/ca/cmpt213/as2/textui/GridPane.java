package ca.cmpt213.as2.textui;

import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.cmpt213.as2.model.CellLocation;
import ca.cmpt213.as2.model.CellState;

/**
 * Class to create GridPane for each grids
 * for the each cell in the maze
 */

public class GridPane {

	final java.net.URL DEAD = getClass().getResource("resources/images/dead.png");
	final java.net.URL PLAYER = getClass().getResource("resources/images/player.png");
	final java.net.URL THUNDER = getClass().getResource("resources/images/thunder.png");
	final java.net.URL COIN = getClass().getResource("resources/images/coin.png");
	final java.net.URL UNREVEALED = getClass().getResource("resources/images/unrevealed.png");
	final java.net.URL WALL = getClass().getResource("resources/images/wall.png");
	final java.net.URL SPACE = getClass().getResource("resources/images/space.png");
	private ObservableMazeGame game;
	public GridPane(ObservableMazeGame game) {
		this.game = game;
	}
	@SuppressWarnings("static-access")
	public JPanel makeGridPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(15, 20));
		for (int i = 0 ; i < game.getMazeHeight() ; i++){
			for (int j = 0 ; j < game.getMazeWidth() ; j++){
				JLabel myLabel = new JLabel();
				CellLocation cell = new CellLocation(j, i);
				CellState state = game.getCellState(cell);
				if (game.isMouseAtLocation(cell) && game.isThundertAtLocation(cell)) {
					myLabel.setIcon(getScaleImageIcon(new ImageIcon(DEAD), 45, 45));
				} else if (game.isMouseAtLocation(cell)) {
					myLabel.setIcon(getScaleImageIcon(new ImageIcon(PLAYER), 45, 45));
				} else if (game.isThundertAtLocation(cell)) {
					myLabel.setIcon(getScaleImageIcon(new ImageIcon(THUNDER), 45, 45));
				} else if (game.isCoinAtLocation(cell)) {
					myLabel.setIcon(getScaleImageIcon(new ImageIcon(COIN), 45, 45));
				} else if (state.isHidden()) {
					myLabel.setIcon(getScaleImageIcon(new ImageIcon(UNREVEALED), 45, 45));
				} else if (state.isWall()) {
					myLabel.setIcon(getScaleImageIcon(new ImageIcon(WALL), 45, 45));
				} else {
					myLabel.setIcon(getScaleImageIcon(new ImageIcon(SPACE), 45, 45));
				}
				
				panel.add(myLabel);
			}
		}
		return panel;
	}
	
	static public ImageIcon getScaleImageIcon(ImageIcon icon, int width, int height) {
		return new ImageIcon(getScaledImage(icon.getImage(), width, height));
	}
	
	static private Image getScaledImage(Image srcImg, int width, int height){
		BufferedImage resizedImg = 
				new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(
				RenderingHints.KEY_INTERPOLATION, 
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, width, height, null);
		g2.dispose();
		return resizedImg;
	}

}
