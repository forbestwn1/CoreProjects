<!DOCTYPE html>
<html>
<body>
    Within test.main.res
	<br>
	EXPRESSION REFERENCE:<%=#|<(expressionInternal)>|#.value + ' 6666 ' %>
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
						"aaa":{
							definition : {
								"criteria": "test.string;1.0.0",
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "This is my world!"
							}
						},
						constantFromContext8: {
							definition : {
								value : {
									dataTypeId: "test.integer;1.0.0",
									value: 9
								}
							}
						},
						constantFromContext9: {
							definition:{
								value : {
									dataTypeId: "test.integer;1.0.0",
									value: 15
								}
							}
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
		"expression" : [
			{
				"name" : "expressionInternal",
				"entity" : {
					"expression" : "?(aaa)?.subString(from:&(constantFromContext8)&,to:&(constantFromContext9)&)",
				}
			}
		],
		"service" : [
		],
		"value" : [
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

