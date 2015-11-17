package ca.cmpt213.as2.textui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.cmpt213.as2.model.CellLocation;
import ca.cmpt213.as2.model.CellState;

/**
 * Class to display the Maze in the frame
 * and call other panel classes to add it
 * It also checks if game is finished
 */

public class MazeDisplayer {
	
	final java.net.URL DEAD = getClass().getResource("resources/images/dead.png");
	final java.net.URL PLAYER = getClass().getResource("resources/images/player.png");
	final java.net.URL THUNDER = getClass().getResource("resources/images/thunder.png");
	final java.net.URL COIN = getClass().getResource("resources/images/coin.png");
	final java.net.URL UNREVEALED = getClass().getResource("resources/images/unrevealed.png");
	final java.net.URL WALL = getClass().getResource("resources/images/wall.png");
	final java.net.URL SPACE = getClass().getResource("resources/images/space.png");
	final String DIE_SOUND = "resources/sounds/THUNDER.wav";
	final String WINNING_SOUND = "resources/sounds/WIN_SOUND.wav";
	private JFrame frame;
	public static void main(String[] args){
		new MazeDisplayer();
	}
	
	public MazeDisplayer() {
		System.out.println(COIN);
		ObservableMazeGame game = new ObservableMazeGame();
		frame = new JFrame();
		frame.setTitle("Maze Game");
		frame.setLayout(new BorderLayout());
		frame.add(makeButtonsPanel(game), BorderLayout.NORTH);
		frame.add(makeGridPanel(game), BorderLayout.CENTER);
		frame.add(makeStatusPanel(game), BorderLayout.SOUTH);
		frame.add(new ArrowControl(game), BorderLayout.WEST);
		frame.pack();
		frame.setVisible(true);
	}

	private Component makeGridPanel(final ObservableMazeGame game) {
		final JPanel panel = new GridPane(game).makeGridPanel();
		game.addChangeListener(new ChangeListener() {
			
			@SuppressWarnings("static-access")
			@Override
			public void stateChanged(ChangeEvent arg0) {
				panel.removeAll();
				panel.revalidate();
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
				if (!gameNotWonOrLost(game)){
					if (game.hasUserLost()){
						playSound(DIE_SOUND);
						JOptionPane.showMessageDialog(null, "You Died!");
						frame.dispose();
						new MazeDisplayer();
					} else {
						playSound(WINNING_SOUND);
						JOptionPane.showMessageDialog(null, "You Won!");
						frame.dispose();
						new MazeDisplayer();
					}
				}
			}
		});
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

	private Component makeStatusPanel(final ObservableMazeGame game) {
		final JPanel mainPanel = new JPanel();
		
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(new StatusPane(game).makeRightPanel(), BorderLayout.EAST);
		game.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				mainPanel.removeAll();
				mainPanel.revalidate();
				mainPanel.add(new StatusPane(game).makeRightPanel(), BorderLayout.EAST);
			}
		});
		return mainPanel;
	}

	private Component makeButtonsPanel(final ObservableMazeGame game) {
		JPanel mainPanel = new JPanel();
		
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(new ButtonPane(frame).makeLeftButtons(), BorderLayout.WEST);
		mainPanel.add(new ButtonPane(frame).makeRightButton(), BorderLayout.EAST);
		
		
		return mainPanel;
	}
	
	private boolean gameNotWonOrLost(ObservableMazeGame game) {
		return !game.hasUserWon() && !game.hasUserLost();
	}
	
	static public void playSound(final String sound) {
		new Thread(new Runnable() 
        {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() 
            {
                try
                {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResource(sound));
                    clip = AudioSystem.getClip();
                    clip.open(inputStream);
                    clip.start(); 
                } catch (UnsupportedAudioFileException e) {
    			e.printStackTrace();
                } catch (IOException e) {
    			e.printStackTrace();
                } catch (LineUnavailableException e) {
    			e.printStackTrace();
                }
            }
        }).start();
	}


}
