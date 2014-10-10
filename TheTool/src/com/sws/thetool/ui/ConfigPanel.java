package com.sws.thetool.ui;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sws.thetool.conf.GlobalConfig;
import com.sws.thetool.core.Engine;

public class ConfigPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel signLb=new JLabel("图片地址:");
	private JTextField signTxt=new JTextField(GlobalConfig.imgTag);
	private JLabel modelLabel=new JLabel("图片模板:");
	private JTextArea model=new JTextArea(GlobalConfig.imgModel);
	private JButton checkViewBtn=new JButton("查看效果");
	private JButton exportBtn=new JButton("导出HTML");
	private JButton	clearBtn=new JButton("清屏");
	public ConfigPanel(){
		setLayout(null);
		
		signLb.setBounds(20,25,70,25);add(signLb);
		signTxt.setForeground(Color.RED);signTxt.setEditable(false);
		signTxt.setBounds(100,25,80,25);add(signTxt);
		modelLabel.setBounds(20,65,70,25);add(modelLabel);
		JScrollPane jsp=new JScrollPane(model);
		jsp.setBounds(100,65,200,100);add(jsp);
		checkViewBtn.setBounds(60,185,70,25);add(checkViewBtn);
		exportBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						synchronized (ConfigPanel.this) {
							JFileChooser chooser=new JFileChooser();
							if(chooser.showDialog(MainBoard.getInstance(),"导出HTML")==JFileChooser.APPROVE_OPTION){
								GlobalConfig.imgModel=model.getText();
								Engine.exportHTML(chooser.getSelectedFile().getAbsolutePath());
							}
						}
					}
				}).start();
			}
		});
		exportBtn.setBounds(140,185,80,25);add(exportBtn);
		clearBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainBoard.getInstance().getMonitor().clear();
				MainBoard.getInstance().getImgPanel().addFocus();
			}
		});
		clearBtn.setBounds(230,185,70,25);add(clearBtn);
	}
	
}
