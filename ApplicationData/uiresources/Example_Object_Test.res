<!DOCTYPE html>
<html>
<body>

	<br>
	Content:<%=?(business.a.aa)?.value + '   6666 ' %>
	<br>
	TextInput:<nosliw-textinput data="business.a.aa"/>  
	<br>
<!--	
	<br>
	Content:<%=#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value + ?(business.a.dd)? + ' 6666 ' %>
	<br>
-->
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
						},
					},
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
	
	<events>
	[
	]
	</events>
	
</html>
