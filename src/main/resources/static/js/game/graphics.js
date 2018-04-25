var style = {fill: "#ddd",stroke: "#aaa","stroke-width": 1,"stroke-linejoin": "round",cursor: "pointer"};
var hoverStyle = {fill: "#A8BED5"};
var animationSpeed = 250;
var x ,y ,w, h;
var mapContainer;
var map;
var selectedInfo;

var scale = 1;
function zoomIn(){
	map.setSize($("#map").width()*1.05,$("#map").height()*1.05);
}

function zoomOut(){
	map.setSize($("#map").width()*0.95,$("#map").height()*0.95);
}

function normal(){
	map.setSize("90vw","90vh");
}

var scrolling = 0;
var spyelement;
function scroll(){
	clearInterval(scrolling);
	if (spyelement.hasClass("left-edge-spy")){
		scrolling = setInterval(function(){ $('#scrollspy').scrollLeft( $('#scrollspy').scrollLeft() - 10); }, 50);
	} else if (spyelement.hasClass("right-edge-spy")){
		scrolling = setInterval(function(){ $('#scrollspy').scrollLeft( $('#scrollspy').scrollLeft() + 10); }, 50);
	} else if (spyelement.hasClass("top-edge-spy")){
		scrolling = setInterval(function(){ $('#scrollspy').scrollTop( $('#scrollspy').scrollTop() - 10); }, 50);
	} else if (spyelement.hasClass("bottom-edge-spy")){
		scrolling = setInterval(function(){ $('#scrollspy').scrollTop( $('#scrollspy').scrollTop() + 10); }, 50);
	}
}

$(document).ready(function() {
	$('#scrollspy').on( 'DOMMouseScroll mousewheel', function ( event ) {
		  if( event.originalEvent.detail > 0 || event.originalEvent.wheelDelta < 0 ) {
			  zoomOut();
		  } else {
			  zoomIn();
		  }
		  return false;
	});
	
	$(".edge-spy").hover(function(){
		spyelement = $(this);
		scroll();
	},
	function(){
		clearInterval(scrolling);
	});
});

function unitButtonClicked(){
	var event = new CustomEvent("division_clicked",{ 
		'detail': {	
			region : $(this).data("region"), 
			unit : $(this).data("unit"),
			name : $(this).data("name")}
		});
	document.dispatchEvent(event);
}


function displayInfo(region){
	selectedInfo = region.id;
	var data = storage.session("territory_"+region.id);
	var html = $("<div></div>").html( $("<h4></h4>").text(region.name));
	if (data){
		if (data.hasTown){
			html.append( $("<h5></h5>").text(storage.dictionary("menu.town.name")) );
		}
		var group = $('<div class="btn-group-vertical btn-block"></div>');
		for (var i = 0; i < data.divisions.length; i++){
			if (data.divisions[i].quantity === 0){
				continue;
			}
			var button = $('<button type="button" class="btn btn-success btn-block"></button>').text(data.divisions[i].name + " ");
			var buble = $('<span class="badge"></span>').text(data.divisions[i].quantity);
			buble.attr("id","count"+data.divisions[i].key);
			button.append(buble);
			button.data("unit",data.divisions[i].key);
			button.data("region",region.id);
			button.data("name",data.divisions[i].name);
			button.on("click",unitButtonClicked);
			if (data.userId !== userId){
				button.addClass("disabled");
			}
			group.append(button);
		}
		html.append(group);
		
		var transfers = $('<ul class="list-group"></ul>');
		for (var i = 0; i < data.transfers.length; i++){
			transfers.append( $('<li class="list-group-item"></li>').text(data.transfers[i].name +" "+storage.dictionary("menu.mobi.transfer")).append( $('<span class="badge"></span>').text(data.transfers[i].quantity) ) )
		}
		html.append(transfers);
	}
	$("#territoryInterface").html(html);
}
function regionClicked(){
	var event = new CustomEvent("region_clicked",{ 'detail': this.id });
	document.dispatchEvent(event);
	storage.region(this.id,displayInfo);
}

function applyHoverStyle(){
	this.animate(hoverStyle, animationSpeed);
	$("#title").text(this.data("params").name);
}

