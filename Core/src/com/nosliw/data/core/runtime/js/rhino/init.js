/**
 * 
 */
var runtimeRhino = nosliw.getNode("runtime.rhino.createRuntime").getData()();

runtimeRhino.interfaceObjectLifecycle.init();

nosliw.generateId = runtimeRhino.getIdService();

nosliw.runtime = runtimeRhino();
