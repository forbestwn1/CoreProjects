<!DOCTYPE html>
<html>
<body>

		<br>
		<br><a href='' nosliw-event="click:newElementInLoop:">New</a><br>
		<br>

		<br>
		<nosliw-loop data="business.a.cc" element="ele" index="index">  
			<br>
			<br>
			<%=?(ele)?.value + '   7777 ' %>   <a href='' nosliw-event="click:deleteElementInLoop:">Delete</a>
			<br>
			TextInput:<nosliw-string data="ele"/>  
			<br>
			TextInput:<nosliw-string data="ele"/>  
			<br>
			<script>
			{
				deleteElementInLoop : function(data, info){
					event.preventDefault();
				
					var node_createContextVariable = nosliw.getNodeData("variable.context.createContextVariable");
					var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
					var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
					var node_createBatchValueInVarOperationRequest = nosliw.getNodeData("variable.valueinvar.operation.createBatchValueInVarOperationRequest");
					var node_ValueInVarOperation = nosliw.getNodeData("variable.valueinvar.operation.ValueInVarOperation");
					var node_valueInVarOperationServiceUtility = nosliw.getNodeData("variable.valueinvar.operation.valueInVarOperationServiceUtility");
					var node_createContextVariable = nosliw.getNodeData("variable.context.createContextVariable");
					var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");

					
							var opRequest = node_createBatchValueInVarOperationRequest(this.getContext());
							var uiDataOperation = new node_ValueInVarOperation("ele", node_valueInVarOperationServiceUtility.createDeleteOperationService(""));
							opRequest.addValueInVarOperation(uiDataOperation);
					node_requestServiceProcessor.processRequest(opRequest, false);
					
/*					
					var requestInfo = node_createServiceRequestInfoSequence({}, {
						success:function(requestInfo, data){
							
						}
					});
					var that = this;
					requestInfo.addRequest(this.getContext().getDataOperationRequest("index", node_valueInVarOperationServiceUtility.createGetOperationService(), {
						success : function(request, data){
							var elePath = data.value;
						
							var opRequest = node_createBatchValueInVarOperationRequest(that.getContext());
							var uiDataOperation = new node_ValueInVarOperation("ele", node_valueInVarOperationServiceUtility.createDeleteOperationService(""));
							opRequest.addValueInVarOperation(uiDataOperation);
							return opRequest;
						}
					}));
					node_requestServiceProcessor.processRequest(requestInfo, false);
*/					
				}
			}
			</script>
			
		</nosliw-loop>


	<br>
	
<!--	
		<br>
		<br><a href='' nosliw-event="click:newElementInLoop:">New</a><br>
		<br>
		<br>Loop:
		<br>
		<nosliw-loop data="business.a.cc" element="ele" elename="index">  
			<br>
			<br>
			<%=?(ele)?.value + '   6666 ' %>   <a href='' nosliw-event="click:deleteElementInLoop:">Delete</a>
			<br>
			<br>
			<script>
			{
				deleteElementInLoop : function(data, info){
					event.preventDefault();
				
					var node_createContextVariable = nosliw.getNodeData("variable.context.createContextVariable");
					var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
					var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
					var node_createBatchValueInVarOperationRequest = nosliw.getNodeData("variable.valueinvar.operation.createBatchValueInVarOperationRequest");
					var node_ValueInVarOperation = nosliw.getNodeData("variable.valueinvar.operation.ValueInVarOperation");
					var node_valueInVarOperationServiceUtility = nosliw.getNodeData("variable.valueinvar.operation.valueInVarOperationServiceUtility");
					var node_createContextVariableInfo = nosliw.getNodeData("variable.context.createContextVariableInfo");
					var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");

					var requestInfo = node_createServiceRequestInfoSequence({}, {
						success:function(requestInfo, data){
							
						}
					});
					var that = this;
					requestInfo.addRequest(this.getContext().getDataOperationRequest("index", node_valueInVarOperationServiceUtility.createGetOperationService(), {
						success : function(request, data){
							var elePath = data.value;
						
							var opRequest = node_createBatchValueInVarOperationRequest(that.getContext());
//							var uiDataOperation = new node_ValueInVarOperation("ele", node_valueInVarOperationServiceUtility.createDeleteElementOperationService("", undefined, elePath));
							var uiDataOperation = new node_ValueInVarOperation("ele", node_valueInVarOperationServiceUtility.createDestroyOperationService(""));
							opRequest.addValueInVarOperation(uiDataOperation);
							return opRequest;
						}
					}));
					node_requestServiceProcessor.processRequest(requestInfo, false);
				}
			}
			</script>
			
		</nosliw-loop>


	<br>
	Content:<%=?(bus.a.aa)?.value + '   6666 ' %>
	<br>
	TextInput_converter:<nosliw-string data="bus.a.aa"/>  
	<br>
	TextInput_converter<nosliw-string data="bus.a.aa"/>  

	<br>
	Content:<%=?(business.a.aa)?.value + '   6666 ' %>
	<br>
	Content:<%=#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value + ?(business.a.dd)? + ' 6666 ' %>
	<br>
	Attribute:<span  style="color:<%=#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value=='s isfff'?'red':'blue'%>">Phone Number : </span> 
	<br>
	TextInput:<nosliw-string data="business.a.aa"/>  
	<br>
	TextInput: <nosliw-string data="business.a.aa"/>  

	<br>
	Switch/case
	<br>
		<nosliw-switch variable="business.a.aa.value">
			<br> switch<br>
			<%=?(internal_switchVariable)?%>
				
				<nosliw-case value="This is my world!">
					<br>First one <br>
				</nosliw-case>
			
				<nosliw-case value="This is my w">
					<br>Second one <br>
				</nosliw-case>

				<nosliw-casedefault>
					<br>Default one <br>
				</nosliw-casedefualt>
			<br>
		</nosliw-switch>
