
var groupImage = '/downloadFile/group.svg';
var userImage = '/downloadFile/user.svg';
var loadingImage = '/downloadFile/loading.svg';
var currentChat, preClick, user;
const CACHE_MAX_SIZE = 50;
var listCacheMessage = [];
var host = location.hostname + (location.port ? ':' + location.port : '');
var websocket = new WebSocket("ws://" + host + "/tchat");
var avatarInputfile = document.querySelector('#avatarinput');
var onlines = [];
$('#avatarinput').on('change', changeAvatar);
$(".messages").animate({ scrollTop: $(document).height() }, "fast");

$("#profile-img").click(function () {
	$("#status-options").toggleClass("active");
});

$(".expand-button").click(function () {
	$("#profile").toggleClass("expanded");
	$("#contacts").toggleClass("expanded");
});

$("#status-options ul li").click(function () {
	$("#profile-img").removeClass();
	$("#status-online").removeClass("active");
	$("#status-away").removeClass("active");
	$("#status-busy").removeClass("active");
	$("#status-offline").removeClass("active");
	$(this).addClass("active");

	if ($("#status-online").hasClass("active")) {
		$("#profile-img").addClass("online");
	} else if ($("#status-away").hasClass("active")) {
		$("#profile-img").addClass("away");
	} else if ($("#status-busy").hasClass("active")) {
		$("#profile-img").addClass("busy");
	} else if ($("#status-offline").hasClass("active")) {
		$("#profile-img").addClass("offline");
	} else {
		$("#profile-img").removeClass();
	};

	$("#status-options").removeClass("active");
});
$('#addcontact').click(function () {
	doMessageBox("username:", function (contactName) {
		postJSON('/rest/addcontact', contactName, function (data) {
			if (data == '') {
				doMessageBox("username not exist or already contacted!");
			} else {
				doMessageBox('created!');
				$('#contacts ul:first-child').prepend(loadContact(data));
			}
		});
		// $.post('/rest/addcontact', contactName, function (data, status) {
		// 	if (data == '') {
		// 		doMessageBox("username not exist!");
		// 	} else {
		// 		doMessageBox(JSON.stringify(data));
		// 	}

		// });
	}, 1);
})
$("#search-input").on("keyup", function () {
	var value = $(this).val().toLowerCase();
	$("#contacts .contact").filter(
		function () {
			$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
		}
	);
});
$('#addmember').click(function () {
	doMessageBox("username:", function (contactName) {
		postJSON('/rest/addusertogroup'
			, JSON.stringify({
				'username': contactName
				, 'chatId': currentChat.chatId
			}), function (data) {
				if (data == false) {
					doMessageBox("error");
				} else {
					doMessageBox('Thành công!');
				}
			});
		// $.post('/rest/addusertogroup'
		// 	, JSON.stringify({
		// 		'username': contactName
		// 		, 'chatId': currentChat.chatId
		// 	})
		// 	, function (data, status) {
		// 		if (data == false) {
		// 			doMessageBox("error");
		// 		} else {
		// 			doMessageBox('Thành công!');
		// 		}

		// 	});
	}, 1);
})
$('#getmember').click(function () {
	$.get('/rest/getmember/' + currentChat.chatId
		, function (data, status) {
			doMessageBox(data);
		});
});
$('#newgroup').click(function () {
	doMessageBox("group name:", function (contactName) {
		postJSON('/rest/newgroup'
			, contactName
			, function (data) {
				if (data == '') {
					doMessageBox("error");
				} else {
					doMessageBox('created!');
					$('#contacts ul:first-child').prepend(loadContact(data));
				}
			});
		// $.post('/rest/newgroup', contactName, function (data, status) {
		// 	if (data == '') {
		// 		doMessageBox("error");
		// 	} else {
		// 		doMessageBox(JSON.stringify(data));
		// 	}

		// });
	}, 1);
})
function changeAvatar() {
	if (avatarInputfile.files.length == 0) return;
	// uploadSingleFile(avatarInputfile.files[0]);
	var formData = new FormData();
	formData.append("file", avatarInputfile.files[0]);
	// $.post('/rest/uploadAvatar',formData,function(data,status){
	// 	console.log(JSON.stringify(data));
	// });
	$.ajax({
		url: '/rest/uploadAvatar',
		type: 'POST',
		data: formData,
		processData: false,
		contentType: false,
		beforeSend: function () {
			$('#profile-img').attr('src', loadingImage);
		},
		success: function (data, textStatus, jqXHR) {
			$('#profile-img').attr('src', data);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			console.log('ERRORS: ' + textStatus);
		}

	});
}
function postJSON(url, data, callback) {
	$.ajax({
		url: url,
		type: 'POST',
		data: data,
		headers: {
			'Content-Type': 'application/json; charset=utf-8'
		},
		success: function (data, textStatus, jqXHR) {
			callback(data);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			console.log('ERRORS: ' + textStatus);
		}
	});
}
function newMessage() {
	message = $(".message-input input").val();
	if ($.trim(message) == '') {
		return false;
	}
	let ms = {
		'type': 'MESSAGE'
		, 'chatId': currentChat.chatId
		, 'content': message
	};
	var tempms = $('<li class="sent"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p>' + message + '</p></li>');
	tempms.appendTo($('.messages ul'));
	sendMessage(ms, tempms);
	$('.message-input input').val(null);
	$('#c-' + ms.chatId + ' .preview')[0].innerHTML = '<span>You: </span>' + message;
	$(".messages").animate({ scrollTop: 9999 }, "fast");
};

