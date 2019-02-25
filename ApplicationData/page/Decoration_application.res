<!DOCTYPE html>
<html>
<body nosliwattribute_placeholder="id:pleaseEmbed">
	<br>
	<br>

	<div id="<%=?(ui.id)?%>" data-role="page" data-fullscreen="true">

		<div data-role="header">
		   <a href="" style="display:<%=?(backIconDisplay)?%>" nosliw-event="click:transferBack:">Back</a>
		   <h1><%=?(ui.title)?%></h1>
		   <a href="" data-icon="gear" nosliw-event="click:refresh:">Refresh</a>
		</div>

		<div data-role="content" id="pleaseEmbed"/>

	</div>


</body>

	<scripts>
	{
		transferBack : function(info, env){
			event.preventDefault();
			env.trigueEvent("transferBack", info.eventData);
		},
		refresh : function(info, env){
			event.preventDefault();
			env.trigueEvent("refresh", info.eventData);
		},
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
		},
		{
			name : "refresh",
			data : {
				element : {
				}
			}
		}
	]
	</events>

</html>

