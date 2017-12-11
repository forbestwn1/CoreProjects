
nosliw.runtime.getResourceService().importResource({"id":{"id":"Example1",
"type":"uiResource"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"Example1",
"type":"resource",
"context":{"public":{"business":{"children":{"a":{"children":{"aa":{"children":{},
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
"private":{}
},
"scriptExpressionsInContent":[{"uiId":"383",
"scriptExpressions":{"0":{"id":"0",
"definition":"#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value + ?(business.a.dd)? + ' 6666 ' ",
"variableNames":["business.a.aa","business.a.dd"],
"expressions":{"0_0":{"id":"0_0_no79",
"expressionDefinition":{"name":"0_0",
"expression":"?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)",
"variableCriterias":{},
"constants":{},
"references":{}
},
"operand":{"type":"operation",
"outputCriteria":"test.string;1.0.0",
"operation":"subString",
"dataTypeId":"test.string;1.0.0",
"parms":{"from":{"type":"constant",
"outputCriteria":"test.integer;1.0.0",
"name":"from",
"data":{"dataTypeId":"test.integer;1.0.0",
"valueFormat":"JSON",
"value":3
}
},
"to":{"type":"constant",
"outputCriteria":"test.integer;1.0.0",
"name":"to",
"data":{"dataTypeId":"test.integer;1.0.0",
"valueFormat":"JSON",
"value":7
}
},
"base":{"type":"variable",
"outputCriteria":"#||test.string;1.0.0||#",
"variableName":"business.a.aa"
}
},
"matchersParms":{"base":{"test.string;1.0.0":{"dataTypeId":"test.string;1.0.0",
"relationship":{"source":"test.string;1.0.0",
"target":"test.string;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{}
}
},
"from":{"test.integer;1.0.0":{"dataTypeId":"test.integer;1.0.0",
"relationship":{"source":"test.integer;1.0.0",
"target":"test.integer;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{}
}
},
"to":{"test.integer;1.0.0":{"dataTypeId":"test.integer;1.0.0",
"relationship":{"source":"test.integer;1.0.0",
"target":"test.integer;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{}
}
}
}
},
"variableInfos":{"business.a.aa":{"status":"close",
"criteria":"test.string;1.0.0"
},
"business.a.bb":{"status":"close",
"criteria":"test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(&amp;(expression)&amp;;;&amp;(parms)&amp;)||@||%"
},
"business.a.cc.element":{"status":"close",
"criteria":"test.string;1.0.0"
}
},
"errorMsgs":[],
"variablesMatchers":{"business.a.aa":{"test.string;1.0.0":{"dataTypeId":"test.string;1.0.0",
"relationship":{"source":"test.string;1.0.0",
"target":"test.string;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{}
}
}
},
"references":{}
}
},
"scriptFunction":function(expressionsData, constantsData, variablesData){
	return expressionsData["0_0"].value + variablesData["business.a.dd"] + ' 6666 ' ;
} 

}
},
"scriptFunction":function(scriptExpressionData){
	return scriptExpressionData["0"];
} 

},{"uiId":"382",
"scriptExpressions":{"0":{"id":"0",
"definition":"?(business.a.aa)?.value + ' 6666 ' ",
"variableNames":["business.a.aa"],
"expressions":{},
"scriptFunction":function(expressionsData, constantsData, variablesData){
	return variablesData["business.a.aa"].value + ' 6666 ' ;
} 

}
},
"scriptFunction":function(scriptExpressionData){
	return scriptExpressionData["0"];
} 

}],
"scriptExpressionInAttributes":[{"uiId":"397",
"scriptExpressions":{"0":{"id":"0",
"definition":"#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value=='s isfff'?'red':'blue'",
"variableNames":["business.a.aa"],
"expressions":{"0_0":{"id":"0_0_no80",
"expressionDefinition":{"name":"0_0",
"expression":"?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)",
"variableCriterias":{},
"constants":{},
"references":{}
},
"operand":{"type":"operation",
"outputCriteria":"test.string;1.0.0",
"operation":"subString",
"dataTypeId":"test.string;1.0.0",
"parms":{"from":{"type":"constant",
"outputCriteria":"test.integer;1.0.0",
"name":"from",
"data":{"dataTypeId":"test.integer;1.0.0",
"valueFormat":"JSON",
"value":3
}
},
"to":{"type":"constant",
"outputCriteria":"test.integer;1.0.0",
"name":"to",
"data":{"dataTypeId":"test.integer;1.0.0",
"valueFormat":"JSON",
"value":7
}
},
"base":{"type":"variable",
"outputCriteria":"#||test.string;1.0.0||#",
"variableName":"business.a.aa"
}
},
"matchersParms":{"base":{"test.string;1.0.0":{"dataTypeId":"test.string;1.0.0",
"relationship":{"source":"test.string;1.0.0",
"target":"test.string;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{}
}
},
"from":{"test.integer;1.0.0":{"dataTypeId":"test.integer;1.0.0",
"relationship":{"source":"test.integer;1.0.0",
"target":"test.integer;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{}
}
},
"to":{"test.integer;1.0.0":{"dataTypeId":"test.integer;1.0.0",
"relationship":{"source":"test.integer;1.0.0",
"target":"test.integer;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{}
}
}
}
},
"variableInfos":{"business.a.aa":{"status":"close",
"criteria":"test.string;1.0.0"
},
"business.a.bb":{"status":"close",
"criteria":"test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(&amp;(expression)&amp;;;&amp;(parms)&amp;)||@||%"
},
"business.a.cc.element":{"status":"close",
"criteria":"test.string;1.0.0"
}
},
"errorMsgs":[],
"variablesMatchers":{"business.a.aa":{"test.string;1.0.0":{"dataTypeId":"test.string;1.0.0",
"relationship":{"source":"test.string;1.0.0",
"target":"test.string;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{}
}
}
},
"references":{}
}
},
"scriptFunction":function(expressionsData, constantsData, variablesData){
	return expressionsData["0_0"].value=='s isfff'?'red':'blue';
} 

}
},
"scriptFunction":function(scriptExpressionData){
	return "color:"+scriptExpressionData["0"];
} 
,
"attribute":"style"
}],
"scriptExpressionTagAttributes":[],
"elementEvents":[{"uiId":"392",
"event":"click",
"function":"newElementInLoop"
}],
"tagEvents":[],
"uiTags":{"386":{"id":"386",
"type":"tag",
"context":{"public":{"business":{"children":{"a":{"children":{"aa":{"children":{},
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
},
"ele":{"children":{},
"definition":"test.string;1.0.0",
"path":{"rootEleName":"business",
"path":"a.cc.element"
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
}
},
"scriptExpressionsInContent":[{"uiId":"387",
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
"elementEvents":[{"uiId":"388",
"event":"click",
"function":"deleteElementInLoop"
}],
"tagEvents":[],
"uiTags":{},
"attributes":{"data":"business.a.cc",
"element":"ele",
"elename":"index",
"nosliwid":"386"
},
"html":"&lt;span&gt; &lt;/span&gt;&lt;span nosliwid=&quot;387&quot;&gt;&lt;/span&gt;&lt;span&gt; &lt;/span&gt;&lt;a href=&quot;&quot; nosliwid=&quot;388&quot;&gt;&lt;span&gt;Delete&lt;/span&gt;&lt;/a&gt;&lt;span&gt; In Loop &lt;/span&gt;&lt;br nosliwid=&quot;389&quot;&gt;",
"constants":{"from":{"literate":{"dataTypeId":"test.integer","value":3},
"processed":true,
"value":{"dataTypeId":"test.integer","value":3}
},
"to":{"literate":{"dataTypeId":"test.integer","value":7},
"processed":true,
"value":{"dataTypeId":"test.integer","value":7}
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

					var index = this.getContext().getContextElementData("index").value;
					
					var requestInfo = node_createBatchUIDataOperationRequest(this.getContext());
					var uiDataOperation = new node_UIDataOperation(new node_createContextVariable("business.a.cc"), node_uiDataOperationServiceUtility.createDeleteElementOperationService("", index));
					requestInfo.addUIDataOperation(uiDataOperation);						
					node_requestServiceProcessor.processRequest(requestInfo, false);
				}
			},
"tagName":"loop"
},
"399":{"id":"399",
"type":"tag",
"context":{"public":{},
"internal":{},
"private":{"internal_data":{"children":{},
"definition":"test.string;1.0.0",
"path":{"rootEleName":"business",
"path":"a.aa"
},
"type":"relative"
}
}
},
"scriptExpressionsInContent":[],
"scriptExpressionInAttributes":[],
"scriptExpressionTagAttributes":[],
"elementEvents":[],
"tagEvents":[],
"uiTags":{},
"attributes":{"data":"business.a.aa",
"nosliwid":"399"
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
"tagName":"textinput"
},
"401":{"id":"401",
"type":"tag",
"context":{"public":{},
"internal":{},
"private":{"internal_data":{"children":{},
"definition":"test.string;1.0.0",
"path":{"rootEleName":"business",
"path":"a.aa"
},
"type":"relative"
}
}
},
"scriptExpressionsInContent":[],
"scriptExpressionInAttributes":[],
"scriptExpressionTagAttributes":[],
"elementEvents":[],
"tagEvents":[],
"uiTags":{},
"attributes":{"data":"business.a.aa",
"nosliwid":"401"
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
"tagName":"textinput"
}
},
"attributes":{},
"html":"&lt;br nosliwid=&quot;384&quot;&gt;&lt;span&gt;Loop: &lt;/span&gt;&lt;br nosliwid=&quot;385&quot;&gt; &lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;386-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;386-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;br nosliwid=&quot;390&quot;&gt; &lt;br nosliwid=&quot;391&quot;&gt;&lt;a href=&quot;&quot; nosliwid=&quot;392&quot;&gt;&lt;span&gt;New&lt;/span&gt;&lt;/a&gt;&lt;br nosliwid=&quot;393&quot;&gt; &lt;!-- Contexts define the variables input for this ui resource --&gt; &lt;span&gt; &lt;/span&gt;&lt;span nosliwid=&quot;382&quot;&gt;&lt;/span&gt;&lt;span&gt; tttttttttt222 &lt;/span&gt;&lt;br nosliwid=&quot;394&quot;&gt;&lt;span&gt; lalalala &lt;/span&gt;&lt;span nosliwid=&quot;383&quot;&gt;&lt;/span&gt;&lt;span&gt; tttttttttt222 &lt;/span&gt;&lt;br nosliwid=&quot;395&quot;&gt;&lt;span&gt;Test Attribute Expression:&lt;/span&gt;&lt;br nosliwid=&quot;396&quot;&gt;&lt;span&gt; &lt;/span&gt;&lt;span style=&quot;&quot; nosliwid=&quot;397&quot;&gt;&lt;span&gt;Phone Number : &lt;/span&gt;&lt;/span&gt;&lt;span&gt; &lt;/span&gt;&lt;!--		&lt;br&gt;&lt;a href='' nosliw-event=&quot;click:testLinkEvent:&quot;&gt;New Department&lt;/a&gt;  --&gt; &lt;br nosliwid=&quot;398&quot;&gt; &lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;399-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;399-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;br nosliwid=&quot;400&quot;&gt; &lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;401-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;401-tag-end&quot;&gt;&lt;/nosliw&gt;    &lt;!-- This part can be used to define context (variable)				it describle data type criteria for each context element and its default value		--&gt;  &lt;!-- This part can be used to define expressions		--&gt;",
"constants":{"from":{"literate":{"dataTypeId":"test.integer","value":3},
"processed":true,
"value":{"dataTypeId":"test.integer","value":3}
},
"to":{"literate":{"dataTypeId":"test.integer","value":7},
"processed":true,
"value":{"dataTypeId":"test.integer","value":7}
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
			var uiDataOperation = new node_UIDataOperation(new node_createContextVariable("business.a.cc"), node_uiDataOperationServiceUtility.createAddElementOperationService("", 4, eleData));
			requestInfo.addUIDataOperation(uiDataOperation);						
			node_requestServiceProcessor.processRequest(requestInfo, false);

			
//			var dataOperationService = node_createDataOperationService(undefined, "WRAPPER_OPERATION_ADDELEMENT", "business.a.cc", ele, 4);
//			this.getContext().requestDataOperation(dataOperationService);
			
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
	},
"uiTagLibs":["loop","textinput"]
}, {"loadPattern":"file"
});

