$(document).ready(function() {
	init();
});

function init() {
	$('#clientMonitoringRefreshButton').click(refreshClientMonitoringTable);
	
	refreshClientMonitoringTable();
}

function refreshClientMonitoringTable() {
	$.getJSON('../webapi/services/clientmonitoring/list', function(data) {
		$('#clientMonitoringTable tbody > tr').remove();
		var l = data.length;
		var jsonData;
		for (i = 0; i < l; i++) {
			jsonData = data[i];
			$('#clientMonitoringTable > tbody:last').append(
					"<tr>" + "<td name='name'>" + jsonData.name + "</td>"
							+ "<td name='componentClassName'>"
							+ jsonData.componentClassName + "</td>"
							+ "<td name='address'>" + jsonData.address
							+ "<td name='status'>" + jsonData.status
							+ "</td>" + "</tr>");
		}
	});
}
