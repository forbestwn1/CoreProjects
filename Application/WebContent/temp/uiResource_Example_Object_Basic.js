
nosliw.runtime.getResourceService().importResource({"id":{"id":"Example_Object_Basic",
"type":"uiResource"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"Example_Object_Basic",
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
"private":{},
"excluded":{}
},
"scriptExpressionsInContent":[{"scriptExpressions":{"0":{"id":"0",
"definition":"#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value + ?(business.a.dd)? + ' 6666 ' ",
"variableNames":["business.a.aa","business.a.dd"],
"expressions":{"0":{"operand":{"type":"operation",
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
"outputCriteria":"test.string;1.0.0",
"variableName":"business.a.aa"
}
},
"matchersParms":{"base":{"test.string;1.0.0":{"dataTypeId":"test.string;1.0.0",
"relationship":{"source":"test.string;1.0.0",
"target":"test.string;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{},
"reverse":false
}
},
"from":{"test.integer;1.0.0":{"dataTypeId":"test.integer;1.0.0",
"relationship":{"source":"test.integer;1.0.0",
"target":"test.integer;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{},
"reverse":false
}
},
"to":{"test.integer;1.0.0":{"dataTypeId":"test.integer;1.0.0",
"relationship":{"source":"test.integer;1.0.0",
"target":"test.integer;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{},
"reverse":false
}
}
}
},
"variablesMatchers":{},
"variableInfos":{"business.a.aa":{"status":"close",
"criteria":"test.string;1.0.0"
},
"business.a.bb":{"status":"close",
"criteria":"test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(&amp;(expression)&amp;;;&amp;(parms)&amp;)||@||%"
},
"business.a.cc.element":{"status":"close",
"criteria":"test.string;1.0.0"
}
}
}
}
}
},
"uiId":"93",
"scriptFunction":function(scriptExpressionData){
	return scriptExpressionData["0"];
} 
,
"scriptExpressionScriptFunction":{"0":function(expressionsData, constantsData, variablesData){
	return expressionsData["0"].value + variablesData["business.a.dd"] + ' 6666 ' ;
} 

}
},{"scriptExpressions":{"0":{"id":"0",
"definition":"?(business.a.aa)?.value + ' 6666 ' ",
"variableNames":["business.a.aa"],
"expressions":{}
}
},
"uiId":"92",
"scriptFunction":function(scriptExpressionData){
	return scriptExpressionData["0"];
} 
,
"scriptExpressionScriptFunction":{"0":function(expressionsData, constantsData, variablesData){
	return variablesData["business.a.aa"].value + ' 6666 ' ;
} 

}
}],
"scriptExpressionInAttributes":[{"scriptExpressions":{"0":{"id":"0",
"definition":"#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value=='s isfff'?'red':'blue'",
"variableNames":["business.a.aa"],
"expressions":{"0":{"operand":{"type":"operation",
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
"outputCriteria":"test.string;1.0.0",
"variableName":"business.a.aa"
}
},
"matchersParms":{"base":{"test.string;1.0.0":{"dataTypeId":"test.string;1.0.0",
"relationship":{"source":"test.string;1.0.0",
"target":"test.string;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{},
"reverse":false
}
},
"from":{"test.integer;1.0.0":{"dataTypeId":"test.integer;1.0.0",
"relationship":{"source":"test.integer;1.0.0",
"target":"test.integer;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{},
"reverse":false
}
},
"to":{"test.integer;1.0.0":{"dataTypeId":"test.integer;1.0.0",
"relationship":{"source":"test.integer;1.0.0",
"target":"test.integer;1.0.0",
"path":[],
"targetType":"self"
},
"subMatchers":{},
"reverse":false
}
}
}
},
"variablesMatchers":{},
"variableInfos":{"business.a.aa":{"status":"close",
"criteria":"test.string;1.0.0"
},
"business.a.bb":{"status":"close",
"criteria":"test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(&amp;(expression)&amp;;;&amp;(parms)&amp;)||@||%"
},
"business.a.cc.element":{"status":"close",
"criteria":"test.string;1.0.0"
}
}
}
}
}
},
"uiId":"97",
"scriptFunction":function(scriptExpressionData){
	return "color:"+scriptExpressionData["0"];
} 
,
"scriptExpressionScriptFunction":{"0":function(expressionsData, constantsData, variablesData){
	return expressionsData["0"].value=='s isfff'?'red':'blue';
} 

},
"attribute":"style"
}],
"scriptExpressionTagAttributes":[],
"elementEvents":[{"uiId":"106",
"event":"click",
"function":"newElementInLoop"
}],
"tagEvents":[],
"uiTags":{"99":{"id":"99",
"type":"tag",
"context":{"public":{},
"internal":{},
"private":{"internal_data":{"children":{},
"definition":"test.string;1.0.0",
"type":"relative",
"path":{"rootEleName":"business",
"path":"a.aa"
}
}
},
"excluded":{}
},
"scriptExpressionsInContent":[],
"scriptExpressionInAttributes":[],
"scriptExpressionTagAttributes":[],
"elementEvents":[],
"tagEvents":[],
"uiTags":{},
"attributes":{"data":"business.a.aa",
"nosliwid":"99"
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
"value":18
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
"events":{},
"services":{},
"tagName":"textinput"
},
"101":{"id":"101",
"type":"tag",
"context":{"public":{},
"internal":{},
"private":{"internal_data":{"children":{},
"definition":"test.string;1.0.0",
"type":"relative",
"path":{"rootEleName":"business",
"path":"a.aa"
}
}
},
"excluded":{}
},
"scriptExpressionsInContent":[],
"scriptExpressionInAttributes":[],
"scriptExpressionTagAttributes":[],
"elementEvents":[],
"tagEvents":[],
"uiTags":{},
"attributes":{"data":"business.a.aa",
"nosliwid":"101"
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
"value":18
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
"events":{},
"services":{},
"tagName":"textinput"
},
"103":{"id":"103",
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
"type":"relative",
"path":{"rootEleName":"business"
}
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
"attributes":{"nosliwid":"103"
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
"value":18
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
"events":{},
"services":{},
"tagName":"debug"
},
"110":{"id":"110",
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
"type":"relative",
"path":{"rootEleName":"business"
}
}
},
"internal":{},
"private":{"internal_data":{"children":{"element":{"children":{},
"definition":"test.string;1.0.0"
}
},
"type":"relative",
"path":{"rootEleName":"business",
"path":"a.cc"
}
}
},
"excluded":{"ele":{"children":{},
"definition":"test.string;1.0.0",
"type":"relative",
"path":{"rootEleName":"business",
"path":"a.cc.element"
}
}
}
},
"scriptExpressionsInContent":[{"scriptExpressions":{"0":{"id":"0",
"definition":"?(index)?",
"variableNames":["index"],
"expressions":{}
}
},
"uiId":"111",
"scriptFunction":function(scriptExpressionData){
	return scriptExpressionData["0"];
} 
,
"scriptExpressionScriptFunction":{"0":function(expressionsData, constantsData, variablesData){
	return variablesData["index"];
} 

}
},{"scriptExpressions":{"0":{"id":"0",
"definition":"?(ele)?.value + ' 7777 ' ",
"variableNames":["ele"],
"expressions":{}
}
},
"uiId":"112",
"scriptFunction":function(scriptExpressionData){
	return scriptExpressionData["0"];
} 
,
"scriptExpressionScriptFunction":{"0":function(expressionsData, constantsData, variablesData){
	return variablesData["ele"].value + ' 7777 ' ;
} 

}
}],
"scriptExpressionInAttributes":[],
"scriptExpressionTagAttributes":[],
"elementEvents":[{"uiId":"115",
"event":"click",
"function":"deleteElementInLoop"
}],
"tagEvents":[],
"uiTags":{"117":{"id":"117",
"type":"tag",
"context":{"public":{},
"internal":{},
"private":{"internal_data":{"children":{},
"definition":"test.string;1.0.0",
"type":"relative",
"path":{"rootEleName":"ele"
}
}
},
"excluded":{}
},
"scriptExpressionsInContent":[],
"scriptExpressionInAttributes":[],
"scriptExpressionTagAttributes":[],
"elementEvents":[],
"tagEvents":[],
"uiTags":{},
"attributes":{"data":"ele",
"nosliwid":"117"
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
"value":18
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
"events":{},
"services":{},
"tagName":"textinput"
},
"119":{"id":"119",
"type":"tag",
"context":{"public":{},
"internal":{},
"private":{"internal_data":{"children":{},
"definition":"test.string;1.0.0",
"type":"relative",
"path":{"rootEleName":"ele"
}
}
},
"excluded":{}
},
"scriptExpressionsInContent":[],
"scriptExpressionInAttributes":[],
"scriptExpressionTagAttributes":[],
"elementEvents":[],
"tagEvents":[],
"uiTags":{},
"attributes":{"data":"ele",
"nosliwid":"119"
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
"value":18
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
"events":{},
"services":{},
"tagName":"textinput"
}
},
"attributes":{"data":"business.a.cc",
"element":"ele",
"index":"index",
"nosliwid":"110"
},
"html":"&lt;br nosliwid=&quot;113&quot;&gt;&lt;span&gt; Index: &lt;/span&gt;&lt;span nosliwid=&quot;111&quot;&gt;&lt;/span&gt;&lt;span&gt; &lt;/span&gt;&lt;br nosliwid=&quot;114&quot;&gt;&lt;span&gt; &lt;/span&gt;&lt;span nosliwid=&quot;112&quot;&gt;&lt;/span&gt;&lt;span&gt; &lt;/span&gt;&lt;a href=&quot;&quot; nosliwid=&quot;115&quot;&gt;&lt;span&gt;Delete&lt;/span&gt;&lt;/a&gt; &lt;br nosliwid=&quot;116&quot;&gt;&lt;span&gt; TextInput:&lt;/span&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;117-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;117-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;br nosliwid=&quot;118&quot;&gt;&lt;span&gt; TextInput:&lt;/span&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;119-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;119-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;br nosliwid=&quot;120&quot;&gt;",
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
"value":18
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
"events":{},
"services":{},
"script":{
				deleteElementInLoop : function(data, info){
					event.preventDefault();
				
					var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
					var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
					var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
					var node_createBatchUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createBatchUIDataOperationRequest");
					var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
					var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
					var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
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
			},
"tagName":"loop"
}
},
"attributes":{},
"html":"&lt;br nosliwid=&quot;94&quot;&gt;&lt;span&gt; Content:&lt;/span&gt;&lt;span nosliwid=&quot;92&quot;&gt;&lt;/span&gt;&lt;span&gt; &lt;/span&gt;&lt;br nosliwid=&quot;95&quot;&gt;&lt;span&gt; Content:&lt;/span&gt;&lt;span nosliwid=&quot;93&quot;&gt;&lt;/span&gt;&lt;span&gt; &lt;/span&gt;&lt;br nosliwid=&quot;96&quot;&gt;&lt;span&gt; Attribute:&lt;/span&gt;&lt;span style=&quot;&quot; nosliwid=&quot;97&quot;&gt;&lt;span&gt;Phone Number : &lt;/span&gt;&lt;/span&gt;&lt;span&gt; &lt;/span&gt;&lt;br nosliwid=&quot;98&quot;&gt;&lt;span&gt; TextInput:&lt;/span&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;99-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;99-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;br nosliwid=&quot;100&quot;&gt;&lt;span&gt; TextInput: &lt;/span&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;101-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;101-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;br nosliwid=&quot;102&quot;&gt; &lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;103-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;103-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;br nosliwid=&quot;104&quot;&gt; &lt;br nosliwid=&quot;105&quot;&gt;&lt;a href=&quot;&quot; nosliwid=&quot;106&quot;&gt;&lt;span&gt;New&lt;/span&gt;&lt;/a&gt;&lt;br nosliwid=&quot;107&quot;&gt; &lt;br nosliwid=&quot;108&quot;&gt; &lt;br nosliwid=&quot;109&quot;&gt; &lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;110-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;110-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;br nosliwid=&quot;121&quot;&gt;   &lt;!-- This part can be used to define context (variable)				it describle data type criteria for each context element and its default value		--&gt;  &lt;!-- This part can be used to define expressions		--&gt;",
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
"value":18
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
"events":{},
"services":{},
"script":{
		newElementInLoop : function(data, info){

			event.preventDefault();

			var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
			var node_createBatchUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createBatchUIDataOperationRequest");
			var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
			var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
			var node_createContextVariableInfo = nosliw.getNodeData("uidata.context.createContextVariableInfo");
			
			var eleData = {
				dataTypeId: "test.string;1.0.0",
				value: "This is my world 33333!"
			};

			var requestInfo = node_createBatchUIDataOperationRequest(this.getContext());
			var uiDataOperation = new node_UIDataOperation(node_createContextVariableInfo("business.a.cc"), node_uiDataOperationServiceUtility.createAddElementOperationService("", eleData, 1));
			requestInfo.addUIDataOperation(uiDataOperation);						
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},
	}
}, {"loadPattern":"file"
});

