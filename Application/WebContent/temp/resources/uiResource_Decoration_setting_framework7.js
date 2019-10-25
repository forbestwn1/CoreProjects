
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
"elementEvents":[{"uiId":"2023",
"event":"click",
"function":"save"
},{"uiId":"2021",
"event":"click",
"function":"new"
},{"uiId":"2022",
"event":"click",
"function":"delete"
},{"uiId":"2020",
"event":"click",
"function":"submit"
}],
"tagEvents":[],
"attributes":{"nosliwattribute_placeholder":"id:pleaseEmbed"
},
"html":"&lt;br nosliwid=&quot;2018&quot;&gt;&lt;span&gt; SettingName : &lt;/span&gt;&lt;span nosliwid=&quot;2017&quot;&gt;&lt;/span&gt;&lt;span&gt; &lt;/span&gt;&lt;div nosliwid=&quot;2019&quot;&gt;  &lt;a href=&quot;&quot; nosliwid=&quot;2020&quot;&gt;&lt;span&gt;Submit&lt;/span&gt;&lt;/a&gt;  &lt;a href=&quot;&quot; nosliwid=&quot;2021&quot;&gt;&lt;span&gt;New&lt;/span&gt;&lt;/a&gt;  &lt;a href=&quot;&quot; style=&quot;&quot; nosliwid=&quot;2022&quot;&gt;&lt;span&gt;Delete&lt;/span&gt;&lt;/a&gt;  &lt;a href=&quot;&quot; style=&quot;&quot; nosliwid=&quot;2023&quot;&gt;&lt;span&gt;Save&lt;/span&gt;&lt;/a&gt; &lt;/div&gt; &lt;!--	&lt;nosliw-contextvalue/&gt;  --&gt; &lt;div id=&quot;pleaseEmbed&quot; nosliwid=&quot;2024&quot;&gt;&lt;/div&gt; &lt;br nosliwid=&quot;2025&quot;&gt;",
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
"uiId":"2017",
"scriptFunction":function(scriptExpressionData){
	return scriptExpressionData["0"];
} 
,
"scriptExpressionScriptFunction":{"0":function(expressionsData, constantsData, variablesData){
	return variablesData["nosliw_module_setting_name"];
} 

}
}],
"scriptExpressionInAttributes":[{"scriptExpressions":{"1":{"definition":"(!?(nosliw_module_setting_persist)?)||?(nosliw_module_setting_modified)?==true?'inline':'none'",
"variableNames":["nosliw_module_setting_persist","nosliw_module_setting_modified"],
"expressions":{}
}
},
"uiId":"2023",
"attribute":"style",
"scriptFunction":function(scriptExpressionData){
	return "display:"+scriptExpressionData["1"];
} 
,
"scriptExpressionScriptFunction":{"1":function(expressionsData, constantsData, variablesData){
	return (!variablesData["nosliw_module_setting_persist"])||variablesData["nosliw_module_setting_modified"]==true?'inline':'none';
} 

}
},{"scriptExpressions":{"1":{"definition":"?(nosliw_module_setting_persist)?==true?'inline':'none'",
"variableNames":["nosliw_module_setting_persist"],
"expressions":{}
}
},
"uiId":"2022",
"attribute":"style",
"scriptFunction":function(scriptExpressionData){
	return "display:"+scriptExpressionData["1"];
} 
,
"scriptExpressionScriptFunction":{"1":function(expressionsData, constantsData, variablesData){
	return variablesData["nosliw_module_setting_persist"]==true?'inline':'none';
} 

}
}],
"scriptExpressionTagAttributes":[],
"uiTags":{},
"script":{ submit : function(info, env){ event.preventDefault(); env.trigueEvent("submitSetting", info.eventData); }, new : function(info, env){ event.preventDefault(); env.trigueEvent("newSetting", info.eventData); }, delete : function(info, env){ event.preventDefault(); env.trigueEvent("deleteSetting", info.eventData); }, save : function(info, env){ event.preventDefault(); env.trigueEvent("saveSetting", info.eventData); }, }
}, {"loadPattern":"file"
});

