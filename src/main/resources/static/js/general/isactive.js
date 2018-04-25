
function checkIfPlayerIsActive(){
	//Ping server that window is focused /blured
	D2O.send("active/"+gameId,!document.hidden);
}

D2O.subscribe("/topic/playeractive/"+gameId,checkIfPlayerIsActive)