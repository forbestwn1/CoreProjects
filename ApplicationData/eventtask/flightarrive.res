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
						}
					},
					"date" : {
						"definition": {
							"criteria": "test.date;1.0.0"
						}
					}
				}
			},
			"protected": {
				"element": {
					"flightInfo": {
						"definition": {
							"criteria": "test.map;1.0.0%%||status:test.string;1.0.0,company:test.string;1.0.0,flight:test.string;1.0.0||%%"
						}
					}
				}
			}
		}
	},

	"schedule" : {
		"start" : "",
		"interval" : 60,
		"end" : "expression or process"		
	},

	"task" : {
		"process" : "checkFlightInfo",
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
				"entity" : {s
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
									"target": "sendEmail"
								}
							},
							{
								"flow": {
									"target": "fail"
								}
							}]
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
							"name": "successEnd",
							"type": "end",
							"output1": {
								"element": {
								}
							}
						}
						{
							"id": "fail",
							"name": "failEnd",
							"type": "end",
							"output1": {
								"element": {
								}
							}
						}
					]
				}
			}
		], 
	}
}
