<!DOCTYPE html>
<html>
<body>

		<nosliw-loop data="business.a.cc" element="ele" index="index">  

						<nosliw-include source="Example_Object_Basic_Include" context="ele=element;index=index" /> 
			
		</nosliw-loop>
  

</body>

	<scripts>
	{
		textInputValueChanged : function(info, env){
			env.trigueEvent("changeInputText", info.eventData);
		},
		
		command_Start :function(data, env){
			return data.data + "   Start";
		},
	}
	</scripts>
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<contexts>
	{
		public : {
			business : {
				definition: {
					a : {
						aa : "test.string;1.0.0",
						cc : "test.array;1.0.0%||element:test.string;1.0.0||%",
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
			name : "changeInputText",
			parms : {
				data : {
					path: "business.a.aa"
				}
			},
		}
	]
	</events>
	
	<commands>
	[
		{
			name : "Start",
			parms : {
				data : {
					path: "business.a.aa"
				}
			},
		}
	]
	</commands>

</html>
