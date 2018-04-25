D6.setBaseUrl(baseUri+'images/dice/');
var agressorId = 0;
var defendorId = 0;
var battlename = "";
var battlerequest = {};

function DICE_callback(total, info, results){
	battlerequest.result = results.splice(0,battlerequest.dice);
	D2O.send("battlerequest",battlerequest);
}

function rollDice(){
	$(".battlebutton").prop('disabled', true);
	closeBattleMode();
	var element = $(this);
	battlerequest = {
			type : "attack",
			gameId : gameId,
			agressorId : agressorId,
			defendorId : defendorId,
			dice : element.data("dice"),
			unitKey : element.data("unit")
	}
	D6.diceToShow(element.data("dice"));
	D6.roll();
}

function loadBattleUnits(){
	var data = storage.session("territory_"+agressorId);
	var group = $('<div class="btn-group-vertical btn-block"></div>');
	for (var i = 0; i < data.divisions.length; i++){
		if (data.divisions[i].quantity === 0){
			continue;
		}
		var button = $('<button type="button" class="btn btn-success btn-block battlebutton"></button>').text(data.divisions[i].name + " ");
		button.append("&nbsp;&nbsp;").append( $('<span class="badge"></span>').text(storage.dictionary("menu.battle.available") + ": "+ data.divisions[i].available) );
		button.append("&nbsp;&nbsp;").append( $('<span class="badge"></span>').text(storage.dictionary("menu.battle.resting") + ": "+ data.divisions[i].resting) );
		button.append("&nbsp;&nbsp;").append( $('<span class="badge"></span>').text(storage.dictionary("menu.battle.as") + ": "+ data.divisions[i].attack) );
		button.data("unit",data.divisions[i].key);
		button.data("dice",data.divisions[i].attack);
		button.on("click",rollDice);
		if (data.divisions[i].available === 0){
			button.prop('disabled', true);
		}
		group.append(button);
	}
	$("#unitContainer").html(group);
}

function refreshBattleUnits(e){
	if (e.detail === agressorId){
		loadBattleUnits();
	}
}

function startBattle(){
	$("#battleTitle").text(storage.dictionary("menu.battleTitle").replace("{{region}}",battlename));
	$("#battleInfo").text(storage.dictionary("menu.battle.atack"));
	$("#resultContainer").html("");
	loadBattleUnits();
}

function handleSelection(region){
	var data = storage.session("territory_"+region.id);
	if(data.userId === userId){
		$("#atacker").text(region.name);
		agressorId = region.id;
	} else {
		$("#opponent").text(region.name);
		battlename = region.name;
		defendorId = region.id;
	}
	if (agressorId !== 0 && defendorId !== 0){
		$("#atackbutton").removeClass("disabled");
	}
}

function selectRegion(e){
	storage.region(e.detail,handleSelection);
}

function closeBattleMode(){
	$("#userInterface").html("");
	document.removeEventListener("region_clicked", selectRegion);
	document.removeEventListener("territoryUpdated", refreshBattleUnits);
}

function stopBattleMode(){
	D2O.send("stopbattlemode",gameId);
}

function openAttackmode(){
	var html = $("<div></div>");
	var list = $('<ul class="list-group"></ul>');
	var region = $('<li class="list-group-item"></li>').text(storage.dictionary("menu.attacker"));
	region.attr("id","atacker");
	var opponent = $('<li class="list-group-item"></li>').text(storage.dictionary("menu.defendor"));
	opponent.attr("id","opponent");
	list.append(region).append(opponent);
	html.append(list);
	
	html.append( $('<button id="atackbutton" type="button" data-toggle="modal" data-target="#attackScreen" class="btn btn-success btn-block disabled"></button>').text(storage.dictionary("menu.atackbuton")).on("click",startBattle) );
	html.append( $('<button type="button" class="btn btn-success btn-block"></button>').text(storage.dictionary("menu.skipbattle")).on("click",stopBattleMode) ); 
	$("#userInterface").html(html);
	D6.diceToShow(0);
	document.addEventListener("region_clicked", selectRegion);
	loadBattleUnits();
}

D2O.subscribe("/user/queue/battlemode",openAttackmode);
