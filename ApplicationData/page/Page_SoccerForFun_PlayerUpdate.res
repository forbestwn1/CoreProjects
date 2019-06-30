<!DOCTYPE html>
<html>
<body>
<%=?(player)?.value==''+''%>
	<nosliw-switch value="<%=?(player)?.value==''+''%>">
		<nosliw-case value="false">
			<br>
			<%=?(player)?.value%>目前在 :  <%=?(status)?.value%>
		
			<br>
			<br>
			你可以选择: 
			<a href='' nosliw-event="click:update:"><%=?(action)?.value%></a>
		</nosliw-case>

		<nosliw-case value="true">
			你还没有注册你的名字，请到球员信息注册你的名字！！
		</nosliw-case>

	</nosliw-switch>

	

<!--
	<nosliw-switch value="<%=?(action)?.value%>">
		<nosliw-case value="offer">
			<a href='' nosliw-event="click:update:">Offer</a>
		</nosliw-case>

		<nosliw-case value="withdraw">
			<a href='' nosliw-event="click:update:">withdraw</a>
		</nosliw-case>

		<nosliw-case value="lookingfor">
			<a href='' nosliw-event="click:update:">lookingfor</a>
		</nosliw-case>

	</nosliw-switch>
-->

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
