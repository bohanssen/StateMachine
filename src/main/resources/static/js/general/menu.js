var clickedIndex;
var scene;
var invites;
var gameId;

function validateForm(){
	var allinvites = ($('#sendInvites').prop('checked')) ? (invites === $("#inviteSet input[name='invite']:checked").length) : true;
	if (!$("input[name='role']:checked").val() || !allinvites) {
		$("#submitbutton").addClass("disabled");
		$("#joinbutton").addClass("disabled");
	} else {
		$("#submitbutton").removeClass("disabled");
		$("#joinbutton").removeClass("disabled");
	}
}

function joinGame(){
	$("#joinbutton").addClass("disabled");
	var request = {
			gameId : gameId,
			role : $("input[name='role']:checked").val(),
	}
	D2O.send("joingame",request);
	$('#createScene').modal('hide');
}

function submit(){
	$("#submitbutton").addClass("disabled");
	var inviteList = [];
	$.each($("#inviteSet input[name='invite']:checked"),function(){ 
		inviteList.push( $(this).val() );
	});
	var request = {
			key : scene.key,
			role : $("input[name='role']:checked").val(),
			invitees : inviteList
	}
	D2O.send("creategame",request);
	$('#createScene').modal('hide');
}

function searchInvites(){
	var input, filter, list, items, i;
	input = document.getElementById("searchInvites");
	filter = input.value.toUpperCase();
	list = document.getElementById("inviteList");
	items = list.getElementsByTagName("a");
	for (i = 0; i < items.length; i++) {
		try {
			if (items[i].innerHTML.toUpperCase().indexOf(filter) > -1) {
				items[i].style.display = "";
			} else {
				items[i].style.display = "none";
			}
		} catch (Exception) {
			// fail
		}
	}
}

function displayRoleSummary(){
	var idVal = $(this).attr("id");
	$("[name=rolelabel]").removeClass('btn-info');
    $("label[for='"+idVal+"']").addClass('btn-info');
    $("#playerSummary").text($(this).data("summary"));
    validateForm();
}

function displayScenarioOptions(data){
	scene = data[clickedIndex];
	$("#sceneTitle").text(scene.title);
	$("#sceneSummary").text(scene.summary);
	var roles = $('<div class="btn-group"></div>');
	invites = scene.roles.length - 1;
	for (var index = 0; index < scene.roles.length; index++){
		var role = scene.roles[index];
		roles.append( $('<label class="btn btn-primary" name="rolelabel"></label>').text(role.title).attr("for","role"+role.id));
		roles.append( $('<input type="radio" name="role" hidden required/>').attr("id","role"+role.id).val(role.id).data("summary",role.summary).on("click",displayRoleSummary));
	}
	$("#playerSelector").html(roles);
}

function displayInviteInfo(){
	var len = $("#inviteSet input[name='invite']:checked").length;
	if (len === invites){
		$("#inviteSet").notify("Selected all available invites for this game ",{"position":"top","className":"success"});
	} else {
		$("#inviteSet").notify("Select: "+invites,{"position":"top","className":"info"});
	}
}
function validateSelection(){
	var len = $("#inviteSet input[name='invite']:checked").length;
	return (len < invites);
}

function changeSelection(){
	var el = $(this);
	var selected = !$("#"+ el.attr("name")).prop('checked');
	if (!selected || validateSelection()){
		$("#"+ el.attr("name")).prop('checked', selected);
		if (selected){
			el.addClass("active");
		} else {
			el.removeClass("active");
		}
		displayInviteInfo();
	} else {
		$("#inviteSet").notify("Maximum: "+invites,{"position":"top"});
	}
	validateForm();
}

function displayContacts(data){
	var list = $('<ul class="list-group" id="inviteList" ></ul>');
	for (var index = 0; index < data.length; index++) {
		if (data[index].id !== userId){
			var item = $('<a class="list-group-item"></a>').attr("name","invite"+data[index].id).text(data[index].name);
			item.on("click",changeSelection);
			var box = $('<input type="checkbox" name="invite" hidden></input>').attr("id","invite"+data[index].id).val(data[index].id);
			item.append(box);
			list.append(item);
		}
	}
	$("#inviteSet").html(list);
}

function openCreate(){
	clickedIndex = $(this).data("index");
	storage.scenes(displayScenarioOptions);
	storage.contacts(displayContacts);
	$("#inviteContainer").show();
	$("#submitbutton").text("Start Game")
	$("#submitbutton").show();
	$("#joinbutton").hide();
	validateForm();
}

