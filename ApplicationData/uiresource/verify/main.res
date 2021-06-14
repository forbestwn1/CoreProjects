<!DOCTYPE html>
<html>
<body>
    Within test.main.res
    
    	<br>
	<br>
    	SERVICE SUBMIT: 	<a href='' nosliw-event="click:submitSimpleServiceWithoutInterface">Submit</a>
	<br>
    
</body>

	<script>
	{
		submitSimpleServiceWithoutInterface : function(info, env){

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

				}
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
				"enhanceContext" : "false"
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
								"path" : "aaa"
							}
						}
					}
				},
				"outputMapping" : {
					"success" : {
						"mapping" : {
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
		"context" : [
		]
	}
	</attachment>

	<event>
	[
	]
	</event>
	
</html>

