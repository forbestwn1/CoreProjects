<!DOCTYPE html>
<html>
<body>
		<br>
		Sum:<%=#|?(schoolList)?.length()|#.value%> 

		<br>
		<nosliw-loop data="schoolList" element="ele" index="index">  
			Index: <%=?(index)?%>
			<br>  1111
			SchoolName: <%=#|?(ele)?.getChildData(name:&(schoolAttribute)&)|#.value%>
			<a href='' nosliw-event="click:getSchoolInfo:">Info</a>
			<br>
			
			<scripts>
			{
				getSchoolInfo : function(info, env){
					event.preventDefault();
				
					var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
					var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
					var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
					var node_createUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createUIDataOperationRequest");
					var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
					var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
					var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
					var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");

					var uiDataOperation = new node_UIDataOperation("ele", node_uiDataOperationServiceUtility.createGetOperationService(""));
					var opRequest = node_createUIDataOperationRequest(this.getContext(), uiDataOperation, {
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

				}
			}
		}
	}
	</contexts>
	
	
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

