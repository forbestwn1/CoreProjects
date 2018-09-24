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

	<script>
	{
		textInputValueChanged : function(info, env){
			env.trigueEvent("changeInputTextIncludeBasic", info.eventData);
		},
	}
	</script>
	
	<context>
	{
		public : {
			element111 : {
				definition: "test.string;1.0.0",
				default: {
					dataTypeId: "test.string;1.0.0",
					value: "Include Element!"
				}
			}
		}
	}
	</context>
	
		<!-- This part can be used to define expressions
		-->
	<expression>
	{
	}
	</expression>
	
	<event>
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
	</event>
	
</html>

