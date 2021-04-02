<!DOCTYPE html>
<html>
<body>
    Within test.main.res
	<!-- Constant  -->
	<br>
	CONSTANT FROM CONTEXT:<%=&(constantFromContext6)&.value + '' %>  
	<br>
	CONSTANT FROM CONTEXT:<%=&(constantFromContext4)& + '' %>  
	<br>
	CONSTANT FROM CONTEXT:<%=&(constantFromContext3)&.c + '' %>  
	<br>
	CONSTANT FROM ATTACHMENT:<%=&(constantFromAtt1)&.value + '' %>  
	<br>
	
	<!-- Expression  -->
	<br>
	EXPRESSION IN CONTENT :<%=?(varFromContext1.attr1.attr11)?.value + '   6666 ' %>
	<br>
	EXPRESSION IN CONTENT:<%=#|?(varFromContext1)?.attr1.attr11.subString(from:&(constantFromContext8)&,to:&(constantFromContext9)&)|#.value + ?(varFromContext1.attr1.attr14)? + ' 6666 ' %>
	<br>
	EXPRESSION IN ATTRIBUTE:<span  style="color:<%=#|?(varFromContext1)?.attr1.attr11.subString(from:&(constantFromContext8)&,to:&(constantFromContext9)&)|#.value=='red'?'red':'blue'%>">Change the input below and color will change according to the value </span> 
	<br>
	EXPRESSION REFERENCE:<%=#|<(expressionInternal)>|#.value + ' 6666 ' %>
	<br>

	<!-- Tag  -->
	<br>
	CUSTOM TAG:<nosliw-string data="varFromContext1.attr1.attr11"/>  
	<br>
	
	<!-- Data validation  -->
	<br>
	CUSTOM TAG WITH RULE(Enum, mandatory, expression):<nosliw-float data="varFromContext1.attr1.attr15" nosliwstaticid="withRule1"/>  <a href='' nosliw-event="click:validateWithRule1">Validation</a>
	<br>
	<nosliw-uierror data="nosliw_validationError" target="withRule1"/>
	<br>
	<br>
	CUSTOM TAG WITH RULE(Enum, mandatory, scrippt):<nosliw-string data="varFromContext1.attr1.attr16" nosliwstaticid="withRule2"/>  <a href='' nosliw-event="click:validateWithRule2">Validation</a>
	<br>
	<nosliw-uierror data="nosliw_validationError" target="withRule2"/>
	<br>
	<br>

	<!-- Service  -->
	<br>
	SERVICE INPUT FROM ENHANCED VARIABLE : <nosliw-string data="local_var_for_parm3.attr1"/>
	<br>
	SERVICE OUTPUT:<%=?(forsimpleservice_1_output1.attr1)?.value%>  
	<br>
	SERVICE OUTPUT:<%=?(local_var_for_output2.attr1)?.value%>  
	<br>
	
	SERVICE SUBMIT: 	<a href='' nosliw-event="click:submitSimpleServiceWithoutInterface">Submit</a>
	<br>
	<br>

	<nosliw-contextvalue/>

