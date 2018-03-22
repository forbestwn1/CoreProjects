<!DOCTYPE html>
<html>
<body>
******************************	Result  ********************************
		<br>
		Sum:<%=#|?(result)?.length()|#.value%>
		<br>
		

		<nosliw-loop data="result" element="ele" index="index">  
			<br>
			<%=?(ele.name)?.value%>   
			<br>
		</nosliw-loop>

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
			result : {
				definition : "test.array;1.0.0",
				default : {
					dataTypeId: "test.array;1.0.0",
					value: [
						{
							dataTypeId: "test.map;1.0.0",
							value: {
								name : {
									dataTypeId: "test.string;1.0.0",
									value: "School1"
								}
							}
						},
												{
							dataTypeId: "test.map;1.0.0",
							value: {
								name : {
									dataTypeId: "test.string;1.0.0",
									value: "School2"
								}
							}
						}					
					]
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

