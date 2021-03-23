<!DOCTYPE html>
<html>
	<body>
<!--
	<br>
	<nosliw-submit title="Submit" datasource="school" parms="schoolType:criteria.schoolType;schoolRating:criteria.schoolRating"  output="result"/>  
	<br>
-->
	<div>
	Query
	<br>
	<nosliw-include source="Page_MySchool_Query"/> 
	</div>
	
	<br>
	<br><a href='' nosliw-event="click:refreshSchoolData:">Refresh</a><br>  
	<br>

	<div>
	Result
	<br>
	<nosliw-include source="Page_MySchool_SchoolList"/> 
	</div>

	<br>

	</body>

	<scripts>
	{
		refreshSchoolData : function(info, env){

			event.preventDefault();

			var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createBatchUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createBatchUIDataOperationRequest");
			var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
			var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
			var node_createContextVariableInfo = nosliw.getNodeData("uidata.context.createContextVariableInfo");
			
			var requestInfo = env.getServiceRequest("getSchoolData", {
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
				"name" : "getSchoolData",
				"provider" : "getSchoolDataService",
				"serviceMapping" :{
					"inputMapping" : {
						"element" : {
							"schoolTypeInService" : {
								"definition" : {
									"path" : "schoolType"
								}
							},
							"schoolRatingInService" : {
								"definition" : {
									"path" : "schoolRating"
								}
							}
						}
					},
					"outputMapping" : {
						"success" : {
							"element" : {
								"schoolList" : {
									"definition" : {
										"path" : "outputInService"
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
				"name" : "getSchoolDataService",
				"serviceId" : "schoolService"
			}		
		]
	}	
	</services>
	
</html>
