package com.dttr.tchat.controller;

import java.security.Principal;
import java.util.List;

import com.dttr.tchat.config.MyHandler;
import com.dttr.tchat.dao.ChatDAO;
import com.dttr.tchat.dao.UserinfoDAO;
import com.dttr.tchat.entities.Chat;
import com.dttr.tchat.entities.Userinfo;
import com.dttr.tchat.model.MChat;
import com.dttr.tchat.model.MListMessage;
import com.dttr.tchat.model.WebsocketMessage;
import com.dttr.tchat.service.FileStorageService;
import com.dttr.tchat.service.JwtService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dttr.tchat.model.UserToGroup;;

@RestController
@RequestMapping("/rest")
public class UserRestController {
	private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);
	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserinfoDAO userService;
	@Autowired
	private ChatDAO chatService;

	@Autowired
	MyHandler myHandler;

	@RequestMapping(value = "/sendmessage", method = RequestMethod.POST)
	public WebsocketMessage sendMessage(Principal principal, @RequestBody WebsocketMessage message) {
		return myHandler.sendMessage(userService.loadUserByUsername(principal.getName()), message);
	}

	////////// USER
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<String> login(@RequestBody Userinfo user) {
		String result = "";
		HttpStatus httpStatus = null;
		try {
			if (userService.checkLogin(user)) {
				result = jwtService.generateTokenLogin(user.getUsername());
				httpStatus = HttpStatus.OK;
			} else {
				result = "Wrong userId and password";
				httpStatus = HttpStatus.BAD_REQUEST;
			}
		} catch (Exception ex) {
			result = "Server Error";
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<String>(result, httpStatus);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<String> createUser(@RequestBody Userinfo user) {
		if (userService.add(user)) {
			return new ResponseEntity<String>("Created!", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>("User Existed!", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/user")
	public Userinfo getUsername(Principal principal) {
		Userinfo user = userService.loadUserByUsername(principal.getName());
		user.setPassword(null);
		return user;
	}

	////////// CONTACT

	@RequestMapping(value = "/addcontact", method = RequestMethod.POST)
	public MChat addContact(Principal principal, @RequestBody String friendName) {
		Userinfo user = userService.loadUserByUsername(principal.getName());
		Userinfo friend = userService.loadUserByUsername(friendName);
		if (friend == null)
			return null;
		MChat mChat = chatService.newChat(user, friend);
		if (mChat != null)
			myHandler.addContact(user, friend, mChat.getChatId());
		return mChat;
	}

	// @RequestMapping(value = "/removecontact", method = RequestMethod.POST)
	// public ResponseEntity<String> removeContact(Principal principal,@RequestBody
	// String friendName) {
	// Userinfo user = userService.loadUserByUsername(principal.getName());
	// Userinfo friend = userService.loadUserByUsername(friendName);
	// if (contactService.removeContact(user, friend)) {
	// return new ResponseEntity<String>("Deleted!", HttpStatus.OK);
	// } else {
	// return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
	// }
	// }
	/////////// CHAT
	@RequestMapping(value = "/newgroup", method = RequestMethod.POST)
	public ResponseEntity<MChat> newGroup(Principal principal, @RequestBody String name) {
		Userinfo user = userService.loadUserByUsername(principal.getName());
		MChat chat = chatService.newGroupChat(user, name);
		if (chat != null) {
			myHandler.newGroup(user, chat.getChatId());
			return new ResponseEntity<MChat>(chat, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<MChat>(chat, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/renamechat/{chatid}", method = RequestMethod.POST)
	public ResponseEntity<String> renameChat(Principal principal, @RequestBody String name,
			@PathVariable("chatid") String chatid) {
		Userinfo user = userService.loadUserByUsername(principal.getName());
		Chat chat = chatService.loadChatById(Integer.valueOf(chatid));
		if (chatService.rename(user, chat, name)) {
			return new ResponseEntity<String>("Renamed to " + name, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/chats", method = RequestMethod.GET)
	public List<MChat> getChats(Principal principal) {
		List<MChat> f = chatService.getChats(userService.loadUserByUsername(principal.getName()));
		return f;
	}

	@RequestMapping(value = "/chat/{chatid}", method = RequestMethod.GET)
	public MChat getChat(Principal principal, @PathVariable("chatid") String chatid) {
		Userinfo user = userService.loadUserByUsername(principal.getName());
		Chat c = chatService.loadChatById(Integer.valueOf(chatid));
		MChat mChat = chatService.getChatInfo(c, user);
		return mChat;
	}
	// //////////MESSAGE
	// @RequestMapping(value = "/sendmessage/{chatid}", method = RequestMethod.POST)
	// public ResponseEntity<String> sendMessage(Principal principal
	// ,@RequestBody String content
	// ,@PathVariable("chatid") String chatid) {
	// Userinfo user = userService.loadUserByUsername(principal.getName());
	// if (messageService.sendMessage(user, Integer.valueOf(chatid), content)!=null)
	// {
	// return new ResponseEntity<String>("send", HttpStatus.OK);
	// } else {
	// return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
	// }
	// }

	@RequestMapping(value = "/messages/{chatid}", method = RequestMethod.GET)
	public MListMessage getMessages(Principal principal, @PathVariable("chatid") String chatid) {
		Userinfo user = userService.loadUserByUsername(principal.getName());
		Chat chat = chatService.loadChatById(Integer.valueOf(chatid));
		return chatService.getMessages(chat, user);
	}

	@PostMapping(value = "/addusertogroup")
	public boolean addUser(Principal principal, @RequestBody UserToGroup userToGroup) {
		Userinfo userinfo = userService.loadUserByUsername(principal.getName());
		Userinfo useradd = userService.loadUserByUsername(userToGroup.getUsername());
		Chat chat = chatService.loadChatById(userToGroup.getChatId());
		if (userinfo == null || useradd == null || chat == null)
			return false;
		boolean b = chatService.addUserToChat(userinfo, useradd, chat);
		if (b)
			myHandler.addUserTochat(useradd, chat);
		return b;
	}

	@GetMapping(value = "/getmember/{chatid}")
	public List<String> getMember(@PathVariable("chatid") Integer chatId) {
		Chat chat = chatService.loadChatById(chatId);
		return chatService.getMember(chat);
	}

	@PostMapping("/uploadAvatar")
	public String uploadFile(@RequestParam("file") MultipartFile file, Principal principal) {
		Userinfo userinfo = userService.loadUserByUsername(principal.getName());
		if (userinfo == null)
			return null;
		String fileName = fileStorageService.storeFile(file);

		String fileDownloadUri = "/downloadFile/" + fileName;
		userService.changeAvatarUrl(userinfo, fileDownloadUri);
		return ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(fileName).toUriString();
	}

}
