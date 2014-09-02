package com.sws.thetool.ui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.sws.thetool.conf.GlobalConfig;
import com.sws.thetool.core.Engine;

public class ImgPanel extends JPanel implements ItemListener,ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JLabel urlLb=new JLabel("网址:");
	private JTextField urlTxt=new JTextField(20);
	private JButton runBtn=new JButton("搜图");
	
	private JButton prevBtn=new JButton("上一张");
	private JLabel img=new JLabel(GlobalConfig.NOIMG);
	private JButton nextBtn=new JButton("下一张");
	private JButton addBtn=new JButton("添加此图片");
	private JLabel countLb=new JLabel("已经添加了0张图片");
	
	private JLabel widthLb=new JLabel("宽度:");
	private JTextField widthTxt=new JTextField("688");
	private JRadioButton clockRd=new JRadioButton("锁定宽度",true);
	private JRadioButton defaultRd=new JRadioButton("默认宽度");
	
	
	public ImgPanel(){
		setLayout(null);
		urlLb.setBounds(25,25,40,25);
		add(urlLb);
		urlTxt.setBounds(65,25,265,25);
		add(urlTxt);
		runBtn.addActionListener(this);
		runBtn.setBounds(340,25,50,25);add(runBtn);
		prevBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new LoadImgRunnable(LoadImgRunnable.PREV)).start();
			}
		});
		prevBtn.setBounds(25,150,60,25);add(prevBtn);
		
		img.addMouseListener(new ImgMouseListener());
		img.setBounds(95,60,GlobalConfig.IMGWIDTH,GlobalConfig.IMGHEIGHT);add(img);
		
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new LoadImgRunnable(LoadImgRunnable.NEXT)).start();
			}
		});
		nextBtn.setBounds(330,150,60,25);add(nextBtn);
		//图片配置
		JPanel sizePanel=new JPanel();
		sizePanel.setLayout(null);
		sizePanel.setBounds(35,355,340,80);
		sizePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"图片配置"));
		add(sizePanel);
		
		clockRd.addItemListener(this);clockRd.setSelected(true);
		defaultRd.addItemListener(this);
		ButtonGroup bg=new ButtonGroup();
		bg.add(clockRd);bg.add(defaultRd);
		clockRd.setBounds(25,30,100,25);sizePanel.add(clockRd);
		defaultRd.setBounds(125,30,100,25);sizePanel.add(defaultRd);
		
		widthLb.setBounds(230,30,30,25);sizePanel.add(widthLb);
		widthTxt.setBounds(260,30,40,25);sizePanel.add(widthTxt);
		
		setBtnsEdnable(false);
		//添加按钮
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton btn=(JButton) e.getSource();
				if(btn.getText().equals("添加此图片"))
					Engine.addCurrentImage();
				else if(btn.getText().equals("从已添加中删除"))
					Engine.removeCurrentImage();
				countLb.setText("已经添加了"+Engine.getSelectedCount()+"张图片");
			}
		});
		addBtn.setForeground(Color.RED);addBtn.setBounds(100,450,100,30);add(addBtn);
		countLb.setBounds(210,450,140,30);add(countLb);
	}
	//载入图片
	private void loadImg(Icon icon){
		img.setIcon(icon);
	}
	
	private void setBtnsEdnable(boolean enabled){
		prevBtn.setEnabled(enabled);nextBtn.setEnabled(enabled);
	}
	//对外公开几个控件
	public int getWidthConfig(){
		return Integer.valueOf(widthTxt.getText());
	}
	public void setWidthConfig(int width){
		widthTxt.setText(String.valueOf(width));
	}
	public boolean isDefaultSize(){
		return defaultRd.isSelected();
	}
	public void setDefaultSize(boolean isDefault){
		if(isDefault)defaultRd.setSelected(true);
		else clockRd.setSelected(true);
	}
	public void imgExist(boolean exist){
		if(exist)
			addBtn.setText("从已添加中删除");
		else
			addBtn.setText("添加此图片");
	}
	/**
	 * 默认多选框监听方法
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {	
		JRadioButton btn = (JRadioButton) e.getItemSelectable();// 返回事件的产生程序
	       if (btn.getText().equals("锁定宽度")&&btn.isSelected()){
	    	   widthTxt.setEnabled(true);
	       }
	       else{
	    	   widthTxt.setEnabled(false);
	       }
	}
	/**
	 * 搜索按钮监听方法
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Engine.setWeb(urlTxt.getText().trim());
				if(Engine.getTotalCount()>0){
					new Thread(new LoadImgRunnable(LoadImgRunnable.NEXT)).start();
					setBtnsEdnable(true);
				}
				else
					img.setIcon(GlobalConfig.NOIMG);
			}
		}).start();
	}
	/**
	 * 载入图片线程
	 * @author sws
	 */
	private class LoadImgRunnable implements Runnable{
		public static final int PREV=-1;
		public static final int NEXT=1;
		private int type;
		public LoadImgRunnable(int _type){
			type=_type;
		}
		@Override
		public void run() {
			synchronized (ImgPanel.this) {
				setBtnsEdnable(false);
				try {
					if(type==NEXT){
						loadImg(Engine.nextImage());
					}else if(type==PREV){
						loadImg(Engine.prevImage());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				setBtnsEdnable(true);
			}
		}
		
	}
	private static class ImgMouseListener extends MouseAdapter{
		private static JDialog imageDialog=new JDialog(MainBoard.getInstance(),true);
		private static JLabel sourceImageLb=new JLabel();
		private static JScrollPane pane=new JScrollPane(sourceImageLb);
		static{
			pane.getVerticalScrollBar().setUnitIncrement(20);
			imageDialog.add(pane);
			imageDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		}
		@Override
		public void mouseClicked(MouseEvent e) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
					ImageIcon sourceImg=Engine.currentSource();
					if(sourceImg!=null){
						int screenW = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
						int screenH = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
						int imgW=sourceImg.getIconWidth()+40;
						int imgH=sourceImg.getIconHeight()+40;
						if(imgW>screenW)imgW=screenW;
						if(imgH>screenH)imgH=screenH;
						imageDialog.setBounds((screenW/2)-(imgW/2),(screenH/2)-(imgH/2),imgW,imgH);
						sourceImageLb.setIcon(sourceImg);
						imageDialog.setVisible(true);
					}
				}
//			}).start();
//		}
	}
}
