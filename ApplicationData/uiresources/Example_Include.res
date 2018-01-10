<!DOCTYPE html>
<html>
	<body>
	<div>
	Include Object
	<nosliw-include source="Example_Object"/> 
	</div>
	<div>
	Include Data
	<nosliw-include source="Example_Data"/> 
	</div>
	<br>
	</body>

	<script>
	{
		newElementInLoopData : function(data, info){

			event.preventDefault();

			var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createBatchUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createBatchUIDataOperationRequest");
			var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
			var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
			var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
			
			var eleData = {
				dataTypeId: "test.string;1.0.0",
				value: "This is my world 33333!"
			};

			var requestInfo = node_createBatchUIDataOperationRequest(this.getContext());
			var uiDataOperation = new node_UIDataOperation(new node_createContextVariable("school"), node_uiDataOperationServiceUtility.createAddElementOperationService("", 4, eleData));
			requestInfo.addUIDataOperation(uiDataOperation);						
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},
	
		newElementInLoop : function(data, info){

			event.preventDefault();

			var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createBatchUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createBatchUIDataOperationRequest");
			var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
			var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
			var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
			
			var eleData = {
				dataTypeId: "test.string;1.0.0",
				value: "This is my world 33333!"
			};

			var requestInfo = node_createBatchUIDataOperationRequest(this.getContext());
			var uiDataOperation = new node_UIDataOperation(new node_createContextVariable("business.a.cc"), node_uiDataOperationServiceUtility.createAddElementOperationService("", 4, eleData));
			requestInfo.addUIDataOperation(uiDataOperation);						
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},
	
		testLinkEvent : function(data, info){
			var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
			
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
			},
			school : {
				definition : "test.array;1.0.0",
				default : {
					dataTypeId: "test.array;1.0.0",
					value: [
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
				
			},
			company : {
				definition : "test.map;1.0.0",
				default : {
					dataTypeId: "test.map;1.0.0",
					value: {
						name : {
							dataTypeId: "test.string;1.0.0",
							value: "Nosliw Company"
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

