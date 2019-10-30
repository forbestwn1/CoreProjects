
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"id":"Decoration_setting_framework7",
"type":"uiResource"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"Decoration_setting_framework7",
"type":"resource",
"context":{"local2Global":{"nosliw_module_setting_name":"nosliw_module_setting_name___public",
"nosliw_module_setting_persist":"nosliw_module_setting_persist___public",
"nosliw_module_setting_modified":"nosliw_module_setting_modified___public"
},
"context":{"element":{"nosliw_module_setting_name___public":{"name":"",
"description":"",
"info":{},
"definition":{"type":"value",
"processed":"true"
},
"defaultValue":""
},
"nosliw_module_setting_name":{"info":{},
"definition":{"type":"relative",
"processed":"true",
"path":{"rootEleName":"nosliw_module_setting_name___public"
},
"parent":"self",
"definition":{"type":"value",
"processed":"true"
}
},
"defaultValue":""
},
"nosliw_module_setting_persist___public":{"name":"",
"description":"",
"info":{},
"definition":{"type":"value",
"processed":"true"
},
"defaultValue":true
},
"nosliw_module_setting_persist":{"info":{},
"definition":{"type":"relative",
"processed":"true",
"path":{"rootEleName":"nosliw_module_setting_persist___public"
},
"parent":"self",
"definition":{"type":"value",
"processed":"true"
}
},
"defaultValue":true
},
"nosliw_module_setting_modified___public":{"name":"",
"description":"",
"info":{},
"definition":{"type":"value",
"processed":"true"
},
"defaultValue":true
},
"nosliw_module_setting_modified":{"info":{},
"definition":{"type":"relative",
"processed":"true",
"path":{"rootEleName":"nosliw_module_setting_modified___public"
},
"parent":"self",
"definition":{"type":"value",
"processed":"true"
}
},
"defaultValue":true
}
}
}
},
"elementEvents":[{"uiId":"1349",
"event":"click",
"function":"new"
},{"uiId":"1348",
"event":"click",
"function":"submit"
},{"uiId":"1351",
"event":"click",
"function":"save"
},{"uiId":"1350",
"event":"click",
"function":"delete"
}],
"tagEvents":[],
"attributes":{"nosliwattribute_placeholder":"id:pleaseEmbed"
},
"html":"&lt;br nosliwid=&quot;1346&quot;&gt;&lt;span&gt; SettingName : &lt;/span&gt;&lt;span nosliwid=&quot;1345&quot;&gt;&lt;/span&gt;&lt;span&gt; &lt;/span&gt;&lt;div nosliwid=&quot;1347&quot;&gt;  &lt;a href=&quot;&quot; nosliwid=&quot;1348&quot;&gt;&lt;span&gt;Submit&lt;/span&gt;&lt;/a&gt;  &lt;a href=&quot;&quot; nosliwid=&quot;1349&quot;&gt;&lt;span&gt;New&lt;/span&gt;&lt;/a&gt;  &lt;a href=&quot;&quot; style=&quot;&quot; nosliwid=&quot;1350&quot;&gt;&lt;span&gt;Delete&lt;/span&gt;&lt;/a&gt;  &lt;a href=&quot;&quot; style=&quot;&quot; nosliwid=&quot;1351&quot;&gt;&lt;span&gt;Save&lt;/span&gt;&lt;/a&gt; &lt;/div&gt; &lt;!--nosliw-contextvalue/--&gt; &lt;div id=&quot;pleaseEmbed&quot; nosliwid=&quot;1352&quot;&gt;&lt;/div&gt; &lt;br nosliwid=&quot;1353&quot;&gt;",
"constants":{},
"events":{"submit":{"name":"submit",
"description":"",
"info":{},
"data":{"element":{}
}
}
},
"commands":{},
"services":{},
"serviceProviders":{},
"scriptExpressionsInContent":[{"scriptExpressions":{"0":{"definition":"?(nosliw_module_setting_name)?",
"variableNames":["nosliw_module_setting_name"],
"expressions":{}
}
},
"uiId":"1345",
"scriptFunction":function(scriptExpressionData){
	return scriptExpressionData["0"];
} 
,
"scriptExpressionScriptFunction":{"0":function(expressionsData, constantsData, variablesData){
	return variablesData["nosliw_module_setting_name"];
} 

}
}],
"scriptExpressionInAttributes":[{"scriptExpressions":{"1":{"definition":"?(nosliw_module_setting_persist)?==true?'inline':'none'",
"variableNames":["nosliw_module_setting_persist"],
"expressions":{}
}
},
"uiId":"1350",
"attribute":"style",
"scriptFunction":function(scriptExpressionData){
	return "display:"+scriptExpressionData["1"];
} 
,
"scriptExpressionScriptFunction":{"1":function(expressionsData, constantsData, variablesData){
	return variablesData["nosliw_module_setting_persist"]==true?'inline':'none';
} 

}
},{"scriptExpressions":{"1":{"definition":"(!?(nosliw_module_setting_persist)?)||?(nosliw_module_setting_modified)?==true?'inline':'none'",
"variableNames":["nosliw_module_setting_persist","nosliw_module_setting_modified"],
"expressions":{}
}
},
"uiId":"1351",
"attribute":"style",
"scriptFunction":function(scriptExpressionData){
	return "display:"+scriptExpressionData["1"];
} 
,
"scriptExpressionScriptFunction":{"1":function(expressionsData, constantsData, variablesData){
	return (!variablesData["nosliw_module_setting_persist"])||variablesData["nosliw_module_setting_modified"]==true?'inline':'none';
} 

}
}],
"scriptExpressionTagAttributes":[],
"uiTags":{},
"script":{ submit : function(info, env){ event.preventDefault(); env.trigueEvent("nosliw_module_setting_submitSetting", info.eventData); }, new : function(info, env){ event.preventDefault(); env.trigueEvent("nosliw_module_setting_newSetting", info.eventData); }, delete : function(info, env){ event.preventDefault(); env.trigueEvent("nosliw_module_setting_deleteSetting", info.eventData); }, save : function(info, env){ event.preventDefault(); env.trigueEvent("nosliw_module_setting_saveSetting", info.eventData); }, }
}, {"loadPattern":"file"
});

