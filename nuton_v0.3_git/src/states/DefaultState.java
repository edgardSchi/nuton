package states;

import application.MainController;
import javafx.scene.input.MouseEvent;

public class DefaultState extends State{
	
	
	public DefaultState(MainController mainController) {
		super(mainController);
		mainController.getFertigBtn().setDisable(true);
	}

	@Override
	public void init() {
		mainController.getSlider().setSnapToTicks(false);
		mainController.getSlider().setValue(0);
	}

	@Override
	public void onClick(MouseEvent e) {
		//System.out.println("DEFAULT STATE");
		//mainController.getToolBarManager().pointButtonEvent(this, e);		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redraw() {
		// TODO Auto-generated method stub
		
	}

	
}
