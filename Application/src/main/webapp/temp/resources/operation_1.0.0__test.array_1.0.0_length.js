
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"operation",
"version":"1.0.0"
},
"id":"*test.array;1.0.0;length"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"1718297470442",
"value":
function (parms, context) {
    return {dataTypeId: "test.integer;1.0.0", value: parms.getParm("base").value.length};
}
,
"operationId":"1718297443190",
"operationName":"length",
"dataTypeName":"test.array;1.0.0",
"operationInfo":{"name":"length",
"type":"normal",
"output":{"isBase":"false",
"id":"1718297443192",
"type":"out",
"dataTypeId":"test.array;1.0.0",
"operationId":"1718297443190"
},
"parms":{"base":{"isBase":"true",
"id":"1718297443191",
"type":"parm",
"dataTypeId":"test.array;1.0.0",
"operationId":"1718297443190",
"name":"base"
}
},
"baseParm":"base"
}
}, {"loadPattern":"file"
});

