package states;

import javafx.scene.input.MouseEvent;

public abstract class State {

	public abstract void init();
	public abstract void onClick(MouseEvent e);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	
}
