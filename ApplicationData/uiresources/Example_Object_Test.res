<!DOCTYPE html>
<html>
<body>
		
		<nosliw-loop data="business.a.cc" element="ele" index="index">  
			<br>
			Index: <%=?(index)?%>
			<br>
		</nosliw-loop>

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
										cc : {criteria:"test.array;1.0.0%||element:test.string;1.0.0||%"},
									}
								}
							},
							defaultValue: {
								a : {
									cc : [
											{
												dataTypeId: "test.string;1.0.0",
												value: "This is my world 1111!"
											},
											{
												dataTypeId: "test.string;1.0.0",
												value: "This is my world 2222!"
											}
									]
								}
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
	
	<events>
	[
	]
	</events>
	
	<commands>
	[
	]
	</commands>

</html>
