
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"operation",
"version":"1.0.0"
},
"id":"*test.map;1.0.0;setChildData"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"1718297470503",
"value":
function (parms, context) {
    var name = parms.getParm("name").value;
    var value = parms.getParm("value");
    this.value[name] = value;
    return this;
}
,
"operationId":"1718297443288",
"operationName":"setChildData",
"dataTypeName":"test.map;1.0.0",
"operationInfo":{"name":"setChildData",
"type":"normal",
"output":{"isBase":"false",
"id":"1718297443292",
"type":"out",
"dataTypeId":"test.map;1.0.0",
"operationId":"1718297443288"
},
"parms":{"base":{"isBase":"true",
"id":"1718297443289",
"type":"parm",
"dataTypeId":"test.map;1.0.0",
"operationId":"1718297443288",
"name":"base"
},
"name":{"isBase":"false",
"id":"1718297443290",
"type":"parm",
"dataTypeId":"test.map;1.0.0",
"operationId":"1718297443288",
"name":"name"
},
"value":{"isBase":"false",
"id":"1718297443291",
"type":"parm",
"dataTypeId":"test.map;1.0.0",
"operationId":"1718297443288",
"name":"value"
}
},
"baseParm":"base"
}
}, {"loadPattern":"file"
});

