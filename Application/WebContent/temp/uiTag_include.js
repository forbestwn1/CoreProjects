
nosliw.runtime.getResourceService().importResource({"id":{"id":"include",
"type":"uiTag"
},
"children":[],
"dependency":{},
"info":{}
}, {"name":"include",
"context":{"group":{"public":{"element":{}
},
"protected":{"element":{}
},
"internal":{"element":{}
},
"private":{"element":{}
}
},
"info":{"inherit":"false",
"escalate":"true"
}
},
"attributes":{"source":{"name":"source",
"description":""
},
"context":{"name":"context",
"description":""
},
"event":{"name":"event",
"description":""
}
},
"event":[],
"script":
function (env) {
    var loc_env = env;
    var loc_resourceView;
    var loc_out = {findFunctionDown: function (name) {
        return loc_resourceView.findFunctionDown(name);
    }, initViews: function (requestInfo) {
        loc_resourceView = loc_env.createDefaultUIView(requestInfo);
        return loc_resourceView.getViews();
    }, destroy: function () {
        loc_resourceView.detachViews();
        loc_resourceView.destroy();
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

