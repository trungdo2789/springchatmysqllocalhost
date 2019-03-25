package com.dttr.tchat.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dttr.tchat.entities.Chat;
import com.dttr.tchat.entities.Message;
import com.dttr.tchat.entities.UserHasChat;
import com.dttr.tchat.entities.UserHasChatId;
import com.dttr.tchat.entities.UserHasMessage;
import com.dttr.tchat.entities.Userinfo;
import com.dttr.tchat.model.MChat;
import com.dttr.tchat.model.MListMessage;
import com.dttr.tchat.model.MMessage;

@Repository
@Transactional(rollbackFor = Exception.class)
public class ChatDAO {
	@Autowired
	private EntityManager entityManager;

	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	public Chat getChatOfTwoUser(Userinfo user, Userinfo friend) {
		Session session = getSession();
		String sql = "select c from " + "Userinfo u , UserHasChat uhc , Chat c " + "where uhc.id.userid in (:uid,:fid) "
				+ "and  u.id=uhc.id.userid " + "and c.id=uhc.id.chatid " + "and c.isgroup=" + false + " group by c "
				+ "having count(*)=2";
		List<Chat> chats = session.createQuery(sql, Chat.class).setParameter("uid", user.getUserid())
				.setParameter("fid", friend.getUserid()).getResultList();
		if (chats.size() > 0) {
			return chats.get(0);
		}
		return null;
	}

	public MChat newChat(Userinfo userinfo, Userinfo friend) {
		Session session = getSession();
		Chat c = getChatOfTwoUser(userinfo, friend);
		MChat mChat = null;
		if (c == null) {
			c = new Chat();
			session.save(c);
			UserHasChat uhc1 = new UserHasChat(new UserHasChatId(userinfo.getUserid(), c.getChatid()), c, userinfo);
			session.save(uhc1);
			UserHasChat uhc2 = new UserHasChat(new UserHasChatId(friend.getUserid(), c.getChatid()), c, friend);
			session.save(uhc2);

			mChat = new MChat();
			mChat.setChatId(c.getChatid());
			mChat.setGroup(false);
			mChat.setLastTime(c.getCreateday());
			mChat.setName(friend.getUsername());
			mChat.setAvatarUrl(friend.getAvatarurl());
			if (friend.getIsonline()) {
				List<String> ol = new ArrayList<>(1);
				ol.add(friend.getUsername());
				mChat.setOnline(ol);
			}

		}

		return mChat;

	}

	public MChat newGroupChat(Userinfo userinfo, String name) {
		Session session = getSession();
		try {
			Chat chat = new Chat();
			chat.setName(name);
			chat.setIsgroup(true);
			chat.setLasttime(chat.getCreateday());
			session.persist(chat);
			UserHasChat userHasChat = new UserHasChat(new UserHasChatId(userinfo.getUserid(), chat.getChatid()), chat,
					userinfo);
			session.persist(userHasChat);
			return getChatInfo(chat,userinfo);
		} catch (Exception e) {
		}

		return null;
	}

	public boolean rename(Userinfo userinfo, Chat chat, String name) {
		Session session = getSession();
		List<UserHasChat> userHasChats = session
				.createQuery("from UserHasChat where userinfo=:user and chat=:chat", UserHasChat.class)
				.setParameter("user", userinfo).setParameter("chat", chat).getResultList();
		if (userHasChats.size() == 0)
			return false;
		chat.setName(name);
		session.update(chat);
		return true;
	}

	public boolean removeUserHasMessage(Userinfo userinfo, Chat chat) {
		Session session = getSession();
		List<UserHasChat> userHasChats = session
				.createQuery("from UserHasChat where userinfo=:user and chat=:chat", UserHasChat.class)
				.setParameter("user", userinfo).setParameter("chat", chat).getResultList();
		if (userHasChats.size() == 0)
			return false;
		List<Message> messages = new ArrayList<Message>(chat.getMessages());
		for (Message ms : messages) {
			List<UserHasMessage> userHasMessage = session
					.createQuery("from UserHasMessage where userinfo=:user and message=:message", UserHasMessage.class)
					.setParameter("user", userinfo).setParameter("message", ms).getResultList();
			if (userHasMessage.size() > 0)
				session.delete(userHasMessage.get(0));
		}
		return true;
	}

	public List<MChat> getChats(Userinfo user) {
		Session session = getSession();
		// get all chat of user
		String hql = "select c from UserHasChat uhc,Chat c " + "where uhc.userinfo=:uid " + "and uhc.chat=c "
				+ "order by lasttime desc";
		List<Chat> lChat = session.createQuery(hql, Chat.class).setParameter("uid", user).getResultList();

		List<MChat> fullChats = new ArrayList<MChat>();
		for (Chat c : lChat) {
			fullChats.add(getChatInfo(c, user));
		}
		return fullChats;
	}

