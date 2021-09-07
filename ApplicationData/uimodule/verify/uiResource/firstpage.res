<!DOCTYPE html>
<html>
<body>
    Within first page
    
    	<br>
	EXPRESSION IN CONTENT :<%=?(aaa)?.value + '   6666 ' %>
	<br>
    	TRIGUE EVENT: 	<a href='' nosliw-event="click:trigueEvent">Submit</a>
	<br>
    
</body>

	<script>
	{
	}
	</script>

	<task>
	[
		{
			"name" : "trigueEvent",
			"taskType": "activity",
			"activityType": "Event_trigue",
			"configuration" : {
				"eventName" : "event1"
			}
		},
	
	]
	</task>

	<valuestructure>
	{
		"group" : {
			"public" : {
				"flat" : {
					"aaa":{
						definition : {
							"criteria": "test.string;1.0.0",
						},
						"defaultValue": {
							"dataTypeId": "test.string;1.0.0",
							"value": "This is my world aaa!"
						}
					},
				},
			}
		}
	}
	</valuestructure>

	
	<contextref>
	[
	]
	</contextref>

	
	<attachment>
	{
	}
	</attachment>

	<event>
	[
		{
			"name" : "event1",
			"output" : [
				{
					"name" : "eventData1",
					"reference" : "aaa"
				}
			]
		}
	]
	</event>
	
	<commands>
	[
	]
	</commands>
	
</html>

