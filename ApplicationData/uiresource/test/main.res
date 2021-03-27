<!DOCTYPE html>
<html>
<body>
    Within test.main.res
	<br>
	CONSTANT FROM CONTEXT:<%=&(constantFromContext6)&.value + '' %>  
	<br>
	CONSTANT FROM CONTEXT:<%=&(constantFromContext4)& + '' %>  
	<br>
	CONSTANT FROM CONTEXT:<%=&(constantFromContext3)&.c + '' %>  
	<br>
	CONSTANT FROM ATTACHMENT:<%=&(constantFromAtt1)&.value + '' %>  
	<br>
	
	<br>
	EXPRESSION IN CONTENT :<%=?(varFromContext1.attr1.attr11)?.value + '   6666 ' %>
	<br>
	EXPRESSION IN CONTENT:<%=#|?(varFromContext1)?.attr1.attr11.subString(from:&(constantFromContext8)&,to:&(constantFromContext9)&)|#.value + ?(varFromContext1.attr1.attr14)? + ' 6666 ' %>
	<br>
	EXPRESSION IN ATTRIBUTE:<span  style="color:<%=#|?(varFromContext1)?.attr1.attr11.subString(from:&(constantFromContext8)&,to:&(constantFromContext9)&)|#.value=='red'?'red':'blue'%>">Change the input below and color will change according to the value </span> 
	<br>
	CUSTOM TAG:<nosliw-string data="varFromContext1.attr1.attr11"/>  
	
	<br>
	CUSTOM TAG WITH RULE:<nosliw-float data="varFromContext1.attr1.attr15"/>  
	<br>

</body>

	<script>
	{
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
												criteria: "test.float;1.0.0",
												rule : [
													{
														ruleType : "mandatory",
														description : "Cannot be blank!!!",
													},
													{
														ruleType : "enum",
														dataSet : [
															{
																dataTypeId: "test.float;1.0.0",
																value: 8
															},
															{
																dataTypeId: "test.float;1.0.0",
																value: 8.5
															},
															{
																dataTypeId: "test.float;1.0.0",
																value: 9
															},
															{
																dataTypeId: "test.float;1.0.0",
																value: 10
															},
														]
													}
												]
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
									value: "This is my world!"
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
							"definition" : {
								"path" : "forsimpleservice_1_parm1"
							}
						},
						"parm2" : {
							"definition" : {
								"path" : "local_var_for_parm2"
							}
						}
					}
				},
				"outputMapping" : {
					"success" : {
						"element" : {
							"forsimpleservice_1_output1" : {
								"definition" : {
									"path" : "simpleOutput1"
								}
							},
							"local_var_for_output2" : {
								"definition" : {
									"path" : "simpleOutput2"
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
							"forsimpleservice_1_output1" : {
								"definition" : {
									"path" : "simpleOutput1"
								}
							},
							"forsimpleservice_1_output2" : {
								"definition" : {
									"path" : "simpleOutput2"
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
				"name": "simpleServiceWithoutInterfaceProvider",
				"referenceId" : "simpleoutput_refinterface"
			},	
			{
				"name": "simpleServiceWithInterfaceProvider",
				"referenceId" : "simpleoutput_internalinterface"
			},	
		],
		"data" : [
			{
				"name": "constantFromAtt1",
				"entity" : {
					dataTypeId: "test.string;1.0.0",
					value: "schoolName"
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

