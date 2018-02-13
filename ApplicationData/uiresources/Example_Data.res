<!DOCTYPE html>
<html>
	<body>
		<br>
		<nosliw-loop data="school" element="ele" index="index">  
			<%=?(ele)?.value + '   6666 ' %>   
			In Loop
			<br>
		</nosliw-loop>

		
<!--	
		<br>
		<nosliw-textinput data="company.name"/>  
		<br>
		<br>
		<nosliw-textinput data="company.name"/>  
		<br>

	<%=?(company.name)?.value + '   6666 ' %>  tttttttttt222  
	<br>
		
		<nosliw-loop data="school" element="ele" index="index">  
			<%=?(ele)?.value + '   6666 ' %>   
			In Loop
			<br>
		</nosliw-loop>
		<br>
		<br><a href='' nosliw-event="click:newElementInLoopData:">New</a><br>
-->	
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
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<context>
	{
		public : {
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

	<constants>
	{
	}
	</constants>
	
		<!-- This part can be used to define expressions
		-->
	<expressions>
	{
	}
	</expressions>
	
</html>

