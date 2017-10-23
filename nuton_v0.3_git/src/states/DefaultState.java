package states;

import java.util.ArrayList;

import application.MainController;
import application.Point;
import javafx.scene.input.MouseEvent;

public class DefaultState extends State{
	
	public DefaultState(MainController mainController) {
		mainController.getFertigBtn().setDisable(true);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(MouseEvent e) {
		System.out.println("DEFAULT STATE");
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
	public ArrayList<Point> getPoints() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
