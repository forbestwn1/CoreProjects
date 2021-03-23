<!DOCTYPE html>
<html>
<body>
	
	<br>
	<br>
	如果你是最初注册的球员，请勾选：
<!--	<nosliw-boolean id="players2019Summer" data="player.registered"/>-->
	
	<br>
	<nosliw-switch value="<%=?(player.registered)?.value%>">
		<nosliw-case value="true">
			<br>
<!--			注册球员列表，请选择你的名字:<nosliw-string-options id="players2019Summer" data="player.name"/>  -->
			<br>
		</nosliw-case>

		<nosliw-case value="false">
			<br>
			请输入你的名字:<nosliw-string data="player.name"/>  
			<br>
		</nosliw-case>
	</nosliw-switch>
	
	<br>
	电子邮件用来通知各种事件(比方说你接到坑，或者有人接你的坑):
	<br>
	<nosliw-string data="player.email"/>  
	<br>
	
	<br>
	<a href='' nosliw-event="click:save:">Save</a>
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
								value: false
							},
							name : {
								dataTypeId: "test.string;1.0.0",
								value: ""
							},
							email : {
								dataTypeId: "test.string;1.0.0",
								value: "wilson@hotmail.com"
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
		save : function(info, env){
			env.trigueEvent("savePlayerInformation", info.eventData);
		},
	}
	</scripts>

</html>

