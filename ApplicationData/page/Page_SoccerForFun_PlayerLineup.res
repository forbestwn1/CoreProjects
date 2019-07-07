<!DOCTYPE html>
<html>
<body>
	
	<br>  
	<span style="color:green">当前阵容:</span>
	<br>  
	<nosliw-loop data="lineup.lineUp" element="spot" index="index">  
		<%=?(index)?%>.&nbsp;
		<nosliw-loop data="spot.players" element="player" index="index">  
			<%=?(player)?%>&nbsp;&nbsp; 
		</nosliw-loop>
		<br>  
	</nosliw-loop>

	<nosliw-loop data="lineup.lineUp1" element="spot" index="index">  
		<%=?(index)?+5%>.&nbsp;
		<nosliw-loop data="spot.players" element="player" index="index">  
			<%=?(player)?%>&nbsp;&nbsp; 
		</nosliw-loop>
		<br>  
	</nosliw-loop>
	
	<nosliw-loop data="lineup.lineUp2" element="spot" index="index">  
		<%=?(index)?+8%>.&nbsp;
		<nosliw-loop data="spot.players" element="player" index="index">  
			<%=?(player)?%>&nbsp;&nbsp; 
		</nosliw-loop>
		<br>  
	</nosliw-loop>
	
	<nosliw-loop data="lineup.lineUp3" element="spot" index="index">  
		<%=?(index)?+12%>.&nbsp;
		<nosliw-loop data="spot.players" element="player" index="index">  
			<%=?(player)?%>&nbsp;&nbsp; 
		</nosliw-loop>
		<br>  
	</nosliw-loop>
	
	<nosliw-loop data="lineup.lineUp4" element="spot" index="index">  
		<%=?(index)?+16%>.&nbsp;
		<nosliw-loop data="spot.players" element="player" index="index">  
			<%=?(player)?%>&nbsp;&nbsp; 
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
							    {
							      "vacant" : true,
							      "players": [
							        "ning"
							      ]
							    },
							    {
							      "vacant" : false,
							      "players": [
							        "Wilson"
							      ]
							    },
							    {
							      "vacant" : true,
							      "players": [
							        "kaida", "tom"
							      ]
							    },
							    {
							      "vacant" : true,
							      "players": [
							        "ning"
							      ]
							    },
							    {
							      "vacant" : false,
							      "players": [
							        "Wilson"
							      ]
							    },
							    {
							      "vacant" : true,
							      "players": [
							        "kaida", "tom"
							      ]
							    },
							    {
							      "vacant" : true,
							      "players": [
							        "ning"
							      ]
							    },
							    {
							      "vacant" : false,
							      "players": [
							        "Wilson"
							      ]
							    },
							    {
							      "vacant" : true,
							      "players": [
							        "kaida", "tom"
							      ]
							    },
							    {
							      "vacant" : true,
							      "players": [
							        "ning"
							      ]
							    },
							    {
							      "vacant" : false,
							      "players": [
							        "Wilson"
							      ]
							    },
							    {
							      "vacant" : true,
							      "players": [
							        "kaida", "tom"
							      ]
							    },
							    {
							      "vacant" : true,
							      "players": [
							        "ning"
							      ]
							    },
							    {
							      "vacant" : false,
							      "players": [
							        "Wilson"
							      ]
							    },
							    {
							      "vacant" : true,
							      "players": [
							        "kaida", "tom"
							      ]
							    },
							    {
							      "vacant" : true,
							      "players": [
							        "ning"
							      ]
							    },
							    {
							      "vacant" : false,
							      "players": [
							        "Wilson"
							      ]
							    },
							    {
							      "vacant" : true,
							      "players": [
							        "kaida", "tom"
							      ]
							    },
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
			var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createBatchUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createBatchUIDataOperationRequest");
			var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
			var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
			var node_createContextVariableInfo = nosliw.getNodeData("uidata.context.createContextVariableInfo");
			
			var requestInfo = env.getServiceRequest("lineupService", {
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
		],
		"provider" : [
			{	
				"name" : "lineupService",
				"serviceId" : "lineupService"
			}		
		]
	}	
	</services>

</html>
