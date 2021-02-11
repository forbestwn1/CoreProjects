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
					var node_createUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createUIDataOperationRequest");
					var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
					var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");

					var uiDataOperation = new node_UIDataOperation("ele", node_uiDataOperationServiceUtility.createGetOperationService(""));
					var opRequest = node_createUIDataOperationRequest(env.context, uiDataOperation, {
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
			var node_createBatchUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createBatchUIDataOperationRequest");
			var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
			
			var requestInfo = env.getServiceRequest("getSchoolData", {
				success : function(request){
				}
			});
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},

	}
	</scripts>

	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<contexts>
	{
		"group" : {
			"public" : {
				"element" : {
					schoolAttribute: {
						definition : {
							value : {
								dataTypeId: "test.string;1.0.0",
								value: "schoolName"
							}
						}
					},
					"schoolRating" : {
						"definition" : {
							"criteria" : "test.float;1.0.0"
						},
						"defaultValue" : {
							"dataTypeId": "test.float;1.0.0",
							"value": 9.0
						}
					},
					schoolType1: {
						definition : {
							value : {
								"dataTypeId": "test.string;1.0.0",
								"value": "Public",
							}
						}
					},
					schoolRating1: {
						definition : {
							value : {
								"dataTypeId": "test.float;1.0.0",
								"value": 9.0
							}
						}
					},

				}
			}
		}
	}
	</contexts>
	
	<services>
	[
		{
			"name" : "getSchoolData",
			"interface" : "schoolservice",
			"provider" : "getSchoolDataService",
			"dataMapping" :{
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
							"schoolListInService" : {
								"definition" : {
									"path" : "outputInService"
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
				"name": "getSchoolDataServiceStandard",
				"referenceId" : "schoolServiceStandard",
				"adaptor" : {
				}
			},
			{
				"name": "getSchoolDataServiceDiverse",
				"referenceId" : "schoolServiceDiverse",
				"adaptor" : {
					"dataMapping" :{
						"inputMapping" : {
							"element" : {
								"schoolTypeInService" : {
									"definition" : {
										"path" : "schoolTypeInServiceDiverse"
									}
								},
								"schoolRatingInService" : {
									"definition" : {
										"path" : "schoolRatingInServiceDiverse"
									}
								}
							}
						},
						"outputMapping" : {
							"success" : {
								"element" : {
									"schoolListInServiceDiverse" : {
										"definition" : {
											"path" : "schoolListInService"
										}
									}
								}
							}
						}
					}
				}
			}	
		],
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

