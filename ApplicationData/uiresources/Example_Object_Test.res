<!DOCTYPE html>
<html>
<body>

	Content:<%=?(business.a.aa)?.value + '   6666 ' %>
		
		<br>
	TextInput:<nosliw-textinput data="business.a.aa"/>  
	<br>


</body>

	<script>
	{
	}
	</script>
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<context>
	{
		public : {
			business : {
				definition: {
					a : {
						aa : "test.string;1.0.0",
						cc : "test.array;1.0.0%||element:test.array;1.0.0||%",
					}
				},
				default: {
					a : {
						aa : {
							dataTypeId: "test.string;1.0.0",
							value: "This is my world!"
						},
						dd : "HELLO!!!!",
						cc : [
							{
								dataTypeId: "test.string;1.0.0",
								value: "This is my world 1111!"
							},
							{
								dataTypeId: "test.string;1.0.0",
								value: "This is my world 2222!"
							},
						]
					}
				}
			},
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