function displayJoinGameDialog(data){
	$("#sceneTitle").text(data.title);
	$("#sceneSummary").text(data.summary);
	var roles = $('<div class="btn-group"></div>');
	for (var index = 0; index < data.roles.length; index++){
		var role = data.roles[index];
		roles.append( $('<label class="btn btn-primary" name="rolelabel"></label>').text(role.title).attr("for","role"+role.id));
		roles.append( $('<input type="radio" name="role" hidden required/>').attr("id","role"+role.id).val(role.id).data("summary",role.summary).on("click",displayRoleSummary));
	}
	$("#playerSelector").html(roles);
	$("#inviteContainer").hide();
	$("#submitbutton").hide();
	$("#joinbutton").show();
}

function showRoles(){
	gameId = $(this).data("id");
	storage.resource(baseUri+"api/roles/"+gameId+"/",displayJoinGameDialog);
}

function loadAvailableScenarios(data){
	for (var index = 0; index < data.length; index++){
		var scene = $('<div class="col-md-4"></div');
		var link = $('<a href="#" data-toggle="modal" data-target="#createScene" class="scenebutton"></a>');
		link.data("index",index);
		link.on("click",openCreate)
		var panel = $('<div class="jumbotron"></div>');
		panel.append( $('<h2></h2>').text(data[index].title) );
		panel.append( $('<p class="crop"></p>').text(data[index].summary) );
		link.html(panel);
		scene.html(link);
		$("#scenarios").append(scene);
	}
}

var mygames = [];
var myinvites = [];
var opengames = [];

function isNumber(n) {
	  return !isNaN(parseFloat(n)) && isFinite(n);
	}

function removeTile(id){
	if (mygames.indexOf(id) !== -1){
		mygames.splice(mygames.indexOf(id), 1);
	}
	if (myinvites.indexOf(id) !== -1){
		myinvites.splice(myinvites.indexOf(id), 1);
	}
	if (opengames.indexOf(id) !== -1){
		opengames.splice(opengames.indexOf(id), 1);
	}
	if (id !== 0){
		$("#tile"+id).remove();
	}
}

function updateGames(data){
	if (isNumber(data)){
		removeTile(data);
	}
	var request = {
		"mygames" 		: mygames,
		"myinvites" 	: myinvites,
		"opengames"		: opengames
	}
	setTimeout(function(){ D2O.send("getmenucontent",request); }, 1000);
}

D2O.subscribe("/topic/updatemenu",updateGames);
document.addEventListener("stomp_connected", updateGames);

function createGameTile(data,ready){
	var game = $('<div class="col-md-4"></div');
	game.attr("id","tile"+data.id);
	var link = $('<a href="#" data-toggle="modal" data-target="#createScene" class="scenebutton"></a></a>');
	if (ready){
		link.attr("data-target","#");
		link.attr("href",baseUri+"secure/game/"+data.id+"/");
	} else {
		link.on("click",showRoles);
		link.data("id",data.id);
	}
	
	var panel = $('<div class="jumbotron"></div>');
	panel.append( $('<h2></h2>').text(data.title) );
	var players = $('<ul class="list-group"></ul>');
	for(var index = 0; index < data.participants.length; index++){
		players.append( $('<li class="list-group-item"></li>').text(data.participants[index]) );
	}
	panel.append(players);
	link.html(panel);
	game.html(link);
	return game;
}

function toggleSection(array,key){
	if (array.length === 0){
		$(key).hide()
	} else {
		$(key).show()
	}
}

function addGames(data){
	var index;
	
	for (index = 0; index < data.mygames.length; index++){
		mygames.push(data.mygames[index].id);
		$('#mygames').append(createGameTile(data.mygames[index],true));
	}
	toggleSection(mygames,"#mygamecontainer");
	
	for (index = 0; index < data.myinvites.length; index++){
		myinvites.push(data.myinvites[index].id);
		$('#myinvites').append(createGameTile(data.myinvites[index]));
	}
	toggleSection(myinvites,"#myinvitecontainer");
	
	for (index = 0; index < data.opengames.length; index++){
		opengames.push(data.opengames[index].id);
		$('#opengames').append(createGameTile(data.opengames[index]));
	}
	toggleSection(opengames,"#opengamescontainer")
}
D2O.subscribe("/user/queue/updatemenu",addGames);

function initializeMenu() {
	$('#inviteList').hide();
	$("#submitbutton").on("click",submit);
	$("#joinbutton").on("click",joinGame);
    $('#sendInvites').change(function() {
      if ($(this).prop('checked')){
    	  $('#inviteList').show();
    	  displayInviteInfo();
      } else {
    	  $('#inviteList').hide();
      }
      validateForm();
    });
    storage.scenes(loadAvailableScenarios);
    storage.contacts(null,true);
}
initfunctions.push(initializeMenu);