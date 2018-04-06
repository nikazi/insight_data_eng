package edgar;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class EDGARSessionWriter {
	String outputPath;
	SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public EDGARSessionWriter(String outputPath) {
		this.outputPath = outputPath;
	}
	static int sessionCounter = 0;
	public void writeSession(EDGARSession cur_session) {
		try {
            // Assume default encoding.
            FileWriter fileWriter =
                new FileWriter(outputPath, true);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write(cur_session.getIp());
            bufferedWriter.write(",");
            bufferedWriter.write(date_format.format(cur_session.getStartTime()));
            bufferedWriter.write(",");
            bufferedWriter.write(date_format.format(cur_session.getEndTime()));
            bufferedWriter.write(",");
            bufferedWriter.write(cur_session.getDuration() + "," + 
            						cur_session.getDocAccessedCount());
            
            bufferedWriter.newLine();
            bufferedWriter.flush();
            // Always close files.
            bufferedWriter.close();
            sessionCounter++;
            System.out.println("Wrote "+sessionCounter+" sessions to output.");
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + outputPath + "'");
        }		
	}

}
