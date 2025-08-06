<!DOCTYPE html>
<html>
<body>
    Within test.main.res
	<br>
	CUSTOM TAG:<nosliw-string data="aa.a"/>  
	<br>
		CUSTOM TAG:<%=?(aa.a)?.value + '   6666 ' %>  
	

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
					aa : {
						definition: {
							child : {
								a : {criteria:"test.string;1.0.0"},
							}
						},
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
		"expression" : [
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

