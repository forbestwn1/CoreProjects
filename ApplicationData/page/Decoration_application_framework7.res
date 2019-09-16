<!DOCTYPE html>
<html>
<body nosliwattribute_placeholder="id:pleaseEmbed">
		<div class="page-content">
			<div class="navbar">
			    <div class="navbar-inner">
			        <div class="left">
			            <a href="" class="link" style="display:<%=?(nosliw_runtime_status.index)?==0?'none':'inline'%>" nosliw-event="click:transferBack:">Back</a>
			        </div>
			        <div class="title"><%=?(nosliw_ui_info.title)?%></div>
			        <div class="right">
<!--			            <a href="" class="link" nosliw-event="click:refresh:">Refresh</a>  -->
			        </div>
			    </div>
			</div>

			<div id="pleaseEmbed"/>
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
					"nosliw_ui_info" : {
						"definition": {
							"child" : {
								"id" : {},
								"title" : {}
							}
						}
					},
					"nosliw_runtime_status" : {
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

