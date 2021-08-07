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
		submitSimpleServiceWithoutInterface1 : function(info, env){

			event.preventDefault();

			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
			
//			var out = node_createServiceRequestInfoSequence(undefined);
			var out =  env.getServiceRequest("simpleServiceWithoutInterface", {
				success : function(request){
				}
			});

			node_requestServiceProcessor.processRequest(out, false);
		},
	}
	</script>

	<task>
	{
		"submitSimpleServiceWithoutInterface2":{
			"taskType": "activity",
			"activityType": "Service_request",
			"configuration" : {
				"serviceUse" : "simpleServiceWithoutInterface"
			}
		},
	
		"trigueEvent":{
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
							"value": "This is my world!"
						}
					},

					"bbb":{
						definition : {
							"criteria": "test.string;1.0.0",
						},
						"defaultValue": {
							"dataTypeId": "test.string;1.0.0",
							"value": "This is my world!"
						}
					},
				},
				"reference" : [
					"internal", "*forlistservice_1_ex"
				]
			}
		}
	}
	</valuestructure>

	
	<contextref>
	[
	]
	</contextref>

	
	<service>
	[
		{
			"name" : "simpleServiceWithoutInterface",
			"status" : "enable",
			"provider" : "simpleServiceWithInterfaceProvider",
			"info" : {
				"enhanceContext" : "true"
			},
			"dataMapping" :{
				"inputMapping" : {
					"mapping" : {
						"parm1" : {
							"definition" : {
								"path" : "aaa"
							}
						},
						"parm2" : {
							"definition" : {
								"path" : "ccc"
							}
						}
					}
				},
				"outputMapping" : {
					"success" : {
						"mapping" : {
							"ddd" : {
								"definition" : {
									"path" : "simpleOutput2"
								}
							}
						}
					}
				}
			}
		}
	]
	</service>


	<attachment>
	{
		"service" : [
			{
				"name": "simpleServiceWithoutInterfaceProvider",
				"referenceId" : "simpleoutput_refinterface"
			},	
			{
				"name": "simpleServiceWithInterfaceProvider",
				"referenceId" : "simpleoutput_internalinterface"
			},	
		],
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
		"valuestructure" : [
			{
				"name": "internal",
				"entity": {
					"internal": {
						"definition": {
							"criteria": "test.float;1.0.0"
						},
						"defaultValue": {
							"dataTypeId": "test.string;1.0.0",
							"value": "Internal variable!"
						}
					},
				} 
			},
		]
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
	
	<command>
	[
		{
			"name" : "command1",
			"task" : "commandTask1",
			"request" : [
				{
					"name" : "parm1",
					"displayName" : "Parm1",
					"dataInfo" : {
						"refPath": "aaa",
					},
				},			
			],
			"result" : {
				"success" : {
					"output" : {
						"output1" : {
							"definition" : {
								"path": "aaa"
							}
						}
					}
				}
			}
		}
	]
	</command>
	
</html>

