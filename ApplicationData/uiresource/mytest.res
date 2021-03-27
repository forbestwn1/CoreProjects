<!DOCTYPE html>
<html>
<body>
    Within mytest.res
	<br>
	CUSTOM TAG WITH RULE:<nosliw-float data="var1"/>  
	<br>

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
					 "var1" : {
						"definition": {
							"criteria" : {
								"criteria": "test.float;1.0.0",
								"rule" : [
									{
										"ruleType" : "mandatory",
										"description" : "Cannot be blank!!!",
									},
									{
										"ruleType" : "enum",
										"dataSet" : [
											{
												"dataTypeId": "test.float;1.0.0",
												"value": 8
											},
											{
												"dataTypeId": "test.float;1.0.0",
												"value": 8.5
											},
											{
												"dataTypeId": "test.float;1.0.0",
												"value": 9
											},
											{
												"dataTypeId": "test.float;1.0.0",
												"value": 10
											},
										]
									}
								]
							}
						},
					 
					 }
				}
			}
		}
	}
	</context>

	
	<contextref>
	[
	]
	</contextref>

	
	<service>
	[
	]
	</service>


	<attachment>
	{
		"service" : [
		],
		"data" : [
		],
		"context" : [
		]
	}
	</attachment>

	<event>
	[
	]
	</event>
	
</html>