$('.submit').click(function () {
	newMessage();
});

$(window).on('keydown', function (e) {
	if (e.which == 13) {
		newMessage();
		return false;
	}
});

function getMessagesCached(chatId) {
	for (let index = 0; index < listCacheMessage.length; index++) {
		if (listCacheMessage[index].chatId == chatId)
			return listCacheMessage[index];
	}
	return null;
}
function cache(ms) {
	if (getMessagesCached(ms.chatId) != null) {
		return false;
	}
	if (listCacheMessage.length >= CACHE_MAX_SIZE) {
		listCacheMessage.pop();
	}
	listCacheMessage.push(ms);
	return true;
}
function loadMessages(ms) {
	var direction;
	// console.log(ms.sender + '>>' + ms.content);
	if (ms.sender != user.username) {
		direction = "replies";
	} else {
		direction = "sent";
	}
	let imageUrl;
	if (ms.avatarUrl == null) {
		imageUrl = userImage;
	} else {
		imageUrl = ms.avatarUrl;
	}
	var li = document.createElement('li');
	li.setAttribute('class', direction);
	li.setAttribute('id', 'ms-' + ms.messageId);
	li.innerHTML = '<img src="' + imageUrl + '" alt="" />'
		+ '<p>' + ms.content + '</p>'
	return li;
}
function getMessages(chatId) {
	var messageList = $('#messages ul:first-child');
	messageList.empty();
	let ms = getMessagesCached(chatId);
	if (ms != null) {
		ms.messages.forEach(element => {
			messageList.append(loadMessages(element));
		});
	} else {
		$.get('/rest/messages/' + chatId, function (data, status) {
			if (data.messages != null) {
				cache(data);
				data.messages.forEach(element => {
					messageList.append(loadMessages(element));
				});
			}
		});
	}

}


