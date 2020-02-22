<!DOCTYPE html>
<html>
<body>
	<br>
	Flight: <%=#|?(flightInfo)?.flight|#.value%>      
	<br>
	Company: <%=#|?(flightInfo)?.company|#.value%>      
	<br>
	Status: <%=#|?(flightInfo)?.status|#.value%>      
	<br>
</body>

	<contexts>
	{
		"group": {
			"public": {
				"element": {
					"flightInfo": {
						"definition": {
							"criteria": "test.map;1.0.0%%||status:test.string;1.0.0,company:test.string;1.0.0,flight:test.string;1.0.0||%%"
						},
						"defaultValue": {
							"dataTypeId": "test.map;1.0.0",
							"value": {
								"status": {
									"dataTypeId": "test.string;1.0.0",
									"value": "arrive"
								},
								"company": {
									"dataTypeId": "test.string;1.0.0",
									"value": "Air Canada"
								},
								"flight": {
									"dataTypeId": "test.string;1.0.0",
									"value": "ABC123"
								}
							}
						}
					}
				}
			}
		}
	}
	</contexts>
	
</html>
