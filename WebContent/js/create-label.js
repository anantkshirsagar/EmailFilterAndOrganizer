function onPageLoad() {
	if (sessionStorage.getItem("id") == null) {
		window.location = "index.html";
	}	
	 document.getElementById("labelSideNav").setAttribute("class","active");
	 getAllLabel();
	 getLabelGridInfoService(null);
}

//Global variable which are used across the multiple functions
var parentLabelsArray = [];
var header = new Array();
header = ['Sr.No', 'Label Name', 'Creation Date', 'Edit'];

function getLabelGridInfoService(searchFilter){
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var labelGridInfo = JSON.parse(xhttp.responseText);
			console.log(labelGridInfo);
			if(Array.isArray(labelGridInfo) && labelGridInfo.length){
				document.getElementById("showDataTableDiv").style.display = "block";
				document.getElementById("noRecordsFoundDiv").style.display = "none";
				createTable(labelGridInfo);
			} else {
				document.getElementById("showDataTableDiv").style.display = "none";
				document.getElementById("noRecordsFoundDiv").style.display = "block";
			}
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

function createTable(gridInfo) {
	var table = document.createElement('table');
	table.setAttribute('id', 'table'); // SET THE TABLE ID.
	table.setAttribute('class','table table-striped valign');

	var tr = table.insertRow(-1);

	for (var i = 0; i < header.length; i++) {
		var th = document.createElement('th'); // TABLE HEADER.
		th.innerHTML = header[i];
		tr.appendChild(th);
	}

	
	var div = document.getElementById('printTableDiv');
	div.innerHTML = "";
	div.appendChild(table); // ADD THE TABLE TO YOUR WEB PAGE.
	
	addRow(gridInfo);
}

function addRow(gridInfo) {
	var table = document.getElementById('table');

	var rowCount = table.rows.length; // GET TABLE ROW COUNT.

	for(var record = 0; record < gridInfo.length; ++record){
		//Create new row each time
		var tr = table.insertRow(record+1); // TABLE ROW.
		tr = table.insertRow(record+1);

		var td = document.createElement('td');
		td = tr.insertCell(0);
		var srNoCell = document.createTextNode(record+1);
		td.appendChild(srNoCell);
		
		var labelNameTD = document.createElement('td');
		labelNameTD = tr.insertCell(1);
		var labelNameCell = document.createTextNode(gridInfo[record].labelName);
		labelNameTD.appendChild(labelNameCell);
		
		var creationDateTD = document.createElement('td');
		creationDateTD = tr.insertCell(2);
		var creationDateCell = document.createTextNode(gridInfo[record].creationDate);
		creationDateTD.appendChild(creationDateCell);
		
		var editButtonTD = document.createElement('td');
		editButtonTD = tr.insertCell(3);
		var name = gridInfo[record].id + "$" +gridInfo[record].labelName + "$" +gridInfo[record].creationDate;
		var editButton = document.createElement('input');
		editButton.setAttribute('type', 'button');
		editButton.setAttribute('value', 'Edit');
		editButton.setAttribute('name', name);
		editButton.setAttribute('onclick', 'edit(this.name)');
		editButton.setAttribute('class', 'btn btn-default btn-danger');
		editButtonTD.appendChild(editButton);
		
//		var deleteButtonTD = document.createElement('td');
//		deleteButtonTD = tr.insertCell(4);
//		var deleteButton = document.createElement('input');
//		deleteButton.setAttribute('type', 'button');
//		deleteButton.setAttribute('value', 'Delete');
//		deleteButton.setAttribute('onclick', 'edit(this)');
//		deleteButton.setAttribute('class', 'btn btn-default btn-danger');
//		deleteButtonTD.appendChild(deleteButton);	
	}
}

var labelJson = {};
var gmailLabelId = "";

function edit(selectedLabelData){
	labelJson = getLabelJson(selectedLabelData);
	gmailLabelId = getGmailLabelId(labelJson.labelName);
	var finalLabelName = getLabelNameFromLabelJson(labelJson.labelName);
	
	document.getElementById("oldLabelName").value = finalLabelName;
	document.getElementById("editLabelName").value = finalLabelName;
	openDialog("editModal");
}

function getLabelNameFromLabelJson(labelName){
	if(labelName.includes('/')){
		return labelName.split("/")[labelName.split("/").length-1];
	}
	return labelName;
}

function editLabelService(){
	labelJson.labelName = document.getElementById("editLabelName").value;
	labelJson.oldLabelName = document.getElementById("oldLabelName").value;
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			alert(JSON.parse(xhttp.responseText));
			closeDialog("editModal");
			getLabelGridInfoService(null);
			getAllLabel();
		}
	};
	
	xhttp.open("POST", "CreateLabelServlet", true);
	xhttp.setRequestHeader("gmailLabelId", gmailLabelId);
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "EDIT_LABEL_SERVICE");
	xhttp.send(JSON.stringify(labelJson));
}


function getGmailLabelId(labelName){
	if(labelName){
		for(var i = 0; i < parentLabelsArray.length; ++i){
			if(parentLabelsArray[i].name == labelName){
				return parentLabelsArray[i].id;
			}
		}
	}
}

function getLabelJson(selectLabelData){
	var delimeter = "$";
	var split = selectLabelData.split(delimeter);
	return {
		id : split[0],
		labelName : split[1],
		oldLabelName : '',
		creationDate : new Date(split[2])
	};
}

function getAllLabel(){
	parentLabelsArray = [];
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			parentLabelsArray = JSON.parse(xhttp.responseText);
			var selectTag = document.getElementById("parentLabelId");
			clearAllOptions(selectTag);
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

function clearAllOptions(selectTag){
	if(selectTag) {
		selectTag.options.length = 0;
	}
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
			alert(xhttp.responseText);
			getLabelGridInfoService(null);
			closeDialog("createModal");
			getAllLabel();
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

function closeDialog(dialogName){
	$('#'+dialogName).modal('hide');
}

function openDialog(dialogName){
	$('#'+dialogName).modal('show');
}

function reload(){
	window.location = window.location;
	getLabelGridInfoService(null);
}