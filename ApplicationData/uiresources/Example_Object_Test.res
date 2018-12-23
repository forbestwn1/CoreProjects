<!DOCTYPE html>
<html>
<body>

	<br>
	Content:<%=?(business.a.aa)?.value + '   6666 ' %>
	<br>
	<br>
	TextInput:<nosliw-textinput data="business.a.aa"/>  

		<nosliw-loop data="business.a.cc" element="ele" index="index">  
			<br>
			Content:<%=?(ele)?.value + '   6666 ' %>
			<br>
			Index: <%=?(index)?.value%>
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
									cc1 : {
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
		{
			name : "changeInputText",
			data : {
				element : {
					data : {
						definition : {
							path: "business.a.aa"
						}
					}
				}
			}
		}
	]
	</events>
	
	<commands>
	[
		{
			name : "Start",
			parm : {
				element : {
					data : {
						definition : {
							path: "business.a.aa"
						}
					}
				}
			}
		}
	]
	</commands>

</html>
