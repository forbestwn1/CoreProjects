{
	"source" : {
		"pollSchedule" : {
			"interval" : 60,
			"start" : ??,
			"end" : ??		
		},
		
		"pollTask" : {
			"input" : {
				"flightNumber": "",
				
			}
			"process" : "checkFlightInfo",	
		
		}
	},
	
	"eventInfo" : {
		"context" : {
		
		}
	
	},
	
	"handler" : {
			
		"process" : "eventHandler",	
	
	},
	
	"attachment" : {
		"process" : [
			{
				"name": "checkFlightInfo",
				"entity" : {
					"activity": [{
							"id": "startActivityId",
							"name": "startActivity",
							"type": "start",
							"flow": {
								"target": "pollFlightState"
							}
						},
						{
							"id": "pollFlightState",
							"name": "pollFlightState",
							"type": "Service_request",
							"provider": "getPearsonFlightArrivalService",
							"inputMapping" : {
								"element" : {
									"flight" : {
										"definition" : {
											"path" : "flight"
										}
									},
									"date" : {
										"definition" : {
											"path" : "date"
										}
									}
								}
							},
							"result": [{
								"name": "success",
								"flow": {
									"target": "checkFlightState"
								},
								"output": {
									"element": {
										"flightInfo": {
											"definition": {
												"path": "outputInService"
											}
										}
									}
								}
							}]
						},
						{
							"id": "checkFlightState",
							"name": "checkFlightState",
							"type": "expression",
							"expression": "#|!(test.string)!.subString(?(inputA)?.b.c,from:?(fromVar)?,to:?(toVar)?)|#",
							"result": [{
								"name": "success",
								"flow": {
									"target": "refreshSchoolInfo"
								}
							}]
						},
						{
							"id": "isValidState",
							"name": "isValidState",
							"type": "switch",
							"expression": "#|!(test.string)!.subString(?(inputA)?.b.c,from:?(fromVar)?,to:?(toVar)?)|#",
							"inputMapping": {
								"element": {
									"schoolData": {
										"definition": { 
											"path": "nosliw_EVENT.data"
										}
									}
								}
							},
							"result": [{
								"value": {
									"dataTypeId": "test.boolean;1.0.0",
									"value": true
								},
								"flow": {
									"target": "withEventEndId"
								}
							},
							{
								"value": {
									"dataTypeId": "test.boolean;1.0.0",
									"value": false
								},
								"flow": {
									"target": "withoutEventEndId"
								}
							}]
						},
						{
							"id": "withEventEndId",
							"name": "withEventEndId",
							"type": "end",
							"output": {
								"element": {
									"event": {
										definition: {
											"path": "flightInfo"
										}
									}
								}
							}
						},
						{
							"id": "withoutEventEndId",
							"name": "withoutEventEndId",
							"type": "end"
						}
					]
				}
			},
			
			{
				"name": "eventHandler",
				"entity" : {
					"activity": [{
							"id": "startActivityId",
							"name": "startActivity",
							"type": "start",
							"flow": {
								"target": "pollFlightState"
							}
						},
						{
							"id": "sendEmail",
							"name": "sendEmail",
							"type": "Service_request",
							"provider": "sendEmailService",
							"inputMapping" : {
								"element" : {
									"address" : {
										"definition" : {
											"path" : "flight"
										}
									},
									"title" : {
										"definition" : {
											"path" : "date"
										}
									}
								}
							},
							"result": [{
								"name": "success",
								"flow": {
									"target": "success"
								},
								"output": {
									"element": {
										"flightInfo": {
											"definition": {
												"path": "outputInService"
											}
										}
									}
								}
							}]
						},
						{
							"id": "success",
							"name": "success",
							"type": "end"
						}
					]
				}			
			}
		
		], 

		"service" : [
			{
				"name": "getPearsonFlightArrivalService",
				"id" : "pearsonFlightArrivalService"
			}	
		]
	}
}
