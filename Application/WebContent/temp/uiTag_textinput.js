
nosliw.runtime.getResourceService().importResource({"id":{"id":"textinput",
"type":"uiTag"
},
"children":[],
"dependency":{"op1":{"id":"base.integer;1.0.0;add",
"type":"operation"
}
},
"info":{}
}, {"name":"textinput",
"context":{"inherit":false,
"public":{"internal_data":{"children":{},
"definition":"test.string;1.0.0",
"type":"relative",
"path":"<%=&(data)&%>"
}
}
},
"attributes":{},
"script":
function (context, parentResourceView, uiTagResource, attributes, env) {
    var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
    var node_createDataOperationRequest = nosliw.getNodeData("uidata.dataoperation.createDataOperationRequest");
    var node_createDataOperationService = nosliw.getNodeData("uidata.dataoperation.createDataOperationService");
    var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
    var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
    var loc_context = context;
    var loc_dataContextEleName = "internal_data";
    var loc_dataVariable = loc_context.createVariable(node_createContextVariable(loc_dataContextEleName));
    var loc_view;
    var loc_revertChange = function () {
    };
    var loc_getViewData = function (name) {
        var value = loc_view.val();
        return {dataTypeId: "test.string;1.0.0", value: value};
    };
    var loc_out = {prv_updateView: function () {
        var data = loc_dataVariable.getData();
        if (data != undefined) {
            var value = data.value.value;
            loc_view.val(value);
        }
    }, prv_setupElementEvent: function () {
        var that = this;
        loc_view.bind("change", function () {
            var data = loc_getViewData();
            var requestInfo = node_createDataOperationRequest(loc_context, {});
            var operationService = node_createDataOperationService(loc_dataVariable, node_CONSTANT.WRAPPER_OPERATION_SET, "", data);
            requestInfo.addOperationRequest(operationService);
            node_requestServiceProcessor.processRequest(requestInfo, false);
        });
    }, ovr_preInit: function () {
    }, ovr_initViews: function (startEle, endEle, requestInfo) {
        loc_view = $("<input type=\"text\"/>");
        return loc_view;
    }, ovr_postInit: function () {
        this.prv_updateView();
        this.prv_setupElementEvent();
        loc_dataVariable.registerDataChangeEventListener(undefined, function () {
            this.prv_updateView();
        }, this);
    }, ovr_processAttribute: function (name, value) {
    }, ovr_handleDataEvent: function (name, event, path, data, requestInfo) {
        this.prv_updateView();
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

