package interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ForInterview {

	public static void main(String[] args) {

		MainApp mainApp = new MainApp(new CreditCardDAO());
		List<CreditCardInfo> results = mainApp.getCreditCardInfoByCreditId(null, null, null);
		
		
		
	}
	
	

}

class MainApp {
	
	private CreditCardDAO dao;
	
	public MainApp(CreditCardDAO d) {
		this.dao = d;
	}
	
	public List<CreditCardInfo> getCreditCardInfoByCreditId(String creditCardId, Date fromDate, Date toDate){
		return this.dao.getCreditCardInfoByCreditId(creditCardId).stream().filter(info->{
			return info.getDate().before(toDate)&&info.getDate().after(fromDate);
		}).collect(Collectors.toList());
	}
}

class CreditCardDAO {
	
	public List<CreditCardInfo> getCreditCardInfoByCreditId(String creditCardId){
		CreditCardInfo info1 = new CreditCardInfo();
		info.da
		CreditCardInfo info2 = new CreditCardInfo();
		return Arrays.asList(info1, info2);
	}
	
}


class CreditCardInfo {
	
	private Date date;
	
	public Date getDate() {
		return this.date;
	}
	public void setDate(Date)
}
