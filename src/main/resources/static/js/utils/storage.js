
//Not stored locally
function storage_getResource(uri,callback,args){
	$.getJSON(uri, function(data) {
		callback(data,uri,args);
	});
}

//session
function storage_session(key,value){
	if (value){
		sessionStorage.setItem(gameId+"_"+key, JSON.stringify(value));
	} else {
		return JSON.parse(sessionStorage.getItem(gameId+"_"+key));
	}
}

function storage_retrieve(id,resource,callback) {
	var uri = baseUri+"api/"+resource+"/";
	var key = resource;
	if (id !== null){
		uri += id+"/";
		key += "."+id;
	}
	if (localStorage.getItem(key) === null){
		$.getJSON(uri, function(data) {
			localStorage.setItem(key, JSON.stringify(data));
			callback(data,id,resource);
		});
	} else {
		callback(JSON.parse(localStorage.getItem(key)),id,resource);
	}
}

function storage_getRegion(id,callback){
	storage_retrieve(id,"region",callback);
}

function storage_getScenarios(callback){
	storage_retrieve(null,"scenes",callback);
}

function storage_getScenario(id,callback){
	storage_retrieve(id,"scenario",callback);
}

function storage_getContacts(callback,force){
	if (force) {
		localStorage.removeItem("contacts");
	}
	storage_retrieve(null,"contacts",callback);
}

////// Dictionary //////
if (localStorage.dictionaryloaded){
	//do nothing dictionary already loaded
} else {
	var uri = baseUri+"api/dictionary/";
	$.getJSON(uri, function(data) {
		localStorage.setItem("dictionaryloaded",true);
		for (var key in data){
			if (data[key]){
				localStorage.setItem("dictionary_"+key, data[key]);
			}	
		}
	});
}
function storage_getmessage(key){
	var result = "Not defined";
	var temp = localStorage.getItem("dictionary_"+key);
	if (temp){
		result = temp;
	}
	return result;
}
////////////////////////

var storage = {
	    "region" 	: storage_getRegion,
	    "scenes" 	: storage_getScenarios,
	    "contacts"	: storage_getContacts,
	    "resource"	: storage_getResource,
	    "scenario"	: storage_getScenario,
	    "session"	: storage_session,
	    "dictionary": storage_getmessage
	}

