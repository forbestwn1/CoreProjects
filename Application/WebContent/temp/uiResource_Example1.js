
nosliw.runtime.getResourceService().importResource({"id":{"id":"Example1",
"type":"uiResource"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"Example1",
"type":"resource",
"context":{"business":{"children":{"a":{"children":{"aa":{"children":{},
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
"scriptExpressionsInContent":[],
"scriptExpressionInAttributes":[],
"scriptExpressionTagAttributes":[],
"elementEvents":[{"uiId":"161",
"event":"click",
"function":"newElementInLoop"
}],
"tagEvents":[],
"uiTags":{"156":{"id":"156",
"type":"tag",
"context":{"business":{"children":{"a":{"children":{"aa":{"children":{},
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
"internal_data":{"children":{"element":{"children":{},
"definition":"test.string;1.0.0"
}
},
"path":{"rootEleName":"business",
"path":"a.cc"
},
"type":"relative"
}
},
"scriptExpressionsInContent":[{"uiId":"157",
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
"attributes":{"data":"business.a.cc",
"element":"ele",
"nosliwid":"156"
},
"html":"&lt;span&gt; &lt;/span&gt;&lt;span nosliwid=&quot;157&quot;&gt;&lt;/span&gt;&lt;span&gt; In Loop &lt;/span&gt;&lt;br nosliwid=&quot;158&quot;&gt;",
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
"html":"&lt;br nosliwid=&quot;154&quot;&gt;&lt;span&gt;Loop: &lt;/span&gt;&lt;br nosliwid=&quot;155&quot;&gt; &lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;156-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;156-tag-end&quot;&gt;&lt;/nosliw&gt; &lt;br nosliwid=&quot;159&quot;&gt; &lt;br nosliwid=&quot;160&quot;&gt;&lt;a href=&quot;&quot; nosliwid=&quot;161&quot;&gt;&lt;span&gt;New&lt;/span&gt;&lt;/a&gt; &lt;!-- Contexts define the variables input for this ui resource --&gt; &lt;!--	&lt;body&gt;	&lt;%=?(business.a.aa)?.value + '   6666 ' %&gt;  tttttttttt222  	&lt;br&gt;	lalalala	&lt;%=#|?(business)?.a.aa.subString(from:&amp;(from)&amp;,to:&amp;(to)&amp;)|#.value + ?(business.a.dd)? + ' 6666 ' %&gt;  tttttttttt222 		&lt;br&gt;Test Attribute Expression:&lt;br&gt;		&lt;span  style=&quot;color:&lt;%=#|?(business)?.a.aa.subString(from:&amp;(from)&amp;,to:&amp;(to)&amp;)|#.value=='s isfff'?'red':'blue'%&gt;&quot;&gt;Phone Number : &lt;/span&gt; 		&lt;br&gt;&lt;a href='' nosliw-event=&quot;click:testLinkEvent:&quot;&gt;New Department&lt;/a&gt;				&lt;br&gt;		&lt;nosliw-textinput data=&quot;business.a.aa&quot;/&gt;  		&lt;br&gt;		&lt;nosliw-textinput data=&quot;business.a.aa&quot;/&gt;  --&gt;    &lt;!-- This part can be used to define context (variable)				it describle data type criteria for each context element and its default value		--&gt;  &lt;!-- This part can be used to define expressions		--&gt;",
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
			
			var node_createDataOperationService = nosliw.getNodeData("uidata.dataoperation.createDataOperationService");
			var node_createDataOperationRequest = nosliw.getNodeData("uidata.dataoperation.createDataOperationRequest");
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			
			var ele = {
				dataTypeId: "test.string;1.0.0",
				value: "This is my world 33333!"
			};

			var dataOperationService = node_createDataOperationService(undefined, "WRAPPER_OPERATION_ADDELEMENT", "business.a.cc", ele, 4);
			this.getContext().requestDataOperation(dataOperationService);
			
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
"uiTagLibs":["loop"]
}, {"loadPattern":"file"
});

