package com.sws.thetool.core;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.sws.thetool.conf.GlobalConfig;
import com.sws.thetool.entity.WebImage;
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
import com.sws.thetool.ui.MainBoard;


public class Engine {
	private static Engine INSTANCE=new Engine();
	private static List<String> imgUrls=new ArrayList<String>();
	private static List<ImageIcon> imagesSource=new ArrayList<ImageIcon>();
	private static List<Icon> imagesCache=new ArrayList<Icon>();
	private static List<WebImage> selectedImgs=new ArrayList<WebImage>();
	
	private static String web;
	private static int currentIndex=-1;
	
	private static HttpClient client=HttpClientBuilder.create().build();
	private static final String IMGURL_REG = "<img[^>]*src=\"([^(>\")]*.jpg[^(>\")]*)?\"([^>]*)?>";
	private static final String DESCURL_REG = "\"descUrl\":\"([^\"]*)\"";
	private Engine(){}
	
	private static void imageFilter(){
		try {
			HttpGet method=new HttpGet(web);
			HttpResponse response=client.execute(method);
			if(response.getStatusLine().getStatusCode()==200){
				StringBuilder sb=new StringBuilder();
				BufferedReader reader=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String buff=null;
				while((buff=reader.readLine())!=null)
					sb.append("\n"+buff);
				parseImage(sb.toString());
			}else
				MainBoard.getInstance().getMonitor().append("http状态码异常!请确认网站是否正常运行!");
		} catch (Exception e) {
			MainBoard.getInstance().getMonitor().append("连接失败!请确认正确地址!");
			e.printStackTrace();
		}
	}
	/**
	 * 过滤图片 ！！！！
	 */
	private static void parseImage(String resp){
		Matcher descMatcher=Pattern.compile(DESCURL_REG).matcher(resp);
		if(descMatcher.find()){
			web=descMatcher.group(1);
			imageFilter();
			return;
		}
		MainBoard.getInstance().getMonitor().append("连接成功!正在过滤图片...");
		Matcher matcher = Pattern.compile(IMGURL_REG).matcher(resp); 
		while(matcher.find())
			imgUrls.add(matcher.group(1));
		MainBoard.getInstance().getMonitor().append("共找到"+imgUrls.size()+"张图片。");
		for(int i=0;i<imgUrls.size();i++){
			imagesCache.add(null);
			imagesSource.add(null);
		}
	}
	//缩放图片方法
	private static Icon zoomImage(ImageIcon image){
		int width=image.getIconWidth();
		int height=image.getIconHeight();
		if(width>GlobalConfig.IMGWIDTH||height>GlobalConfig.IMGHEIGHT){
			float fDegree;
			int zoomW,zoomH;
			if(width-GlobalConfig.IMGWIDTH>height-GlobalConfig.IMGHEIGHT){
				fDegree=width/GlobalConfig.IMGWIDTH;
				zoomW=GlobalConfig.IMGWIDTH;
				zoomH=(int)(height/fDegree);
			}
			else{
				fDegree=height/GlobalConfig.IMGHEIGHT;
				zoomW=(int)(width/fDegree);
				zoomH=GlobalConfig.IMGHEIGHT;
			}
			image=new ImageIcon(image.getImage().getScaledInstance(zoomW,zoomH,Image.SCALE_FAST));
		}
		return image;
	}
	
