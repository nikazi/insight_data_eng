package edgar;

import java.util.Date;

/**
 * This class represent a session.
 * @author azadehn
 *
 */
public class EDGARSession {
	String ip;
	Date startTime;
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	Date endTime;
	int docAccessedCount = 0;
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date time) {
		startTime = time;
	}


	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date time) {
		endTime = time;		
	}

	public int getDocAccessedCount() {
		return docAccessedCount;
	}
	
	public void ResetDocsAccessedCount() {
		docAccessedCount = 0;		
	}

	public void IncrementDocsAccessed() {
		docAccessedCount++;		
	}

	public int getDuration() {
		// TODO Auto-generated method stub
		return (int) ((endTime.getTime()-startTime.getTime())/1000) + 1;
	}

}
