
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
},
"school":{"children":{},
"definition":"test.array;1.0.0",
"type":"absolute",
"default":{"dataTypeId":"test.array;1.0.0","value":[{"dataTypeId":"test.string;1.0.0","value":"This is my world 1111!"},{"dataTypeId":"test.string;1.0.0","value":"This is my world 2222!"}]}
},
"company":{"children":{},
"definition":"test.map;1.0.0",
"type":"absolute",
"default":{"dataTypeId":"test.map;1.0.0","value":{"name":{"dataTypeId":"test.string;1.0.0","value":"Nosliw Company"}}}
}
},
"internal":{},
"private":{}
},
"scriptExpressionsInContent":[{"uiId":"275",
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

},{"uiId":"276",
"scriptExpressions":{"0":{"id":"0",
"definition":"?(company.name)?.value + ' 6666 ' ",
"variableNames":["company.name"],
"expressions":{},
"scriptFunction":function(expressionsData, constantsData, variablesData){
	return variablesData["company.name"].value + ' 6666 ' ;
} 

}
},
"scriptFunction":function(scriptExpressionData){
	return scriptExpressionData["0"];
} 

}],
"scriptExpressionInAttributes":[],
"scriptExpressionTagAttributes":[],
"elementEvents":[],
"tagEvents":[],
"uiTags":{"278":{"id":"278",
"type":"tag",
"context":{"public":{},
"internal":{},
"private":{"internal_data":{"children":{},
"path":{"rootEleName":"company",
"path":"name"
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
"attributes":{"data":"company.name",
"nosliwid":"278"
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
"281":{"id":"281",
"type":"tag",
"context":{"public":{},
"internal":{},
"private":{"internal_data":{"children":{},
"path":{"rootEleName":"company",
"path":"name"
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
"attributes":{"data":"company.name",
"nosliwid":"281"
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
"283":{"id":"283",
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
"school":{"children":{},
"definition":"test.array;1.0.0",
"path":{"rootEleName":"school"
},
"type":"relative"
},
"company":{"children":{},
"definition":"test.map;1.0.0",
"path":{"rootEleName":"company"
},
"type":"relative"
},
"ele":{"children":{},
"path":{"rootEleName":"school",
"path":"element"
},
"type":"relative"
}
},
"internal":{},
"private":{"internal_data":{"children":{},
"definition":"test.array;1.0.0",
"path":{"rootEleName":"school"
},
"type":"relative"
}
}
},
"scriptExpressionsInContent":[{"uiId":"284",
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
"elementEvents":[],
"tagEvents":[],
"uiTags":{},
"attributes":{"data":"school",
"element":"ele",
"elename":"index",
"nosliwid":"283"
},
"html":"&lt;span&gt; &lt;/span&gt;&lt;span nosliwid=&quot;284&quot;&gt;&lt;/span&gt;&lt;span&gt; In Loop &lt;/span&gt;&lt;br nosliwid=&quot;285&quot;&gt;",
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
"tagName":"loop"
}
},
"attributes":{},
"html":"&lt;br nosliwid=&quot;277&quot;&gt; &lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;278-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;278-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;br nosliwid=&quot;279&quot;&gt; &lt;br nosliwid=&quot;280&quot;&gt; &lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;281-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;281-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;br nosliwid=&quot;282&quot;&gt; &lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;283-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;283-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;br nosliwid=&quot;286&quot;&gt;&lt;span&gt; &lt;/span&gt;&lt;span nosliwid=&quot;275&quot;&gt;&lt;/span&gt;&lt;span&gt; tttttttttt222 &lt;/span&gt;&lt;br nosliwid=&quot;287&quot;&gt;&lt;span&gt; &lt;/span&gt;&lt;span nosliwid=&quot;276&quot;&gt;&lt;/span&gt;&lt;span&gt; tttttttttt222 &lt;/span&gt;&lt;!--		&lt;nosliw-switch variable=&quot;business.a.aa.value&quot;&gt;			&lt;br&gt; switch&lt;br&gt;						&lt;%=?(internal_switchVariable)?%&gt;								&lt;nosliw-case value=&quot;This is my world!&quot;&gt;					&lt;br&gt;First one &lt;br&gt;				&lt;/nosliw-case&gt;							&lt;nosliw-case value=&quot;This is my w&quot;&gt;					&lt;br&gt;Second one &lt;br&gt;				&lt;/nosliw-case&gt;				&lt;nosliw-casedefault&gt;					&lt;br&gt;Default one &lt;br&gt;				&lt;/nosliw-casedefualt&gt;						&lt;br&gt;				&lt;/nosliw-switch&gt;		&lt;br&gt;Loop:		&lt;br&gt;		&lt;nosliw-loop data=&quot;business.a.cc&quot; element=&quot;ele&quot; elename=&quot;index&quot;&gt;  			&lt;%=?(ele)?.value + '   6666 ' %&gt;   &lt;a href='' nosliw-event=&quot;click:deleteElementInLoop:&quot;&gt;Delete&lt;/a&gt;			In Loop			&lt;br&gt;			&lt;script&gt;			{				deleteElementInLoop : function(data, info){					alert(&quot;cccccc&quot;);					event.preventDefault();									var node_createContextVariable = nosliw.getNodeData(&quot;uidata.context.createContextVariable&quot;);					var node_CONSTANT = nosliw.getNodeData(&quot;constant.CONSTANT&quot;);					var node_requestServiceProcessor = nosliw.getNodeData(&quot;request.requestServiceProcessor&quot;);					var node_createBatchUIDataOperationRequest = nosliw.getNodeData(&quot;uidata.uidataoperation.createBatchUIDataOperationRequest&quot;);					var node_UIDataOperation = nosliw.getNodeData(&quot;uidata.uidataoperation.UIDataOperation&quot;);					var node_uiDataOperationServiceUtility = nosliw.getNodeData(&quot;uidata.uidataoperation.uiDataOperationServiceUtility&quot;);					var node_createContextVariable = nosliw.getNodeData(&quot;uidata.context.createContextVariable&quot;);					var index = this.getContext().getContextElementData(&quot;index&quot;).value;										var requestInfo = node_createBatchUIDataOperationRequest(this.getContext());					var uiDataOperation = new node_UIDataOperation(new node_createContextVariable(&quot;business.a.cc&quot;), node_uiDataOperationServiceUtility.createDeleteElementOperationService(&quot;&quot;, index));					requestInfo.addUIDataOperation(uiDataOperation);											node_requestServiceProcessor.processRequest(requestInfo, false);				}			}			&lt;/script&gt;					&lt;/nosliw-loop&gt;		&lt;br&gt;		&lt;br&gt;&lt;a href='' nosliw-event=&quot;click:newElementInLoop:&quot;&gt;New&lt;/a&gt;&lt;br&gt;					&lt;%=?(business.a.aa)?.value + '   6666 ' %&gt;  tttttttttt222  	&lt;br&gt;	lalalala	&lt;%=#|?(business)?.a.aa.subString(from:&amp;(from)&amp;,to:&amp;(to)&amp;)|#.value + ?(business.a.dd)? + ' 6666 ' %&gt;  tttttttttt222 		&lt;br&gt;Test Attribute Expression:&lt;br&gt;		&lt;span  style=&quot;color:&lt;%=#|?(business)?.a.aa.subString(from:&amp;(from)&amp;,to:&amp;(to)&amp;)|#.value=='s isfff'?'red':'blue'%&gt;&quot;&gt;Phone Number : &lt;/span&gt; 		&lt;br&gt;		&lt;nosliw-textinput data=&quot;business.a.aa&quot;/&gt;  		&lt;br&gt;		&lt;nosliw-textinput data=&quot;business.a.aa&quot;/&gt;  		&lt;br&gt;--&gt;    &lt;!-- This part can be used to define context (variable)				it describle data type criteria for each context element and its default value		--&gt;  &lt;!-- This part can be used to define expressions		--&gt;",
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

