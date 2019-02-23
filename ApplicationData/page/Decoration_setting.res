<!DOCTYPE html>
<html>
<body>
	<br>

	<div data-role="content" id="pleaseEmbed"/>
	<br>

    <a href="settings" data-icon="gear" nosliw-event="click:submit:">Submit</a>

</body>

	<scripts>
	{
		submit : function(info, env){
			event.preventDefault();
			env.trigueEvent("submit", info.eventData);
		}
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

