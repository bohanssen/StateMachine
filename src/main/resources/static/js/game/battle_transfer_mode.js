var btr = {};

function submitBattleTransfer(){
	D2O.send("battlemobilisationsubmit",btr);
	$('#attackScreen').modal('hide'); 
	$(".attackclose").prop('disabled', false);
}

function adjustTransfer(){
	var slider = $(this);
	var key = slider.attr('id').replace("slider_","");
	$("#sb_"+key).text(slider.attr('max') - slider.val());
	$("#cb_"+key).text(slider.val());
	btr.transfers[key] = slider.val();
}

function createRow(data,unit){
	var row = $("<tr></tr>");
	var title = $("<td></td>").text(unit.name);
	var suplier = $("<td></td>").text(data.suplier +" ");
	var sb = $('<span class="badge"></span>').attr("id","sb_"+unit.key).text(unit.quantity);
	suplier.append(sb);
	
	var slider = $('<input type="range" value="0" min="0" max="100" name="transfer"></input>');
	slider.attr('max',unit.quantity);
	slider.attr('id','slider_'+unit.key);
	slider.on('input change',adjustTransfer);
	
	var consumer = $("<td></td>").text(data.consumer +" ");
	var cb = $('<span class="badge"></span>').attr("id","cb_"+unit.key).text("0");
	consumer.append(cb)
	
	row.append(title);
	row.append(suplier);
	row.append($("<td></td>").html(slider));
	row.append(consumer);
	
	btr.transfers[unit.key] = 0;
	return row;
}

function openBattleTransfer(data){
	btr = {};
	btr.gameId = gameId;
	btr.suplier = data.suplierId;
	btr.consumer = data.consumerId;
	btr.transfers = {};
	$(".attackclose").prop('disabled', true);
	$("#battleTitle").text(storage.dictionary("menu.battle.victoryTitle").replace("{{region}}",data.consumer));
	$("#battleInfo").text(storage.dictionary("menu.battle.victroysummary").replace("{{suplier}}",data.suplier).replace("{{consumer}}",data.consumer));
	$("#resultContainer").html("");
	var group = $('<table class="table table-striped"><tbody></tbody></table>');
	for (var i = 0; i < data.available.length; i++){
		if (data.available[i].quantity === 0){
			continue;
		}
		group.append(createRow(data,data.available[i]));
	}
	$("#unitContainer").html(group);
	var button = $('<button type="button" class="btn btn-success btn-block"></button>').text(storage.dictionary("menu.mobi.submit"));
	button.on("click",submitBattleTransfer);
	$("#unitContainer").append(button);
	$('#attackScreen').modal('show'); 
}
D2O.subscribe("/user/queue/battleTransfer",openBattleTransfer);