<!DOCTYPE html>
<html>
<body>
    Within test.main.res
    
    	<br>
	EXPRESSION IN CONTENT :<%=?(aaa)?.value + '   6666 ' %>
	<br>
	EXPRESSION IN CONTENT :<%=?(ddd)?.value + '   6666 ' %>
	<br>
	EXPRESSION IN CONTENT :<%=?(ccc)?.value + '   6666 ' %>
	<br>
	EXPRESSION IN CONTENT :<%=?(internal)?.value + '   6666 ' %>
	<br>
	EXPRESSION IN CONTENT :<%=?(forlistservice_1_ex_parm1)?.value + '   6666 ' %>
	
	<br>
	CUSTOM TAG:<nosliw-string data="ccc"/>  
	<br>
    	SERVICE SUBMIT: 	<a href='' nosliw-event="click:submitSimpleServiceWithoutInterface1">Submit</a>
	<br>
	<br>
    	SERVICE SUBMIT: 	<a href='' nosliw-event="click:submitSimpleServiceWithoutInterface2">Submit</a>
	<br>
	<br>
    	TRIGUE EVENT: 	<a href='' nosliw-event="click:trigueEvent">Submit</a>
	<br>
    
</body>

	<script>
	{
	}
	</script>

	<task>
	{
		"select1":{
			"taskType": "activity",
			"activityType": "Event_trigue",
			"configuration" : {
				"eventName" : "event1"
			}
		},
	
	}
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

					"bbb":{
						definition : {
							"criteria": "test.string;1.0.0",
						},
						"defaultValue": {
							"dataTypeId": "test.string;1.0.0",
							"value": "This is my world bbb!"
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
		"dataexpression" : [
		],
		"value" : [
			{
				"name": "constantFromAtt1",
				"entity" : 
				{
					"value" : {
						dataTypeId: "test.string;1.0.0",
						value: "Constant in attachment"
					}
				}
			},
			{
				"name": "constantFromAtt2",
				"entity" : 
				{
					"value" : {
							dataTypeId: "test.integer",
							value: 15
					}
				}
			},
		],
	}
	</attachment>

	<event>
	[
		{
			"name" : "event1",
			"value" : {
				"eventData1" : {
					"definition" : {
						"path": "aaa"
					}
				}
			}
		}
	]
	</event>
	
	<commands>
	[
	]
	</commands>
	
</html>

