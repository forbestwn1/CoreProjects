<!DOCTYPE html>
<html>
<body>

	<br>
	Content:<%=?(bus.a.aa)?.value + '   6666 ' %>
	<br>
	TextInput_converter:<nosliw-textinput data="bus.a.aa"/>  
	<br>
	TextInput_converter<nosliw-textinput data="bus.a.aa"/>  

</body>

	<script>
	{
	
	}
	</script>
	
	<constants>
	{
	}
	</constants>
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<context>
	{
		public : {
			bus : {
				definition: {
					a : {
						aa : "test.url;1.0.0"
					}
				},
				default: {
					a : {
						aa : {
							dataTypeId: "test.url;1.0.0",
							value: "This is my world!"
						}
					}
				}
			}
		}
	}
	</context>
	
		<!-- This part can be used to define expressions
		-->
	<expressions>
	{
		
	
	}
	</expressions>
	
</html>

