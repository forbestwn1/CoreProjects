
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"operation",
"version":"1.0.0"
},
"id":"*test.array;1.0.0;getChildData"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"1718297470444",
"value":
function (parms, context) {
    var name = parms.getParm("name").value;
    return this.value[parseInt(name)];
}
,
"operationId":"1718297443193",
"operationName":"getChildData",
"dataTypeName":"test.array;1.0.0",
"operationInfo":{"name":"getChildData",
"type":"normal",
"output":{"isBase":"false",
"id":"1718297443196",
"type":"out",
"dataTypeId":"test.array;1.0.0",
"operationId":"1718297443193"
},
"parms":{"base":{"isBase":"true",
"id":"1718297443194",
"type":"parm",
"dataTypeId":"test.array;1.0.0",
"operationId":"1718297443193",
"name":"base"
},
"name":{"isBase":"false",
"id":"1718297443195",
"type":"parm",
"dataTypeId":"test.array;1.0.0",
"operationId":"1718297443193",
"name":"name"
}
},
"baseParm":"base"
}
}, {"loadPattern":"file"
});

