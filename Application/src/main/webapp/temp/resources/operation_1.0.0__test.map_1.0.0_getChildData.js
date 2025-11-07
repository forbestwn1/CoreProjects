
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"operation",
"version":"1.0.0"
},
"id":"*test.map;1.0.0;getChildData"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"1718297470501",
"value":
function (parms, context) {
    var name = parms.getParm("name").value;
    return this.value[name];
}
,
"operationId":"1718297443284",
"operationName":"getChildData",
"dataTypeName":"test.map;1.0.0",
"operationInfo":{"name":"getChildData",
"type":"normal",
"output":{"isBase":"false",
"id":"1718297443287",
"type":"out",
"dataTypeId":"test.map;1.0.0",
"operationId":"1718297443284"
},
"parms":{"base":{"isBase":"true",
"id":"1718297443285",
"type":"parm",
"dataTypeId":"test.map;1.0.0",
"operationId":"1718297443284",
"name":"base"
},
"name":{"isBase":"false",
"id":"1718297443286",
"type":"parm",
"dataTypeId":"test.map;1.0.0",
"operationId":"1718297443284",
"name":"name"
}
},
"baseParm":"base"
}
}, {"loadPattern":"file"
});

