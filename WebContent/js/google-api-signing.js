function onSignIn(googleUser) {
	var profile = googleUser.getBasicProfile();
	console.log("google user: " +googleUser.isSignedIn());
	if(googleUser.isSignedIn()){
		console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
		console.log('Name: ' + profile.getName());
		console.log('Image URL: ' + profile.getImageUrl());
		console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
		
		if (typeof(Storage) !== "undefined") {
			console.log("Session storage is supported.");
			sessionStorage.setItem("id", profile.getId());
			sessionStorage.setItem("name", profile.getName());
			sessionStorage.setItem("imageURL", profile.getImageUrl());
			sessionStorage.setItem("email", profile.getEmail());
		}
		window.location = "create-label.html";
	} else {
		alert("User must be signed in...");
	}
}

function renderButton() {
    gapi.signin2.render('my-signin2', {
      'scope': 'profile email',
      'width': 240,
      'height': 50,
      'longtitle': true,
      'theme': 'dark',
      'onsuccess': onSuccess,
      'onfailure': onFailure
    });
}

function onSuccess(googleUser) {
	console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
}
function onFailure(error) {
	console.log(error);
}

function checkIsUserSignedIn() {
	gapi.load('auth2', function () {
		var auth2 = gapi.auth2.init({
		 	client_id: '751114025630-eph7m0437nntkq7sk5t8ps8arqhph275.apps.googleusercontent.com'
		 });
		 if(!auth2.isSignedIn.get()){
			 var auth2Instance = gapi.auth2.getAuthInstance();
			 console.log(" is signed: " +auth2Instance.isSignedIn);
		 }
//		var auth2 = 
//		if(!auth2.isSignedIn.get()){
//			window.location = "index.html";
//		}
		console.log(" Is Signed in : " +auth2.isSignedIn.get());
	});
}