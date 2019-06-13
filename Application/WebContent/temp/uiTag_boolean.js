
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"id":"boolean",
"type":"uiTag"
},
"children":[],
"dependency":{},
"info":{}
}, {"name":"boolean",
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
"path":{"rootEleName":"<%=&(nosliwattribute_data)&%>"
},
"parent":"default",
"definition":{"type":"data",
"processed":"false",
"criteria":{"status":"close",
"criteria":"test.boolean;1.0.0",
"info":{}
}
}
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
"parent":"default"
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
        return {dataTypeId: "test.boolean;1.0.0", value: loc_view.prop("checked")};
    };
    var loc_updateView = function (request) {
        loc_env.executeDataOperationRequestGet(loc_dataVariable, "", {success: function (requestInfo, data) {
            var value = false;
            if (data != undefined) {
                value = data.value.value;
            }
            loc_view.prop("checked", value);
        }}, request);
    };
    var loc_setupUIEvent = function () {
        loc_view.bind("change", function () {
            var data = loc_getViewData();
            loc_env.executeBatchDataOperationRequest([loc_env.getDataOperationSet(loc_dataVariable, "", data)]);
            loc_env.trigueEvent("valueChanged", data);
        });
    };
    var loc_out = {initViews: function (requestInfo) {
        loc_view = $("<input type=\"checkbox\"/>");
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

