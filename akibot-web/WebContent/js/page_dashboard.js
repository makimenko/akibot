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
}

function refreshClientMonitoringTable() {
	$.getJSON('../webapi/services/ClientMonitoring',
			function(data) {
				$('#clientMonitoringTable tbody > tr').remove();
				var l = data.length;
				var jsonData;
				for (i = 0; i < l; i++) {
					jsonData = data[i];
					$('#clientMonitoringTable > tbody:last').append(
							"<tr><td name='name'>" + jsonData.name + "</td>"
									+ "</tr>");
				}
			});
}
