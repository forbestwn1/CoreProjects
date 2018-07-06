
nosliw.runtime.getResourceService().importResource({"id":{"id":"Example_App_Result_School",
"type":"uiResource"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"Example_App_Result_School",
"type":"resource",
"context":{"public":{"result":{"children":{},
"definition":"test.array;1.0.0",
"type":"absolute",
"default":{"dataTypeId":"test.array;1.0.0","value":[{"dataTypeId":"test.map;1.0.0","value":{"geo":{"dataTypeId":"test.geo;1.0.0","value":{"latitude":43.651299,"longitude":-79.579473}},"schoolName":{"dataTypeId":"test.string;1.0.0","value":"School1"},"schoolRating":{"dataTypeId":"test.float;1.0.0","value":6}}},{"dataTypeId":"test.map;1.0.0","value":{"geo":{"dataTypeId":"test.geo;1.0.0","value":{"latitude":43.649016,"longitude":-79.485059}},"schoolName":{"dataTypeId":"test.string;1.0.0","value":"School2"},"schoolRating":{"dataTypeId":"test.float;1.0.0","value":8.5}}}]}
}
},
"internal":{},
"private":{},
"excluded":{}
},
"scriptExpressionsInContent":[{"uiId":"290",
"scriptExpressions":{"0":{"id":"0",
"definition":"#|?(result)?.length()|#.value",
"variableNames":["result"],
"expressions":{"0":{"operand":{"type":"operation",
"outputCriteria":"test.integer;1.0.0",
"operation":"length",
"dataTypeId":"test.array;1.0.0",
"parms":{"base":{"type":"variable",
"outputCriteria":"test.array;1.0.0",
"variableName":"result"
}
},
"matchersParms":{"base":{"test.array;1.0.0":{"dataTypeId":"test.array;1.0.0",
"relationship":{"source":"test.array;1.0.0",
"target":"test.array;1.0.0",
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
"variableInfos":{"result":{"status":"close",
"criteria":"test.array;1.0.0"
}
}
}
},
"scriptFunction":function(expressionsData, constantsData, variablesData){
	return expressionsData["0"].value;
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
"uiTags":{"293":{"id":"293",
"type":"tag",
"context":{"public":{"result":{"children":{},
"definition":"test.array;1.0.0",
"path":{"rootEleName":"result"
},
"type":"relative"
}
},
"internal":{},
"private":{"internal_data":{"children":{},
"definition":"test.array;1.0.0",
"path":{"rootEleName":"result"
},
"type":"relative"
}
},
"excluded":{"ele":{"children":{},
"path":{"rootEleName":"result",
"path":"element"
},
"type":"relative"
}
}
},
"scriptExpressionsInContent":[{"uiId":"296",
"scriptExpressions":{"0":{"id":"0",
"definition":"?(ele.schoolRating)?.value",
"variableNames":["ele.schoolRating"],
"expressions":{},
"scriptFunction":function(expressionsData, constantsData, variablesData){
	return variablesData["ele.schoolRating"].value;
} 

}
},
"scriptFunction":function(scriptExpressionData){
	return scriptExpressionData["0"];
} 

},{"uiId":"295",
"scriptExpressions":{"0":{"id":"0",
"definition":"?(ele.schoolName)?.value",
"variableNames":["ele.schoolName"],
"expressions":{},
"scriptFunction":function(expressionsData, constantsData, variablesData){
	return variablesData["ele.schoolName"].value;
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
"attributes":{"data":"result",
"element":"ele",
"index":"index",
"nosliwid":"293"
},
"html":"&lt;div style=&quot;height:40px;width:200px;&quot; nosliwid=&quot;294&quot;&gt;  &lt;br nosliwid=&quot;297&quot;&gt; &lt;span&gt; SchoolName: &lt;/span&gt; &lt;span nosliwid=&quot;295&quot;&gt;&lt;/span&gt; &lt;span&gt; &lt;/span&gt; &lt;br nosliwid=&quot;298&quot;&gt; &lt;span&gt; Rating: &lt;/span&gt; &lt;span nosliwid=&quot;296&quot;&gt;&lt;/span&gt; &lt;span&gt; &lt;/span&gt;&lt;/div&gt;",
"constants":{},
"tagName":"map"
}
},
"attributes":{},
"html":"&lt;br nosliwid=&quot;291&quot;&gt;&lt;span&gt; Sum:&lt;/span&gt;&lt;span nosliwid=&quot;290&quot;&gt;&lt;/span&gt;&lt;span&gt; &lt;/span&gt;&lt;br nosliwid=&quot;292&quot;&gt; &lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;293-tag-start&quot;&gt;&lt;/nosliw&gt;&lt;nosliw style=&quot;display:none;&quot; nosliwid=&quot;293-tag-end&quot;&gt;&lt;/nosliw&gt;    &lt;!-- This part can be used to define context (variable)				it describle data type criteria for each context element and its default value		--&gt;  &lt;!-- This part can be used to define expressions		--&gt;",
"constants":{},
"script":{
	
	}
}, {"loadPattern":"file"
});