-->		

</body>

	<script>
	{
		newElementInLoop : function(data, info){

			event.preventDefault();

			var node_createContextVariable = nosliw.getNodeData("variable.context.createContextVariable");
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createBatchValueInVarOperationRequest = nosliw.getNodeData("variable.valueinvar.operation.createBatchValueInVarOperationRequest");
			var node_ValueInVarOperation = nosliw.getNodeData("variable.valueinvar.operation.ValueInVarOperation");
			var node_valueInVarOperationServiceUtility = nosliw.getNodeData("variable.valueinvar.operation.valueInVarOperationServiceUtility");
			var node_createContextVariableInfo = nosliw.getNodeData("variable.context.createContextVariableInfo");
			
			var eleData = {
				dataTypeId: "test.string;1.0.0",
				value: "This is my world 33333!"
			};

			var requestInfo = node_createBatchValueInVarOperationRequest(this.getContext());
			var uiDataOperation = new node_ValueInVarOperation(node_createContextVariableInfo("business.a.cc"), node_valueInVarOperationServiceUtility.createAddElementOperationService("", eleData, 2));
			requestInfo.addValueInVarOperation(uiDataOperation);						
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},
	
		testLinkEvent : function(data, info){
			var node_createContextVariable = nosliw.getNodeData("variable.context.createContextVariable");
			
			event.preventDefault();
			alert("aaaaa");
			
			var context = this.getContext();
			var variable = context.createVariable(new node_createContextVariable("business.a.aa"));
			variable.registerDataChangeEventListener(undefined, function(eventName){
				alert(eventName);
			});
			
			variable.requestDataOperation({
				command:"WRAPPER_OPERATION_SET",
				parms :
				{
					value: {
						dataTypeId: "test.string",
						value: "This is "
					}
					
				}
			});
			
		},
	}
	</script>
	
	<constants>
	{
			aaaa : "<%=5+6+7%>",
			bbbb : "<%=(5+6+7)>5%>",
			cccc : {
						a : 12345,
						b : true,
						c : "good",
						d : "<%=5+6+7%>"
					},
			dddd : "<%=&(cccc)&.a+6%>",

			ffff : "<%=#|&(#test##string___Thisismyworldabcdef)&|#%>",
			eeee : "<%=#|&(ffff)&.subString(from:&(#test##integer___3)&,to:&(#test##integer___7)&)|#%>",
			
				base: {
					dataTypeId: "test.string",
					value: "This is my world!"
				},
				from: {
					dataTypeId: "test.integer",
					value: 3
				},
				to: {
					dataTypeId: "test.integer",
					value: 7
				},
				
				childResource: {
					inherit : true,
					public : {
						name : {
							path : "business.a.aa"
						}
					}
				}
			
	}
	</constants>
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<context>
	{
		public : {
			business : {
				definition: {
					a : {
						aa : "test.string;1.0.0",
						bb : "test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(&(expression)&;;&(parms)&)||@||%",
						cc : {
							element : "test.string;1.0.0"
						}
					}
				},
				default: {
					a : {
						aa : {
							dataTypeId: "test.string;1.0.0",
							value: "This is my world!"
						},
						dd : "HELLO!!!!",
						cc : [
							{
								dataTypeId: "test.string;1.0.0",
								value: "This is my world 1111!"
							},
							{
								dataTypeId: "test.string;1.0.0",
								value: "This is my world 2222!"
							},
						]
					}
				}
			},
			bus : {
				definition: {
					a : {
						aa : "test.url;1.0.0"
					}
				},
				default: {
					a : {
						aa : {
							dataTypeId: "test.url;1.0.0",
							value: "This is my world!"
						}
					}
				}
			}
		}
	}
	</context>
	
		<!-- This part can be used to define expressions
		-->
	<expressions>
	{
		
	
	}
	</expressions>
	
</html>

