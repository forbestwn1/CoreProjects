
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"id":"floatinput",
"type":"uiTag"
},
"children":[],
"dependency":{"op1":{"id":"test.integer;1.0.0;add",
"type":"operation"
}
},
"info":{}
}, {"name":"floatinput",
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
"criteria":"test.float;1.0.0",
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
"event":[],
"script":
function (env) {
    var loc_env = env;
    var loc_dataVariable = env.createVariable("internal_data");
    var loc_view;
    var loc_revertChange = function () {
    };
    var loc_getViewData = function () {
        return {dataTypeId: "test.float;1.0.0", value: parseFloat(loc_view.val())};
    };
    var loc_updateView = function (request) {
        env.executeDataOperationRequestGet(loc_dataVariable, "", {success: function (requestInfo, data) {
            if (data == undefined) {
                loc_view.val("");
            } else {
                loc_view.val(data.value.value);
            }
        }}, request);
    };
    var loc_setupUIEvent = function () {
        loc_view.bind("change", function () {
            env.executeBatchDataOperationRequest([env.getDataOperationSet(loc_dataVariable, "", loc_getViewData())]);
        });
    };
    var loc_out = {preInit: function () {
    }, initViews: function (requestInfo) {
        loc_view = $("<input type=\"text\"/>");
        return loc_view;
    }, postInit: function (request) {
        loc_updateView(request);
        loc_setupUIEvent();
        loc_dataVariable.registerDataOperationEventListener(undefined, function (event, eventData, request) {
            loc_updateView(request);
        }, this);
    }, processAttribute: function (name, value) {
    }, destroy: function () {
        loc_dataVariable.release();
        loc_view.remove();
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

