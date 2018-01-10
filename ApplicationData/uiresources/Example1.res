<!DOCTYPE html>
<html>
	<body>
	<div>
	Include Tag
	<br>
	<nosliw-include source="Example1_child" context="childResource"/> </div>
	<br>
	
	<div>
	Html:<%=?(bus.a.aa)?.value + '   end ' %>
	</div>
	<br>
	<div>
	TextInput:<nosliw-textinput data="bus.a.aa"/>  
	<br>
	TextInput:<nosliw-textinput data="bus.a.aa"/>  
	</div>

<!--
		<br>Loop:
		<br>
		<nosliw-loop data="business.a.cc" element="ele" elename="index">  
			<%=?(ele)?.value + '   6666 ' %>   <a href='' nosliw-event="click:deleteElementInLoop:">Delete</a>
			In Loop
			<br>

			<script>
			{
				deleteElementInLoop : function(data, info){
					alert("cccccc");
					event.preventDefault();


				
					var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
					var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
					var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
					var node_createBatchUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createBatchUIDataOperationRequest");
					var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
					var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
					var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");

					var index = this.getContext().getContextElementData("index").value;
					
					var requestInfo = node_createBatchUIDataOperationRequest(this.getContext());
					var uiDataOperation = new node_UIDataOperation(new node_createContextVariable("business.a.cc"), node_uiDataOperationServiceUtility.createDeleteElementOperationService("", index));
					requestInfo.addUIDataOperation(uiDataOperation);						
					node_requestServiceProcessor.processRequest(requestInfo, false);
				}
			}
			</script>
			
		</nosliw-loop>
		<br>
		<br><a href='' nosliw-event="click:newElementInLoop:">New</a><br>
-->

	
	<!--	
		<br>
		<nosliw-textinput data="company.name"/>  
		<br>
		<br>
		<nosliw-textinput data="company.name"/>  
		<br>

		<nosliw-loop data="school" element="ele" elename="index">  
			<%=?(ele)?.value + '   6666 ' %>   
			In Loop
			<br>
		</nosliw-loop>
		<br>
		<br><a href='' nosliw-event="click:newElementInLoopData:">New</a><br>
		
	<%=?(company.name)?.value + '   6666 ' %>  tttttttttt222  
	<br>
	

	<br>
-->


		
	<%=?(business.a.aa)?.value + '   6666 ' %>  tttttttttt222  

	<br>
	<nosliw-textinput data="business.a.aa"/>  
	<br>
	<nosliw-textinput data="business.a.aa"/>  

	<br>
	lalalala
	<%=#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value + ?(business.a.dd)? + ' 6666 ' %>  tttttttttt222 

		<br>Test Attribute Expression:<br>
		<span  style="color:<%=#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value=='s isfff'?'red':'blue'%>">Phone Number : </span> 

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

