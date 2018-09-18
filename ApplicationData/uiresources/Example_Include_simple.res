<!DOCTYPE html>
<html>
<body>

			<br>
			************************Included:
			<br>
			<%=?(element111)?.value + '   7777 ' %>   
			<br>
			TextInput:<nosliw-textinput data="element111"/> 
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

