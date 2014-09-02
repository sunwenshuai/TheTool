package com.sws.thetool;

import javax.swing.UIManager;

import com.sws.thetool.ui.MainBoard;

public class TheTool {
	public static void main(String[] args) {
		
		MainBoard app=null;
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
			app=MainBoard.getInstance();
			app.setVisible(true);
		} catch (Exception e) {
			app.exit();
			e.printStackTrace();
		}
	}
}
