<!DOCTYPE html>
<html>
<body>
		<nosliw-style>
			.red1 {
			  color: red;
			}
			.intag {
			  color: red;
			}
			
		</nosliw-style>

		<span class="red1">HHHHHHHHHHHHHHHHHHHHHHHHHHHHHH</span>
		<nosliw-include source="Example_Include_simple" context="" event="changeInputTextIncludeBasic=changeInputTextIncludeBasicMapped"/> 

		<nosliw-include source="Example_Object_Basic_Include" context="element=business.a.aa" /> 
		
	<br>
	Content:<%=?(business.a.aa)?.value + '   6666 ' %>
	<br>
	Content:<%=#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value + ?(business.a.dd)? + ' 6666 ' %>
	<br>
	Attribute:<span  style="color:<%=#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value=='s isfff'?'red':'blue'%>">Phone Number : </span> 
	<br>
	<br>
	TextInput:<nosliw-string data="business.a.aa"/>  
	<br>
	TextInput: <nosliw-string data="business.a.aa" nosliw-event="valueChanged:textInputValueChanged:"/>  

	
		<br>
		<br><a href='' nosliw-event="click:newElementInLoop:">New</a><br>
		<br>
		<br>

		
		<nosliw-loop data="business.a.cc" element="ele" index="index">  

		<nosliw-style>
			.intag {
			  color: blue;
			}
		</nosliw-style>

		<span class="intag">BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB</span>

<!--				<nosliw-include source="Example_Object_Basic_Include" context="element=ele" />   -->
		

			<br>
			Index: <%=?(index)?%>
			<br>
			<%=?(ele)?.value + '   7777 ' %>   <a href='' nosliw-event="click:deleteElementInLoop:">Delete</a>
			<br>
			TextInput:<nosliw-string data="ele"/> 
			<br>
			<scripts>
			{
				deleteElementInLoop : function(data, info){
					event.preventDefault();
				
					var node_createContextVariable = nosliw.getNodeData("variable.context.createContextVariable");
					var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
					var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
					var node_createBatchUIDataOperationRequest = nosliw.getNodeData("variable.uidataoperation.createBatchUIDataOperationRequest");
					var node_UIDataOperation = nosliw.getNodeData("variable.uidataoperation.UIDataOperation");
					var node_uiDataOperationServiceUtility = nosliw.getNodeData("variable.uidataoperation.uiDataOperationServiceUtility");
					var node_createContextVariable = nosliw.getNodeData("variable.context.createContextVariable");
					var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");

					
							var opRequest = node_createBatchUIDataOperationRequest(this.getContext());
							var uiDataOperation = new node_UIDataOperation("ele", node_uiDataOperationServiceUtility.createDeleteOperationService(""));
							opRequest.addUIDataOperation(uiDataOperation);
					node_requestServiceProcessor.processRequest(opRequest, false);
					
/*					
					var requestInfo = node_createServiceRequestInfoSequence({}, {
						success:function(requestInfo, data){
							
						}
					});
					var that = this;
					requestInfo.addRequest(this.getContext().getDataOperationRequest("index", node_uiDataOperationServiceUtility.createGetOperationService(), {
						success : function(request, data){
							var elePath = data.value;
						
							var opRequest = node_createBatchUIDataOperationRequest(that.getContext());
							var uiDataOperation = new node_UIDataOperation("ele", node_uiDataOperationServiceUtility.createDeleteOperationService(""));
							opRequest.addUIDataOperation(uiDataOperation);
							return opRequest;
						}
					}));
					node_requestServiceProcessor.processRequest(requestInfo, false);
*/					
				}
			}
			</scripts>
			
		</nosliw-loop>
  

</body>

	<scripts>
	{
		newElementInLoop : function(data, info, env){

			event.preventDefault();

			var node_createContextVariable = nosliw.getNodeData("variable.context.createContextVariable");
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createBatchUIDataOperationRequest = nosliw.getNodeData("variable.uidataoperation.createBatchUIDataOperationRequest");
			var node_UIDataOperation = nosliw.getNodeData("variable.uidataoperation.UIDataOperation");
			var node_uiDataOperationServiceUtility = nosliw.getNodeData("variable.uidataoperation.uiDataOperationServiceUtility");
			var node_createContextVariableInfo = nosliw.getNodeData("variable.context.createContextVariableInfo");
			
			var eleData = {
				dataTypeId: "test.string;1.0.0",
				value: "This is my world 33333!"
			};

			var requestInfo = node_createBatchUIDataOperationRequest(this.getContext());
			var uiDataOperation = new node_UIDataOperation(node_createContextVariableInfo("business.a.cc"), node_uiDataOperationServiceUtility.createAddElementOperationService("", eleData, 1));
			requestInfo.addUIDataOperation(uiDataOperation);						
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},
		
		textInputValueChanged : function(info, env){
			env.trigueEvent("changeInputText", info.eventData);
		},
		
		command_Start :function(data, env){
			return data.data + "   Start";
		},
	}
	</scripts>
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<contexts>
	{
		group : {
			public : {
				element : {
					business : {
						definition: {
							child : {
								a : {
									child : {
										aa : {criteria:"test.string;1.0.0"},
										cc : {criteria:"test.array;1.0.0%||element:test.string;1.0.0||%"},
									}
								}
							}
						},
						defaultValue: {
							a : {
								aa : {
									dataTypeId: "test.string;1.0.0",
									value: "This is my world!"
								},
								cc : [
									{
										dataTypeId: "test.string;1.0.0",
										value: "This is my world 1111!"
									},
									{
										dataTypeId: "test.string;1.0.0",
										value: "This is my world 2222!"
									}
								],
								ee : {
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
								}
							}
						}
					},
					aaaa : {
						definition: {
							value : "<%=5+6+7%>",
						}
					},
					bbbb : {
						definition: {
							value : "<%=(5+6+7)>5%>"
						}
					},
					cccc : {
						definition: {
							value : {
								a : 12345,
								b : true,
								c : "good",
								d : "<%=5+6+7%>"
							}
						}
					},
					dddd : {
						definition : {
							value : "<%=&(cccc)&.a+6%>"
						}
					},
					ffff : {
						definition: {
							value : "<%=#|&(#test##string___Thisismyworldabcdef)&|#%>"
						}
					},
					eeee : {
						definition: {
							value : "<%=#|&(ffff)&.subString(from:&(#test##integer___3)&,to:&(#test##integer___7)&)|#%>"
						}
					},
					base: {
						definition : {
							value : {
								dataTypeId: "test.string",
								value: "This is my world!"
							}
						}
					},
					from: {
						definition : {
							value : {
								dataTypeId: "test.integer",
								value: 3
							}
						}
					},
					to: {
						definition:{
							value : {
								dataTypeId: "test.integer",
								value: 7
							}
						}
					}
				}
			}
		}
	}
	</contexts>
	
	<events>
	[
		{
			name : "changeInputText",
			data : {
				element : {
					data : {
						definition : {
							path: "business.a.aa"
						}
					}
				}
			}
		}
	]
	</events>
	
	<commands>
	[
		{
			name : "Start",
			parm : {
				element : {
					data : {
						definition : {
							path: "business.a.aa"
						}
					}
				}
			}
		}
	]
	</commands>
	
	<attachment>
	{
		"expression" : [
		],
		
		"data": [
					
		]
	}
	</attachment>

</html>
