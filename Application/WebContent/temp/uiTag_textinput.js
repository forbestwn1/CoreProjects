
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
    var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
    var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
    var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
    var node_createBatchUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createBatchUIDataOperationRequest");
    var node_UIDataOperation = nosliw.getNodeData("uidata.uidataoperation.UIDataOperation");
    var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
    var node_createUIDataOperationRequest = nosliw.getNodeData("uidata.uidataoperation.createUIDataOperationRequest");
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
    var loc_out = {prv_updateView1: function () {
        var data = loc_dataVariable.getData();
        if (data != undefined) {
            var value = data.value.value;
            loc_view.val(value);
        }
    }, prv_updateView: function () {
        var uiDataOperation = new node_UIDataOperation(loc_dataVariable, node_uiDataOperationServiceUtility.createGetOperationService(""));
        var requestInfo = node_createUIDataOperationRequest(loc_context, uiDataOperation, {success: function (requestInfo, data) {
            if (data != undefined) {
                var value = data.value.value;
                loc_view.val(value);
            }
        }});
        node_requestServiceProcessor.processRequest(requestInfo, false);
    }, prv_setupElementEvent: function () {
        var that = this;
        loc_view.bind("change", function () {
            var data = loc_getViewData();
            var requestInfo = node_createBatchUIDataOperationRequest(loc_context);
            var uiDataOperation = new node_UIDataOperation(loc_dataVariable, node_uiDataOperationServiceUtility.createSetOperationService("", data));
            requestInfo.addUIDataOperation(uiDataOperation);
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

