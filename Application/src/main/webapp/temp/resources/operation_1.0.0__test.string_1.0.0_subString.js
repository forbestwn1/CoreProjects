
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"operation",
"version":"1.0.0"
},
"id":"*test.string;1.0.0;subString"
},
"children":[],
"dependency":{"globalHelper":{"resourceTypeId":{"resourceType":"jshelper",
"version":"1.0.0"
},
"id":"*1718297470528"
},
"op1":{"resourceTypeId":{"resourceType":"operation",
"version":"1.0.0"
},
"id":"*test.integer;1.0.0;add"
}
},
"info":{}
}, {"id":"1718297470527",
"value":
function (parms, context) {
    var from = parms.getParm("from").value;
    var to = parms.getParm("to").value;
    var outStr = this.value.substring(from, to);
    return {dataTypeId: "test.string;1.0.0", value: outStr};
}
,
"operationId":"1718297443311",
"operationName":"subString",
"dataTypeName":"test.string;1.0.0",
"operationInfo":{"name":"subString",
"type":"normal",
"output":{"isBase":"false",
"id":"1718297443315",
"type":"out",
"dataTypeId":"test.string;1.0.0",
"operationId":"1718297443311"
},
"parms":{"base":{"isBase":"true",
"id":"1718297443312",
"type":"parm",
"dataTypeId":"test.string;1.0.0",
"operationId":"1718297443311",
"name":"base"
},
"from":{"isBase":"false",
"id":"1718297443313",
"type":"parm",
"dataTypeId":"test.string;1.0.0",
"operationId":"1718297443311",
"name":"from"
},
"to":{"isBase":"false",
"id":"1718297443314",
"type":"parm",
"dataTypeId":"test.string;1.0.0",
"operationId":"1718297443311",
"name":"to"
}
},
"baseParm":"base"
}
}, {"loadPattern":"file"
});

