package ca.cmpt213.as2.textui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import ca.cmpt213.as2.model.MoveDirection;

/**
 * Class to allow users to control
 * player with arrow keys
 */

@SuppressWarnings("serial")
public class ArrowControl extends JPanel {

	final String INVALID_MOVE_SOUND = "resources/sounds/INVALID_MOVE.wav";
	private static final String[] KEYS = {"UP", "DOWN", "LEFT", "RIGHT"};
	private ObservableMazeGame game;
	public ArrowControl(ObservableMazeGame game) {
		this.game = game;
		registerKeyPresses();
	}
	
	public void registerKeyPresses() {
		for (int i = 0; i < KEYS.length; i++) {
			String key = KEYS[i];
			this.getInputMap().put(KeyStroke.getKeyStroke(key), key);
			this.getActionMap().put(key, getKeyListener(key));
		}
	}

	public AbstractAction getKeyListener(final String move) {
		return new AbstractAction() {
			MoveDirection moveDir;
			public void actionPerformed(ActionEvent evt) {
				
				switch(move){
				case "UP":
					moveDir = MoveDirection.MOVE_UP;
					break;
				case "DOWN":
					moveDir = MoveDirection.MOVE_DOWN;
					break;
				case "LEFT":
					moveDir = MoveDirection.MOVE_LEFT;
					break;
				case "RIGHT":
					moveDir = MoveDirection.MOVE_RIGHT;
					break;
				}
				if (game.isValidPlayerMove(moveDir)){
					game.doThunderMoves();
					game.recordPlayerMove(moveDir);
				} else {
					playSound(INVALID_MOVE_SOUND);
				}
			}
		};
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
