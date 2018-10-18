<!DOCTYPE html>
<html>
<body>

			<br>
			************************Included:
			<br>
			Index: <%=?(index)?%>  
			<br>
			TextInput:<nosliw-textinput data="element"/> 
			<br>
			<%=?(element)?.value + '   7777 ' %>   
			<br>
			*************************************

</body>

<!--
			TextInput:<nosliw-textinput data="element"/> 
			<br>

			Index: <%=?(index)?%>  
-->

	<scripts>
	{
	}
	</scripts>
	
	<contexts>
	{
		public : {
			index : {
				definition: "test.integer;1.0.0",
				default: {
					dataTypeId: "test.integer;1.0.0",
					value: 100
				}
			},
			element : {
				definition: "test.string;1.0.0",
				default: {
					dataTypeId: "test.string;1.0.0",
					value: "Include Element!"
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
			parms : {
				data : {
					path: "element"
				}
			},
		}
	]
	</events>
	
</html>

