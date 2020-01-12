function onPageLoad() {
	if (sessionStorage.getItem("id") == null) {
		window.location = "index.html";
	}	
	document.getElementById("runStatusSideNav").setAttribute("class","active");
	getRunGridInfoService();
}

var header = new Array();
header = ['Sr.No', 'Run Name', 'Label Filter', 'Delete Filter', 'Failure Reason', 'Status'];

function getRunGridInfoService(){
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var runGridInfo = JSON.parse(xhttp.responseText);
			if(Array.isArray(runGridInfo) && runGridInfo.length){
				document.getElementById("showDataTableDiv").style.display = "block";
				document.getElementById("noRecordsFoundDiv").style.display = "none";
				createTable(runGridInfo);
			} else {
				document.getElementById("showDataTableDiv").style.display = "none";
				document.getElementById("noRecordsFoundDiv").style.display = "block";
			}
		}
	};
	
	xhttp.open("POST", "RunServlet", true);
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "RUN_GRID_INFO_SERVICE");
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

		var srNoTD = document.createElement('td');
		srNoTD = tr.insertCell(0);
		var srNoCell = document.createTextNode(record+1);
		srNoTD.appendChild(srNoCell);
		
		var runNameTD = document.createElement('td');
		runNameTD = tr.insertCell(1);
		var runNameCell = document.createTextNode(gridInfo[record].runName);
		runNameTD.appendChild(runNameCell);
		
		var labelFilterNameTD = document.createElement('td');
		labelFilterNameTD = tr.insertCell(2);
		var labelFilterNameCell = document.createTextNode(gridInfo[record].labelFilterName == undefined ? '-' : gridInfo[record].labelFilterName);
		labelFilterNameTD.appendChild(labelFilterNameCell);
		
		var deleteFilterNameTD = document.createElement('td');
		deleteFilterNameTD = tr.insertCell(3);
		var deleteFilterNameCell = document.createTextNode(gridInfo[record].deleteFilterName == undefined ? '-' : gridInfo[record].deleteFilterName);
		deleteFilterNameTD.appendChild(deleteFilterNameCell);
		
		var failureReasonTD = document.createElement('td');
		failureReasonTD = tr.insertCell(4);
		var failureReasonCell = document.createTextNode(gridInfo[record].failureReason == undefined ? '-' : gridInfo[record].failureReason);
		failureReasonTD.appendChild(failureReasonCell);
		
		var runStatusTD = document.createElement('td');
		runStatusTD = tr.insertCell(5);
		var runStatusCell = document.createTextNode(gridInfo[record].runStatus);
		runStatusTD.appendChild(runStatusCell);
	}
}

