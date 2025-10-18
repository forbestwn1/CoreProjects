
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"operation",
"version":"1.0.0"
},
"id":"*test.float;1.0.0;largerThan"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"1718297470477",
"value":
function (parms, context) {
    var base = this.value;
    var data = parms.getParm("data").value;
    if (base > data) {
        return {dataTypeId: "test.boolean;1.0.0", value: true};
    } else {
        return {dataTypeId: "test.boolean;1.0.0", value: false};
    }
}
,
"operationId":"1718297443236",
"operationName":"largerThan",
"dataTypeName":"test.float;1.0.0",
"operationInfo":{"name":"largerThan",
"type":"normal",
"output":{"isBase":"false",
"id":"1718297443239",
"type":"out",
"dataTypeId":"test.float;1.0.0",
"operationId":"1718297443236"
},
"parms":{"base":{"isBase":"true",
"id":"1718297443237",
"type":"parm",
"dataTypeId":"test.float;1.0.0",
"operationId":"1718297443236",
"name":"base"
},
"data":{"isBase":"false",
"id":"1718297443238",
"type":"parm",
"dataTypeId":"test.float;1.0.0",
"operationId":"1718297443236",
"name":"data"
}
},
"baseParm":"base"
}
}, {"loadPattern":"file"
});

