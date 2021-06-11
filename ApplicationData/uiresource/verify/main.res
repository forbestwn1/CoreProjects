<!DOCTYPE html>
<html>
<body>
    Within test.main.res
    
    	<br>
    	<br>
	EXPRESSION IN CONTENT :<%=?(aaa)?.value + '   6666 ' %>
	<br>
    CUSTOM TAG:<nosliw-string data="aaa"/>   
    
</body>

	<script>
	{
	}
	</script>


	<valuestructure>
	{
		"group" : {
			"public" : {
				"flat" : {
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
						"aaa":{
							definition : {
								"criteria": "test.string;1.0.0",
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "This is my world!"
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
	</valuestructure>

	
	<contextref>
	[
	]
	</contextref>

	
	<service>
	[
		{
			"name" : "simpleServiceWithInterface",
			"status" : "disabled",
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
		"dataexpression" : [
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
			},
			{
				"name": "constantFromAtt2",
				"entity" : 
				{
					"value" : {
							dataTypeId: "test.integer",
							value: 15
					}
				}
			},
		],
		"context" : [
		]
	}
	</attachment>

	<event>
	[
	]
	</event>
	
</html>

