
var mobi_region = 0;
var mobi_unit = "";

function clearMobiSelection(){
	mobi_region = 0;
	mobi_unit = "";
	$("#source").text(storage.dictionary("menu.mobi.src"));
	$("#unit").text(storage.dictionary("menu.mobi.unit"));
}

function selectRegionForMobilisation(data){
	if (data.id === mobi_region) {
		clearMobiSelection();
	} else if (mobi_region === 0) {
		mobi_region = data.id;
		$("#source").text(data.name);
	} else {
		var request = {
				gameId : gameId,
				unitKey : mobi_unit,
				source : mobi_region,
				target : data.id
		}
		D2O.send("mobilisationrequest",request);
	}
}

function mobi_region_select(e){
	storage.region(e.detail,selectRegionForMobilisation);
}

function selectUnitMobi(e){
	mobi_unit = e.detail.unit;
	$("#unit").text(e.detail.name);
}

function mobiReset(){
	D2O.send("mobilisationreset",gameId);
}

function mobiSubmit(){
	D2O.send("mobilisationsubmit",gameId);
	$("#userInterface").html("");
	document.removeEventListener("region_clicked", mobi_region_select);
	document.removeEventListener("division_clicked", selectUnitMobi);
}


function openMobilisationmode(){
	mobi_region = 0;
	mobi_unit = "";
	var html = $("<div></div>");
	var list = $('<ul class="list-group"></ul>');
	var region = $('<li class="list-group-item"></li>').text(storage.dictionary("menu.mobi.src"));
	region.attr("id","source");
	var unit = $('<li class="list-group-item"></li>').text(storage.dictionary("menu.mobi.unit"));
	unit.attr("id","unit");
	list.append(region).append(unit);
	html.append(list);
	
	html.append( $('<button type="button" class="btn btn-success btn-block"></button>').text(storage.dictionary("menu.mobi.clear")).on("click",clearMobiSelection) );
	html.append( $('<button type="button" class="btn btn-success btn-block"></button>').text(storage.dictionary("menu.mobi.reset")).on("click",mobiReset) );
	html.append( $('<button type="button" class="btn btn-success btn-block"></button>').text(storage.dictionary("menu.mobi.submit")).on("click",mobiSubmit) ); 
	
	$("#userInterface").html(html);
	document.addEventListener("region_clicked", mobi_region_select);
	document.addEventListener("division_clicked", selectUnitMobi);
}

D2O.subscribe("/user/queue/openmobilisationmode",openMobilisationmode);