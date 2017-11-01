<!DOCTYPE html>
<html>
	<!-- Contexts define the variables input for this ui resource -->
	<body>
<!--	<%= ' 6666 ' %>  tttttttttt11  -->  
	<b>
	lalalala
	<%= #|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value + ' 6666 ' %>  tttttttttt222    

	</body>

	<script>
	{
	}
	</script>
	
	<constants>
	{
			aaaa : "<%=5+6+7%>",
			bbbb : "<%=(5+6+7)>5%>",
			cccc : {
						a : 12345,
						b : true,
						c : "good",
						d : "<%=5+6+7%>"
					},
			dddd : "<%=&(cccc)&.a+6%>",

			ffff : "<%=#|&(#test##string___Thisismyworldabcdef)&|#%>",
			eeee : "<%=#|&(ffff)&.subString(from:&(#test##integer___3)&,to:&(#test##integer___7)&)|#%>",
			
				base: {
					dataTypeId: "test.string",
					value: "This is my world!"
				},
				from: {
					dataTypeId: "test.integer",
					value: 3
				},
				to: {
					dataTypeId: "test.integer",
					value: 7
				}
			
	}
	</constants>
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<context>
	{
		business : {
			definition: {
				a : {
					aa : "test.string;1.0.0",
					bb : "test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(&(expression)&;;&(parms)&)||@||%"
				}
			},
			default: {
				a : {
					aa : {
						dataTypeId: "test.string;1.0.0",
						value: "This is my world!"
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

