<!DOCTYPE html>
<html>
<body>

	<br>
	TextInput:<nosliw-textinput data="business.a.aa"/>  
	<br>
	
	<nosliw-debug/>

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
						bb : "test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(&(expression)&;;&(parms)&)||@||%",
						cc : {
							element : "test.string;1.0.0"
						}
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
	<expressions>
	{
	}
	</expressions>
	
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
				},
				
				childResource: {
					inherit : true,
					public : {
						name : {
							path : "business.a.aa"
						}
					}
				}
			
	}
	</constants>
	
</html>

