
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
function (context, uiTagResource, attributes, env) {
    var loc_dataContextEle = "internal_data";
    var loc_view;
    var loc_revertChange = function () {
    };
    var node_createDataOperationRequest = nosliw.getNodeData("uidata.dataoperation.createDataOperationRequest");
    var node_DataOperationService = nosliw.getNodeData("uidata.dataoperation.DataOperationService");
    var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
    var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
    var loc_getViewData = function (name) {
        var value = loc_view.val();
        return {dataTypeId: "test.string;1.0.0", value: value};
    };
    var loc_out = {prv_updateView: function () {
        var data = context.getContextElementData(loc_dataContextEle);
        if (data != undefined) {
            var value = data.value.value;
            loc_view.val(value);
        }
    }, prv_setupElementEvent: function () {
        var that = this;
        loc_view.bind("change", function () {
            var data = loc_getViewData();
            var requestInfo = node_createDataOperationRequest(context, {});
            var operationService = new node_DataOperationService(loc_dataContextEle, node_CONSTANT.WRAPPER_OPERATION_SET, data);
            requestInfo.addOperationRequest(operationService);
            node_requestServiceProcessor.processRequest(requestInfo, false);
        });
    }, ovr_preInit: function () {
    }, ovr_initViews: function () {
        loc_view = $("<input type=\"text\"/>");
        return loc_view;
    }, ovr_postInit: function () {
        this.prv_updateView();
        this.prv_setupElementEvent();
    }, ovr_processAttribute: function (name, value) {
    }, ovr_handleDataEvent: function (name, event, path, data, requestInfo) {
        this.prv_updateView();
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

