<!DOCTYPE html>
<html>
<body>
	<br>
	TextInput:<nosliw-string1 data="business"/>  
	<br>

	Content:<%=?(business)?.value%>

</body>

	<scripts>
	{
	}
	</scripts>
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<contexts>
	{
		group : {
			public : {
				element : {
					business : {
						definition: {
							criteria : {
								dataInfo : {
									criteria: "test.string;1.0.0",
									rule : [
										{
											ruleType : "enum",
											enumCode : "schoolType"
										}
									]
								},
							}
						},
						defaultValue :{
							dataTypeId: "test.string;1.0.0",
							value: "Public"
						},
					},
				}
			}
		}
	}
	</contexts>
	
	<events>
	[
	]
	</events>
	
	<commands>
	[
	]
	</commands>
	
	<attachment>
	{
		"expression" : [
		],
		
		"data": [
					
		]
	}
	</attachment>

</html>
