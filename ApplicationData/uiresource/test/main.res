<!DOCTYPE html>
<html>
<body>
	<br>
	AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA		
</body>

	<script>
	{
	}
	</script>

	
	<context>
	{
		"group" : {
			"public" : {
				"element" : {
				}
			}
		}
	}
	</context>
	
	<service>
	[
		{
			"name" : "simpleServiceWithoutInterface",
			"interface" : "service_simpleoutput",
			"provider" : "simpleServiceWithoutInterfaceProvider",
			"info" : {
				"enhanceContext" : "true"
			},
			"dataMapping" :{
				"inputMapping" : {
					"element" : {
						"parm1" : {
							"definition" : {
								"path" : "forsimpleservice_1_parm1"
							}
						},
						"parm2" : {
							"definition" : {
								"path" : "local_var_for_parm2"
							}
						}
					}
				},
				"outputMapping" : {
					"success" : {
						"element" : {
							"forsimpleservice_1_output1" : {
								"definition" : {
									"path" : "simpleOutput1"
								}
							},
							"local_var_for_output2" : {
								"definition" : {
									"path" : "simpleOutput2"
								}
							}
						}
					}
				}
			}
		}
	]
	</service>

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
				"name": "simpleServiceWithoutInterfaceProvider",
				"referenceId" : "simpleoutput_refinterface"
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

	<event>
	[
	]
	</event>
	
</html>

