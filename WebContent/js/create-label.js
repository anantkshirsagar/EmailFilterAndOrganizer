function onPageLoad(){
	if(sessionStorage.getItem("id") == null){
		window.location = "index.html";
	}
}
