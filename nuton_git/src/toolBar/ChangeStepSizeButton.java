package toolBar;

import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class ChangeStepSizeButton extends ToolBarButton {

	public ChangeStepSizeButton(ToolBarManager tbm) {
		this.tbm = tbm;
		
	}
	
	@Override
	public void onClick() {
		TextInputDialog dialog = new TextInputDialog("" + (int)tbm.getMainController().getSettings().getSchrittweite());
		dialog.setTitle("Schrittweite ändern");
		dialog.setHeaderText(null);
		dialog.setContentText("Neue Schrittweite (ms): ");
		
		dialog.getEditor().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				int i = 1;
				if (!newValue.matches("\\d*")) {
					dialog.getEditor().setText(newValue.replaceAll("[^\\d]", "1"));
				} else {
					if (!newValue.isEmpty()) {
						i = Integer.parseInt(newValue);
					}						
				}
								
				if (i > 10000) {
					dialog.getEditor().setText(newValue.replaceAll(newValue, "10000"));
				}
				
				if (i == 0) {
					dialog.getEditor().setText(newValue.replaceAll(newValue, "1"));
				}
				
				if (newValue.isEmpty()) {
					dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
				} else {
					dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
				}
			}
			
		});
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			int schrittweite = Integer.parseInt(dialog.getEditor().getText());
			tbm.getMainController().getSettings().setSchrittweite(schrittweite);
		} else {

		}
	}

}
