<!DOCTYPE html>
<html>
<body>
		<nosliw-loop data="business.a.cc" element="ele" index="index">  
		<span class="intag">BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB</span>
			<br>
			Index: <%=?(index)?%>
			<br>
			<%=?(ele)?.value + '   7777 ' %>   <a href='' nosliw-event="click:deleteElementInLoop:">Delete</a>
			<br>
			TextInput:<nosliw-string1 data="ele"/> 
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
					},

				}
			}
		}
	}
	</contexts>
	
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
	
	<attachment>
	{
		"expression" : [
		],
		
		"data": [
					
		]
	}
	</attachment>

</html>
