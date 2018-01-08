
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
"context":{"inherit":false,
"public":{}
},
"attributes":{},
"script":
function (context, parentResourceView, uiTagResource, attributes, env) {
    var loc_context = context;
    var loc_dataContextEleName = "internal_data";
    var loc_dataVariable = env.createVariable(loc_dataContextEleName);
    var loc_view;
    var loc_revertChange = function () {
    };
    var loc_getViewData = function () {
        var value = loc_view.val();
        return {dataTypeId: "test.string;1.0.0", value: value};
    };
    var loc_updateView = function () {
        env.executeDataOperationRequestGet(loc_dataVariable, "", {success: function (requestInfo, data) {
            if (data != undefined) {
                var value = data.value.value;
                loc_view.val(value);
            }
        }});
    };
    var loc_setupElementEvent = function () {
        var that = this;
        loc_view.bind("change", function () {
            env.executeBatchDataOperationRequest([env.getDataOperationSet(loc_dataVariable, "", loc_getViewData())]);
        });
    };
    var loc_out = {ovr_preInit: function () {
    }, ovr_initViews: function (startEle, endEle, requestInfo) {
        loc_view = $("<input type=\"text\"/>");
        return loc_view;
    }, ovr_postInit: function () {
        loc_updateView();
        loc_setupElementEvent();
        loc_dataVariable.registerDataChangeEventListener(undefined, function () {
            loc_updateView();
        }, this);
    }, ovr_processAttribute: function (name, value) {
    }, ovr_handleDataEvent: function (name, event, path, data, requestInfo) {
        loc_updateView();
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

