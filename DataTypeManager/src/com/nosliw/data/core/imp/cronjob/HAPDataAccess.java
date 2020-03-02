package com.nosliw.data.core.imp.cronjob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.cronjob.HAPInstancePollSchedule;
import com.nosliw.data.core.imp.io.HAPDBSource;

public class HAPDataAccess {

	private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);

	private HAPDBSource m_dbSource;

	private long i = System.currentTimeMillis();

	public List<HAPCronJobState> findAllValidJobState() {
		List<HAPCronJobState> out = new ArrayList<HAPCronJobState>();
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM CRONJOB_STATE where POLLTIME<'"+getTimeStr(Instant.now())+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				HAPCronJobState cronJobState = this.getCronJobStateFromResult(resultSet);
				if(cronJobState!=null)  out.add(cronJobState);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
		
	}
	
	public HAPInstanceCronJob updateOrNewCronJob(HAPInstanceCronJob cronJobInstance) {
		if(cronJobInstance.getId()==null) {
			cronJobInstance.setId(this.generateId());
		}
		String content = HAPCronJobInstanceSerializer.toString(cronJobInstance);
		HAPFileUtility.writeFile(this.getCronJobInstanceFile(cronJobInstance.getId()), content);
		return cronJobInstance;
	}
	
	public HAPInstanceCronJob getCronJob(String id) {
		return HAPCronJobInstanceSerializer.parse(HAPFileUtility.readFile(this.getCronJobInstanceFile(id)));
	}
	
	public void deleteCronJob(String instanceId) {
		HAPFileUtility.deleteFolder(this.getCronJobInstanceFolder(instanceId));
	}
	
	private String getCronJobInstanceFile(String instanceId) {
		return this.getCronJobInstanceFolder(instanceId) + "cronjob.json";
	}

	private String getCronJobInstanceFolder(String instanceId) {
		return HAPFileUtility.getCronJobInstanceFolder() + instanceId + "/";
	}

	public HAPCronJobState updateOrNewState(HAPCronJobState state) {
		try {
			if(state.getId()==null) {
				//create
				String id = this.generateId();
				PreparedStatement statement = this.getConnection().prepareStatement("INSERT INTO CRONJOB_STATE (ID,CRONJOBID,POLLTIME,STATE) VALUES ('"+
							id+"', '"+state.getCronJobId()+"', '"+getTimeStr(state.getSchedule().getPollTime())+"', '"+HAPJsonUtility.buildJson(state.getState(), HAPSerializationFormat.JSON)+"');");
				statement.execute();
				state.setId(id);
			}
			else {
				//update
				PreparedStatement statement = this.getConnection().prepareStatement("UPDATE CRONJOB_STATE SET POLLTIME='"+getTimeStr(state.getSchedule().getPollTime())+ "' STATE='"+HAPJsonUtility.buildJson(state.getState(), HAPSerializationFormat.JSON)+"'  WHERE ID='"+state.getId()+"'");
				statement.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return state;
	}
	
	public HAPCronJobState getState(String jobId) {
		HAPCronJobState out = null;
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM CRONJOB_STATE where CRONJOBID='"+jobId+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				out = this.getCronJobStateFromResult(resultSet);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public void deleteState(String jobId) {
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("DELETE FROM CRONJOB_STATE WHERE CRONJOBID='"+jobId+"';");
			statement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HAPCronJobState getCronJobStateFromResult(ResultSet resultSet) {
		HAPCronJobState out = null;
		try {
			String cronJobId = (String)resultSet.getObject("CRONJOBID");
			String id = (String)resultSet.getObject("ID");
			Instant pollTime = resultSet.getTime("POLLTIME").toInstant();
			Map<String, HAPData> state = HAPDataUtility.buildDataWrapperMap(resultSet.getObject("STATE"));

			out = new HAPCronJobState(id, cronJobId, new HAPInstancePollSchedule(pollTime), state);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	private String generateId() {  return i+++"";   }
	
	private String getTimeStr(Instant time) {   return DATE_TIME_FORMATTER.toFormat().format(time);    }
	
	private Instant parseTimeStr(String timeStr) {    
		LocalDateTime ldt = LocalDateTime.parse( timeStr , DATE_TIME_FORMATTER );
		return ldt.toInstant(ZoneOffset.UTC);
	}
	
	protected Connection getConnection(){		return this.m_dbSource.getConnection();	}

}
