<!DOCTYPE html>
<html>
<body>

<!--
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
-->

	
			<br>
			<%=?(status)?.value%>
		
			<br>
			<br>
			 
			你可以选择:<a href='' nosliw-event="click:update:"><%=?(action)?.value%></a>

			<br>
			<br>
			<br>
			<%=?(oweTo)?.value%>
			<br>
			<br>
			<%=?(oweFrom)?.value%>
			<br>

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
					"oweTo" : {
						"definition" : {
							"criteria":"test.string;1.0.0"
						},
					},
					"oweFrom" : {
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

			var node_createContextVariable = nosliw.getNodeData("variable.context.createContextVariable");
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createBatchValueInVarOperationRequest = nosliw.getNodeData("variable.valueinvar.operation.createBatchValueInVarOperationRequest");
			var node_ValueInVarOperation = nosliw.getNodeData("variable.valueinvar.operation.ValueInVarOperation");
			var node_valueInVarOperationServiceUtility = nosliw.getNodeData("variable.valueinvar.operation.valueInVarOperationServiceUtility");
			var node_createContextVariableInfo = nosliw.getNodeData("variable.context.createContextVariableInfo");
			
			var requestInfo = env.getServiceRequest("updateLineupService", {
				success : function(request){
				}
			});
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},
		
		command_updateData : function(data, request, env){

			var node_createContextVariable = nosliw.getNodeData("variable.context.createContextVariable");
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createBatchValueInVarOperationRequest = nosliw.getNodeData("variable.valueinvar.operation.createBatchValueInVarOperationRequest");
			var node_ValueInVarOperation = nosliw.getNodeData("variable.valueinvar.operation.ValueInVarOperation");
			var node_valueInVarOperationServiceUtility = nosliw.getNodeData("variable.valueinvar.operation.valueInVarOperationServiceUtility");
			var node_createContextVariableInfo = nosliw.getNodeData("variable.context.createContextVariableInfo");
			
			var requestInfo = env.getServiceRequest("updateLineupService", {
				success : function(request){
				}
			}, request);
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},
		
	}
	</scripts>

	<services>
	[
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
							"oweTo" : {
								"definition" : {
									"path" : "oweTo"
								}
							},
							"oweFrom" : {
								"definition" : {
									"path" : "oweFrom"
								}
							},
						}
					}
				}
			}
		}
	]	
	</services>

	<attachment>
	{
		"service" : [
			{	
				"name" : "updateLineupService",
				"id" : "updateLineupService"
			}		
		]
	}
	</attachment>

</html>
