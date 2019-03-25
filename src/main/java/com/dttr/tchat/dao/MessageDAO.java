package com.dttr.tchat.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dttr.tchat.entities.Chat;
import com.dttr.tchat.entities.Message;
import com.dttr.tchat.entities.UserHasChat;
import com.dttr.tchat.entities.UserHasMessage;
import com.dttr.tchat.entities.UserHasMessageId;
import com.dttr.tchat.entities.Userinfo;

@Repository
@Transactional(rollbackFor = Exception.class)
public class MessageDAO {

	@Autowired
	private EntityManager entityManager;
	
	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	
	public void remove(int uid,int msid) {
		Session session=getSession();
		Userinfo userinfo=session.get(Userinfo.class, uid);
		Message message=session.get(Message.class, msid);
		UserHasMessage userHasMessage=session.get(UserHasMessage.class, new UserHasMessageId(msid, uid));
		if(userinfo==null||message==null||userHasMessage==null) throw(new NoResultException());
		session.delete(userHasMessage);
		if(message.getUserHasMessages().size()==1)
			session.delete(message);
		
	}
	public void seen(int uid,int msid) {
		Session session=getSession();
		UserHasMessage userHasMessage=session.get(UserHasMessage.class, new UserHasMessageId(msid, uid));
		if(userHasMessage!=null) {
			if(userHasMessage.getSeentime()!=null)
				userHasMessage.setSeentime(new Date());
		}else {
			throw(new NoResultException("userHasMessage not exist"));
		}
	}
	public Message sendMessage(Userinfo user,Integer chatId,String content) {
		Session session=getSession();
		Message ms=new Message();
		Chat chat=session.get(Chat.class, Integer.valueOf(chatId));
		List<UserHasChat> ushc=session.createQuery("from UserHasChat ushc where ushc.chat=:chat and ushc.userinfo=:user",UserHasChat.class)
				.setParameter("chat", chat)
				.setParameter("user", user)
				.getResultList();
		if(ushc.size()==0) return null;
		ms.setUserinfo(user);
		ms.setChat(chat);
		ms.setContent(content);
		ms.setSendtime(new Date());
		session.persist(ms);
		Hibernate.initialize(chat);
		Set<UserHasChat> userHasChats=chat.getUserHasChats();
		for(UserHasChat uhc:userHasChats) {
			UserHasMessage uhm=new UserHasMessage(new UserHasMessageId(ms.getMessid(), uhc.getUserinfo().getUserid())
					, ms, uhc.getUserinfo());
			if(uhc.getUserinfo().getUserid()==user.getUserid()) {
				uhm.setSeentime(new Date());
			}
			session.persist(uhm);
		}
		chat.setLasttime(ms.getSendtime());
		session.update(chat);
		return ms;
	}
	

}
