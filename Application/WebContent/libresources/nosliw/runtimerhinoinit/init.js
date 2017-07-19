/**
 * 
 */
nosliw.runtimeName = "rhino";

nosliw.initModules();

var runtimeRhino = nosliw.getNodeData("runtime.rhino.createRuntime")(nosliw.runtimeName);

nosliw.runtime = runtimeRhino;


runtimeRhino.interfaceObjectLifecycle.init();

nosliw.generateId = runtimeRhino.getIdService().generateId;

