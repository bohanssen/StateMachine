var contacts = {};
var unreadCount = 0;
var unreadCounts = {};

function scroll(id){
	var objDiv = document.getElementById("conversationBody"+id);
	objDiv.scrollTop = objDiv.scrollHeight;
}

function searchFunction() {
	var input, filter, list, item, i;
	input = document.getElementById("searchInput");
	filter = input.value.toUpperCase();
	list = document.getElementById("contactsList");
	items = list.getElementsByTagName("a");
	for (i = 0; i < items.length; i++) {
		try {
			if (items[i].innerHTML.toUpperCase().indexOf(filter) > -1) {
				items[i].style.display = "";
			} else {
				items[i].style.display = "none";
			}
		} catch (Exception) {
			// fail
		}
	}
}

function back(){
	$(".conversationcontainer").hide();
	$("#contacts").show();
	$("#searchInput").show();
}

function chatInput(e){
	if(e.which === 13) {
		var input = $(this);
		D2O.send("instantmessage",{target : input.data("id"), message : input.val()});
		input.val("");
	}
}

function scrollAuto(){
	$(this).scrollTop($(this).prop("scrollHeight"));
}

function loadConv(id){
	var key = "#conversation" + id;
	if ( $(key).length === 0) { 
		unreadCounts[id] = 0;
		var conversation = $("<div class='conversationcontainer'></div>").attr("id",key.replace("#",""));
		var backbutton = $('<button type="button" class="btn btn-primary btn-xs"><i class="fa fa-arrow-circle-o-left" aria-hidden="true"></i> Back</button>');
		backbutton.on("click", back);
		backbutton.data("key",key);
		conversation.append(backbutton);
		var body = $("<div class='conversation'></div>").attr("id","conversationBody"+id);
		body.on("click",scrollAuto);
		conversation.append(body);
		$.getJSON(baseUri+"api/conversation/"+id+"/", function(data) {
			for (var index = 0; index < data.length; index++){
				body.prepend($("<p></p>").text(data[index].author.nickname+": "+data[index].message));
			}
		});
		var input = $("<input type='text'></input>");
		input.on("keypress",chatInput);
		input.data("id",id);
		conversation.append( input );
		$("#conversations").append(conversation);
		$(key).hide();
	}
	return key;
}

function conversation(){
	var key = loadConv($(this).data("key"));
	$("#contacts").hide();
	$("#searchInput").hide();
	$(key).show();
	scroll($(this).data("key"));
}

function updateContactlist(){
	var list = $('<div class="list-group" id="contactsList" ></div>');
	list.append($('<a class="list-group-item disabled"></a>').text("offline"));
	Object.keys(contacts).forEach(function (key) {
		var item = $('<a class="list-group-item"></a>').text(contacts[key].username);
		var counter = $("<span class='badge IMB'></span>");
		counter.data("id",key);
		item.append(counter);
		item.on("click", conversation);
		item.data("key",key);
		if (contacts[key].online){
			list.prepend(item);
		} else {
			list.append(item);
		}
	});
	list.prepend($('<a class="list-group-item disabled"></a>').text("online"));
	$("#contacts").html(list);
}

function showChat(){
	$("#chatWindow").show();
	$("#chatButton").hide();
}

function hideChat(){
	$("#chatWindow").hide();
	$("#chatButton").show();
}

function chatStatus(data){
	if (data.id !== userId){
		contacts[data.id] = data;
	}
	if (data.id !== userId && data.online === true && contacts[data.id] && contacts[data.id].online !== true){
		$.notify(data.username + " is online", "info");
	}
	if (data.id === userId && data.online === false){
		D2O.send("subscribe",{});
	}
	updateContactlist()
}

D2O.subscribe("/topic/chatStatus",chatStatus);
D2O.subscribe("/user/queue/chatStatus",chatStatus);

function updateCounters(){
	if (unreadCount !== 0){
		$("#unreadMessages").text(unreadCount);
	} else {
		$("#unreadMessages").text("");
	}
	$(".IMB").each(function(){
		var elem = $(this);
		var count = unreadCounts[elem.data("id")];
		if (count !== 0){
			elem.text(count);
		} else {
			elem.text("");
		}
	});
}

function highlite(data,id){
	new Audio(baseUri+'sounds/ping.mp3').play();
	$.notify(data.author.nickname+": "+data.message, "success");
	if ($("#chatButton").is(":visible")){
		$(".conversationcontainer").hide();
		$("#contacts").hide();
		$("#searchInput").hide();
		$("#conversation"+id).show();
		$("#chatButton").effect("highlight", {}, 3000);
	}
	updateCounters();
}

function appendMessage(data){
	var id = (data.author.id === userId) ? data.receiver.id : data.author.id;
	loadConv(id);
	var message = $("<p></p>").text(data.author.nickname+": "+data.message);
	message.data("mid",data.id);
	message.data("cid",id);
	
	unreadCount++;
	unreadCounts[id]++;
	
	if (data.author.id !== userId){
		highlite(data,id);
		message.addClass("IM");
	}
	window.setTimeout(function(){
		$("#conversationBody"+id).append(message);
		scroll(id);
	},500);
}
D2O.subscribe("/user/queue/pushmessage", appendMessage);

D2O.postconnect = function(){
	D2O.send("subscribe",{});
}

D2O.predisconnect = function(){
	D2O.send("unsubscribe",{});
}

window.setInterval(function(){
	$(".IM").each(function(){
		var elem = $(this);
		if (elem.is(":visible")){
			elem.removeClass("IM");
			D2O.send("acknowledge",{id: elem.data("mid")});
			unreadCount--;
			unreadCounts[elem.data("cid")]--;
			updateCounters();
		}
	});
},500);


D2O.subscribe("/user/queue/changenickname", function(data){ $.notify("Your nickname: "+data.username,"info");nickname = data.username; });

function updateNickname(e){
	if(e.which === 13) {
		D2O.send("changenickname", {nickname : $("#nickname").val()})
		$("#nickname").hide();
	}
}

function showNickname(){
	$("#nickname").val(nickname);
	$("#nickname").show();
}

function initChat(){
	$("#chatWindow").draggable();
	$("#chatButton").on("click",showChat);
	$("#closeChat").on("click",hideChat);
	$("#changeNickname").on("click",showNickname);
	$("#nickname").on("keypress",updateNickname);
}
initfunctions.push(initChat);