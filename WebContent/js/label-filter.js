function onPageLoad() {
	if (sessionStorage.getItem("id") == null) {
		window.location = "index.html";
	}
	document.getElementById("labelFilterSideNav").setAttribute("class","active");
	getAllLabel();
	getLabelFilterGridInfoService(null);
}

function getValue(id){
	return document.getElementById(id).value;
}

function getCheckboxValue(id){
	return document.getElementById(id).checked;
}

var labelsArray = [];
var header = new Array();
header = ['Sr.No', 'Filter Name', 'Label Name', 'Creation Date', 'Edit', 'Remove', 'Run'];


function clearAllOptions(selectTag){
	if(selectTag) {
		selectTag.options.length = 0;
	}
}

function getAllLabel(){
	labelsArray = [];
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			labelsArray = JSON.parse(xhttp.responseText);
			var selectTag = document.getElementById("labelName");
			fillLabelsSelectTag(selectTag);
		}
	};
	
	xhttp.open("POST", "CreateLabelServlet", true);
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "GET_ALL_LABEL");
	xhttp.send();
}

function fillLabelsSelectTag(selectTagObject){
	clearAllOptions(selectTagObject);
	var optionTag = document.createElement("option");
	optionTag.text = "";
	selectTagObject.options.add(optionTag, 0);
	for(var i = 0; i < labelsArray.length; ++i){
		optionTag = document.createElement("option");
		optionTag.text = labelsArray[i].name;
		selectTagObject.options.add(optionTag, i+1);
	}
}

function labelFilterService() {
	var xhttp = new XMLHttpRequest();
	
	var label = getValue("labelName");
	var isEmailFilter = getCheckboxValue("isEmailFilter");
	var filterName = getValue("filterName");
	var emailIds = splitIntoArray(getValue("emailIds"));
	var isSubjectFilter = getCheckboxValue("isSubjectFilter");
	var subjectKeywords = splitIntoArray(getValue("subjectKeywords"));
	var isBodyFilter = getCheckboxValue("isBodyFilter");
	var bodyKeywords = splitIntoArray(getValue("bodyKeywords"));
	
	var filterJson = {
		label : label,
		isEmailFilter : isEmailFilter,
		filterName : filterName,
		emailIds : emailIds,
		isSubjectFilter : isSubjectFilter,
		subjectKeywords : subjectKeywords,
		isBodyFilter : isBodyFilter,
		bodyKeywords : bodyKeywords
	}
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			getLabelFilterGridInfoService(null);
			closeDialog("labelFilterModal");
		}
	};
	xhttp.open("POST", "LabelFilterServlet", true);
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "SAVE_LABEL_FILTER");
	xhttp.send(JSON.stringify(filterJson));
}

function getLabelFilterGridInfoService(searchFilter){
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var labelGridInfo = JSON.parse(xhttp.responseText);
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
	
	xhttp.open("POST", "LabelFilterServlet", true);
	if(searchFilter){
		xhttp.setRequestHeader("searchFilter", searchFilter);
	}
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "LABEL_FILTER_GRID_INFO_SERVICE");
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
		
		var filterNameTD = document.createElement('td');
		filterNameTD = tr.insertCell(1);
		var filterNameCell = document.createTextNode(gridInfo[record].filterName);
		filterNameTD.appendChild(filterNameCell);
		
		var labelNameTD = document.createElement('td');
		labelNameTD = tr.insertCell(2);
		var labelNameCell = document.createTextNode(gridInfo[record].label);
		labelNameTD.appendChild(labelNameCell);
		
		var creationDateTD = document.createElement('td');
		creationDateTD = tr.insertCell(3);
		var creationDateCell = document.createTextNode(gridInfo[record].creationDate);
		creationDateTD.appendChild(creationDateCell);
		
		var name = gridInfo[record].id + "$" +gridInfo[record].filterName + "$" +gridInfo[record].label + "$" +gridInfo[record].creationDate + "$" + gridInfo[record].isEmailFilter + "$" + gridInfo[record].emailIds + "$" + gridInfo[record].isSubjectFilter + "$" + gridInfo[record].subjectKeywords+ "$" + gridInfo[record].isBodyFilter+ "$" + gridInfo[record].bodyKeywords;

		var editButtonTD = document.createElement('td');
		editButtonTD = tr.insertCell(4);
		var editButton = document.createElement('input');
		editButton.setAttribute('type', 'button');
		editButton.setAttribute('value', 'Edit');
		editButton.setAttribute('name', name);
		editButton.setAttribute('onclick', 'edit(this.name)');
		editButton.setAttribute('class', 'btn btn-default btn-danger');
		editButtonTD.appendChild(editButton);
		
		var deleteButtonTD = document.createElement('td');
		deleteButtonTD = tr.insertCell(5);
		var deleteButton = document.createElement('input');
		deleteButton.setAttribute('type', 'button');
		deleteButton.setAttribute('value', 'Delete');
		deleteButton.setAttribute('name', name);
		deleteButton.setAttribute('onclick', 'deleteFilter(this.name)');
		deleteButton.setAttribute('class', 'btn btn-default btn-danger');
		deleteButtonTD.appendChild(deleteButton);
		
		var runButtonTD = document.createElement('td');
		runButtonTD = tr.insertCell(6);
		var runButton = document.createElement('input');
		runButton.setAttribute('type', 'button');
		runButton.setAttribute('value', 'Run');
		runButton.setAttribute('name', name);
		runButton.setAttribute('onclick', 'run(this.name)');
		runButton.setAttribute('class', 'btn btn-default btn-danger');
		runButtonTD.appendChild(runButton);	
	}
}

