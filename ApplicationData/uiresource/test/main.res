<!DOCTYPE html>
<html>
<body>
	<br>
	AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA		
</body>

	<scripts>
	{
	}
	</scripts>

	
	<contexts>
	{
		"group" : {
			"public" : {
				"element" : {
				}
			}
		}
	}
	</contexts>
	
	<services>
	[
	]
	</services>

	<contextref>
	[
		{
			"name" : "forlistservice_1_local",
			"categary" : "public"
		},
		{
			"name" : "forsimpleservice_1_local",
			"categary" : "public"
		},
		{
			"name" : "forlistservice_1_global",
			"categary" : "public"
		},
		{
			"name" : "forsimpleservice_1_global",
			"categary" : "public"
		},
		{
			"name" : "internal",
			"categary" : "public"
		},
	]
	</contextref>

	<attachment>
	{
		"service" : [
			{
				"name": "getSchoolDataService",
				"referenceId" : "schoolService"
			}	
		],
		"data" : [
			{
				"name": "schoolAttribute",
				"entity" : {
					dataTypeId: "test.string;1.0.0",
					value: "schoolName"
				}
			}
		],
		"context" : [
			{
				"name": "forlistservice_1_local",
				"referenceId": {
					"structure" : "local",
					"id" : "forlistservice_1"
				}
			},
			{
				"name": "forsimpleservice_1_local",
				"referenceId": {
					"structure" : "local",
					"id" : "forsimpleservice_1"
				}
			},
			{
				"name": "forlistservice_1_global",
				"referenceId": "forlistservice_1_ex"
			},
			{
				"name": "forsimpleservice_1_global",
				"referenceId": "forsimpleservice_1_ex" 
			},
			{
				"name": "internal",
				"entity": {
					"internal": {
						"definition": {
							"criteria": "test.float;1.0.0"
						},
					},
				} 
			},
		]
	}
	</attachment>

	<events>
	[
		{
			name : "selectSchool",
			data : {
				element : {
					data : {
						definition : {
							path: "schoolList.element"
						}
					}
				}
			}
		}
	]
	</events>
	
</html>

