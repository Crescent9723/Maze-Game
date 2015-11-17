package ca.cmpt213.as2.textui;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class to display information
 * about total number of coin to collect
 * and coin collected now
 */

public class StatusPane {
	private ObservableMazeGame game;
	public StatusPane(ObservableMazeGame game){
		this.game = game;
	}
	public JPanel makeRightPanel() {
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
		rightPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		JLabel myLabel = new JLabel("Collected coins: " + game.getNumberCoinCollected() + " of " + game.getNumberCoinToCollect());
		rightPanel.add(myLabel);
		return rightPanel;
	}

}
