
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"operation",
"version":"1.0.0"
},
"id":"*test.string;1.0.0;subString"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"1580152069955",
"value":
function (parms, context) {
    var from = parms.getParm("from").value;
    var to = parms.getParm("to").value;
    var outStr = this.value.substring(from, to);
    outStr = context.getResourceDataByName("globalHelper").d(outStr);
    return {dataTypeId: "test.string;1.0.0", value: outStr};
}
,
"operationId":"1561472058582",
"operationName":"subString",
"dataTypeName":"test.string;1.0.0",
"operationInfo":{"name":"subString",
"type":"normal",
"output":{"isBase":"false",
"id":"1582589950354",
"type":"out",
"dataTypeId":"test.string;1.0.0",
"operationId":"1582589950350"
},
"parms":{"base":{"isBase":"true",
"id":"1582589950351",
"type":"parm",
"dataTypeId":"test.string;1.0.0",
"operationId":"1582589950350",
"name":"base"
},
"from":{"isBase":"false",
"id":"1582589950352",
"type":"parm",
"dataTypeId":"test.string;1.0.0",
"operationId":"1582589950350",
"name":"from"
},
"to":{"isBase":"false",
"id":"1582589950353",
"type":"parm",
"dataTypeId":"test.string;1.0.0",
"operationId":"1582589950350",
"name":"to"
}
},
"baseParm":"base"
}
}, {"loadPattern":"file"
});

