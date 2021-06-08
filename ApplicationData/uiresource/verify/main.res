<!DOCTYPE html>
<html>
<body>
    Within test.main.res
    
    	<br>
	CONSTANT FROM CONTEXT:<%=&(constantFromContext5)&.value + '' %>  
	<br>
	CONSTANT FROM CONTEXT:<%=&(constantFromContext1)& + '' %>  
	<br>
	CONSTANT FROM ATTACHMENT:<%=&(constantFromAtt1)&.value + '' %>  
	<br>
    	<br>
	EXPRESSION IN CONTENT :<%=?(varFromContext1.attr1.attr11)?.value + '   6666 ' %>
	<br>
    	CUSTOM TAG:<nosliw-string data="varFromContext1.attr1.attr11"/>   
    
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
						constantFromContext1 : {
							definition: {
								value : "<%=5+6+7%>",
							}
						},
						constantFromContext5 : {
							definition: {
								value : "<%=#|&(#test##string___Thisismyworldabcdef)&|#%>"
							}
						},
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
	
	]
	</service>


	<attachment>
	{
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

