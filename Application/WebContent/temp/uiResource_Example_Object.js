
nosliw.runtime.getResourceService().importResource({"id":{"id":"Example_Object",
"type":"uiResource"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"Example_Object",
"type":"resource",
"context":{"public":{"bus":{"children":{"a":{"children":{"aa":{"children":{},
"definition":"test.url;1.0.0"
}
}
}
},
"type":"absolute",
"default":{"a":{"aa":{"dataTypeId":"test.url;1.0.0","value":"This is my world!"}}}
},
"business":{"children":{"a":{"children":{"aa":{"children":{},
"definition":"test.string;1.0.0"
},
"bb":{"children":{},
"definition":"test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(&amp;(expression)&amp;;;&amp;(parms)&amp;)||@||%"
},
"cc":{"children":{"element":{"children":{},
"definition":"test.string;1.0.0"
}
}
}
}
}
},
"type":"absolute",
"default":{"a":{"aa":{"dataTypeId":"test.string;1.0.0","value":"This is my world!"},"dd":"HELLO!!!!","cc":[{"dataTypeId":"test.string;1.0.0","value":"This is my world 1111!"},{"dataTypeId":"test.string;1.0.0","value":"This is my world 2222!"}]}}
}
},
"internal":{},
"private":{},
"excluded":{}
},
"scriptExpressionsInContent":[],
"scriptExpressionInAttributes":[],
"scriptExpressionTagAttributes":[],
"elementEvents":[{"uiId":"1100",
"event":"click",
"function":"newElementInLoop"
}],
"tagEvents":[],
"uiTags":{"1097":{"id":"1097",
"type":"tag",
"context":{"public":{"bus":{"children":{"a":{"children":{"aa":{"children":{},
"definition":"test.url;1.0.0"
}
}
}
},
"path":{"rootEleName":"bus"
},
"type":"relative"
},
"business":{"children":{"a":{"children":{"aa":{"children":{},
"definition":"test.string;1.0.0"
},
"bb":{"children":{},
"definition":"test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(&amp;(expression)&amp;;;&amp;(parms)&amp;)||@||%"
},
"cc":{"children":{"element":{"children":{},
"definition":"test.string;1.0.0"
}
}
}
}
}
},
"path":{"rootEleName":"business"
},
"type":"relative"
}
},
"internal":{},
"private":{},
"excluded":{}
},
"scriptExpressionsInContent":[],
"scriptExpressionInAttributes":[],
"scriptExpressionTagAttributes":[],
"elementEvents":[],
"tagEvents":[],
"uiTags":{},
"attributes":{"nosliwid":"1097"
},
"html":"",
"constants":{"from":{"literate":{"dataTypeId":"test.integer","value":3},
"processed":true,
"value":{"dataTypeId":"test.integer","value":3}
},
"to":{"literate":{"dataTypeId":"test.integer","value":7},
"processed":true,
"value":{"dataTypeId":"test.integer","value":7}
},
"childResource":{"literate":{"public":{"name":{"path":"business.a.aa"}},"inherit":true},
"processed":true,
"value":{"public":{"name":{"path":"business.a.aa"}},"inherit":true}
},
"aaaa":{"literate":"<%=5+6+7%>",
"processed":true,
"value":18.0
},
"bbbb":{"literate":"<%=(5+6+7)>5%>",
"processed":true,
"value":true
},
"cccc":{"literate":{"a":12345,"b":true,"c":"good","d":18},
"processed":true,
"value":{"a":12345,"b":true,"c":"good","d":18}
},
"dddd":{"literate":"<%=&(cccc)&.a+6%>",
"processed":true,
"value":12351.0
},
"ffff":{"literate":"<%=#|&(#test##string___Thisismyworldabcdef)&|#%>",
"processed":true,
"value":{"dataTypeId":"test.string;1.0.0","valueFormat":"LITERATE","value":"Thisismyworldabcdef"}
},
"eeee":{"literate":"<%=#|&(ffff)&.subString(from:&(#test##integer___3)&,to:&(#test##integer___7)&)|#%>",
"processed":true,
"value":{"dataTypeId":"test.string;1.0.0","value":"sismfff"}
},
"base":{"literate":{"dataTypeId":"test.string","value":"This is my world!"},
"processed":true,
"value":{"dataTypeId":"test.string","value":"This is my world!"}
}
},
"tagName":"debug"
},
"1105":{"id":"1105",
"type":"tag",
"context":{"public":{"bus":{"children":{"a":{"children":{"aa":{"children":{},
"definition":"test.url;1.0.0"
}
}
}
},
"path":{"rootEleName":"bus"
},
"type":"relative"
},
"business":{"children":{"a":{"children":{"aa":{"children":{},
"definition":"test.string;1.0.0"
},
"bb":{"children":{},
"definition":"test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(&amp;(expression)&amp;;;&amp;(parms)&amp;)||@||%"
},
"cc":{"children":{"element":{"children":{},
"definition":"test.string;1.0.0"
}
}
}
}
}
},
"path":{"rootEleName":"business"
},
"type":"relative"
}
},
"internal":{},
"private":{"internal_data":{"children":{"element":{"children":{},
"definition":"test.string;1.0.0"
}
},
"path":{"rootEleName":"business",
"path":"a.cc"
},
"type":"relative"
}
},
"excluded":{"ele":{"children":{},
"definition":"test.string;1.0.0",
"path":{"rootEleName":"business",
"path":"a.cc.element"
},
"type":"relative"
}
}
},
"scriptExpressionsInContent":[{"uiId":"1106",
"scriptExpressions":{"0":{"id":"0",
"definition":"?(ele)?.value + ' 6666 ' ",
"variableNames":["ele"],
"expressions":{},
"scriptFunction":function(expressionsData, constantsData, variablesData){
	return variablesData["ele"].value + ' 6666 ' ;
} 

}
},
"scriptFunction":function(scriptExpressionData){
	return scriptExpressionData["0"];
} 

}],
"scriptExpressionInAttributes":[],
"scriptExpressionTagAttributes":[],
"elementEvents":[{"uiId":"1109",
"event":"click",
"function":"deleteElementInLoop"
}],
"tagEvents":[],
"uiTags":{},
"attributes":{"data":"business.a.cc",
"element":"ele",
"elename":"index",
"nosliwid":"1105"
},
"html":"&lt;br nosliwid=&quot;1107&quot;&gt; &lt;br nosliwid=&quot;1108&quot;&gt;&lt;span&gt; &lt;/span&gt;&lt;span nosliwid=&quot;1106&quot;&gt;&lt;/span&gt;&lt;span&gt; &lt;/span&gt;&lt;a href=&quot;&quot; nosliwid=&quot;1109&quot;&gt;&lt;span&gt;Delete&lt;/span&gt;&lt;/a&gt; &lt;br nosliwid=&quot;1110&quot;&gt; &lt;br nosliwid=&quot;1111&quot;&gt;",
"constants":{"from":{"literate":{"dataTypeId":"test.integer","value":3},
"processed":true,
"value":{"dataTypeId":"test.integer","value":3}
},
"to":{"literate":{"dataTypeId":"test.integer","value":7},
"processed":true,
"value":{"dataTypeId":"test.integer","value":7}
},
"childResource":{"literate":{"public":{"name":{"path":"business.a.aa"}},"inherit":true},
"processed":true,
"value":{"public":{"name":{"path":"business.a.aa"}},"inherit":true}
},
"aaaa":{"literate":"<%=5+6+7%>",
"processed":true,
"value":18.0
},
"bbbb":{"literate":"<%=(5+6+7)>5%>",
"processed":true,
"value":true
},
"cccc":{"literate":{"a":12345,"b":true,"c":"good","d":18},
"processed":true,
"value":{"a":12345,"b":true,"c":"good","d":18}
},
"dddd":{"literate":"<%=&(cccc)&.a+6%>",
"processed":true,
"value":12351.0
},
"ffff":{"literate":"<%=#|&(#test##string___Thisismyworldabcdef)&|#%>",
"processed":true,
"value":{"dataTypeId":"test.string;1.0.0","valueFormat":"LITERATE","value":"Thisismyworldabcdef"}
},
"eeee":{"literate":"<%=#|&(ffff)&.subString(from:&(#test##integer___3)&,to:&(#test##integer___7)&)|#%>",
"processed":true,
"value":{"dataTypeId":"test.string;1.0.0","value":"sismfff"}
},
"base":{"literate":{"dataTypeId":"test.string","value":"This is my world!"},
"processed":true,
"value":{"dataTypeId":"test.string","value":"This is my world!"}
}
},
"script":{
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
					var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");

					var requestInfo = node_createServiceRequestInfoSequence({}, {
						success:function(requestInfo, data){
							
						}
					});
					var that = this;
					requestInfo.addRequest(this.getContext().getDataOperationRequest("index", node_uiDataOperationServiceUtility.createGetOperationService(), {
						success : function(request, data){
							var elePath = data.value;
						
							var opRequest = node_createBatchUIDataOperationRequest(that.getContext());
//							var uiDataOperation = new node_UIDataOperation("ele", node_uiDataOperationServiceUtility.createDeleteElementOperationService("", undefined, elePath));
							var uiDataOperation = new node_UIDataOperation("ele", node_uiDataOperationServiceUtility.createDestroyOperationService(""));
							opRequest.addUIDataOperation(uiDataOperation);
							return opRequest;
						}
					}));
					node_requestServiceProcessor.processRequest(requestInfo, false);
				}
			},
"tagName":"loop"
}
},
"attributes":{},
"html":"&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;1097-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;1097-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;br nosliwid=&quot;1098&quot;&gt; &lt;br nosliwid=&quot;1099&quot;&gt;&lt;a href=&quot;&quot; nosliwid=&quot;1100&quot;&gt;&lt;span&gt;New&lt;/span&gt;&lt;/a&gt;&lt;br nosliwid=&quot;1101&quot;&gt; &lt;br nosliwid=&quot;1102&quot;&gt; &lt;br nosliwid=&quot;1103&quot;&gt;&lt;span&gt;Loop: &lt;/span&gt;&lt;br nosliwid=&quot;1104&quot;&gt; &lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;1105-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;1105-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;!--			&lt;br&gt;	Content:&lt;%=?(bus.a.aa)?.value + '   6666 ' %&gt;	&lt;br&gt;	TextInput_converter:&lt;nosliw-textinput data=&quot;bus.a.aa&quot;/&gt;  	&lt;br&gt;	TextInput_converter&lt;nosliw-textinput data=&quot;bus.a.aa&quot;/&gt;  	&lt;br&gt;	Content:&lt;%=?(business.a.aa)?.value + '   6666 ' %&gt;	&lt;br&gt;	Content:&lt;%=#|?(business)?.a.aa.subString(from:&amp;(from)&amp;,to:&amp;(to)&amp;)|#.value + ?(business.a.dd)? + ' 6666 ' %&gt;	&lt;br&gt;	Attribute:&lt;span  style=&quot;color:&lt;%=#|?(business)?.a.aa.subString(from:&amp;(from)&amp;,to:&amp;(to)&amp;)|#.value=='s isfff'?'red':'blue'%&gt;&quot;&gt;Phone Number : &lt;/span&gt; 	&lt;br&gt;	TextInput:&lt;nosliw-textinput data=&quot;business.a.aa&quot;/&gt;  	&lt;br&gt;	TextInput: &lt;nosliw-textinput data=&quot;business.a.aa&quot;/&gt;  	&lt;br&gt;	Switch/case	&lt;br&gt;		&lt;nosliw-switch variable=&quot;business.a.aa.value&quot;&gt;			&lt;br&gt; switch&lt;br&gt;			&lt;%=?(internal_switchVariable)?%&gt;								&lt;nosliw-case value=&quot;This is my world!&quot;&gt;					&lt;br&gt;First one &lt;br&gt;				&lt;/nosliw-case&gt;							&lt;nosliw-case value=&quot;This is my w&quot;&gt;					&lt;br&gt;Second one &lt;br&gt;				&lt;/nosliw-case&gt;				&lt;nosliw-casedefault&gt;					&lt;br&gt;Default one &lt;br&gt;				&lt;/nosliw-casedefualt&gt;			&lt;br&gt;		&lt;/nosliw-switch&gt;--&gt;    &lt;!-- This part can be used to define context (variable)				it describle data type criteria for each context element and its default value		--&gt;  &lt;!-- This part can be used to define expressions		--&gt;",
"constants":{"from":{"literate":{"dataTypeId":"test.integer","value":3},
"processed":true,
"value":{"dataTypeId":"test.integer","value":3}
},
"to":{"literate":{"dataTypeId":"test.integer","value":7},
"processed":true,
"value":{"dataTypeId":"test.integer","value":7}
},
"childResource":{"literate":{"public":{"name":{"path":"business.a.aa"}},"inherit":true},
"processed":true,
"value":{"public":{"name":{"path":"business.a.aa"}},"inherit":true}
},
"aaaa":{"literate":"<%=5+6+7%>",
"processed":true,
"value":18.0
},
"bbbb":{"literate":"<%=(5+6+7)>5%>",
"processed":true,
"value":true
},
"cccc":{"literate":{"a":12345,"b":true,"c":"good","d":18},
"processed":true,
"value":{"a":12345,"b":true,"c":"good","d":18}
},
"dddd":{"literate":"<%=&(cccc)&.a+6%>",
"processed":true,
"value":12351.0
},
"ffff":{"literate":"<%=#|&(#test##string___Thisismyworldabcdef)&|#%>",
"processed":true,
"value":{"dataTypeId":"test.string;1.0.0","valueFormat":"LITERATE","value":"Thisismyworldabcdef"}
},
"eeee":{"literate":"<%=#|&(ffff)&.subString(from:&(#test##integer___3)&,to:&(#test##integer___7)&)|#%>",
"processed":true,
"value":{"dataTypeId":"test.string;1.0.0","value":"sismfff"}
},
"base":{"literate":{"dataTypeId":"test.string","value":"This is my world!"},
"processed":true,
"value":{"dataTypeId":"test.string","value":"This is my world!"}
}
},
"script":{
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
			var uiDataOperation = new node_UIDataOperation(new node_createContextVariable("business.a.cc"), node_uiDataOperationServiceUtility.createAddElementOperationService("", 2, eleData));
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
}, {"loadPattern":"file"
});

