package com.dttr.tchat.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.dttr.tchat.dao.ChatDAO;
// import com.dttr.tchat.dao.ChatDAO;
import com.dttr.tchat.dao.MessageDAO;
import com.dttr.tchat.dao.UserinfoDAO;
import com.dttr.tchat.entities.Chat;
import com.dttr.tchat.entities.Message;
import com.dttr.tchat.entities.Userinfo;
import com.dttr.tchat.model.WebsocketMessage;
import com.dttr.tchat.model.WebsocketMessage.Type;
import com.dttr.tchat.service.JwtService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MyHandler extends TextWebSocketHandler {
	Logger logger = LoggerFactory.getLogger(MyHandler.class);

	@Autowired
	private ChatDAO chatHome;
	@Autowired
	private UserinfoDAO userinfoHome;
	@Autowired
	private MessageDAO messageHome;
	@Autowired
	private JwtService jwtService;

	// storage list of user session with key is username(many session for one user)
	private HashMap<String, List<WebSocketSession>> usersSession = new HashMap<>();
	// storage list username is member online of chat , chatid is key
	private HashMap<Integer, List<String>> chatsMembers = new HashMap<>();

	/**
	 * send message called from post request
	 */
	public WebsocketMessage sendMessage(Userinfo userinfo, WebsocketMessage websocketMessage) {
		if (!chatHome.isUserHasChat(userinfo, chatHome.loadChatById(websocketMessage.getChatId()))) {
			return null;
		}
		try {
			websocketMessage = messageProcess(userinfo, websocketMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return websocketMessage;
	}

	/**
	 * update member of chat when a new chat between to user was created
	 */
	public void addContact(Userinfo userinfo1, Userinfo userinfo2, Integer chatId) {
		List<String> ls = new ArrayList<>();
		ls.add(userinfo1.getUsername());
		ls.add(userinfo2.getUsername());
		chatsMembers.put(chatId, ls);
	}

	/**
	 * update member of chat when a new groupchat created
	 */
	public void newGroup(Userinfo userinfo, Integer chatId) {
		List<String> ls = new ArrayList<>();
		ls.add(userinfo.getUsername());
		chatsMembers.put(chatId, ls);
	}

	/**
	 * insert a user to chat
	 */
	public void addUserTochat(Userinfo userinfo, Chat chat) {
		chatsMembers.get(chat.getChatid()).add(userinfo.getUsername());
	}

	// @Override
	// public void afterConnectionEstablished(WebSocketSession session) throws
	// Exception {
	// logger.info("add session");
	// }

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		if (session.getAttributes().get("username") == null) {
			// get username from token
			// first time client need send a token
			String userName = jwtService.getUsernameFromToken(message.getPayload());
			if (userName != null) {
				Userinfo userinfo = userinfoHome.loadUserByUsername(userName);
				if (userinfo != null) {
					// save this session
					session.getAttributes().put("username", userName);
					if (usersSession.get(userName) != null) {
						usersSession.get(userName).add(session);
					} else {
						List<WebSocketSession> lw = new ArrayList<>();
						lw.add(session);
						usersSession.put(userName, lw);
					}
				}

				List<Chat> chats = userinfoHome.chats(userinfo);
				WebsocketMessage mess = new WebsocketMessage();
				mess.setType(Type.ONLINE);
				mess.setSender(userName);
				for (Chat c : chats) {
					// join user in to its chat
					List<String> members = chatsMembers.get(c.getChatid());

					if (members == null) {
						members = new ArrayList<>();
						members.add(userName);
						chatsMembers.put(c.getChatid(), members);
					} else {
						// send online status to another user
						mess.setChatId(c.getChatid());
						messageProcess(userinfo, mess);
						userinfoHome.online(userinfo);

						if (!members.contains(userName)) {
							members.add(userName);
						}
					}
				}

			} else {
				session.sendMessage(new TextMessage("Unauthorized"));
			}
		} else {

			try {
				String username = (String) session.getAttributes().get("username");
				Userinfo userinfo = userinfoHome.loadUserByUsername(username);
				ObjectMapper mapper = new ObjectMapper();
				WebsocketMessage mess = mapper.readValue(message.getPayload(), WebsocketMessage.class);
				if (!chatHome.isUserHasChat(userinfo, chatHome.loadChatById(mess.getChatId()))) {
					return;
				}
				// send users message to another user of chat
				switch (mess.getType()) {
				// case MESSAGE:
				// messageProcess(userinfo, mess);
				// break;
				// case ONLINE:

				// break;
				// case OFFLINE:

				// break;
				case SEEN:
					messageProcess(userinfo, mess);
					messageHome.seen(userinfo.getUserid(), mess.getMessageId());
					break;
				// case TYPING:
				// messageProcess(userinfo, mess);
				// break;
				// case TYPED:
				// messageProcess(userinfo, mess);
				// break;
				default:
					messageProcess(userinfo, mess);
					break;
				}
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private WebsocketMessage messageProcess(Userinfo userinfo, WebsocketMessage mess) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<String> members = chatsMembers.get(mess.getChatId());
		if (members == null) {
			return null;
		} else {
			if (mess.getType() == Type.MESSAGE) {
				Message ms = messageHome.sendMessage(userinfo, mess.getChatId(), mess.getContent());
				mess.setSendTime(ms.getSendtime());
				mess.setMessageId(ms.getMessid());
				mess.setSender(userinfo.getUsername());
				mess.setAvatarUrl(ms.getUserinfo().getAvatarurl());
			}
			mess.setSender(userinfo.getUsername());
			for (String username : members) {
				List<WebSocketSession> wsessions = usersSession.get(username);
				if (wsessions != null && wsessions.size() > 0) {
					for (int i = 0; i < wsessions.size(); i++) {
						WebSocketSession ss = wsessions.get(i);
						if (ss.isOpen())
							ss.sendMessage(new TextMessage(mapper.writeValueAsString(mess)));
						else {
							wsessions.remove(ss);
							i--;
						}
					}
				}
			}
		}
		return mess;
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		try {
			Object obj = session.getAttributes().get("username");
			if (obj != null) {
				Userinfo userinfo = userinfoHome.loadUserByUsername((String) obj);
				logger.info("close session of " + userinfo.getUsername());
				List<WebSocketSession> wss = usersSession.get(userinfo.getUsername());
				wss.remove(session);
				if (wss.size() == 0) {
					usersSession.remove(userinfo.getUsername());
					userinfoHome.offline(userinfo);
				}
				// send offline status to other user
				List<Chat> chats = userinfoHome.chats(userinfo);
				WebsocketMessage mess = new WebsocketMessage();
				mess.setType(Type.OFFLINE);
				mess.setSender(userinfo.getUsername());
				for (Chat c : chats) {
					mess.setChatId(c.getChatid());
					messageProcess(userinfo, mess);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}