</body>

	<script>
	{
		validateWithRule1 : function(info, env){

			event.preventDefault();

			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
			
			var out = node_createServiceRequestInfoSequence(undefined);
			out.addRequest(env.getUIValidationRequest(env.getTagsByAttribute(node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_STATICID, "withRule1"), {
				success : function(request, errorMessages){
				}
			}));
			
			node_requestServiceProcessor.processRequest(out, false);
		},

		validateWithRule2 : function(info, env){

			event.preventDefault();

			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
			
			var out = node_createServiceRequestInfoSequence(undefined);
			out.addRequest(env.getUIValidationRequest(env.getTagsByAttribute(node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_STATICID, "withRule2"), {
				success : function(request, errorMessages){
				}
			}));
			
			node_requestServiceProcessor.processRequest(out, false);
		},

		submitSimpleServiceWithoutInterface : function(info, env){

			event.preventDefault();

			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
			
//			var out = node_createServiceRequestInfoSequence(undefined);
			var out =  env.getServiceRequest("simpleServiceWithoutInterface", {
				success : function(request){
				}
			});

			node_requestServiceProcessor.processRequest(out, false);
		},
	
	}
	</script>


	<context>
	{
		"group" : {
			"public" : {
				"element" : {
						varFromContext1 : {
							definition: {
								child : {
									attr1 : {
										child : {
											attr11 : {criteria:"test.string;1.0.0"},
											attr12 : {criteria:"test.array;1.0.0%||element:test.string;1.0.0||%"},
											attr13 : {},
											attr14 : {},
											attr15 : {
												"criteria" : {
													"criteria": "test.float;1.0.0",
													"rule" : [
														{
															"ruleType" : "mandatory",
															"description" : "Cannot be blank!!!",
														},
														{
															"ruleType" : "enum",
															"dataSet" : [
																{
																	"dataTypeId": "test.float;1.0.0",
																	"value": 8
																},
																{
																	"dataTypeId": "test.float;1.0.0",
																	"value": 8.5
																},
																{
																	"dataTypeId": "test.float;1.0.0",
																	"value": 9
																},
																{
																	"dataTypeId": "test.float;1.0.0",
																	"value": 10
																},
															]
														},
														{
															"description":"Cannot bigger than 9!!!",
															"info":{},
															"ruleType":"expression",
															"expression":"?(data)?.largerThan(data:&(#test##float___9)&).opposite()"
														},
													]
												}
											},
											attr16 : {
												"criteria" : {
													"criteria": "test.string;1.0.0",
													"rule" : [
														{
															"ruleType" : "mandatory",
															"description" : "Cannot be blank!!!",
														},
														{
															ruleType : "enum",
															enumCode : "test_string"
														},
														{
															ruleType : "jsscript",
															description : "Cannot longer than 7!!!",
															script : "return that.value.length<=7"
														},
													]
												}
											}
										}
									}
								}
							},
							defaultValue: {
								attr1 : {
									attr11 : {
										dataTypeId: "test.string;1.0.0",
										value: "Color is red"
									},
									attr12 : {
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
									},
									attr13 : [
										{
											dataTypeId: "test.string;1.0.0",
											value: "This is my world 1111!"
										},
										{
											dataTypeId: "test.string;1.0.0",
											value: "This is my world 2222!"
										}
									],
								}
							}
						},				
						constantFromContext1 : {
							definition: {
								value : "<%=5+6+7%>",
							}
						},
						constantFromContext2 : {
							definition: {
								value : "<%=(5+6+7)>5%>"
							}
						},
						constantFromContext3 : {
							definition: {
								value : {
									a : 12345,
									b : true,
									c : "good",
									d : "<%=5+6+7%>"
								}
							}
						},
						constantFromContext4 : {
							definition : {
								value : "<%=&(constantFromContext3)&.a+6%>"
							}
						},
						constantFromContext5 : {
							definition: {
								value : "<%=#|&(#test##string___Thisismyworldabcdef)&|#%>"
							}
						},
						constantFromContext6 : {
							definition: {
								value : "<%=#|&(constantFromContext5)&.subString(from:&(#test##integer___3)&,to:&(#test##integer___7)&)|#%>"
							}
						},
						constantFromContext7: {
							definition : {
								value : {
									dataTypeId: "test.string",
									value: "Constant data from context"
								}
							}
						},
						constantFromContext8: {
							definition : {
								value : {
									dataTypeId: "test.integer",
									value: 9
								}
							}
						},
						constantFromContext9: {
							definition:{
								value : {
									dataTypeId: "test.integer",
									value: 15
								}
							}
						}
				}
			}
		}
	}
	</context>

	
	<contextref>
	[
		{
			"name" : "forlistservice_1_local",
			"categary" : "public"
		},
		{
			"name" : "forsimpleservice_1_local",
			"categary" : "public"
		},
		{
			"name" : "forlistservice_1_global",
			"categary" : "public"
		},
		{
			"name" : "forsimpleservice_1_global",
			"categary" : "public"
		},
		{
			"name" : "internal",
			"categary" : "public"
		},
	]
	</contextref>

	
	<service>
	[
		{
			"name" : "simpleServiceWithoutInterface",
			"interface" : "service_simpleoutput",
			"provider" : "simpleServiceWithoutInterfaceProvider",
			"info" : {
				"enhanceContext" : "true"
			},
			"dataMapping" :{
				"inputMapping" : {
					"element" : {
						"parm1" : {
							"description" : "input from context node with default value",
							"definition" : {
								"path" : "forsimpleservice_1_parm1"
							}
						},
						"parm2" : {
							"description" : "input from context node without default value",
							"definition" : {
								"path" : "forsimpleservice_1_parm2"
							}
						},
						"parm3" : {
							"description" : "input from context node enhanced",
							"definition" : {
								"path" : "local_var_for_parm3.attr1"
							}
						},
						"parm4" : {
							"description" : "input from constant defined in context",
							"definition" : {
								"path" : "constantFromContext7"
							}
						},
						"parm5" : {
							"description" : "input from constant value",
							definition : {
								value : {
									dataTypeId: "test.string",
									value: "Constant value!"
								}
							}
						},
						"parm6" : {
							"description" : "input from constant defined in attachment",
							definition : {
								"constant" : "constantFromAtt1"
							}
						},
						"parm7" : {
							"description" : "input from context leaf node",
							definition : {
								"path" : "varFromContext1.attr1.attr11"
							}
						},
					}
				},
				"outputMapping" : {
					"success" : {
						"element" : {
							"forsimpleservice_1_output1" : {
								"definition" : {
									"child" : {
										"attr1" : {
											"path" : "simpleOutput1"	
										}
									}
								}
							},
							"local_var_for_output2" : {
								"description" : "output to enhance variable",
								"definition" : {
									"child" : {
										"attr1" : {
											"path" : "simpleOutput2"	
										}
									}
								}
							}
						}
					}
				}
			}
		},
		{
			"name" : "simpleServiceWithInterface",
			"provider" : "simpleServiceWithInterfaceProvider",
			"info" : {
				"enhanceContext" : "true"
			},
			"dataMapping" :{
				"inputMapping" : {
					"element" : {
						"parm1" : {
							"definition" : {
								"path" : "forsimpleservice_1_parm1"
							}
						},
						"parm2" : {
							"definition" : {
								"path" : "forsimpleservice_1_parm2"
							}
						}
					}
				},
				"outputMapping" : {
					"success" : {
						"element" : {
						}
					}
				}
			}
		}
	]
	</service>


	<attachment>
	{
		"expression" : [
			{
				"name" : "expressionInternal",
				"entity" : {
					"expression" : "?(varFromContext1)?.attr1.attr11.subString(from:&(constantFromContext8)&,to:&(constantFromContext9)&)",
				}
			},
			{
				"name" : "expressionLocal",
				"referenceId": {
					"structure" : "local",
					"id" : "forsimpleservice_1"
				}
			},
			{
				"name" : "expressionExternal",
				"referenceId": "",
				"adpator" : {
				}
			},
		],
		"service" : [
			{
				"name": "simpleServiceWithoutInterfaceProvider",
				"referenceId" : "simpleoutput_refinterface"
			},	
			{
				"name": "simpleServiceWithInterfaceProvider",
				"referenceId" : "simpleoutput_internalinterface"
			},	
		],
		"value" : [
			{
				"name": "constantFromAtt1",
				"entity" : 
				{
					"value" : {
						dataTypeId: "test.string;1.0.0",
						value: "Constant in attachment"
					}
				}
			}
		],
		"context" : [
			{
				"name": "forlistservice_1_local",
				"referenceId": {
					"structure" : "local",
					"id" : "forlistservice_1"
				}
			},
			{
				"name": "forsimpleservice_1_local",
				"referenceId": {
					"structure" : "local",
					"id" : "forsimpleservice_1"
				}
			},
			{
				"name": "forlistservice_1_global",
				"referenceId": "forlistservice_1_ex"
			},
			{
				"name": "forsimpleservice_1_global",
				"referenceId": "forsimpleservice_1_ex" 
			},
			{
				"name": "internal",
				"entity": {
					"internal": {
						"definition": {
							"criteria": "test.float;1.0.0"
						},
					},
				} 
			},
		]
	}
	</attachment>

	<event>
	[
	]
	</event>
	
</html>