function recoverColor(){
	this.animate({fill: this.data("color")}, animationSpeed);
	$("#title").html(sessionStorage.title);
}

function addBuble(region){
	var bbox = region.getBBox();
	var posx = bbox.x + (bbox.width * 0.4);
	var posy = bbox.y + (bbox.height * 0.4);
	var text = map.text(posx+10, posy+10, "");
	var buble = map.rect(posx, posy, 20, 20, 3);
	text.id = "count"+region.id;
	buble.id = "buble"+region.id;
	text.attr("stroke", "#FFFFFF");
	text.data("count", 0);
	buble.attr("stroke", "#aaa");
	text.toFront();
	text.hide();
	buble.hide();
}

var regionVectors = {};
function drawRegion(data){
	var region = map.path(data.vector);
	region.attr(style);
	region.id = data.id;
	region.data("params",data);
	region.data("color","#ddd");
	region.mouseover(applyHoverStyle);
	region.click(regionClicked);
	region.mouseout(recoverColor);
	addBuble(region);
	regionVectors[data.id] = region;
}

function loadRegions(data){
	x = data.originx; 
	y = data.originy; 
	w = data.width;
	h = data.height;
	mapContainer = document.getElementById("map");
	mapContainer.innerHTML = "";
	map = Raphael(mapContainer);
	map.setViewBox(x,y,w,h,true);
	map.setSize('90vw', '90vh');
	sessionStorage.title = data.title;
	$("#title").text(data.title);
	for (var index = 0; index < data.regionIds.length; index++){
		storage.region(data.regionIds[index],drawRegion)
	}
}

function initializeGraphics() {
	$("#optionWindow").draggable();
	$("#territoryWindow").draggable();
	$("#attackScreen").draggable({
	    handle: ".modal-header"
	});
    storage.scenario(scenarioKey,loadRegions);
}
initfunctions.unshift(initializeGraphics);

//Stomp below this line
function territoryUpdatedEvent(id){
	var event = new CustomEvent("territoryUpdated",{ 
		'detail': id
		});
	document.dispatchEvent(event);
}

function updateRegion(data){
	storage.session("territory_"+data.id,data);
	territoryUpdatedEvent(data.id);
	if (selectedInfo === data.id){
		storage.region(data.id,displayInfo);
	}
	var region = map.getById(data.id);
	region.animate({fill: data.color}, animationSpeed);
	region.data("color",data.color);
	
	var buble = map.getById("buble"+data.id);
	var count = map.getById("count"+data.id);
	
	if (data.hasTown){
		buble.attr("fill","#aaa");
	} else {
		buble.attr("fill","");
	}
	
	if (data.total !== 0){
		count.attr("text",data.total);
		buble.show();
		count.show();
	} else {
		buble.hide();
		count.hide();
	}
}

function initTerritories(data){
	for (var i = 0; i < data.length; i++){
		updateRegion(data[i]);
	}
}
D2O.subscribe("/user/queue/territory",initTerritories);
D2O.subscribe("/topic/territory/"+gameId,updateRegion);

function refreshClient(){
	setTimeout( function(){
		D2O.send("getterritories",gameId);
		D2O.send("boardConnect",gameId);
	},500);
}
D2O.subscribe("/topic/refresh/"+gameId,refreshClient);

function requestTerritories(){
	D2O.send("boardConnect",gameId);
	D2O.send("getterritories",gameId);
}

function displayPlayerInfo(data){
	$("#levelidicator").text(data.level);
	$("#bonusindicator").text(data.bonus);
	var prog = (data.cash === 0) ? 100 : data.cash;
	while (prog > 100){ 
		prog = prog/3;
	}
	$("#cashbar").css({
	    'background-image': 'none',
	    'background-color': data.color,
	    'width' : prog + "%"
	}).text("Gold: " + data.cash);
	$("#aditionalInfo").text(data.info);
	if (data.nickname) {
		$("#turnindicator").text(storage.dictionary("menu.turn").replace("{{user}}",data.nickname));
	}
}

D2O.subscribe("/user/queue/info",displayPlayerInfo);
document.addEventListener("stomp_connected", requestTerritories);