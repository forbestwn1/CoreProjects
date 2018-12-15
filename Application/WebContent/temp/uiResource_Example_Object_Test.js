
nosliw.runtime.getResourceService().importResource({"id":{"id":"Example_Object_Test",
"type":"uiResource"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"Example_Object_Test",
"type":"resource",
"context":{"nameMapping":{"business":"business___public"
},
"context":{"element":{"business___public":{"name":"",
"description":"",
"info":{},
"definition":{"type":"node",
"defaultValue":{"a":{"aa":{"dataTypeId":"test.string;1.0.0","value":"This is my world!"}}},
"child":{"a":{"type":"node",
"child":{"aa":{"criteria":{"status":"open",
"criteria":"test.string;1.0.0",
"info":{}
}
}
}
}
}
}
},
"business":{"info":{},
"definition":{"type":"relative",
"path":{"rootEleName":"business___public"
},
"isToParent":false
}
}
}
}
},
"scriptExpressionsInContent":[{"scriptExpressions":{"0":{"id":"0",
"definition":"?(business.a.aa)?.value + ' 6666 ' ",
"variableNames":["business___public.a.aa"],
"expressions":{}
}
},
"uiId":"4",
"scriptFunction":function(scriptExpressionData){
	return scriptExpressionData["0"];
} 
,
"scriptExpressionScriptFunction":{"0":function(expressionsData, constantsData, variablesData){
	return variablesData["business___public.a.aa"].value + ' 6666 ' ;
} 

}
}],
"scriptExpressionInAttributes":[],
"scriptExpressionTagAttributes":[],
"elementEvents":[],
"tagEvents":[],
"uiTags":{},
"attributes":{},
"html":"&lt;br nosliwid=&quot;5&quot;&gt;&lt;span&gt; Content:&lt;/span&gt;&lt;span nosliwid=&quot;4&quot;&gt;&lt;/span&gt;&lt;span&gt; &lt;/span&gt;&lt;!--		&lt;br&gt;	TextInput:&lt;nosliw-textinput data=&quot;business.a.aa&quot;/&gt;  	&lt;br&gt;	Content:&lt;%=#|?(business)?.a.aa.subString(from:&amp;(from)&amp;,to:&amp;(to)&amp;)|#.value + ?(business.a.dd)? + ' 6666 ' %&gt;	&lt;br&gt;--&gt;   &lt;!-- This part can be used to define context (variable)				it describle data type criteria for each context element and its default value		--&gt;  &lt;!-- This part can be used to define expressions		--&gt;",
"constants":{},
"events":{},
"commands":{},
"services":{},
"script":{ }
}, {"loadPattern":"file"
});

