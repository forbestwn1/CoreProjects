<!DOCTYPE html>
<html>
<body>
<div style="height:1500px">
	
	<br>  
	<span style="color:green">当前阵容:</span>
	<br>  
	<nosliw-loop data="lineup.lineUp" element="spot" index="index">  
		<%=?(index)?+1%>.&nbsp;
		<nosliw-loop data="spot.players" element="player" index="index">  
			<span style="display:inline-block;width:90px;min-width:90px;max-width:90px;"><%=?(player)?%></span> 
		</nosliw-loop>
		<br>  
	</nosliw-loop>

	<br>  
	<span style="color:red">Waiting List:</span>
	<br>  
	<nosliw-loop data="lineup.waitingList" element="spot" index="index">
		<%=?(index)?%>.&nbsp;
		<%=?(spot)?%>
		<br>  
	</nosliw-loop>
</div>
</body>

	<contexts>
	{
		"group" : {
			"public" : {
				"element" : {
					"lineup" : {
						"definition" : {
						},
						"defaultValue": {
							  "waitingList": ["Peter", "David"],
							  "lineUp": [
							  ],
						}
					},
				}
			}
		}
	}
	</contexts>

	<scripts>
	{
		command_updateData : function(data, request, env){
			var node_createContextVariable = nosliw.getNodeData("variable.context.createContextVariable");
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createBatchValueInVarOperationRequest = nosliw.getNodeData("variable.valueinvar.operation.createBatchValueInVarOperationRequest");
			var node_ValueInVarOperation = nosliw.getNodeData("variable.valueinvar.operation.ValueInVarOperation");
			var node_valueInVarOperationServiceUtility = nosliw.getNodeData("variable.valueinvar.operation.valueInVarOperationServiceUtility");
			var node_createContextVariableInfo = nosliw.getNodeData("variable.context.createContextVariableInfo");
			
			var requestInfo = env.getServiceRequest("lineupService", {
				success : function(request){
				}
			});
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},

	}
	</scripts>

	<services>
	[
		{
			"name" : "lineupService",
			"provider" : "lineupService",
			"serviceMapping" :{
				"inputMapping" : {
					"element" : {
					}
				},
				"outputMapping" : {
					"success" : {
						"element" : {
							"lineup" : {
								"definition" : {
									"path" : "lineup.value"
								}
							}
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
				"name" : "lineupService",
				"id" : "lineupService"
			}		
		]
	}
	</attachment>

</html>
