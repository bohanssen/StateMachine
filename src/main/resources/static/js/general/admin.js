function updateSucces(data){
	$.notify(data.message,data.level);
	$("[id$="+data.key.split("_")[1]+"]").removeClass("btn-success");
	$("[id$="+data.key.split("_")[1]+"]").removeClass("disabled");
	$("[id$="+data.key.split("_")[1]+"]").addClass('btn-danger');
	
	$("#"+data.key).removeClass("btn-danger");
	$("#"+data.key).addClass("btn-success");
	$("#"+data.key).addClass("disabled");
	if (data.alert){
		alert(data.alert);
	}
}
function postData(data){
	$.ajax({
        type: "POST",
        url: baseUri+"api/updateUserRights",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        crossDomain: true,
        dataType: "json",
        success: updateSucces,
        error: function (jqXHR, status) {
            $.notify('fail: ' + status.code,"error");
        }
     });
}

function changeStatus(element){
	var data = {};
	if ($(element).hasClass( 'disabled' )){
		return;
	}
	data.property = element.id.split("_")[0];
	data.id = parseInt(element.id.split("_")[1]);
	data.key = element.id;
	postData(data);
}

function loadDB(){
	$.ajax({
        type: "GET",
        url: baseUri+"api/loadDB",
        contentType: "application/json; charset=utf-8",
        crossDomain: true,
        dataType: "json",
        success: updateSucces,
        error: function (jqXHR, status) {
            $.notify('fail: ' + status.code,"error");
        }
     });
}