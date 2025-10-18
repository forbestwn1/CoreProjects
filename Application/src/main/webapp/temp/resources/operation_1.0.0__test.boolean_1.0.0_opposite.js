
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"operation",
"version":"1.0.0"
},
"id":"*test.boolean;1.0.0;opposite"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"1718297470458",
"value":
function (parms, context) {
    var bValue = parms.getParm("base").value;
    var outValue;
    if (bValue == true) {
        outValue = false;
    } else {
        if (bValue == false) {
            outValue = true;
        }
    }
    return {dataTypeId: "test.boolean;1.0.0", value: outValue};
}
,
"operationId":"1718297443205",
"operationName":"opposite",
"dataTypeName":"test.boolean;1.0.0",
"operationInfo":{"name":"opposite",
"type":"normal",
"output":{"isBase":"false",
"id":"1718297443207",
"type":"out",
"dataTypeId":"test.boolean;1.0.0",
"operationId":"1718297443205"
},
"parms":{"base":{"isBase":"true",
"id":"1718297443206",
"type":"parm",
"dataTypeId":"test.boolean;1.0.0",
"operationId":"1718297443205",
"name":"base"
}
},
"baseParm":"base"
}
}, {"loadPattern":"file"
});

