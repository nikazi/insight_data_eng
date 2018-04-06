package edgar;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

public class SessionExtractor {
	
	public static void main (String args[]) throws Exception {
		if (args.length<3) {
			System.out.println("Missing crititcal arguments. "
					+ "Pass input files paths: <InactivityFilePath> <DataFilePath> <OutputPath");
			return;
		}
		String inactivityConfigFilePath = args[0];
		String dataFilePath = args[1];
    		
		if (!inactivityConfigFilePath.isEmpty() &&
				!dataFilePath.isEmpty()) {
			// Delete the output file first.
			new File(args[2]).delete();
			ExtractSessions(
					new EDGARInputReader(dataFilePath, inactivityConfigFilePath),
					new EDGARSessionWriter(args[2]));
		}
	}
	
	/**
	 * Extract information from EDGAR data. For more information see:
	 * https://github.com/InsightDataScience/edgar-analytics
	 * @param input_reader
	 * @param session_writer
	 * @throws ParseException
	 */
	public static void ExtractSessions(EDGARInputReader input_reader,
								EDGARSessionWriter session_writer) throws ParseException {
		// This holds only unfinished sessions and any completed 
		// session will be removed from this hashmap. The Key is IP.
		HashMap<String, EDGARSession> open_sessions = new HashMap<>();
		while(input_reader.readNextRecord()) {
			EDGARSession cur_session = open_sessions.get(input_reader.getIP());
			Date cur_timestamp = input_reader.getTimeStamp();
			if (cur_session == null) {
				cur_session = new EDGARSession();
				cur_session.setStartTime(cur_timestamp);
				cur_session.setIp(input_reader.getIP());
				open_sessions.put(input_reader.getIP(), cur_session);
			} else {
				
				long time_gap_seconds = 
						(cur_timestamp.getTime() - 
						cur_session.getEndTime().getTime()) / 1000; 
				

				if (time_gap_seconds> input_reader.getInactivityPeriod()) {
					session_writer.writeSession(cur_session);
					cur_session.ResetDocsAccessedCount();
					cur_session.setStartTime(cur_timestamp);
				} else {
					cur_session.setEndTime(cur_timestamp);
				}
			}
			cur_session.setEndTime(cur_timestamp);
			cur_session.IncrementDocsAccessed();
		}
		
		// Assume all open sessions are ended, write all of them.
		for (EDGARSession cur_session : open_sessions.values()) {
		  session_writer.writeSession(cur_session);
		}
	}
}
