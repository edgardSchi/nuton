package toolBar;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import savingFile.TempSaving;

public class ShowDistanceButton extends ToolBarItem {
	
	public ShowDistanceButton(ToolBarManager tbm) {
		this.tbm = tbm;
		ToggleButton button = new ToggleButton();
		button.setPrefSize(ToolBarItem.SIZE, ToolBarItem.SIZE);
		button.setMaxSize(SIZE, SIZE);
		button.setMinSize(SIZE, SIZE);
		
		button.selectedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(button.isSelected()) {
					TempSaving.setShowDistance(true);
				} else {
					TempSaving.setShowDistance(false);
				}
				tbm.getMainController().redraw();
			}
			
		});
		
		button.setSelected(false);
		
		Image icon = new Image(getClass().getResourceAsStream("/showDistanceIcon.png"));	
		ImageView view = new ImageView(icon);
		button.setGraphic(view);
			
		this.node = button;
	}

	@Override
	public void onClick() {

	}

}
