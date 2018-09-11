
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
"context":{"public":{},
"protected":{},
"internal":{},
"private":{"internal_data":{"children":{},
"definition":"test.string;1.0.0",
"type":"relative",
"info":{"info":{}
},
"path":{"rootEleName":"<%=&(data)&%>"
},
"isToParent":false
}
},
"info":{"inherit":"false"
}
},
"attributes":{},
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
            env.executeBatchDataOperationRequest([env.getDataOperationSet(loc_dataVariable, "", loc_getViewData())]);
        });
    };
    var loc_out = {preInit: function () {
    }, initViews: function (requestInfo) {
        loc_view = $("<input type=\"text\"/>");
        return loc_view;
    }, postInit: function (requestInfo) {
        loc_updateView(requestInfo);
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

