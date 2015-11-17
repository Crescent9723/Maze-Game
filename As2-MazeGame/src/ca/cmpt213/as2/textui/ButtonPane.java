package ca.cmpt213.as2.textui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Class to create buttons on the top of the maze
 * It generates buttons and return these components
 */


public class ButtonPane {
	private JFrame frame;
	final java.net.URL DEAD = getClass().getResource("resources/images/dead.png");
	final java.net.URL PLAYER = getClass().getResource("resources/images/player.png");
	final java.net.URL THUNDER = getClass().getResource("resources/images/thunder.png");
	final java.net.URL COIN = getClass().getResource("resources/images/coin.png");
	final java.net.URL UNREVEALED = getClass().getResource("resources/images/unrevealed.png");
	final java.net.URL WALL = getClass().getResource("resources/images/wall.png");
	final java.net.URL SPACE = getClass().getResource("resources/images/space.png");
	public ButtonPane(JFrame frame){
		this.frame = frame;
	}
	
	public JPanel makeRightButton() {
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
		rightPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		JButton aboutButton = new JButton("About");
		aboutButton.setFocusable(false);
		aboutButton.setSize(100, 30);
		
		aboutButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = "Game written by Justin Choi, 2014. \n Assignment for CMPT 213 \n \n" +
						"Images by Everaldo Coelho http://www.everaldo.com \n" +
						"(In the Crystal Project Application set) \n" +
						"Sounds from Freesound Org. http://www.freesound.org";
				JOptionPane.showMessageDialog(null, message, "About Author", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(PLAYER));
			}
		});
		rightPanel.add(aboutButton);
		return rightPanel;
	}
	public JPanel makeLeftButtons() {
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
		leftPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JButton newGameButton = new JButton("New Game");
		newGameButton.setFocusable(false);
		newGameButton.setSize(100, 30);
		newGameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int check = JOptionPane.showConfirmDialog(null, 
						"Do you really want to abandon the current game?", 
						"New Game Message", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.INFORMATION_MESSAGE, 
						new ImageIcon(PLAYER));
				if (check == JOptionPane.YES_OPTION){
					frame.dispose();
					new MazeDisplayer();
				} else {
					
				}
			}
		});
		JButton helpButton = new JButton("Help...");
		helpButton.setFocusable(false);
		helpButton.setSize(100, 30);
		helpButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = "Welcome to Escape ThunderLand! \n" +
						"You need to collect all the coins to escape from the Thunderland. \n" +
						"But weather is horrible in this world and there are thunders coming toward you \n" +
						"Avoid and collect all the coins to escape from the Thunderland \n" +
						"Moves with arrow keys";
				JOptionPane.showMessageDialog(null, 
						message, 
						"About Game", 
						JOptionPane.INFORMATION_MESSAGE, 
						new ImageIcon(COIN));
			}
		});
		leftPanel.add(newGameButton);
		leftPanel.add(helpButton);
		return leftPanel;
	}

}
