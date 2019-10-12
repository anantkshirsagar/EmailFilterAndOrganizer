function checkName() {
	var name = document.getElementById("name").value;
	var regex = /^[a-zA-Z]*$/;
	if (!regex.test(name)) {
		alert("Enter Valid Name.");
		document.getElementById("name").value = "";
	}
}

function checkMobileNo() {
	var mobileNo = document.getElementById("mobileNo").value
	var regex = /^\d{10}$/;
	if (!regex.test(mobileNo)) {
		alert("Mobile no must be ten digits.")
		document.getElementById("mobileNo").value = "";
	}
}

function checkEmail() {
	var email = document.getElementById("email").value;
	var regex = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
	if (!regex.test(email)) {
		alert("Enter valid email address.")
		document.getElementById("email").value = "";
	}
}

function checkPassword() {
	var password = document.getElementById("password").value;
	var confirmPassword = document.getElementById("confirmPassword").value;
	if (password != confirmPassword) {
		alert("Password dosent match.")
		document.getElementById("password").value = "";
		document.getElementById("confirmPassword").value = "";
	}
}