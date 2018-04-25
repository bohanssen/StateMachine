var stompClient = null;
var d2o_queues = [];
var d2o_methods = {};
var d2o_pingjob;

var D2O = {
	    "connect" : d2o_connect,
	    "send" : function(queue,request){stompClient.send("/app/"+queue, {}, JSON.stringify(request));},
	    "subscribe" : function(queue,fn){
	    	d2o_methods[queue] = fn; 
	    	d2o_queues.push(queue);},
	    "disconnect" : d2o_disconnect,
	    "postconnect" : null,
	    "predisconnect": null,
	}

function notification(data){$.notify(data.msg, data.level);}
D2O.subscribe("/user/queue/notification",notification);
D2O.subscribe("/topic/notification",notification);

function d2o_callback(raw){
	d2o_methods[raw.headers.destination](JSON.parse(raw.body));
}

function d2o_reconnect(err){
	console.log("stomp error: "+err);
	setTimeout(d2o_connect,500);
}

function d2o_logging(line){
	if (line.indexOf("/ping") === -1){
		console.log(line);
	}
}
function d2o_connect() {
    var socket = new SockJS(baseUri+'websocket');
    stompClient = Stomp.over(socket);
    stompClient.debug = d2o_logging;
    stompClient.connect({}, function () {
        for (var index = 0; index < d2o_queues.length; ++index) {
        	stompClient.subscribe(d2o_queues[index], d2o_callback);
        }     
        if (d2o_pingjob){
        	window.clearInterval(d2o_pingjob);
        }
        d2o_pingjob = window.setInterval(d2o_ping, 1000);
        if (D2O.postconnect !== null){
        	D2O.postconnect();
        	var event = new CustomEvent("stomp_connected");
        	document.dispatchEvent(event);
        }
    },d2o_reconnect);
}

var auth = true;
var missedPings = 0;
function d2o_ping(){
          if (!auth){
                   missedPings += 1;
          }
          if (missedPings === 5){
                   location.reload();
          }
          auth = false;
          D2O.send("ping",{});
}
D2O.subscribe("/user/queue/ping",function(data){ auth =data; missedPings = 0; });

function d2o_disconnect() {
    if (stompClient !== null) {
    	if (D2O.predisconnect !== null){
        	D2O.predisconnect();
        }
        stompClient.disconnect();
    }
}
