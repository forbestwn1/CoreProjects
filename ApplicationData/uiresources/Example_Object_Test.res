<!DOCTYPE html>
<html>
<body>

	Content:<%=#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value%>

	<br>
	TextInput:<nosliw-textinput data="business.a.aa"/>  
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
							child : {
								a : {
									child : {
										aa : {criteria:"test.string;1.0.0"},
									}
								}
							},
							defaultValue: {
								a : {
									aa : {
										dataTypeId: "test.string;1.0.0",
										value: "This is my world!"
									}
								}
							}
						}
					},
					from: {
						definition : {
							value : {
								dataTypeId: "test.integer",
								value: 3
							}
						}
					},
					to: {
						definition:{
							value : {
								dataTypeId: "test.integer",
								value: 7
							}
						}
					}
				}
			}
		}
	}
	</contexts>
	
		<!-- This part can be used to define expressions
		-->
	<expressions>
	{
	}
	</expressions>
	
</html>
