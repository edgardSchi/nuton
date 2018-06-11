package toolBar;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import savingFile.TempSaving;

public class ShowPointsButton extends ToolBarItem {
	
	public ShowPointsButton(ToolBarManager tbm) {
		this.tbm = tbm;
		ToggleButton button = new ToggleButton();
		button.setPrefSize(ToolBarItem.SIZE, ToolBarItem.SIZE);
		button.setMaxSize(SIZE, SIZE);
		button.setMinSize(SIZE, SIZE);
		
		button.setSelected(true);
		
		button.selectedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(button.isSelected()) {
					TempSaving.setShowPoints(true);
				} else {
					TempSaving.setShowPoints(false);
				}
				tbm.getMainController().redraw();
			}
			
		});
		
		
		Image icon = new Image(getClass().getResourceAsStream("/showPointsIcon.png"));	
		ImageView view = new ImageView(icon);
		button.setGraphic(view);
			
		this.node = button;
	}

	@Override
	public void onClick() {
		// TODO Auto-generated method stub
		
	}

}
