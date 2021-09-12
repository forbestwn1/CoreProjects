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

	<component>
	{
		"valuestructure":
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
		},
		
		"task" : [
			{
				"name" : "trigueEvent",
				"taskType": "activity",
				"activityType": "Event_trigue",
				"configuration" : {
					"eventName" : "event1"
				}
			},
		],
		
		"event" : [
			{
				"name" : "event1",
				"output" : [
					{
						"name" : "eventData1",
						"reference" : "aaa"
					}
				]
			}
		],
		
	}
	</component>
</html>

