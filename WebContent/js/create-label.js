function onPageLoad() {
	if (sessionStorage.getItem("id") == null) {
		window.location = "index.html";
	}	
	 document.getElementById("labelSideNav").setAttribute("class","active");
	 getAllLabel();
	 getLabelGridInfoService(null);
}

var parentLabelsArray = [];

function getLabelGridInfoService(searchFilter){
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var labelGridInfo = JSON.parse(xhttp.responseText);
			console.log(labelGridInfo);
			printTableData(labelGridInfo);
		}
	};
	
	xhttp.open("POST", "CreateLabelServlet", true);
	if(searchFilter){
		xhttp.setRequestHeader("searchFilter", searchFilter);
	}
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "LABEL_GRID_INFO_SERVICE");
	xhttp.send();
}

function printTableData(gridInfo){
	if(gridInfo){
		var table = ""+
		"<thead>"+
			"<tr>"+
				"<th>Sr No.</th>"+
				"<th>Label Name</th>"+
				"<th>Creation Date</th>"+
				"<th>Action</th>"+
			"</tr>"+
		"</thead>";
		for(var i = 0; i < gridInfo.length; ++i){
			table += "<tbody>"+
				"<tr>"+
					"<td>"+gridInfo[i].srNo+"</td>"+
					"<td>"+gridInfo[i].labelName+"</td>"+
					"<td>"+gridInfo[i].creationDate+"</td>"+
					"<td>"+
						"<button type=\"button\" class=\"btn btn-default btn-danger\" onclick="+editLabelService(gridInfo[i])+" aria-label=\"Left Align\">"+
							"Edit"+
						"</button>&nbsp;&nbsp;"+
						"<button type=\"button\" class=\"btn btn-default btn-danger\" aria-label=\"Left Align\">"+
							"Delete"+
						"</button>"+
					"</td>"+
				"</tr>"+
			"</tbody>";
		}
		document.getElementById("printTable").innerHTML = table;
	}
}

function editLabelService(labelInfo){
	console.log(labelInfo);
}

function getAllLabel(){
	var xhttp = new XMLHttpRequest();
	var parentLabels = [];
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			parentLabelsArray = JSON.parse(xhttp.responseText);
			var selectTag = document.getElementById("parentLabelId");
			var optionTag = document.createElement("option");
			optionTag.text = "";
			selectTag.options.add(optionTag, 0);
			for(var i = 0; i < parentLabelsArray.length; ++i){
				optionTag = document.createElement("option");
				optionTag.text = parentLabelsArray[i].name;
				selectTag.options.add(optionTag, i+1);
			}			
		}
	};
	
	xhttp.open("POST", "CreateLabelServlet", true);
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "GET_ALL_LABEL");
	xhttp.send();
}

function createLabelService() {
	var xhttp = new XMLHttpRequest();
	
	var label = document.getElementById("labelName").value;
	var parentLabelName = document.getElementById("parentLabelId").value;
	var userEmailId = sessionStorage.getItem("email");

	var labelJson = {
		label: label,
		gmailParentLabelId: parentLabelName,
		userEmailId: userEmailId
	}
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			console.log(JSON.parse(xhttp.responseText));
			getLabelGridInfoService(null);
			closeDialog();
		}
	};
	xhttp.open("POST", "CreateLabelServlet", true);
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "CREATE_LABEL_SERVICE");
	xhttp.send(JSON.stringify(labelJson));
}

function clearInputs(){
	document.getElementById("parentLabelId").value = "";
	document.getElementById("labelName").value = "";
}

function closeDialog(){
	$('#myModal').modal('hide');
}