<!DOCTYPE html>
<html>
<body>
	
	Are you registered player:
	<nosliw-boolean id="players2019Summer" data="player.registered"/>
	
	<br>
	<nosliw-switch value="<%=?(player.registered)?.value%>">
		<nosliw-case value="true">
			<br>
			Your name:<nosliw-string-options id="players2019Summer" data="player.name"/>
			<br>
		</nosliw-case>

		<nosliw-case value="false">
			<br>
			Your name:<nosliw-textinput data="player.name"/>  
			<br>
		</nosliw-case>
	</nosliw-switch>
	
	<br>
	Email:<nosliw-textinput data="player.email"/>  
	<br>
	
	<br>
	<br><a href='' nosliw-event="click:save:">Save</a><br>
	<br>
	
	<nosliw-contextvalue/>

</body>

	<contexts>
	{
		"group" : {
			"public" : {
				"element" : {
					"player" : {
						"definition" : {
							"child" : {
								"registered" : {criteria:"test.boolean;1.0.0"},
								"name" : {criteria:"test.string;1.0.0"},
								"email" : {criteria:"test.string;1.0.0"},
							}
						},
						"defaultValue": {
							registered : {
								dataTypeId: "test.boolean;1.0.0",
								value: false
							},
							name : {
								dataTypeId: "test.string;1.0.0",
								value: "Wilson"
							},
							email : {
								dataTypeId: "test.string;1.0.0",
								value: ""
							},
						}
						
					}
				}
			}
		}
	}
	</contexts>

	<scripts>
	{
		save : function(data, info, env){
		
		},
	}
	</scripts>


</html>

