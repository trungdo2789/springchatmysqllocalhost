package com.dttr.tchat.model;

public class UserToGroup{
    String username;
    Integer chatId;

    public UserToGroup() {
        
    }
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
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

}