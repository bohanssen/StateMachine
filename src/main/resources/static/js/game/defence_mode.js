var battlerequest = {};

function rollDefenceDice(){
	$(".battlebutton").prop('disabled', true);
	var element = $(this);
	battlerequest = {
			type : "defence",
			gameId : gameId,
			dice : element.data("dice"),
			unitKey : element.data("unit")
	}
	D6.diceToShow(element.data("dice"));
	D6.roll();
}

function loadDefenceUnits(territoryId){
	var data = storage.session("territory_"+territoryId);
	var group = $('<div class="btn-group-vertical btn-block"></div>');
	for (var i = 0; i < data.divisions.length; i++){
		if (data.divisions[i].quantity === 0){
			continue;
		}
		var button = $('<button type="button" class="btn btn-success btn-block battlebutton"></button>').text(data.divisions[i].name + " ");
		button.append("&nbsp;&nbsp;").append( $('<span class="badge"></span>').text(storage.dictionary("menu.battle.available") + ": "+ data.divisions[i].available) );
		button.append("&nbsp;&nbsp;").append( $('<span class="badge"></span>').text(storage.dictionary("menu.battle.resting") + ": "+ data.divisions[i].resting) );
		button.append("&nbsp;&nbsp;").append( $('<span class="badge"></span>').text(storage.dictionary("menu.battle.ds") + ": "+ data.divisions[i].defence) );
		button.data("unit",data.divisions[i].key);
		button.data("dice",data.divisions[i].defence);
		button.on("click",rollDefenceDice);
		if (data.divisions[i].available === 0){
			button.prop('disabled', true);
		}
		group.append(button);
	}
	$("#unitContainer").html(group);
}

function setName(data){
	$("#battleTitle").text(storage.dictionary("menu.defenceTitle").replace("{{region}}",data.name));
}

function openDefencemode(territoryId){
	storage.region(territoryId,setName);
	$("#battleInfo").text(storage.dictionary("menu.battle.defence"));
	loadDefenceUnits(territoryId);
	$("#resultContainer").html("");
	$('#attackScreen').modal('show'); 
}

D2O.subscribe("/user/queue/defencemode",openDefencemode);

function displayBattleresult(data){
	var html = $("<div></div>");
	var list = $('<ul class="list-group"></ul>');
	list.append( $('<li class="list-group-item"></li>').text(data.victor +": "+ data.vresult) );
	list.append( $('<li class="list-group-item"></li>').text(data.loser +": "+ data.lresult) );
	html.append(list);
	
	var bar = $('<div class="progress"></div>');
	bar.append( $('<div class="progress-bar progress-bar-success" role="progressbar"></div>').attr("style","width:"+data.vpercent+"%").text(data.victor) );
	bar.append( $('<div class="progress-bar progress-bar-danger" role="progressbar"></div>').attr("style","width:"+data.lpercent+"%").text(data.loser) );
	html.append(bar);
	$("#resultContainer").html(html);
}
D2O.subscribe("/user/queue/battleresult",displayBattleresult);