package ca.cmpt213.as2.textui;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt213.as2.model.MazeGame;
import ca.cmpt213.as2.model.MoveDirection;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class to decorate the MazeGame class to
 * update the game information using observer pattern
 */

public class ObservableMazeGame extends MazeGame{
	private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
	
	public ObservableMazeGame() {
		super();
	}
	
	@Override
	public void recordPlayerMove(MoveDirection move) {
		super.recordPlayerMove(move);
		notifyListeners();
	}
	
	@Override
	public void doThunderMoves() {
		super.doThunderMoves();
		notifyListeners();
	}
	
	public void addChangeListener(ChangeListener listener){
		listeners.add(listener);
	}
	
	private void notifyListeners() {
		
		ChangeEvent event = new ChangeEvent(this);
        for(ChangeListener listener : listeners) {
                listener.stateChanged(event);
        }
        
	}
	
}