	//单例
	public static Engine getInstance(){
		return INSTANCE;
	}
	//设置网址
	public static synchronized void setWeb(String _web){
		clear();
		web=_web;
		MainBoard.getInstance().getMonitor().append("开始连接"+_web);
		imageFilter();
	}
	//图片切换
	public static Icon nextImage(){
		if(imgUrls.size()<1){ 
			return GlobalConfig.NOIMG;
		}
		if(currentIndex>=(getTotalCount()-1)){
			MainBoard.getInstance().getMonitor().append("已经是最后一张!");
			return imagesCache.get(currentIndex);
		}
		MainBoard.getInstance().getMonitor().append("载入图片("+(currentIndex+1)+"/"+getTotalCount()+")");
		//如果图片缓存中没有下一张图片  就去缓存
		if(imagesCache.get(currentIndex+1)==null){
			try {
				ImageIcon image=new ImageIcon(new URL(imgUrls.get(currentIndex+1)));
				//放入原图片
				imagesSource.set(currentIndex+1, image);
				//放入缩放后的图片到缓存
				imagesCache.set(currentIndex+1,zoomImage(image));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		WebImage image=new WebImage();image.setUrl(imgUrls.get(currentIndex+1));
		initImgUI(image);
		return imagesCache.get(++currentIndex);
	}
	//根据选择的图片初始化imgPane的参数
	private static void initImgUI(WebImage wi){
		boolean exis=selectedImgs.contains(wi);
		MainBoard.getInstance().getImgPanel().imgExist(exis);
		if(exis){
			wi=selectedImgs.get(selectedImgs.indexOf(wi));
			MainBoard.getInstance().getImgPanel().setDefaultSize(wi.isDefaultSize());
			if(!wi.isDefaultSize())
				MainBoard.getInstance().getImgPanel().setWidthConfig(wi.getWidth());
		}
	}
	public static Icon prevImage(){
		if(imgUrls.size()<1){ 
			return GlobalConfig.NOIMG;
		}
		if(currentIndex<1){ 
			MainBoard.getInstance().getMonitor().append("已经是第一张!");
			return imagesCache.get(currentIndex);
		}
		MainBoard.getInstance().getMonitor().append("载入图片("+(currentIndex+1)+"/"+getTotalCount()+")");
		//如果图片缓存中没有上一张图片  就去缓存
		if(imagesCache.get(currentIndex-1)==null){
			try {
				ImageIcon image=new ImageIcon(new URL(imgUrls.get(currentIndex-1)));
				imagesSource.set(currentIndex-1, image);
				imagesCache.set(currentIndex-1,zoomImage(image));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		WebImage image=new WebImage();image.setUrl(imgUrls.get(currentIndex-1));
		initImgUI(image);
		return imagesCache.get(--currentIndex);
	}
	public static synchronized ImageIcon currentSource(){
		return imagesSource.get(currentIndex);
	}
	
	public static int getCurrentIndex(){
		return currentIndex;
	}
	//添加图片
	public static void addCurrentImage(){
		WebImage image=new WebImage();
		image.setUrl(imgUrls.get(currentIndex));
		image.setDefaultSize(MainBoard.getInstance().getImgPanel().isDefaultSize());
		if(!image.isDefaultSize()){
		image.setWidth(MainBoard.getInstance().getImgPanel().getWidthConfig());
		}
		selectedImgs.add(image);
		MainBoard.getInstance().getImgPanel().imgExist(true);
	}
	//移除已经添加的图片
	public static void removeCurrentImage(){
		WebImage image=new WebImage();image.setUrl(imgUrls.get(currentIndex));
		if(selectedImgs.contains(image))
			selectedImgs.remove(currentIndex--);
		MainBoard.getInstance().getImgPanel().imgExist(false);
	}
	//得到图片数量
	public static int getTotalCount(){
		return imgUrls.size();
	}
	public static int getSelectedCount(){
		return selectedImgs.size();
	}
	public static void exportHTML(String fileName){
		if(selectedImgs.size()<1){
			MainBoard.getInstance().getMonitor().append("并没有选择图片!");
			return;
		}
		try {
			File html=new File(fileName+".html");
			if(!html.exists())html.createNewFile();
			FileWriter writer=null;
			try {
				writer=new FileWriter(html);
				//模板前缀
				writer.write(GlobalConfig.modelPrefix);
				for(WebImage image:selectedImgs){
					String imageStr=image.getUrl();
					if(!image.isDefaultSize())
						imageStr+="\" width=\""+image.getWidth();
					writer.write(GlobalConfig.imgModel.replaceAll(GlobalConfig.imgTag,imageStr)+"\n");
				}
				//模板后缀
				writer.write(GlobalConfig.modelSuffix);
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//清理
	public static void clear(){
		web=null;
		imgUrls.clear();
		selectedImgs.clear();
		imagesCache.clear();
		imagesSource.clear();
		System.gc();
		currentIndex=-1;
	}
}
