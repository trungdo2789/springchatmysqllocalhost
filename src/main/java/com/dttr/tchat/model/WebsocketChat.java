package com.dttr.tchat.model;

import java.util.List;

public class WebsocketChat {
	Integer chatId;
	List<String> usernames;

	/**
	 * @return the chatId
	 */
	public Integer getChatId() {
		return chatId;
	}

	/**
	 * @param chatId the chatId to set
	 */
	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	/**
	 * @return the usernames
	 */
	public List<String> getUsernames() {
		return usernames;
	}

	/**
	 * @param usernames the usernames to set
	 */
	public void setUsernames(List<String> usernames) {
		this.usernames = usernames;
	}
}
