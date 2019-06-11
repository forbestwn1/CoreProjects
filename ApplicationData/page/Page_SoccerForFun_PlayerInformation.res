<!DOCTYPE html>
<html>
<body>
	<br>
	请提供信息，你是谁，包括
	<br>
		名字： 
	<br>
		邮件地址
	<br>
	
	Are you registered player:
	<nosliw-bolean id="players2019Summer" data="player.registered"/>
	
	<br>
	SchoolType:<nosliw-options id="players2019Summer" data="player.name"/>
	<br>
	
	
	Name : 
	TextInput:<nosliw-textinput data="player.name"/>  
	<br>
	
	Email : 
	TextInput:<nosliw-textinput data="player.email"/>  
	<br>
	
	<br>
	<br><a href='' nosliw-event="click:save:">Save</a><br>
	<br>
	

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
								value: undefined
							},
							name : {
								dataTypeId: "test.string;1.0.0",
								value: ""
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

