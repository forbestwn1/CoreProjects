
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"operation",
"version":"1.0.0"
},
"id":"*test.array;1.0.0;getChildDataByIndex"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"1718297470452",
"value":
function (parms, context) {
    var index = parms.getParm("index").value;
    return this.value[index];
}
,
"operationId":"1718297443197",
"operationName":"getChildDataByIndex",
"dataTypeName":"test.array;1.0.0",
"operationInfo":{"name":"getChildDataByIndex",
"type":"normal",
"output":{"isBase":"false",
"id":"1718297443200",
"type":"out",
"dataTypeId":"test.array;1.0.0",
"operationId":"1718297443197"
},
"parms":{"base":{"isBase":"true",
"id":"1718297443198",
"type":"parm",
"dataTypeId":"test.array;1.0.0",
"operationId":"1718297443197",
"name":"base"
},
"index":{"isBase":"false",
"id":"1718297443199",
"type":"parm",
"dataTypeId":"test.array;1.0.0",
"operationId":"1718297443197",
"name":"index"
}
},
"baseParm":"base"
}
}, {"loadPattern":"file"
});

