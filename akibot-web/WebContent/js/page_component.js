/**
 * Created by francesco on 2/10/14.
 * 
 * Twitter: https://twitter.com/francesco_geek Google+:
 * https://plus.google.com/+FrancescoIannazzoGeek
 * 
 */
$(document).ready(function() {
	init();
});

function init() {
	refreshClientMonitoringTable();

	$('#clientMonitoringRefreshButton').click(refreshClientMonitoringTable);
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
							+ "</td>" + "</tr>");
		}
	});
}
