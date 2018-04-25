var selectedUnit;

function undeploy(e){
	var request = {
		gameId : gameId,
		unitKey : e.detail.unit,
		region : e.detail.region
	};
	D2O.send("undeployunit",request);
}

function selectUnit(){
	selectedUnit = $(this).attr("id");
	$("#selection").text($(this).attr("name"));
}

function deploy(e){
	if (selectedUnit){
		var request = {
			gameId : gameId,
			unitKey : selectedUnit,
			region : e.detail
		};
		D2O.send("deployunit",request);
	}
}

function unasignedUnits(data){
	var html = $("<div></div>").text(storage.dictionary("menu.units"));
	html.append( $("<p></p>").text(storage.dictionary("menu.unit.selected")).append( $("<span id='selection'></span>")) );
	var group = $('<div class="btn-group-vertical btn-block"></div>');
	for (var i = 0; i < data.length; i++){
		var button = $('<button type="button" class="btn btn-success btn-block"></button>').text(data[i].name + " ");
		var buble = $('<span class="badge"></span>').text(data[i].quantity);
		buble.attr("id","count"+data[i].key);
		button.append(buble);
		button.attr("id",data[i].key);
		button.attr("name",data[i].name);
		button.on("click",selectUnit);
		group.append(button);
	}
	html.append(group);
	$("#userInterface").html(html);
	document.addEventListener("region_clicked", deploy);
	document.addEventListener("division_clicked", undeploy);
}
D2O.subscribe("/user/queue/units",unasignedUnits);

function unasignedUnitsUpdate(data){
	var total = 0;
	for (var k in data){
		if (data[k]){
			total += data[k];
			$("#count"+k).text(data[k]);
			$("#"+k).show();
		} else {
			$("#"+k).hide();
		}
	}
	if (total === 0){
		document.removeEventListener("region_clicked", deploy);
		document.removeEventListener("division_clicked", undeploy);
	}
}
D2O.subscribe("/user/queue/unitsUpdate",unasignedUnitsUpdate);