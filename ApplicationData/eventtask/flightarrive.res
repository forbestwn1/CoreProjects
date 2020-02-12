{
	"name" : "flightarrive",
	"id" : "flightarrive",
	"context" : {
		"group": {
			"public": {
				"element": {
					"flightNumber": {
						"definition": {
							"criteria": "test.string;1.0.0"
						},
					},
					"date" : {
						"definition": {
							"criteria": "test.string;1.0.0"
						},
					},
					"flightInfo": {
						"definition": {
							"criteria": "test.map;1.0.0%%||status:test.string;1.0.0,company:test.string;1.0.0,flight:test.string;1.0.0||%%"
						},
					}
				}
			}
		}
	},

	"source" : {
		"pollSchedule" : {
			"interval" : 60,
			"start" : "",
			"end" : ""		
		},
		
		"pollTask" : {
			"process" : "checkFlightInfo",	
		}
	},
	
	"eventInfo" : {
		"context" : {
			"element" : {
				"flightInfo": {
					"definition": {
						"path": "flightInfo"
					},
				}
			}
		}
	},
	
	"handler" : {
		"process" : "eventHandler",	
	},
	
	"attachment" : {
		"service" : [
			{
				"name": "getPearsonFlightArrivalService",
				"id" : "pearsonFlightArrivalService"
			},	
			{
				"name": "sendEmailService",
				"id" : "sendEmailService"
			}	
		],
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
											"path" : "flightNumber"
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
							"type": "switch",
							"expression": "#|?(flightInfo)?.getChildData(name:&(#test##string___status)&)|#",
							"branch": [{
								"data": {
									"dataTypeId": "test.string;1.0.0",
									"value": "arrive"
								},
								"flow": {
									"target": "withEventEndId"
								}
							},
							{
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
							"type": "end",
							"output": {
								"element": {
								}
							}
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
								"target": "sendEmail"
							}
						},
						{
							"id": "sendEmail",
							"name": "sendEmail",
							"type": "Service_request",
							"provider": "sendEmailService",
							"inputMapping" : {
								"element" : {
								}
							},
							"result": [{
								"name": "success",
								"flow": {
									"target": "success"
								},
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
	}
}
