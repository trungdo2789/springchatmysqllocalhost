package com.dttr.tchat.model;

import java.util.Date;
import java.util.List;


public class MChat {
	Integer chatId;
	String name;
	boolean isGroup;
	String lastMessage;
	Date lastTime;
	String sender;
	Date seentime;
	String avatarUrl;
	List<String> online;
	public Integer getChatId() {
		return chatId;
	}
	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isGroup() {
		return isGroup;
	}
	public MChat() {
		super();
	}
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	public String getLastMessage() {
		return lastMessage;
	}
	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public Date getSeentime() {
		return seentime;
	}
	public void setSeentime(Date seentime) {
		this.seentime = seentime;
	}

	/**
	 * @return the online
	 */
	public List<String> getOnline() {
		return online;
	}

	/**
	 * @param online the online to set
	 */
	public void setOnline(List<String> online) {
		this.online = online;
	}

	/**
	 * @return the avatarUrl
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * @param avatarUrl the avatarUrl to set
	 */
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	
	

}
