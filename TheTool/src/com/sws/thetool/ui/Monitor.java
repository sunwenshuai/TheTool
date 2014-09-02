package com.sws.thetool.ui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class Monitor extends JScrollPane {
	private static final long serialVersionUID = 1L;
	private static JTextArea textArea=new JTextArea("欢迎使用！\t作者:孙文帅");
	public Monitor(){
		super(textArea);
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"控制台"));
		textArea.setForeground(Color.GREEN);
		textArea.setBackground(Color.BLACK);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();  
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
	
	public void clear(){
		textArea.setText("欢迎使用！\t作者:孙文帅");
	}
	public void append(String str){
		textArea.append("\n"+str);
		textArea.setSelectionStart(textArea.getText().length());
	}
}
