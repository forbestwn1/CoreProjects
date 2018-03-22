<!DOCTYPE html>
<html>
<body>
	<br>
************************************   Query  **************************************
	<br>
	TextInput:<nosliw-textinput data="business.a.aa"/>  

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
							value: "Public"
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

