package com.sws.thetool.entity;


public class WebImage {
	private String url;
	private boolean defaultSize=true;
	private int width;
	public boolean isDefaultSize() {
		return defaultSize;
	}
	public void setDefaultSize(boolean defaultSize) {
		this.defaultSize = defaultSize;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public boolean equals(Object obj) {
		WebImage other = (WebImage) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
}
