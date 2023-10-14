<!DOCTYPE html>
<html>
<body>

		<br>
		<br>
		Url convert : <nosliw-test data="url"></nosliw-test>  
		<br>
		<br>
		Url value:<%=?(url)?.value%>
		<br>
		<br>
		Url datatype:<%=?(url)?.dataTypeId%>
		<br>
		<br>


<!--

		<nosliw-test data="parm1"></nosliw-test>  
		<br>
		<br><a href='' nosliw-event="click:invokeService:">InvokeService</a><br>
		<br>
		<br>
		Service return:<%=?(output1)?.value%>
		<br>

		Url convert : <nosliw-test data="url"></nosliw-test>  
		<br>
		<br>
		Url value:<%=?(url)?.value%>
		<br>
		<br>
		Url datatype:<%=?(url)?.dataTypeId%>
		<br>
		<br>
		<br>

		<span class="red1">HHHHHHHHHHHHHHHHHHHHHHHHHHHHHH</span>
		<div>
		<span class="red1">HHHHHHHHHHHHHHHHHHHHHHHHHHHHHH</span>
		<br>
		</div>
		
		<br>
		Content:<%=?(baseVarNormal)?.value + '   6666 ' %>
		<br>

  	<br>
	Attribute:<span style="color:<%=?(attr1)?.value=='1234'?'red':'blue'%>">Phone Number : </span> 
	<br>
	Attribute:<span style="color:<%=?(baseVarNormal)?.value=='1234'?'red':'blue'%>">Phone Number : </span> 
		<br>
		<br><a href='' nosliw-event="click:tagEventHandler:">New</a><br>
		<br>

		
		<nosliw-test data="baseVarNormal"></nosliw-test>  
		<br>
		<nosliw-test data="baseVarNormal" nosliw-event="attrchange:customTagAttrChangeHandler:" attr1="<%=?(baseVarNormal)?.value=='1234'?'red':'blue'%>">
		
		    Hello world!!!!

			<br>
			Content:<%=?(inTagVar)?.value + '   7777 ' %>
			<br>
			<br>
			<br><a href='' nosliw-event="click:tagEventHandler:">New</a><br>
			<br>

			<valuecontext>
			{
				"entity": [
					{
						"groupType" : "public",
						"valueStructure" : {
							"inTagVar": {
								"definition":{
									"criteria": "test.string;1.0.0"
								},
								"defaultValue": {
									"dataTypeId": "test.string;1.0.0",
									"value": "default value of inTagVar"
								}
							},
						}
					}
				]
			}
			</valuecontext>


		</nosliw-test>  
		<br>
-->
</body>

	<script>
	{
		invokeService : function(event, info, env){
			env.executeGetInvokeServiceRequest("service1", "default");
		},
	
		tagEventHandler : function(event, info, env){
			console.log(JSON.stringify(arguments));
		},
		
		customTagAttrChangeHandler : function(event, info, env){
			console.log(JSON.stringify(arguments));
		},
		
		textInputValueChanged : function(info, env){
			env.trigueEvent("changeInputText", info.eventData);
		},
		
		command_Start :function(data, env){
			return data.data + "   Start";
		},
	}
	</script>
	
	<service>
	[
		{
			"embeded": {
				"info": {
					"name" : "service1",
					"status": "disabled1"
				},
				"entity" : {
					"serviceKey" : {
						"id" : "TestBasicService"
					}
				}
			},
			"adapter":[
				{
					"name" : "default",
					"status": "disabled1",
					"entityType" : "dataAssociationInteractive",
					"entity" : {
						"in" : {
							"serviceParm1" : {
								"definition" : {
									"mapping" : {
										"elementPath": "parm1"
									}
								}
							}
						},
						"out" : {
							"success" : {
								"output1" : {
									"definition" : {
										"mapping" : {
											"elementPath": "outputInService1"
										}
									}
								}
							}
						}
					}
				}
			]
		},
	]
	</service>
	
	<valuecontext>
	{
		"entity": [
			{
				"groupType" : "public",
				"valueStructure" : {
					"url": {
						"definition":{
							"criteria": "test.url;1.0.0"
						},
						"defaultValue": {
							"dataTypeId": "test.url;1.0.0",
							"value": "default value of url"
						}
					},
					"parm1": {
						"definition":{
							"criteria": "test.string;1.0.0"
						},
						"defaultValue": {
							"dataTypeId": "test.string;1.0.0",
							"value": "default value of parm1"
						}
					},
					"output1": {
						"definition":{
							"criteria": "test.string;1.0.0"
						},
						"defaultValue": {
							"dataTypeId": "test.string;1.0.0",
							"value": "default value of outputInService1"
						}
					},
					"baseVarNormal": {
						"definition":{
							"criteria": "test.string;1.0.0"
						},
						"defaultValue": {
							"dataTypeId": "test.string;1.0.0",
							"value": "default value of baseVarNormal"
						}
					},
					
					"attr1": {
						"definition":{
							"criteria": "test.string;1.0.0"
						},
						"defaultValue": {
							"dataTypeId": "test.string;1.0.0",
							"value": "1234"
						}
					}
				}
			}
		]
	}
	</valuecontext>
	
	<attachment>
	{
		"extra": {
			"status": "disabled1",
			"name": "parent attachment" 
		},
		"entity": {
			"data" : [
				{
					"name" : "constantBase",
					"entity": {
						"dataTypeId": "test.string;1.0.0",
						"value": "012345678901234567890"
					}
				},
				{
					"name" : "constantFrom",
					"entity": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 5
					}
				},
				{
					"name" : "constantTo",
					"entity": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 7
					}
				}
			]
		}
	}
	</attachment>

</html>
