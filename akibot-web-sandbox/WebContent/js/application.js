/**
 * Created by francesco on 2/10/14.
 * 
 * Twitter: https://twitter.com/francesco_geek Google+:
 * https://plus.google.com/+FrancescoIannazzoGeek
 * 
 */
$(document).ready(function() {

	$('#addButton').click(createWorkDay);

	getWorkDays();
	getMyBeanList();

	console.log("addedsListeners");

	initDefaults();

});

function initDefaults() {
	// Set Default Values
	var date = new Date();
	console.log("Date: " + date.getDate());
	var currentDay = date.getDate();
	var currentMonth = date.getMonth() + 1;
	if (currentMonth < 10) {
		currentMonth = '0' + currentMonth;
	}
	var currentYear = date.getFullYear();

	var formattedDate = currentYear + '-' + currentMonth + '-' + currentDay;

	console.log(formattedDate);
	$('#inputDate').attr('value', formattedDate);
	$("#inputBegin").val('');
	$("#inputEnd").val('');
	$("#inputDescription").val('');

	$('#alertmsg').hide();
	$('#successmsg').hide();
}

function showErrorMsg(messageText) {

	$('#alertmsg').html(messageText);
	$('#alertmsg').show();

}

function createWorkDay() {
	console.log("WorkDay");
	// Hide message area
	$('#successmsg').hide();
	$('#alertmsg').hide();

	if (validateInputFields()) {

		var inputDate = $('#inputDate').val();
		var inputBegin = $('#inputBegin').val();
		var inputEnd = $('#inputEnd').val();
		var inputDescription = $('#inputDescription').val();

		var myObject = new Object();
		myObject.date = inputDate;
		myObject.beginTime = inputBegin + ':00';
		myObject.endTime = inputEnd + ':00';
		myObject.description = inputDescription;

		// caclulate hours
		var hours = calculateHours(inputBegin, inputEnd);
		myObject.hours = hours;

		var jsonDataStr = JSON.stringify(myObject);
		console.log('jsonDataStr' + jsonDataStr);

		var myURL = "webapi/services/timesheet/create";
		$.ajax({
			type : "PUT",
			url : myURL,
			contentType : "application/json",
			data : jsonDataStr,
			success : function(result) {
				getWorkDays();
				initDefaults();
				$('#successmsg').show();

			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("jqXHR statusCode" + jqXHR.statusCode());
				console.log("textStatus " + textStatus);
				console.log("errorThrown " + errorThrown);
			}
		});
	}
}

function getWorkDays() {
	// Hide message area
	$('#successmsg').hide();
	$('#alertmsg').hide();

	// Perform GET request
	$.getJSON(
					'webapi/services/timesheet',
					function(data) {

						// Empty old table entries, select all child rows of
						// tbody element
						$('#myTable tbody > tr').remove();

						var l = data.length;

						var jsonData;
						for (i = 0; i < l; i++) {
							jsonData = data[i];

							// Add Entries to myTable

							$('#myTable > tbody:last')
									.append(
											"<tr><td name='id'>"
													+ jsonData.id
													+ "</td>"
													+ "<td name='date'>"
													+ jsonData.date
													+ "</td>"
													+ "<td name='beginTime'>"
													+ jsonData.beginTime
													+ "</td>"
													+ "<td>"
													+ jsonData.endTime
													+ "</td>"
													+ "<td>"
													+ jsonData.hours
													+ "</td>"
													+ "<td>"
													+ jsonData.description
													+ "</td>"
													+ "<td><button id=\"delButton\" class=\"btn btn-primary\" action=\"delete\">Delete</button></td>"
													+ "</tr>");
						}
						// Register event handler on all delete buttons
						$("button[action='delete']").click(deleteWorkDay);

					});

}

function deleteWorkDay() {

	// Hide message area
	$('#successmsg').hide();
	$('#alertmsg').hide();

	// Determine ID of current selected table row
	var workDayId = $(this).parent().parent().find("td[name='id']").html();
	var myURL = "webapi/services/timesheet/delete/"
			+ workDayId;

	// Perform DELETE request
	$.ajax({
		type : "DELETE",
		url : myURL,
		contentType : "application/json",
		success : function(result) {
			$('#successmsg').show();
			getWorkDays();
			initDefaults();
		}
	});
}

function calculateHours(beginTime, endTime) {
	var beginDateTime = new Date();
	var endDateTime = new Date();

	// get Hours
	var hours = getHours(beginTime);
	var minutes = getMinutes(beginTime);

	beginDateTime.setHours(hours, minutes, 0, 0);
	console.log("Begin Date Time: " + beginDateTime);
	hours = getHours(endTime);
	minutes = getMinutes(endTime);

	endDateTime.setHours(hours, minutes, 0, 0);
	console.log("End Date Time " + endDateTime);

	// calculate hours
	var hourDiff = endDateTime - beginDateTime;

	hourDiff = hourDiff / 60 / 60 / 1000;
	// Round Hours to decimal value
	hourDiff = Number((hourDiff).toFixed(2));
	console.log("Hours calculated: " + hourDiff);

	return hourDiff;
}

function getHours(timeString) {
	var res = timeString.split(':');
	return res[0];
}

function getMinutes(timeString) {
	var res = timeString.split(':');
	return res[1];
}

function validateInputFields() {

	var inputDate = $('#inputDate').val();

	// Validate Time Fields and Date field

	var inputBegin = $('#inputBegin').val();
	var inputEnd = $('#inputEnd').val();

	if ((!inputBegin.match(/^\d{2,}:(?:[0-5]\d)$/))
			|| (!inputEnd.match(/^\d{2,}:(?:[0-5]\d)$/))
			|| Date.parse(inputDate) == null) {
		showErrorMsg("Wrong Date or Time input!");
		return false;
	} else {
		return true;
	}
}
	
// **************************************************************************************

function getMyBeanList() {
	// Hide message area
	$('#successmsg').hide();
	$('#alertmsg').hide();

	// Perform GET request
	$.getJSON(
					'webapi/services/mybean',
					function(data) {

						// Empty old table entries, select all child rows of
						// tbody element
						$('#myBeanList tbody > tr').remove();

						var l = data.length;

						var jsonData;
						for (i = 0; i < l; i++) {
							jsonData = data[i];

							// Add Entries to myTable

							$('#myBeanList > tbody:last')
									.append(
											"<tr><td name='id'>"
													+ jsonData.id
													+ "</td>"
													+ "<td name='name'>"
													+ jsonData.name
													+ "</td>"
													+ "</tr>");
						}

					});

}


