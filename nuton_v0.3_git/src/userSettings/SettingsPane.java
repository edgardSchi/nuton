package userSettings;

import javafx.scene.layout.AnchorPane;

public abstract class SettingsPane {

	protected AnchorPane pane;
	
	public abstract void confirmSettings();
	
	public void anchorPane(AnchorPane aPane) {
		aPane.setBottomAnchor(pane, 0.0);
		aPane.setRightAnchor(pane, 0.0);
		aPane.setLeftAnchor(pane, 0.0);
		aPane.setTopAnchor(pane, 0.0);
		//aPane.getChildren().setAll(pane);
	}
	
	protected void setPane(AnchorPane aPane) {
		pane = aPane;
	}
	
	public AnchorPane getPane() {
		return pane;
	}
	
}
