<!DOCTYPE html>
<html>
<body>
	<br>
	FlightNumber:<nosliw-string data="flightNumber"/>  
	<br>
	<br>
<!--	Date:<nosliw-date data="date"/> -->
	<br>
	<br>
</body>

	<contexts>
	{
		"group": {
			"public": {
				"element": {
					"flightNumber": {
						"definition": {
							"criteria": "test.string;1.0.0"
						},
						"defaultValue" : {
							"dataTypeId": "test.string;1.0.0",
							"value": "A4H2M1"
						}
					},
					"date" : {
						"definition": {
							"criteria": "test.date;1.0.0"
						},
						"defaultValue" : {
							"dataTypeId": "test.date;1.0.0",
							"value": "2019"
						}
					}
				}
			}
		}
	}
	</contexts>
	
</html>
