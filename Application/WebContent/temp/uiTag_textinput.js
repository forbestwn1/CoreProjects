
nosliw.runtime.getResourceService().importResource({"id":{"id":"textinput",
"type":"uiTag"
},
"children":[],
"dependency":{"op1":{"id":"test.integer;1.0.0;add",
"type":"operation"
}
},
"info":{}
}, {"name":"textinput",
"context":{"group":{"public":{"element":{}
},
"protected":{"element":{}
},
"internal":{"element":{}
},
"private":{"element":{"internal_data":{"name":"",
"description":"",
"info":{},
"definition":{"type":"relative",
"processed":"false",
"path":{"rootEleName":"<%=&(nosliwAttribute_data)&%>"
},
"isToParent":false
}
}
}
}
},
"info":{"inherit":"false"
}
},
"attributes":{"data":{"name":"data",
"description":""
}
},
"event":[{"name":"valueChanged",
"description":"",
"info":{},
"data":{"element":{"value":{"name":"",
"description":"",
"info":{},
"definition":{"type":"relative",
"processed":"false",
"path":{"rootEleName":"internal_data"
},
"isToParent":false
}
}
}
}
}],
"script":
function (env) {
    var loc_env = env;
    var loc_dataVariable = env.createVariable("internal_data");
    var loc_view;
    var loc_revertChange = function () {
    };
    var loc_getViewData = function () {
        return {dataTypeId: "test.string;1.0.0", value: loc_view.val()};
    };
    var loc_updateView = function (request) {
        env.executeDataOperationRequestGet(loc_dataVariable, "", {success: function (requestInfo, data) {
            loc_view.val(data.value.value);
        }}, request);
    };
    var loc_setupUIEvent = function () {
        loc_view.bind("change", function () {
            var data = loc_getViewData();
            env.executeBatchDataOperationRequest([env.getDataOperationSet(loc_dataVariable, "", data)]);
            loc_env.trigueEvent("valueChanged", data);
        });
    };
    var loc_out = {initViews: function (requestInfo) {
        loc_view = $("<input type=\"text\"/>");
        return loc_view;
    }, postInit: function (requestInfo) {
        loc_updateView(requestInfo);
        loc_setupUIEvent();
        loc_dataVariable.registerDataOperationEventListener(undefined, function (event, eventData, request) {
            loc_updateView(request);
        }, this);
    }, destroy: function () {
        loc_dataVariable.release();
        loc_view.remove();
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