function deleteFilter(selectedFilterData){
	labelFilterJson = getLabelJson(selectedFilterData);
	var filterId = labelFilterJson.id;
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			getLabelFilterGridInfoService(null);
			console.log("Filter removed successfully...");
		}
	};
	
	xhttp.open("POST", "LabelFilterServlet", true);
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "DELETE_FILTER_SERVICE");
	xhttp.setRequestHeader("filterId", filterId);
	xhttp.send();
}

function run(selectedFilterData){
	labelFilterJson = getLabelJson(selectedFilterData);
	var filterData = getFilterWrapperObjectFromLabelFilter(labelFilterJson);
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			console.log("Creating run...");
		}
	};
	
	xhttp.open("POST", "RunServlet", true);
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "LABEL_RUN_SERVICE");
	xhttp.send(JSON.stringify(filterData));
}

function splitIntoArray(string){
	if(string){
		return string.split(",");
	}
	return null;
}

function closeDialog(dialogName){
	$('#'+dialogName).modal('hide');
}

/*Edit label filter*/

var labelFilterJson = {};

function edit(selectedFilterData){
	labelFilterJson = getLabelJson(selectedFilterData);
	document.getElementById("editFilterName").value = labelFilterJson.editFilterName;
	var selectTagObject = document.getElementById("editLabelName");
	fillLabelsSelectTag(selectTagObject);
	setDefaultValueToLabelDropDown(labelFilterJson.editLabelName);
	document.getElementById("editIsEmailFilter").checked = labelFilterJson.editIsEmailFilter == 'true' ? true : false;
	document.getElementById("editEmailIds").value = labelFilterJson.editEmailIds == 'undefined' ? '' : labelFilterJson.editEmailIds;
	document.getElementById("editIsSubjectFilter").checked = labelFilterJson.editIsSubjectFilter == 'true' ? true : false;
	document.getElementById("editSubjectKeywords").value = labelFilterJson.editSubjectKeywords == 'undefined' ? '' : labelFilterJson.editSubjectKeywords;
	document.getElementById("editIsBodyFilter").checked = labelFilterJson.editIsBodyFilter == 'true' ? true : false;
	document.getElementById("editBodyKeywords").value = labelFilterJson.editBodyKeywords == 'undefined' ? '' : labelFilterJson.editBodyKeywords;
	
	openDialog("editLabelFilterModal");
}

function setDefaultValueToLabelDropDown(value){
    $("#editLabelName").val(value);
}

function getLabelJson(selectLabelData){
	var delimeter = "$";
	var split = selectLabelData.split(delimeter);
	return {
		id : split[0],
		editFilterName : split[1],
		editLabelName : split[2],
		creationDate : new Date(split[3]),
		editIsEmailFilter : split[4],
		editEmailIds : split[5],
		editIsSubjectFilter : split[6],
		editSubjectKeywords : split[7],
		editIsBodyFilter : split[8],
		editBodyKeywords : split[9]
	};
}

function openDialog(dialogName){
	$('#'+dialogName).modal('show');
}

function editLabelFilterService(){
	var editedLabelFilterData = getEditedLabelFilter(labelFilterJson.id);
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			closeDialog("editLabelFilterModal");
			getLabelFilterGridInfoService(null);
		}
	};
	
	xhttp.open("POST", "LabelFilterServlet", true);
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "EDIT_LABEL_FILTER");
	xhttp.send(JSON.stringify(editedLabelFilterData));
}

function getEditedLabelFilter(filterId){
	return {
		id : filterId,
		label : getValue("editLabelName"),
		filterName : getValue("editFilterName"),
		isEmailFilter : getCheckboxValue("editIsEmailFilter"),
		emailIds : splitIntoArray(getValue("editEmailIds")),
		isSubjectFilter : getCheckboxValue("editIsSubjectFilter"),
		subjectKeywords : splitIntoArray(getValue("editSubjectKeywords")),
		isBodyFilter : getCheckboxValue("editIsBodyFilter"),
		bodyKeywords : splitIntoArray(getValue("editBodyKeywords"))
	};
}

function getFilterWrapperObjectFromLabelFilter(labelFilter){
	return {
		id : labelFilter.id,
		label : labelFilter.editLabelName,
		filterName : labelFilter.editFilterName,
		isEmailFilter : labelFilter.editIsEmailFilter,
		emailIds : splitIntoArray(labelFilter.editEmailIds),
		isSubjectFilter : labelFilter.editIsSubjectFilter,
		subjectKeywords : splitIntoArray(labelFilter.editSubjectKeywords),
		isBodyFilter : labelFilter.editIsBodyFilter,
		bodyKeywords : splitIntoArray(labelFilter.editBodyKeywords)
	};
}