function chatOnClick(chat, eventSource) {
	currentChat = chat;
	if (preClick != null) {
		$(preClick).removeClass('active');
	}
	preClick = eventSource;
	$(eventSource).addClass('active');
	getMessages(chat.chatId);
	if (chat.group) {
		$('#addmember').show();
		$('#getmember').show();
	}
	else {
		$('#addmember').hide();
		$('#getmember').hide();
	}
	$('#contactname')[0].innerText = chat.name;
	let avatarImage;
	if (chat.avatarUrl == null) {
		avatarImage = chat.group ? groupImage : userImage;
	} else {
		avatarImage = chat.avatarUrl;
	}
	$('#contact-image').attr('src', avatarImage);
	$(".messages").animate({ scrollTop: 9999 }, "fast");
}
function loadContact(c) {
	let status = "", message = "", name = "";
	if (c.online!=null&&c.online.length > 0) {
		status = "online";
	}
	if (c.lastMessage != null) {
		message = c.lastMessage;
	} else {
		message = "</br>";
	}
	if (c.name != null) {
		name = c.name;
	}
	let imageUrl;
	if (c.avatarUrl == null) {
		if (c.group) {
			imageUrl = groupImage;
		} else {
			imageUrl = userImage;
		}
	} else {
		imageUrl = c.avatarUrl;
	}
	var li = document.createElement('li');
	li.setAttribute('class', 'contact');
	li.setAttribute('id', 'c-' + c.chatId);
	li.innerHTML = '<div class="wrap">'
		+ '<span class="contact-status ' + status + '"></span>'
		+ '<img src="' + imageUrl + '" alt="" />'
		+ '<div class="meta">'
		+ '<div class="name">'
		+ '<div class="contactname" style="width:70%;">' + name + '</div>'
		+ '<div class="lasttime" style="width:30%;">' + dateFormat(new Date(c.lastTime)) + '</div>'
		+ '</div>'
		+ '<p class="preview"> ' + message + ' </p>'
		+ '</div>'
		+ '</div>'
	li.onclick = function () {
		chatOnClick(c, li);
	}
	onlines[onlines.length] = {
		chatId: c.chatId,
		members: c.online
	}
	return li;
}

function getChats() {
	let contacts = $('#contacts ul:first-child');
	$.get('/rest/chats', function (data, status) {
		data.forEach(element => {
			// onlines[onlines.length] = {
			// 	chatId: element.chatId,
			// 	members: element.online
			// }
			contacts.append(loadContact(element));
		});
		$('#contacts ul li:first-child').click();
	});
}
function loadProfile() {
	$.get('/rest/user', function (data, status) {
		user = data;
		// console.log(username);
		$('#profile-name').text(user.username);
		$('#profile-img').attr('src', data.avatarurl != null ? data.avatarurl : userImage);
	});
}
$(document).ready(function () {
	$.get("/token", function (data, status) {
		$.ajaxSetup({
			headers: {
				'Authorization': data,
				// 'Content-Type': 'application/json; charset=utf-8'
			}
		});
		loadProfile();
		getChats();
	});
});

function sendMessage(ms, tempms) {
	postJSON('/rest/sendmessage'
		, JSON.stringify(ms)
		, function (data) {
			// console.log('send message post return: ' + JSON.stringify(data));
			$(tempms).remove();
		});
	// $.post('/rest/sendmessage', JSON.stringify(ms), function (data, status) {
	// 	console.log('send message post return: ' + JSON.stringify(data));
	// 	$(tempms).remove();
	// });
}
websocket.onopen = function () {
	$.get("/token", function (data, status) {
		websocket.send(data);
	});
};

function getOnlinesOfChat(chatId) {
	for (let i = 0; i < onlines.length; i++) {
		let element = onlines[i];
		if (element.chatId == chatId)
			return element;
	}
	return null;
}
websocket.onmessage = function (message) {
	var ms = JSON.parse(message.data);
	let online;
	switch (ms.type) {
		case 'MESSAGE':
			let mslist = getMessagesCached(ms.chatId);
			if (mslist != null) {
				mslist.messages[mslist.messages.length] = ms;
			}
			let c = document.getElementById('c-' + ms.chatId);
			if (c == null) {
				let contacts = $('#contacts ul:first-child');
				$.get('/rest/chat/' + ms.chatId, function (data, status) {
					contacts.prepend(loadContact(data));
				});
			} else {
				$('#c-' + ms.chatId + ' .preview')[0].innerText = ms.content;
				$('#c-' + ms.chatId + ' .lasttime')[0].innerText = dateFormat(new Date(ms.sendTime));
				$(c).insertBefore($('#contacts ul li:first-child'));
			}
			if (currentChat.chatId == ms.chatId) {
				$('#messages ul:first-child').append(loadMessages(ms));
				$(".messages").animate({ scrollTop: 9999 }, "fast");
			}
			break;
		case 'ONLINE':
			if (user == null || ms.sender == user.username) return;
			$('#c-' + ms.chatId + ' .contact-status:first-child').addClass('online');
			online = getOnlinesOfChat(ms.chatId);
			if (online == null) {
				onlines[onlines.length] = {
					chatId: ms.chatId,
					members: [ms.sender]
				}
			} else {
				online.members[online.members.length] = ms.sender;
			}
			// console.log(message.data)
			break;
		case 'OFFLINE':
			online = getOnlinesOfChat(ms.chatId);
			if (online == null) return;
			for (let i = 0; i < online.members.length; i++) {
				if (online.members[i] == ms.sender) {
					online.members.splice(i, 1);
				}
			}
			if (online.members.length == 0)
				$('#c-' + ms.chatId + ' .contact-status:first-child').removeClass('online');
			// console.log(message.data)
			break;
		default:
			break;
	}
};
websocket.onclose = function () {
	alert("websocket has closed!");
	location.reload();
};
websocket.onerror = function (message) {
	console.log(message.data)
};

