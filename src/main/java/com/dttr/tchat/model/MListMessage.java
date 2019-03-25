package com.dttr.tchat.model;

import java.util.List;


public class MListMessage {
	Integer chatId;
	List<MMessage> messages;
	List<String> seens;

	/**
	 * @return the seens
	 */
	public List<String> getSeens() {
		return seens;
	}
	/**
	 * @param seens the seens to set
	 */
	public void setSeens(List<String> seens) {
		this.seens = seens;
	}

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
	 * @return the messages
	 */
	public List<MMessage> getMessages() {
		return messages;
	}
	/**
	 * @param messages the messages to set
	 */
	public void setMessages(List<MMessage> messages) {
		this.messages = messages;
	}
	
}
