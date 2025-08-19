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

	<script>
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
	</script>

	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<context>
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
	</context>
	
	<service>
	[
		{
			"name" : "getSchoolData",
			"interface" : "schoolservice",
			"provider" : "getSchoolDataServiceStandard",
			"dataMapping" :{
				"inputMapping" : {
					"element" : {
						"schoolTypeInService" : {
							"definition" : {
								"path" : "schoolType"
							}
						},
					}
				},
				"outputMapping" : {
					"success" : {
						"element" : {
							"schoolList" : {
								"definition" : {
									"path" : "schoolListInService"
								}
							}
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

	<event>
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
	</event>
	
</html>