/****************************************DATE FORMAT****************************************************** */
function dateFormat(date) {
	let n = new Date();
	if (date.getFullYear() != n.getFullYear()) {
		return date.getDate() + '/' + date.getMonth() + '/' + date.getFullYear();
	} else if (date.getMonth() != n.getMonth()) {
		return date.getDate() + ' Tháng ' + (parseInt(date.getMonth(), 10) + 1);
	} else if (date.getDay() != n.getDay()) {
		if (date.getDay() == 7) {
			return 'CN';
		} else {
			return 'T' + (date.getDay() + 1);
		}
	} else {
		return date.getHours() + ':' + date.getMinutes();
	}
}
function dateFormatFull(date) {
	let n = new Date(), d;
	if (date.getFullYear() != n.getFullYear()) {
		d = date.getDate() + '/' + date.getMonth() + '/' + date.getFullYear();
	} else if (date.getMonth() != n.getMonth()) {
		d = date.getDate() + ' Tháng ' + (parseInt(date.getMonth(), 10) + 1);
	} else if (date.getDay() != n.getDay()) {
		if (date.getDay() == 7) {
			d = 'CN';
		} else {
			d = 'T' + (date.getDay() + 1);
		}
	} else {
		return date.getHours() + ':' + date.getMinutes();
	}
	return d + ' ' + date.getHours() + ':' + date.getMinutes();
}
/////////////////////upload
// function uploadSingleFile(file) {
//     var formData = new FormData();
//     formData.append("file", file);

//     var xhr = new XMLHttpRequest();
//     xhr.open("POST", "/rest/uploadAvatar");

//     xhr.onload = function() {
//         console.log(xhr.responseText);
//         var response = JSON.parse(xhr.responseText);
//         if(xhr.status == 200) {
// 			console.log(response);
//             // singleFileUploadError.style.display = "none";
//             // singleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
//             // singleFileUploadSuccess.style.display = "block";
//         } else {
// 			console.log("error : " +xhr.status);
//             // singleFileUploadSuccess.style.display = "none";
//             // singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
//         }
//     }

//     xhr.send(formData);
// }

// function uploadMultipleFiles(files) {
//     var formData = new FormData();
//     for(var index = 0; index < files.length; index++) {
//         formData.append("files", files[index]);
//     }

//     var xhr = new XMLHttpRequest();
//     xhr.open("POST", "/rest/uploadAvatar");

//     xhr.onload = function() {
//         console.log(xhr.responseText);
//         var response = JSON.parse(xhr.responseText);
//         if(xhr.status == 200) {
//             // multipleFileUploadError.style.display = "none";
//             // var content = "<p>All Files Uploaded Successfully</p>";
//             // for(var i = 0; i < response.length; i++) {
//             //     content += "<p>DownloadUrl : <a href='" + response[i].fileDownloadUri + "' target='_blank'>" + response[i].fileDownloadUri + "</a></p>";
//             // }
//             // multipleFileUploadSuccess.innerHTML = content;
//             // multipleFileUploadSuccess.style.display = "block";
//         } else {
//             // multipleFileUploadSuccess.style.display = "none";
//             // multipleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
//         }
//     }

//     xhr.send(formData);
// }
