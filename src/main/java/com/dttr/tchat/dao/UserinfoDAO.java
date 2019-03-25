package com.dttr.tchat.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dttr.tchat.entities.Chat;
import com.dttr.tchat.entities.Userinfo;

@Repository
@Transactional(rollbackFor = Exception.class)
public class UserinfoDAO {

	@Autowired
	private EntityManager entityManager;
	
	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public boolean add(Userinfo user) {
		Session session=getSession();
		Userinfo userinfo=loadUserByUsername(user.getUsername());
		if(userinfo==null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			session.persist(user);
			return true;
		}
		return false;
	}
	public boolean checkLogin(Userinfo user) {
		Userinfo userinfo = loadUserByUsername(user.getUsername());
		if (userinfo != null && passwordEncoder.matches(user.getPassword(), userinfo.getPassword()))
			return true;
		return false;
		
	}
	public Userinfo login(Userinfo userinfo) {
		Session session=getSession();
		List<Userinfo> user =session
				.createQuery("from Userinfo where username=:username and password=:password",Userinfo.class)
				.setParameter("username", userinfo.getUsername())
				.setParameter("password", userinfo.getPassword())
				.getResultList();
		if(user.size()>0)return user.get(0);
		return null;
	}

	public void online(Userinfo userinfo) {
		userinfo.setIsonline(true);
		getSession().update(userinfo);
	}

	public void offline(Userinfo userinfo) {
		userinfo.setIsonline(false);
		getSession().update(userinfo);
	}

	public Userinfo loadUserByUsername(String username) {
		Session session=getSession();
		List<Userinfo> userinfo = session.createQuery("from Userinfo where username=:username", Userinfo.class)
				.setParameter("username", username).getResultList();
		if (userinfo.size() > 0)
			return userinfo.get(0);
		return null;
	}

	public List<Chat> chats(Userinfo userinfo){
		List<Chat> chats=getSession()
			.createQuery("select uhc.chat from UserHasChat uhc where uhc.userinfo=:userinfo",Chat.class)
			.setParameter("userinfo", userinfo)
			.getResultList();
		return chats;
	}
	public void changeAvatarUrl(Userinfo userinfo,String url){
		userinfo.setAvatarurl(url);
		getSession().saveOrUpdate(userinfo);
	}
}
