function onPageLoad() {
	if (sessionStorage.getItem("id") == null) {
		window.location = "index.html";
	}
	document.getElementById("labelFilterSideNav").setAttribute("class","active");
}

function getValue(id){
	return document.getElementById(id).value;
}

function getCheckboxValue(id){
	return document.getElementById(id).checked;
}

function labelFilterService() {
	var xhttp = new XMLHttpRequest();
	
	var label = getValue("labelName");
	var isEmailFilter = getCheckboxValue("isEmailFilter");
	var emailIds = getValue("emailIds");
	var isSubjectFilter = getCheckboxValue("isSubjectFilter");
	var subjectKeywords = getValue("subjectKeywords");
	var isBodyFilter = getCheckboxValue("isBodyFilter");
	var bodyKeywords = getValue("bodyKeywords");
	
	var deleteFilterJson = {
		label : label,
		isEmailFilter : isEmailFilter,
		emailIds : emailIds,
		isSubjectFilter : isSubjectFilter,
		subjectKeywords : subjectKeywords,
		isBodyFilter : isBodyFilter,
		bodyKeywords : bodyKeywords
	}
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			console.log(JSON.parse(xhttp.responseText));
			alert(xhttp.responseText);
		}
	};
	xhttp.open("POST", "LabelFilterServlet", true);
	xhttp.send(JSON.stringify(deleteFilterJson));
}
