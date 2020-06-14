<!DOCTYPE html>
<html>
<body>
			<nosliw-loop data="business.a.cc" element="ele" index="index">  


			<br>
			Index: <%=?(index)?%>
			<br>
			TextInput:<nosliw-textinput data="ele"/> 
			<br>
			<scripts>
			{
				deleteElementInLoop : function(data, info){
					event.preventDefault();
				
					var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
					var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
					var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
					var node_createBatchUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createBatchUIDataOperationRequest");
					var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
					var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
					var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
					var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");

					
							var opRequest = node_createBatchUIDataOperationRequest(this.getContext());
							var uiDataOperation = new node_UIDataOperation("ele", node_uiDataOperationServiceUtility.createDeleteOperationService(""));
							opRequest.addUIDataOperation(uiDataOperation);
					node_requestServiceProcessor.processRequest(opRequest, false);
					
/*					
					var requestInfo = node_createServiceRequestInfoSequence({}, {
						success:function(requestInfo, data){
							
						}
					});
					var that = this;
					requestInfo.addRequest(this.getContext().getDataOperationRequest("index", node_uiDataOperationServiceUtility.createGetOperationService(), {
						success : function(request, data){
							var elePath = data.value;
						
							var opRequest = node_createBatchUIDataOperationRequest(that.getContext());
							var uiDataOperation = new node_UIDataOperation("ele", node_uiDataOperationServiceUtility.createDeleteOperationService(""));
							opRequest.addUIDataOperation(uiDataOperation);
							return opRequest;
						}
					}));
					node_requestServiceProcessor.processRequest(requestInfo, false);
*/					
				}
			}
			</scripts>
			
		</nosliw-loop>
  
</body>

	<scripts>
	{
	}
	</scripts>
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<contexts>
	{
		group : {
			public : {
				element : {
					business : {
						definition: {
							child : {
								a : {
									child : {
										aa : {criteria:"test.string;1.0.0"},
										cc : {criteria:"test.array;1.0.0%||element:test.string;1.0.0||%"},
									}
								}
							}
						},
						defaultValue: {
							a : {
								aa : {
									dataTypeId: "test.string;1.0.0",
									value: "This is my world!"
								},
								cc : [
									{
										dataTypeId: "test.string;1.0.0",
										value: "This is my world 1111!"
									},
									{
										dataTypeId: "test.string;1.0.0",
										value: "This is my world 2222!"
									}
								],
								ee : {
									dataTypeId: "test.array;1.0.0",
									value: [
										{
											dataTypeId: "test.string;1.0.0",
											value: "This is my world 1111!"
										},
										{
											dataTypeId: "test.string;1.0.0",
											value: "This is my world 2222!"
										}
									]
								}
							}
						}
					},
					from: {
						definition : {
							value : {
								dataTypeId: "test.integer",
								value: 3
							}
						}
					},
					to: {
						definition:{
							value : {
								dataTypeId: "test.integer",
								value: 7
							}
						}
					}
				}
			}
		}
	}
	</contexts>
	
	<events>
	[
		{
			name : "changeInputText",
			data : {
				element : {
					data : {
						definition : {
							path: "business.a.aa"
						}
					}
				}
			}
		}
	]
	</events>
	
	<commands>
	[
		{
			name : "Start",
			parm : {
				element : {
					data : {
						definition : {
							path: "business.a.aa"
						}
					}
				}
			}
		}
	]
	</commands>
	
	<attachment>
	{
		"expression" : [
		],
		
		"data": [
					
		]
	}
	</attachment>

</html>
