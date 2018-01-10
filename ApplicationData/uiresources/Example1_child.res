<!DOCTYPE html>
<html>
	<body>

	<br>
	<%=?(name)?.value + '   6666 ' %>  tttttttttt222  
	<br>
	<%=?(childName)?.value + '   6666 ' %>  tttttttttt222  
	<br>

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
			
			name : {
				default: {
							dataTypeId: "test.string;1.0.0",
							value: "This is child name!"
						}
			},
			childName : {
				default: {
							dataTypeId: "test.string;1.0.0",
							value: "This is child!"
						}
			},
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

