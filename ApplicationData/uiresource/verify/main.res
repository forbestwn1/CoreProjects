<!DOCTYPE html>
<html>
<body>
    Within test.main.res
	<br>
<!--	EXPRESSION REFERENCE:<%=#|<(expressionInternal)>|#.value + ' 6666 ' %>-->
	<br>
<!--	EXPRESSION REFERENCE:<%=#|<(expressionLocal)>|#.value + ' 6666 ' %>-->
	<br>
<!--	
		EXPRESSION IN CONTENT :<%=?(aaa___public)?.value + '   6666 ' %>
	

	<br>
	
	<nosliw-string data="aaa"/>
	<br>
	-->
<!--	<nosliw-contextvalue/> -->
</body>

	<script>
	{
	}
	</script>


	<valueStructure>
	{
		"group" : {
			"public" : {
				"flat" : {
					constantFromContext7: {
						definition : {
							value : {
								dataTypeId: "test.string",
								value: "Constant data from context"
							}
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

						"parm4" : {
							"description" : "input from constant defined in context",
							"definition" : {
								"path" : "constantFromContext7"
							}
						},
					}
				},
				"outputMapping" : {
					"success" : {
						"element" : {
						}
					}
				}
			}
		},
	
	]
	</service>


	<attachment>
	{
		"dataexpression" : [
		],
		"service" : [
			{
				"name": "simpleServiceWithoutInterfaceProvider",
				"referenceId" : "simpleoutput_refinterface"
			},	
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

