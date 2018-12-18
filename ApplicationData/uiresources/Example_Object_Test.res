<!DOCTYPE html>
<html>
<body>
		<nosliw-include source="Example_Object_Basic_Include" context="element=business.a.aa" /> 

		<nosliw-loop data="business.a.cc" element="ele" index="index">  
		</nosliw-loop>
		
<!--		
		<nosliw-include source="Example_Include_simple" context="" event="changeInputTextIncludeBasic=changeInputTextIncludeBasicMapped"/> 

		
	<br>
	Content:<%=?(business.a.aa)?.value + '   6666 ' %>

	TextInput:<nosliw-textinput data="business.a.aa"/>  
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
										cc : {criteria:"test.array;1.0.0%||element:test.string;1.0.0||%"},
									}
								}
							},
							defaultValue: {
								a : {
									aa : {
										dataTypeId: "test.string;1.0.0",
										value: "This is my world!"
									},
									cc : [
										{
											dataTypeId: "test.string;1.0.0",
											value: "This is my world 1111!"
										},
										{
											dataTypeId: "test.string;1.0.0",
											value: "This is my world 2222!"
										}
									],
									ee : {
										dataTypeId: "test.array;1.0.0",
										value: [
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
	
	<commands>
	[
	]
	</commands>

</html>
