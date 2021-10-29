/*******************************************************************************
 * Nuton
 *   Copyright (C) 2018-2019 Edgard Schiebelbein
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *   
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.nuton.toolBar;

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
		//TODO: After fixing settings
		TextInputDialog dialog = new TextInputDialog("" /*+ (int)tbm.getMainController().getSettings().getSchrittweite()*/);
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
			//TODO: After fixing settings
			//tbm.getMainController().getSettings().setSchrittweite(schrittweite);
		} else {

		}
	}

}
