<!DOCTYPE html>
<html>
<body>
    Within second page
    <br>
	<nosliw-string data="bbb"/>
    
    <br>
	EXPRESSION IN CONTENT :<%=?(bbb)?.value + '   6666 ' %>
	<br>
    
</body>

	<script>
	{
	}
	</script>

	<component>
	{
		"valuestructure":
		{
			"group" : {
				"public" : {
					"flat" : {
						"bbb":{
							definition : {
								"criteria": "test.string;1.0.0",
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "This is my world bbb!"
							}
						},
					},
				}
			}
		},
	}
	</component>

</html>
