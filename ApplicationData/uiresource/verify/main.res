<!DOCTYPE html>
<html>
<body>
    Within test.main.res
    
    	<br>
	CONSTANT FROM CONTEXT:<%=&(constantFromContext5)&.value + '' %>  
	<br>
	CONSTANT FROM CONTEXT:<%=&(constantFromContext1)& + '' %>  
	<br>
	CONSTANT FROM ATTACHMENT:<%=&(constantFromAtt1)&.value + '' %>  
	<br>
    
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
						constantFromContext1 : {
							definition: {
								value : "<%=5+6+7%>",
							}
						},
						constantFromContext5 : {
							definition: {
								value : "<%=#|&(#test##string___Thisismyworldabcdef)&|#%>"
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

