<!DOCTYPE html>
<html>
<body>
	<br>
	<br>

	<div id="<%=?(ui.id)?%>" data-role="page" data-fullscreen="true">

		<div data-role="header">
		   <a href="logout">Back</a>
		   <h1><%=?(ui.title)?%></h1>
		   <a href="settings" data-icon="gear">Refresh</a>
		</div>

		<div data-role="content" id="pleaseEmbed"/>

	</div>


</body>

	<contexts>
	{
		"group" : {
			"public" : {
				"element" : {
					"ui" : {
						"definition": {
							"child" : {
								"id" : {},
								"title" : {},
							}
						},
					}				
				}
			}
		}
	}
	</contexts>

</html>

