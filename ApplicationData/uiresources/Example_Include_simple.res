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
		group : {
			public : {
				element : {
					element111 : {
						definition: {
							criteria: "test.string;1.0.0",
							defaultValue: {
								dataTypeId: "test.string;1.0.0",
								value: "Include Element!"
							}
						}
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
			parm : {
				element : {
					data : {
						definition : {
							path: "element111"
						}
					}
				}
			},
		}
	]
	</commands>
	
	<events>
	[
		{
			name : "changeInputTextIncludeBasic",
			data : {
				element : {
					data : {
						definition : {
							path: "element111"
						}
					}
				}
			}
		}
	]
	</events>
	
</html>

