<!DOCTYPE html>
<html>
<body>
	
	Your status: lineup/provider/waitingList/nothing
	
	Provide
	Looking for
	Quite waiting list

</body>

	<contexts>
	{
		"group" : {
			"public" : {
				"element" : {
					"player" : {
						"definition" : {
							"child" : {
								"registered" : {criteria:"test.boolean;1.0.0"},
								"name" : {criteria:"test.string;1.0.0"},
								"email" : {criteria:"test.string;1.0.0"},
							}
						},
						"defaultValue": {
							registered : {
								dataTypeId: "test.boolean;1.0.0",
								value: false
							},
							name : {
								dataTypeId: "test.string;1.0.0",
								value: "Wilson"
							},
							email : {
								dataTypeId: "test.string;1.0.0",
								value: "wilson@hotmail.com"
							},
						}
					},
					"lineup" : {
					
					}
				}
			}
		}
	}
	</contexts>

	<scripts>
	{
		save : function(info, env){
			env.trigueEvent("savePlayerInformation", info.eventData);
		},
	}
	</scripts>


</html>

