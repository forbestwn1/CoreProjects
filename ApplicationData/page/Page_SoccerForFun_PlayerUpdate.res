<!DOCTYPE html>
<html>
<body>
	
	<%=?(player)?.value%> status:  <%=?(status)?.value%>

	<br>
	Action: 
	<br>
	<nosliw-switch value="<%=?(action)?.value%>">
		<nosliw-case value="offer">
			<br>
			<br><a href='' nosliw-event="click:update:">Offer</a><br>
			<br>
		</nosliw-case>

		<nosliw-case value="withdraw">
			<br>
			<br><a href='' nosliw-event="click:update:">withdraw</a><br>
			<br>
		</nosliw-case>

		<nosliw-case value="lookingfor">
			<br>
			<br><a href='' nosliw-event="click:update:">lookingfor</a><br>
			<br>
		</nosliw-case>

	</nosliw-switch>

	<nosliw-contextvalue/>

</body>

	<contexts>
	{
		"group" : {
			"public" : {
				"element" : {
					"player" : {
						"definition" : {
							"criteria":"test.string;1.0.0"
						},
						"defaultValue": {
							dataTypeId: "test.string;1.0.0",
							value: "peter"
						}
					},
					"status" : {
						"definition" : {
							"criteria":"test.string;1.0.0"
						},
					},
					"action" : {
						"definition" : {
							"criteria":"test.string;1.0.0"
						},
					},
				}
			}
		}
	}
	</contexts>

	<scripts>
	{
		update : function(info, env){

			event.preventDefault();

			var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createBatchUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createBatchUIDataOperationRequest");
			var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
			var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
			var node_createContextVariableInfo = nosliw.getNodeData("uidata.context.createContextVariableInfo");
			
			var requestInfo = env.getServiceRequest("updateLineupService", {
				success : function(request){
				}
			});
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},
		
		command_updateData : function(data, request, env){
			event.preventDefault();

			var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createBatchUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createBatchUIDataOperationRequest");
			var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
			var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
			var node_createContextVariableInfo = nosliw.getNodeData("uidata.context.createContextVariableInfo");
			
			var requestInfo = env.getServiceRequest("updateLineupService", {
				success : function(request){
				}
			});
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},
		
	}
	</scripts>

	<services>
	{
		"use" : [
			{
				"name" : "updateLineupService",
				"provider" : "updateLineupService",
				"serviceMapping" :{
					"inputMapping" : {
						"element" : {
							"player" : {
								"definition" : {
									"path" : "player"
								}
							},
							"action" : {
								"definition" : {
									"path" : "action"
								}
							}
						}
					},
					"outputMapping" : {
						"success" : {
							"element" : {
								"status" : {
									"definition" : {
										"path" : "status"
									}
								},
								"action" : {
									"definition" : {
										"path" : "action"
									}
								},
							}
						}
					}
				}
			}
		],
		"provider" : [
			{	
				"name" : "updateLineupService",
				"serviceId" : "updateLineupService"
			}		
		]
	}	
	</services>

</html>
