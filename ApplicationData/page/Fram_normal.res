<!DOCTYPE html>
<html>
<body>
	<br>
	<br>

	<div data-role="page" data-fullscreen="true">

		<div data-role="header">
		   <nosliw-back href="logout">Back</nosliw-back>
		   <h1>Title</h1>
		   <nosliw-refresh href="settings" data-icon="gear">Refresh</nosliw-refresh>
		</div>

		<div data-role="content">
			<nosliw-include source="Example_Object_Basic_Include" context="element=business.a.aa" /> 
		</div>

	</div>


</body>

	<contexts>
	{
		"group" : {
			"public" : {
				"element" : {
					"schoolType" : {
						"definition" : {
							"criteria" : "test.options;1.0.0"
						},
						"defaultValue" : {
							"dataTypeId": "test.options;1.0.0",
							"value": {
								"value" : "Public",
								"optionsId" : "schoolType"
							}
						}
					},
					"schoolRating" : {
						"definition" : {
							"criteria" : "test.float;1.0.0"
						},
						"defaultValue" : {
							"dataTypeId": "test.float;1.0.0",
							"value": 9.0
						}
					}
					
				}
				
			}
		
		}
	}
	</contexts>

</html>

