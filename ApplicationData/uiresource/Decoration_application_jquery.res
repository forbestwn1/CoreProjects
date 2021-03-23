<!DOCTYPE html>
<html>
<body nosliwattribute_placeholder="id:pleaseEmbed">
	<br>
	<br>

	<div id="<%=?(nosliw_uiInfo.id)?%>" data-role="page" data-fullscreen="true">

		<div data-role="header">
		   <a href="" style="display:<%=?(nosliw_uiStatus.index)?==0?'none':'inline'%>" nosliw-event="click:transferBack:">Back</a>
		   <h1><%=?(nosliw_uiInfo.title)?%></h1>
		   <a href="" data-icon="gear" nosliw-event="click:refresh:">Refresh</a>
		</div>

		<div data-role="content" id="pleaseEmbed"/>

	</div>


</body>

	<scripts>
	{
		transferBack : function(info, env){
			event.preventDefault();
			env.trigueEvent("nosliw_transferBack", info.eventData);
		},
		refresh : function(info, env){
			event.preventDefault();
			env.trigueEvent("nosliw_refresh", info.eventData);
		},
	}
	</scripts>


	<contexts>
	{
		"group" : {
			"public" : {
				"element" : {
					"nosliw_uiInfo" : {
						"definition": {
							"child" : {
								"id" : {},
								"title" : {}
							}
						}
					},
					"nosliw_uiStatus" : {
						"definition": {
							"child" : {
								"index" : {},
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

