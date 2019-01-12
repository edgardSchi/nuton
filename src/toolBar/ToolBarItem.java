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
package toolBar;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;

public abstract class ToolBarItem {
	
	public static final int SIZE = 25;
	protected ToolBarManager tbm;
	protected Node node;
	
	public Node getNode() {
		return node;
	};
	
	public abstract void onClick();
	
	protected Button createButton() {
		Button button = new Button();
		node = button;
		button.setPrefWidth(SIZE);
		button.setPrefHeight(SIZE);
		button.setMaxSize(SIZE, SIZE);
		button.setMinSize(SIZE, SIZE);
		return button;
	}
	
	protected ToggleButton createToggleButton() {
		ToggleButton button = new ToggleButton();
		node = button;
		button.setPrefWidth(SIZE);
		button.setPrefHeight(SIZE);
		button.setMaxSize(SIZE, SIZE);
		button.setMinSize(SIZE, SIZE);
		return button;
	}
	
	
}
