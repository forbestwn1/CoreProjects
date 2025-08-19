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

	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<contexts>
	{
		"group" : {
			"public" : {
				"element" : {
					"schoolList" : {
						"definition" : {
							"criteria" : "test.array;1.0.0%%||element:test.map;1.0.0%%||geo:test.geo;1.0.0,schoolName:test.string;1.0.0,schoolRating:test.float;1.0.0||%%||%%"
						},
						"defaultValue" : {
							"dataTypeId": "test.array;1.0.0",
							"value": [
								{
									"dataTypeId": "test.map;1.0.0",
									"value": {
										"schoolName" : {
											"dataTypeId": "test.string;1.0.0",
											"value": "School1"
										},
										"schoolRating" : {
											"dataTypeId": "test.float;1.0.0",
											"value": 6.0
										},
										"geo" : {
											"dataTypeId": "test.geo;1.0.0",
											"value": {
												"latitude" :  43.651299,
												"longitude" : -79.579473
											}
										}
									}
								},
								{
									"dataTypeId": "test.map;1.0.0",
									"value": {
										"schoolName" : {
											"dataTypeId": "test.string;1.0.0",
											"value": "School2"
										},
										"schoolRating" : {
											"dataTypeId": "test.float;1.0.0",
											"value": 8.5
										},
										"geo" : {
											"dataTypeId": "test.geo;1.0.0",
											"value": {
												"latitude" :  43.649016, 
												"longitude" : -79.485059
											}
										}
									}
								}					
							]
						}
					},
					schoolAttribute: {
						definition : {
							value : {
								dataTypeId: "test.string;1.0.0",
								value: "schoolName"
							}
						}
					},
					"schoolType" : {
						"definition" : {
							"criteria" : "test.string;1.0.0"
						},
						"defaultValue" : {
							"dataTypeId": "test.string;1.0.0",
							"value": "Public",
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
	]
	</services>

	<attachment>
	{
		"service" : [
			{
				"name": "getSchoolDataService",
				"referenceId" : "schoolService"
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

