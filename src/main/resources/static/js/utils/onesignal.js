console.log("Web push not available in development environment");
var OneSignal = window.OneSignal || [];
OneSignal.push(["init", {
      appId: onesignalId,
      autoRegister: true,
      notifyButton: {
        enable: true
      }
}]);

var pushId = "";
OneSignal.push(function() {               
  OneSignal.getUserId().then(function(userId) {
	  pushId =  userId;
  });
});

var tags = {};
tags["appId"] = appId;
tags[appId] = userId;
tags[appId+"Role"] = role;
OneSignal.push(["sendTags", tags])