var initfunctions = [];

initfunctions.push(D2O.connect);

window.onload = function () { 
	for (var i = 0; i < initfunctions.length; i++) {
		initfunctions[i]();
	}
}

window.onbeforeunload = function(){
    D2O.disconnect();
    return null;
}