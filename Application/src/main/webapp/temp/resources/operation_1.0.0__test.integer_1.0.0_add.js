
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"operation",
"version":"1.0.0"
},
"id":"*test.integer;1.0.0;add"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"1718297470485",
"value":
function (parms, context) {
    var parm1 = parms.getParm("data1").value;
    var parm2 = parms.getParm("data2").value;
    var out = parm1 + parm2;
    return {dataTypeId: "test.integer;1.0.0", value: out};
}
,
"operationId":"1718297443252",
"operationName":"add",
"dataTypeName":"test.integer;1.0.0",
"operationInfo":{"name":"add",
"type":"normal",
"output":{"isBase":"false",
"id":"1718297443255",
"type":"out",
"dataTypeId":"test.integer;1.0.0",
"operationId":"1718297443252"
},
"parms":{"data1":{"isBase":"true",
"id":"1718297443253",
"type":"parm",
"dataTypeId":"test.integer;1.0.0",
"operationId":"1718297443252",
"name":"data1"
},
"data2":{"isBase":"false",
"id":"1718297443254",
"type":"parm",
"dataTypeId":"test.integer;1.0.0",
"operationId":"1718297443252",
"name":"data2"
}
},
"baseParm":"data1"
}
}, {"loadPattern":"file"
});

