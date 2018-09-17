
nosliw.runtime.getResourceService().importResource({"id":{"id":"include",
"type":"uiTag"
},
"children":[],
"dependency":{},
"info":{}
}, {"name":"include",
"context":{"public":{},
"protected":{},
"internal":{},
"private":{},
"info":{"inherit":"false"
}
},
"attributes":{},
"script":
function (env) {
    var loc_env = env;
    var loc_out = {initViews: function (requestInfo) {
        var resourceView = loc_env.createDefaultUIResourceView(requestInfo);
        return resourceView.getViews();
    }, postInit: function () {
    }, preInit: function () {
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

