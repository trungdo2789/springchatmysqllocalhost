package com.dttr.tchat.model;

import java.util.Date;

public class ShortUserHasMessage {
	private String username;
	private boolean isonline;
	private Date seentime;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isIsonline() {
		return isonline;
	}
	public void setIsonline(boolean isonline) {
		this.isonline = isonline;
	}
	public Date getSeentime() {
		return seentime;
	}
	public void setSeentime(Date seentime) {
		this.seentime = seentime;
	}
	public ShortUserHasMessage(String username, boolean isonline, Date seentime) {
		super();
		this.username = username;
		this.isonline = isonline;
		this.seentime = seentime;
	}
	public ShortUserHasMessage() {
		super();
	}
	
}
