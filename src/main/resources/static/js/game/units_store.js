
var highlightInterval;
var highlightid;
function highLight(id){
	clearInterval(highlightInterval);
	regionVectors[id].animate({fill: "orange"}, animationSpeed);
	highlightInterval = setInterval(function(){
		var color = regionVectors[id].attrs.fill === "orange" ? regionVectors[id].data("color"): "orange";
		regionVectors[id].animate({fill: color}, animationSpeed);
	},1000);
	regionVectors[highlightid].animate({fill: regionVectors[highlightid].data("color")}, animationSpeed);
}

function selectCapitol(event){
	if ($("#capitol option[value='"+event.detail+"']").length > 0){
		$("#capitol").val(event.detail);
		highLight(event.detail);
		highlightid = event.detail;
	}
}

function closestore(){
	$("#userInterface").html("");
	document.removeEventListener("region_clicked", selectCapitol);
	D2O.send("closestore",gameId);	
	clearInterval(highlightInterval);
	regionVectors[highlightid].animate({fill: regionVectors[highlightid].data("color")}, animationSpeed);
}

function buyunit(){
	var request = {
		gameId : gameId,
		unitKey : $(this).data("key"),
		region : $("#capitol").val()
	};
	D2O.send("buyunit",request);
}

function openStore(data){
	var html = $("<div></div>");
	html.append( $('<label for="capitol"></label>').text(storage.dictionary("menu.capitol")));
	var select = $('<select class="form-control" id="capitol"></select>');
	var value;
	
	for (var k in data.capitols){
		if (data.capitols[k]){
			value = data.capitols[k];
			select.append( $('<option></option>').text(k).attr("value",value) );
		}
	}
	
	highlightid = value;
	highLight(value);
	select.val(value);
	select.on("change",function(){
		highLight(this.value);
	});
	
	var group = $('<div class="btn-group-vertical btn-block"></div>');
	for (var i = 0; i < data.units.length; i++){
		var text = data.units[i].name + " ";
		text += "("+data.units[i].offence+"/"+data.units[i].defence+") ";
		var button = $('<button type="button" class="btn btn-success btn-block"></button>').text(text);
		button.append( $('<span class="badge"></span>').text(data.units[i].cost) );
		button.data("key",data.units[i].key);
		button.on("click",buyunit);
		group.append(button);
	}
	html.append(select);
	html.append(group);
	var close = $('<button type="button" class="btn btn-danger btn-block"></button>').text(storage.dictionary("menu.close"));
	close.on("click",closestore);
	html.append(close);
	$("#userInterface").html(html);
	document.addEventListener("region_clicked", selectCapitol);
}

D2O.subscribe("/user/queue/store",openStore);