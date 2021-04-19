<!DOCTYPE html>
<html>
<body>
    Within test.main.res
	<br>
<!--	EXPRESSION REFERENCE:<%=#|<(expressionInternal)>|#.value + ' 6666 ' %>-->
	<br>
<!--	EXPRESSION REFERENCE:<%=#|<(expressionLocal)>|#.value + ' 6666 ' %>-->
	<br>
	
		EXPRESSION IN CONTENT :<%=?(aaa___public)?.value + '   6666 ' %>
	

	<br>
<!--	<nosliw-contextvalue/> -->
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
		"dataexpression" : [
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

