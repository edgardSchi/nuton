package states.tracking;

import application.MainController;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import states.State;
import tracking.TrackingManager;

public class TrackingState extends State {
	
	private TrackingManager trackingManager;
	private int clickCount;
	private Task<Void> task;
	private Thread thread;

	public TrackingState(MainController mainController) {
		super(mainController);
		clickCount = 0;
	}

	@Override
	public void init() {
		reset();
	}

	@Override
	public void onClick(MouseEvent e) {
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fertigBtnClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		clickCount = 0;
	}

	@Override
	public void redraw() {
		// TODO Auto-generated method stub
		
	}

}
