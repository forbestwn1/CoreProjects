package com.nosliw.service.soccer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.service.provide.HAPExecutableService;
import com.nosliw.data.core.service.provide.HAPProviderService;
import com.nosliw.data.core.service.provide.HAPResultService;
import com.nosliw.data.core.service.provide.HAPUtilityService;

public class HAPServiceUpdateLineup implements HAPExecutableService, HAPProviderService{

	@Override
	public HAPResultService execute(Map<String, HAPData> parms){

		String actionStr = null;
		String statusStr = null;
		if(parms.get("player")!=null) {
			String player = (String)parms.get("player").getValue();
			
			HAPData actionData = parms.get("action");
			String action = actionData==null? null : (String)actionData.getValue();
			
			HAPActionResult actionResult = null;
			actionResult = HAPPlayerLineupManager.getInstance().updateLineUp(player, action);
			
			HAPPlayerStatus playerStatus = null;
			if(actionResult==null) {
				playerStatus = HAPPlayerLineupManager.getInstance().getLineup().getPlayerStatus(player);
			}
			else {
				playerStatus = actionResult.getPlayerStatus();
			}
			actionStr = playerStatus.getActions().get(0);
			statusStr = player + "目前在 :  " + playerStatus.getStatusDescription();

			if(action!=null) {
				List<String> affectedPlayers = new ArrayList<String>();
				affectedPlayers.add(player);
				if(actionResult.getAffectedPlayer()!=null)  affectedPlayers.addAll(actionResult.getAffectedPlayer());
				this.sendEmailToPlayers(affectedPlayers);
			}
		
		}
		else {
			actionStr = "";
			statusStr = "还没有提供你的名字，请首先提供你的名字！！";
		}
		
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		output.put("action", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), actionStr));
		output.put("status", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), statusStr));
		return HAPUtilityService.generateSuccessResult(output);
	}
	
	private void sendEmailToPlayers(List<String> players) {
		for(String player : players) {
			String email = HAPPlayerLineupManager.getInstance().getPlayerInfo(player).getEmail();
			if(HAPBasicUtility.isStringNotEmpty(email)) {
				HAPPlayerStatus playerStatus = HAPPlayerLineupManager.getInstance().getLineup().getPlayerStatus(player);
				this.sendEmail(email, "你在soccer for fun的状态更新", player+", 你在soccer for fun group的状态为"+playerStatus.getStatusDescription());
			}
		}
	}
	
	
	 private void sendEmail(String to, String subject, String text) {  
		  
		  String host="smtp.live.com";  
		  final String user="mylastkilometer@hotmail.com";//change accordingly  
		  final String password="tony0818";//change accordingly  
		    
		   //Get the session object  
		   Properties props = new Properties();  

		   props.setProperty("mail.transport.protocol", "smtp");
		    props.setProperty("mail.host", "smtp.live.com");
		    props.put("mail.smtp.starttls.enable", "true");
		    props.put("mail.smtp.auth", "true");
		     
		   Session session = Session.getDefaultInstance(props,  
		    new javax.mail.Authenticator() {  
		      @Override
			protected PasswordAuthentication getPasswordAuthentication() {  
		    return new PasswordAuthentication(user,password);  
		      }  
		    });  
		  
		   //Compose the message  
		    try {  
		     MimeMessage message = new MimeMessage(session); 
		     
		     message.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
		     MimeBodyPart messageBodyPart = new MimeBodyPart();
		    // set charset to UTF8
		     messageBodyPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
		     
		     message.setFrom(new InternetAddress(user));  
		     message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
		     message.setSubject(subject, "UTF-8");
		     message.setText(text, "UTF-8");  
		       
		    //send the message  
		     Transport.send(message);  
		  
		     System.out.println("message sent successfully...");  
		   
		     } catch (MessagingException e) {e.printStackTrace();}  
		 }  	
}
