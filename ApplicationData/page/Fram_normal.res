<!DOCTYPE html>
<html>
<body>
	<br>
	<br>

	<div id="<%=?(ui.id)?%>" data-role="page" data-fullscreen="true">

		<div data-role="header">
		   <a href="logout" style="display:<%=?(backIconDisplay)?%>" nosliw-event="click:transferBack:">Back</a>
		   <h1><%=?(ui.title)?%></h1>
		   <a href="settings" data-icon="gear">Refresh</a>
		</div>

		<div data-role="content" id="pleaseEmbed"/>

	</div>


</body>

	<scripts>
	{
		transferBack : function(info, env){
			env.trigueEvent("transferBack", info.eventData);
		}
	}
	</scripts>


	<contexts>
	{
		"group" : {
			"public" : {
				"element" : {
					"ui" : {
						"definition": {
							"child" : {
								"id" : {},
								"title" : {}
							}
						}
					},
					"backIconDisplay" : {
						"definition" : {
						},
						"defaultValue" : "none"
					}
				}
			}
		}
	}
	</contexts>

	<events>
	[
		{
			name : "transferBack",
			data : {
				element : {
				}
			}
		}
	]
	</events>

</html>

