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
package application;

import java.io.InputStream;
import java.net.URL;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class UpdateChecker {
	
	private String data;
	private String address;
	private String currentVersion;
	
	public UpdateChecker(String address, String currentVersion) {
		this.address = address;
		this.currentVersion = currentVersion;
	}
	
	public boolean readData() {
		try {
			URL url = new URL(address);
			
			InputStream stream = null;
			
			stream = url.openStream();
			
			int i = 0;
			StringBuffer buffer = new StringBuffer("");
			
			while(i != -1) {
				i = stream.read();
				
				buffer.append((char) i);
			}
			data = buffer.toString();
			System.out.println(data);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String getVersion() {
		return data.substring(data.indexOf("[version]") + 9, data.indexOf("[/version"));
	}
	
	public String getChange() {
		return data.substring(data.indexOf("[change]") + 8, data.indexOf("[/change]"));
	}
	
	private void createDialog() {
		String newline = System.getProperty("line.separator");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Neues Update verfügbar!");
		alert.setHeaderText("Ein neues Update ist verfügbar");

		Label label = new Label("Changelog:");

		
		String changelog = "Version " + getVersion() + newline + newline + getChange();
		TextArea textArea = new TextArea(changelog);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);
		alert.setContentText("Eine neue Version von Nuton ist verfügbar.");
		alert.showAndWait();
	}
	
	public void checkVersion() {
		System.out.println(getVersion().replaceAll(".", ""));
		if(!getVersion().equals(currentVersion)) {
			createDialog();
		} else {
			newestVersion();
		}
	}
	
	private void newestVersion() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Keine Updates gefunden.");
		alert.setHeaderText("Es konnten keine neuen Updates gefunden werden.");

		alert.setContentText("Nuton befindet sich auf dem neusten Stand.");
		alert.showAndWait();
	}
	
	public void errorDialog() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Updates konnten nicht gelesen werden.");
		alert.setHeaderText("Verbindung zum Server fehlgeschlagen.");

		alert.setContentText("Es konnte keine Verbindung zum Server hergestellt werden. Versuchen Sie es später erneut oder überprüfen Sie die Interneteinstellungen.");
		alert.showAndWait();
	}

}
