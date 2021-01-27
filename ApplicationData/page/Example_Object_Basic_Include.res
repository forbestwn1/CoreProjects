<!DOCTYPE html>
<html>
<body>

			<br>
			************************Included:
			<br>
			Index: <%=?(index)?.value%>  
			<br>
			TextInput:<nosliw-string data="element"/> 
			<br>
			<%=?(element)?.value + '   7777 ' %>   
			<br>
			*************************************

</body>

<!--
			TextInput:<nosliw-string data="element"/> 
			<br>

			Index: <%=?(index)?%>  
-->

	<scripts>
	{
	}
	</scripts>
	
	<contexts>
	{
		group : {
			public : {
				element : {
					index : {
						definition : {
							criteria: "test.integer;1.0.0"
						},
						defaultValue: {
							dataTypeId: "test.integer;1.0.0",
							value: 100
						}
					},
					element : {
						definition : {
							criteria: "test.string;1.0.0"
						},
						defaultValue: {
							dataTypeId: "test.string;1.0.0",
							value: "Include Element!"
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
		{
			name : "changeInputTextInclude",
			data : {
				element : {
					data : {
						definition : {
							path: "element"
						}
					}
				}
			}
		}
	]
	</events>
	
</html>

