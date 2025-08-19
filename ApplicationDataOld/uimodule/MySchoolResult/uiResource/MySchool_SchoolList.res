<!DOCTYPE html>
<html>
<body>
	<br>
	<br><a href='' nosliw-event="click:refreshSchoolData:">Refresh</a><br>
	SchoolRating:<nosliw-float data="schoolRating"/>  
	<br>
	<br>
	SchoolType:<nosliw-string data="schoolType"/>
	<br>
	<br>
		<br>
		Sum:<%=#|?(schoolList)?.length()|#.value%> 

		<br>
		<nosliw-loop data="schoolList" element="ele" index="index">  
			Index: <%=?(index)?%>
			<br>  
			SchoolName: <%=#|?(ele)?.getChildData(name:&(schoolAttribute)&)|#.value%>
			<a href='' nosliw-event="click:getSchoolInfo:">Info</a>
			<br>
			
			<scripts>
			{
				getSchoolInfo : function(info, env){
					event.preventDefault();
				
					var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
					var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
					var node_createValueInVarOperationRequest = nosliw.getNodeData("variable.valueinvar.operation.createValueInVarOperationRequest");
					var node_ValueInVarOperation = nosliw.getNodeData("variable.valueinvar.operation.ValueInVarOperation");
					var node_valueInVarOperationServiceUtility = nosliw.getNodeData("variable.valueinvar.operation.valueInVarOperationServiceUtility");

					var uiDataOperation = new node_ValueInVarOperation("ele", node_valueInVarOperationServiceUtility.createGetOperationService(""));
					var opRequest = node_createValueInVarOperationRequest(env.context, uiDataOperation, {
						success : function(request, data){
							env.trigueEvent("selectSchool", data);
						}
					});
					node_requestServiceProcessor.processRequest(opRequest, false);
				}
			}
			</scripts>
			
		</nosliw-loop>
		
</body>

	<scripts>
	{
		refreshSchoolData : function(info, env){

			event.preventDefault();

			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createBatchValueInVarOperationRequest = nosliw.getNodeData("variable.valueinvar.operation.createBatchValueInVarOperationRequest");
			var node_ValueInVarOperation = nosliw.getNodeData("variable.valueinvar.operation.ValueInVarOperation");
			
			var requestInfo = env.getServiceRequest("getSchoolData", {
				success : function(request){
				}
			});
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},

	}
	</scripts>

	
	<attachment>
	{
		"data" : [
			{
				"name": "schoolAttribute",
				"entity" : {
					dataTypeId: "test.string;1.0.0",
					value: "schoolName"
				}
			}
		]
	}
	</attachment>

	<events>
	[
		{
			name : "selectSchool",
			data : {
				element : {
					data : {
						definition : {
							path: "schoolList.element"
						}
					}
				}
			}
		}
	]
	</events>
	
</html>

