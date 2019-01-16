<!DOCTYPE html>
<html>
<body>
	<br>
	SchoolName: <%=#|?(schoolData)?.getChildData(name:&(schoolAttribute)&)|#.value%>   
	<br>
</body>

		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<contexts>
	{
		"group": {
			"public": {
				"element": {
					"schoolData": {
						"definition": {
							"criteria": "test.map;1.0.0%%||geo:test.geo;1.0.0,schoolName:test.string;1.0.0,schoolRating:test.float;1.0.0||%%"
						},
						"defaultValue": {
							"dataTypeId": "test.map;1.0.0",
							"value": {
								"schoolName": {
									"dataTypeId": "test.string;1.0.0",
									"value": "School1"
								},
								"schoolRating": {
									"dataTypeId": "test.float;1.0.0",
									"value": 6.0
								},
								"geo": {
									"dataTypeId": "test.geo;1.0.0",
									"value": {
										"latitude": 43.651299,
										"longitude": -79.579473
									}
								}
							}
						}
					},
					"schoolAttribute": {
						"definition": {
							"value": {
								"dataTypeId": "test.string",
								"value": "schoolName"
							}
						}
					}
				}
			}
		}
	}
	</contexts>
	
</html>
