<!DOCTYPE html>
<html>
<body>

			<br>
			************************Included:
			<br>
			<%=?(element111)?.value + '   7777 ' %>   
			<br>
			TextInput:<nosliw-textinput data="element111" nosliw-event="valueChanged:textInputValueChanged:"/>
			<br>
			*************************************

</body>

	<scripts>
	{
		textInputValueChanged : function(info, env){
			env.trigueEvent("changeInputTextIncludeBasic", info.eventData);
		},
		command_Include : function(data, env){
			return data.data + "   Include";
		},
	}
	</scripts>
	
	<contexts>
	{
		public : {
			element : {
				element111 : {
					definition: "test.string;1.0.0",
					default: {
						dataTypeId: "test.string;1.0.0",
						value: "Include Element!"
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
	
	<commands>
	[
		{
			name : "Include",
			parms : {
				data : {
					path: "element111"
				}
			},
		}
	]
	</commands>
	
	<events>
	[
		{
			name : "changeInputTextIncludeBasic",
			parms : {
				data : {
					path: "element111"
				}
			},
		}
	]
	</events>
	
</html>

