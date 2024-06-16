
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"converter",
"version":"1.0.0"
},
"id":"*test.url;1.0.0"
},
"children":[],
"dependency":{},
"info":{}
}, {"id":"1718297470533",
"value":
function (data, toDataType, reverse, context) {
    var prefix = "http:";
    if (toDataType == "test.string;1.0.0") {
        if (reverse == false) {
            return {dataTypeId: "test.string;1.0.0", value: prefix + data.value, info: {fromUrl: true}};
        } else {
            var value = data.value;
            var info = data.info;
            if (info != undefined && info.fromUrl == true) {
                var index = value.indexOf(prefix);
                if (index == 0) {
                    value = value.substring(prefix.length);
                }
            }
            return {dataTypeId: "test.url;1.0.0", value: value};
        }
    }
    return data;
}
,
"dataTypeName":"test.url;1.0.0"
}, {"loadPattern":"file"
});

