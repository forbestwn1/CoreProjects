<!DOCTYPE html>
<html>
<body>
    Within test.main.res
    
    	<br>
    	<br>
	EXPRESSION IN CONTENT :<%=?(aaa)?.value + '   6666 ' %>
	<br>
    	<!--CUSTOM TAG:<nosliw-string data="aaa"/>-->   
    
</body>

	<script>
	{
	}
	</script>


	<valuestructure>
	{
		"group" : {
			"public" : {
				"flat" : {
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
	</valuestructure>

	
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
		"value" : [
			{
				"name": "constantFromAtt1",
				"entity" : 
				{
					"value" : {
						dataTypeId: "test.string;1.0.0",
						value: "Constant in attachment"
					}
				}
			},
			{
				"name": "constantFromAtt2",
				"entity" : 
				{
					"value" : {
							dataTypeId: "test.integer",
							value: 15
					}
				}
			},
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

