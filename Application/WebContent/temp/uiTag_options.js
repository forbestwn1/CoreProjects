
nosliw.runtime.getResourceService().importResource({"id":{"id":"options",
"type":"uiTag"
},
"children":[],
"dependency":{"op1":{"id":"test.integer;1.0.0;add",
"type":"operation"
}
},
"info":{}
}, {"name":"options",
"context":{"inherit":false,
"public":{}
},
"attributes":{},
"script":
function (env) {
    var node_OperationParm = nosliw.getNodeData("expression.entity.OperationParm");
    var loc_env = env;
    var loc_dataVariable = env.createVariable("internal_data");
    var loc_view;
    var loc_revertChange = function () {
    };
    var loc_getViewData = function () {
        return {dataTypeId: "test.string;1.0.0", value: loc_view.val()};
    };
    var loc_updateView = function () {
        env.executeDataOperationRequestGet(loc_dataVariable, "", {success: function (requestInfo, data) {
            loc_view.val(data.value.value);
        }});
    };
    var loc_setupUIEvent = function () {
        loc_view.bind("change", function () {
            window.alert("aaaa");
            env.executeBatchDataOperationRequest([env.getDataOperationSet(loc_dataVariable, "", loc_getViewData())]);
        });
    };
    var loc_out = {preInit: function () {
    }, initViews: function (requestInfo) {
        loc_view = $("<select/>");
        var operationParms = [];
        operationParms.push(new node_OperationParm({dataTypeId: "test.string;1.0.0", value: loc_env.getAttributeValue("id")}, "optionsId"));
        loc_env.executeExecuteOperationRequest("test.options;1.0.0", "all", operationParms, {success: function (request, optionsValueArray) {
            _.each(optionsValueArray.value, function (optionsValue, i) {
                loc_view.append($("<option>", {value: optionsValue.value, text: optionsValue.value}));
            });
            loc_updateView();
        }});
        return loc_view;
    }, postInit: function () {
        loc_setupUIEvent();
        loc_dataVariable.registerDataOperationEventListener(undefined, function () {
            loc_updateView();
        }, this);
    }, destroy: function () {
        loc_dataVariable.release();
        loc_view.remove();
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

