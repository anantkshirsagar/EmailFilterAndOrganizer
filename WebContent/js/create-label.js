function onPageLoad() {
	if (sessionStorage.getItem("id") == null) {
		window.location = "index.html";
	}	
	 document.getElementById("labelSideNav").setAttribute("class","active");
}

function createLabelService() {
	var xhttp = new XMLHttpRequest();
	
	var label = document.getElementById("labelName").value;
	var parentLabel = document.getElementById("parentLabel").value;
	var labelJson = {
		label: label,
		parentLabel: parentLabel
	}
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			alert(xhttp.responseText);
			console.log(JSON.parse(xhttp.responseText));
		}
	};
	xhttp.open("POST", "CreateLabelServlet", true);
	xhttp.send(JSON.stringify(labelJson));
}
