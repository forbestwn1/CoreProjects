<!DOCTYPE html>
<html>
<body nosliwattribute_placeholder="id:pleaseEmbed">
	<br>
	SettingName : <%=?(nosliw_setting_name)?%>
	
	<div>
		<a href="" nosliw-event="click:submit:">Submit</a>
		<a href="" nosliw-event="click:new:">New</a>
		<a href="" nosliw-event="click:delete:" style="display:<%=?(nosliw_setting_persist)?==true?'inline':'none'%>">Delete</a>
		<a href="" nosliw-event="click:save:" style="display:<%=(!?(nosliw_setting_persist)?)||?(nosliw_setting_modified)?==true?'inline':'none'%>">Save</a>
	</div>

<!--	<nosliw-contextvalue/>  -->



	<div id="pleaseEmbed"/>
	<br>


</body>

	<scripts>
	{
		submit : function(info, env){
			event.preventDefault();
			env.trigueEvent("submitSetting", info.eventData);
		},
		new : function(info, env){
			event.preventDefault();
			env.trigueEvent("newSetting", info.eventData);
		},
		delete : function(info, env){
			event.preventDefault();
			env.trigueEvent("deleteSetting", info.eventData);
		},
		save : function(info, env){
			event.preventDefault();
			env.trigueEvent("saveSetting", info.eventData);
		},
	}
	</scripts>


	<contexts>
	{
		"group" : {
			"public" : {
				"element" : {
					"nosliw_setting_persist" : {
						"definition" : {
						},
						defaultValue: true
					},
					"nosliw_setting_modified" : {
						"definition" : {
						},
						defaultValue: true
					},
					"nosliw_setting_name" : {
						"definition" : {
						},
						defaultValue: ""
					},
				}
			}
		}
	}
	</contexts>

	<events>
	[
		{
			name : "submit",
			data : {
				element : {
				}
			}
		}
	]
	</events>

</html>

