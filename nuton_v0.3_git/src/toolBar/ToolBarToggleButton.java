package toolBar;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import states.State;

public abstract class ToolBarToggleButton extends ToolBarItem {

	protected ToggleButton button;
	
	public ToolBarToggleButton() {
		button = createToggleButton();
		button.selectedProperty().addListener((p, ov, nv) -> {
	       if (button.isSelected()) {
	    	   System.out.println("Fire");
	       }
	    });
	}
	
	@Override
	public void onClick() {
		// TODO Auto-generated method stub
		
	}
	
	public void setIcon(Image icon) {
		ImageView view = new ImageView(icon);
		button.setGraphic(view);
	}
	
	public ToggleButton getToggleButton() {
		return button;
	}
	
	public boolean isSelected() {
		return button.isSelected();
	}
	
	public abstract void addPoint(State state, MouseEvent e);

}
