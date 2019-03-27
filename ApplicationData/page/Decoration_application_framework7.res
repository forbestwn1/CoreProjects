<!DOCTYPE html>
<html>
<body nosliwattribute_placeholder="id:pleaseEmbed">
	<br>
	<br>

	<div class="page stacked" data-name="<%=?(nosliw_uiInfo.id)?%>">
		<div class="page-content">
			<div class="navbar">
			    <div class="navbar-inner">
			        <div class="left">
			            <a href="" class="link" style="display:<%=?(nosliw_uiStatus.index)?==0?'none':'inline'%>" nosliw-event="click:transferBack:">Back</a>
			        </div>
			        <div class="title"><%=?(nosliw_uiInfo.title)?%></div>
			        <div class="right">
			            <a href="" class="link" nosliw-event="click:refresh:">Refresh</a>
			        </div>
			    </div>
			</div>

			<div id="pleaseEmbed"/>
		</div>
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
