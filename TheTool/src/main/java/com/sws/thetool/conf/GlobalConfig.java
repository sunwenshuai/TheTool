package com.sws.thetool.conf;

import java.awt.Toolkit;
import javax.swing.Icon;
import javax.swing.ImageIcon;
public final class GlobalConfig {
	private GlobalConfig(){};
	private static final String NOIMGPATH= "/noimg.jpg";
	public static final Icon NOIMG=new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			GlobalConfig.class.getResource(NOIMGPATH)));
	public static final int IMGWIDTH=225;
	public static final int IMGHEIGHT=280;
	public static String imgTag="#URL";
	public static String imgModel="<p><img src=\""+imgTag+"\" /></p>\n<p>&nbsp;</p>"; 
	
	
	public static String modelPrefix="<table style=\"text-align:center;\" bgcolor=\"#010101\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"688\">\n<tr><td>\n";
	
	public static String modelSuffix="</td></tr>\n</table>";
}
