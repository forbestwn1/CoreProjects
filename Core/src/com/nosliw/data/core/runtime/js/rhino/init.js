/**
 * 
 */
var runtimeRhino = nosliw.getNode("runtimerhino.createRuntime").getData()();

runtimeRhino.interfaceObjectLifecycle.init();

nosliw.generateId = runtimeRhino.getIdService();
