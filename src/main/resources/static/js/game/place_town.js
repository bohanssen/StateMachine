var regionId;
function placeTown(){
	var request = {
		gameId : gameId,
		region : regionId
	};
	D2O.send("placetown",request);
}

function getSelectedRegion(data){
	if (storage.session("territory_"+data.id).userId === userId){
		$("#townButton").text(data.name);
		$("#townButton").removeClass("disabled");
		regionId = data.id;
		$("#townInfo").text(storage.dictionary("menu.town.disclaimer"));
	} else {
		$.notify(storage.dictionary("menu.town.error"),"error");
	}
}

function selectRegionForTown(e){
	storage.region(e.detail,getSelectedRegion);
}

function townMenu(data){
	var html = $("<div></div>");
	if (data){
		html.text(storage.dictionary("menu.town"));
		var button = $('<button id="townButton" type="button" class="btn btn-success btn-block disabled"></button>').text("Select region");
		button.on("click",placeTown);
		html.append(button);
		html.append( $("<span id='townInfo'></span>"));
		document.addEventListener("region_clicked", selectRegionForTown);
	} else {
		document.removeEventListener("region_clicked", selectRegionForTown);
	}
	$("#userInterface").html(html);
}
D2O.subscribe("/user/queue/town",townMenu);