	public MChat getChatInfo(Chat c, Userinfo user) {
		Session session = getSession();
		MChat fc = new MChat();
		fc.setChatId(c.getChatid());
		fc.setGroup(c.getIsgroup());
		fc.setLastTime(c.getLasttime());
		// get last message
		List<Message> messages = session.createQuery(
				"select uhm.message from UserHasMessage uhm where uhm.message.chat=:chat and uhm.userinfo=:user order by uhm.message.sendtime desc",
				Message.class).setParameter("chat", c).setParameter("user", user).setMaxResults(1).getResultList();
		if (messages.size() > 0) {
			fc.setLastMessage(messages.get(0).getContent());
			fc.setSender(messages.get(0).getUserinfo().getUsername());
			List<UserHasMessage> userHasMessages = session
					.createQuery("from UserHasMessage where message=:ms and userinfo=:u", UserHasMessage.class)
					.setParameter("ms", messages.get(0)).setParameter("u", user).getResultList();
			if (userHasMessages.size() > 0)
				fc.setSeentime(userHasMessages.get(0).getSeentime());
		}

		// name of chat
		if (c.getName() == null) {
			List<UserHasChat> uhc = session
					.createQuery("from UserHasChat where chat=:chat and userinfo!=:u", UserHasChat.class)
					.setParameter("chat", c).setParameter("u", user).getResultList();
			if (uhc.size() > 0){
				fc.setName(uhc.get(0).getUserinfo().getUsername());
				fc.setAvatarUrl(uhc.get(0).getUserinfo().getAvatarurl());
			}
		} else {
			fc.setName(c.getName());
		}
		List<String> onlines = session.createQuery(
				"select userinfo.username from UserHasChat where chat=:chat and userinfo!=:user and userinfo.isonline=true",
				String.class).setParameter("chat", c).setParameter("user", user).getResultList();
		fc.setOnline(onlines);
		return fc;
	}

	public boolean addUserToChat(Userinfo userinfo, Userinfo useradd, Chat chat) {
		Session session = getSession();
		if (chat.getIsgroup() == false || !isUserHasChat(userinfo, chat))
			return false;
		session.persist(new UserHasChat(new UserHasChatId(useradd.getUserid(), chat.getChatid()), chat, useradd));
		return true;
	}

	public boolean removeUser(Userinfo userinfo, Chat chat) {
		Session session = getSession();
		if (chat.getIsgroup() == false)
			return false;
		List<UserHasChat> userHasChats = session
				.createQuery("from UserHasChat where userinfo=:user and chat=:chat", UserHasChat.class)
				.setParameter("user", userinfo).setParameter("chat", chat).getResultList();
		if (userHasChats.size() == 0)
			return false;
		else
			session.delete(userHasChats.get(0));

		if (chat.getUserHasChats().size() == 1) {
			for (Message ms : chat.getMessages()) {
				ms.getUserHasMessages().clear();
			}
			chat.getMessages().clear();
			session.delete(chat);
		}
		return true;
	}

	public Chat loadChatById(int chatid) {
		Session session = getSession();
		Chat c = session.get(Chat.class, chatid);
		return c;
	}

	public List<String> getMember(Chat chat) {
		Session session = getSession();
		List<String> luser = new ArrayList<>();
		if (chat != null) {
			List<Userinfo> users = session
					.createQuery("select uhc.userinfo from UserHasChat uhc where uhc.chat=:chat", Userinfo.class)
					.setParameter("chat", chat).getResultList();

			for (Userinfo user : users) {
				luser.add(user.getUsername());
			}
		}
		return luser;
	}

	public List<Userinfo> getChatMember(Chat chat) {
		Session session = getSession();
		List<Userinfo> users = null;
		if (chat != null) {
			users = session.createQuery("select uhc.userinfo from UserHasChat uhc where uhc.chat=:chat", Userinfo.class)
					.setParameter("chat", chat).getResultList();

		}
		return users;
	}

	public boolean isUserHasChat(Userinfo userinfo, Chat chat) {
		Session session = getSession();
		List<UserHasChat> users = session
				.createQuery("from UserHasChat where chat=:chat and userinfo=:user", UserHasChat.class)
				.setParameter("chat", chat).setParameter("user", userinfo).getResultList();
		if (users.size() > 0)
			return true;
		return false;
	}

	public MListMessage getMessages(Chat chat, Userinfo userinfo) {
		Session session = getSession();
		String hql = "select ms from Message ms,UserHasMessage uhm " + "where ms.chat=:chat " + "and uhm.message=ms "
				+ "and uhm.userinfo=:user " + "order by ms.sendtime";
		List<Message> messages = session.createQuery(hql, Message.class).setParameter("user", userinfo)
				.setParameter("chat", chat).getResultList();

		if (messages.size() == 0)
			return null;

		hql = "from UserHasMessage where message=:message";
		List<UserHasMessage> uhm = session.createQuery(hql, UserHasMessage.class)
				.setParameter("message", messages.get(messages.size() - 1)).getResultList();
		List<String> seens = new ArrayList<>();
		if (uhm.size() != 0) {
			for (UserHasMessage uh : uhm) {
				if (uh.getSeentime() != null && !uh.getUserinfo().getUsername().equals(userinfo.getUsername())) {
					seens.add(uh.getUserinfo().getUsername());
				}
			}
		}
		MListMessage mListMessage = new MListMessage();
		List<MMessage> mmss=new ArrayList<>();
		for (Message ms : messages) {
			MMessage mms=new MMessage(ms);
			mms.setAvatarUrl(ms.getUserinfo().getAvatarurl());
			mmss.add(mms);
		}
		mListMessage.setMessages(mmss);
		mListMessage.setSeens(seens);
		mListMessage.setChatId(chat.getChatid());
		
		return mListMessage;
	}
}
