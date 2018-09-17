<!DOCTYPE html>
<html>
<body>

			<br>
			************************Included:
			<br>
			Index: <%=?(index)?%>
			<br>
			<%=?(element)?.value + '   7777 ' %>   
			<br>
			TextInput:<nosliw-textinput data="element"/> 
			<br>
			*************************************

</body>

	<script>
	{
	}
	</script>
	
	<context>
	{
		public : {
			index : {
				definition: "test.integer;1.0.0",
				default: {
					dataTypeId: "test.integer;1.0.0",
					value: 100
				}
			},
			element111 : {
				definition: "test.string;1.0.0",
				default: {
					dataTypeId: "test.string;1.0.0",
					value: "Include Element!"
				}
			}
		}
	}
	</context>
	
		<!-- This part can be used to define expressions
		-->
	<expression>
	{
	}
	</expression>
	
</html>

