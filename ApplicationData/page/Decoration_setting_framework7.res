<!DOCTYPE html>
<html>
<body nosliwattribute_placeholder="id:pleaseEmbed">
	<br>
	<div>
		<a href="" nosliw-event="click:new:">New</a>
		<a href="" nosliw-event="click:delete:">Delete</a>
		<a href="" nosliw-event="click:save:">Save</a>
	</div>

	<div data-role="content" id="pleaseEmbed"/>
	<br>

    <a href="" nosliw-event="click:submit:">Submit</a>

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

