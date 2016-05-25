package com.sws.thetool.ui;

import java.awt.Toolkit;

import javax.swing.JFrame;

/*
 *  			   _ooOoo_ 
                  o8888888o 
                  88" . "88 
                  (| -_- |) 
                  O\  =  /O 
               ____/`---'\____ 
             .'  \\|     |//  `. 
            /  \\|||  :  |||//  \ 
           /  _||||| -:- |||||-  \ 
           |   | \\\  -  /// |   | 
           | \_|  ''\---/''  |   | 
           \  .-\__  `-`  ___/-. / 
         ___`. .'  /--.--\  `. . __ 
      ."" '<  `.___\_<|>_/___.'  >'"". 
     | | :  `- \`.;`\ _ /`;.`/ - ` : | | 
     \  \ `-.   \_ __\ /__ _/   .-` /  / 
======`-.____`-.___\_____/___.-`____.-'====== 
                   `=---=' 
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ 
 *   		佛祖保佑       永无BUG 
 */
public class MainBoard extends JFrame {
	private static final long serialVersionUID = 1L;
	private static MainBoard INSTANCE;
	
	private ImgPanel imgPanel=new ImgPanel();
	private ConfigPanel configPanel=new ConfigPanel();
	private Monitor monitor=new Monitor();
	private MainBoard(){
		initPanel();
		initFrame();
	}
	
	private void initFrame(){
		try {
			setSize(DefaultConfig.BOARD_WIDTH,DefaultConfig.BOARD_HEIGHT);
			setResizable(false);
			setIconImage(
					Toolkit.getDefaultToolkit().getImage(
							this.getClass().getResource(DefaultConfig.BOARD_ICON_PATH)));
			setTitle("TheTool");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			//置于屏幕中央
			double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
			double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			setLocation( (int) (width - getWidth()) / 2,(int) (height - getHeight()) / 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void initPanel(){
		setLayout(null);
		imgPanel.setBounds(0,0,400,550);
		add(imgPanel);
		configPanel.setBounds(400,0,400,210);
		add(configPanel);
		monitor.setBounds(420,220,300,260);
		add(monitor);
	}
	public static MainBoard getInstance(){
		if(INSTANCE==null)
			INSTANCE=new MainBoard();
		return INSTANCE;
	}
	
	public void exit(){
		System.exit(1);
	}
	public Monitor getMonitor(){
		return monitor;
	}
	public ImgPanel getImgPanel(){
		return imgPanel;
	}
	/**
	 * MainBoard的内部静态非可变类，用来作MainBoard的默认配置 
	 * @author sws
	 */
	private static final class DefaultConfig{
		//默认主面板宽度
		public static final int BOARD_WIDTH=750;
		//默认主面板高度
		public static final int BOARD_HEIGHT=550;
		//默认图标路径（相对路径）
		public static final String BOARD_ICON_PATH= "/icon.jpg";
	}
}
