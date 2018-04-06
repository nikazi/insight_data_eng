package edgar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is responsible to load EDGAR input data in the streaming way.
 * @author azadehn
 *
 */
public class EDGARInputReader {
	int inactivityPeriod = -1;
	BufferedReader dataReader = null;
	
	// These are column indexes extracted from header line.
	int ipIndex = -1;
	int dateIndex = -1;
	int timeIndex = -1;
	int cikIndex = -1;
	int accessionIndex = -1;
	int extentionIndex = -1;
	
	SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	String curIP = "";
	Date curDate = null;
	int curCik = -1;
	int curAccession = -1;
	int curExtention = -1;
	
	public EDGARInputReader(String dataFilePath, String inactivityPeriodFilePath) throws Exception {
		System.out.println("EDGAR file: " +dataFilePath);
		System.out.println("Inactivitiy file: " + inactivityPeriodFilePath);
		// 1. Read inactivity period
		String inactivityFileContent = 
				Files.readAllLines(Paths.get(inactivityPeriodFilePath)).get(0);
		try {
			inactivityPeriod = Integer.valueOf(inactivityFileContent);
		} catch (NumberFormatException e) {
			throw (new Exception(
					"Couldn't load inaactivity period, the content of the file is not number: " 
						+ inactivityFileContent,
					e.getCause()));
		}

		// 2. Open data file reader and load the header.

		// FileReader reads text files in the default encoding.
		FileReader fileReader = new FileReader(dataFilePath);

		// Always wrap FileReader in BufferedReader.
		dataReader = new BufferedReader(fileReader);

		String header_line = dataReader.readLine();
		ProcessHeaderLine(header_line);
	}

	/**
	 * Parse the header line to find column indexes of columns we need.
	 * @param header_line
	 * @throws Exception
	 */
	private void ProcessHeaderLine(String header_line) throws Exception {
		String[] columns = header_line.split(",");	
		for (int i=0;i<columns.length;i++) {
			if (columns[i].equals("ip")) 
				ipIndex = i;
			else if (columns[i].equals("date")) 
				dateIndex = i;
			else if (columns[i].equals("time")) 
				timeIndex = i;
			else if (columns[i].equals("cik")) 
				cikIndex = i;
			else if (columns[i].equals("accession")) 
				accessionIndex = i;
			else if (columns[i].equals("extention")) 
				extentionIndex = i;
		}
		if (ipIndex == -1 ||
				dateIndex == -1 ||
				timeIndex == -1 ||
				cikIndex == -1  ||
				accessionIndex == -1 ||
				extentionIndex == -1)
			throw(new Exception("Header line format is invalid: " + header_line));
	}

	/**
	 * Closes any open file reader. Always call it when you're done using the
	 * reader.
	 * 
	 * @throws IOException
	 */
	public void Close() throws IOException {
		if (dataReader != null)
			dataReader.close();
	}

	/**
	 * Read the next row in the data file and extract information of interest (ip, date, ...) from it.
	 * @return Returns false if it reaches to the end of file.
	 * @throws ParseException 
	 * @throws IOException
	 */
	public boolean readNextRecord() throws ParseException {
		String row_content;
		try {
			row_content = dataReader.readLine();
			if (row_content==null) return false;
			System.out.println("Loading next input row: " + row_content);
			String[] column_values = row_content.split(",");
			curIP = column_values[ipIndex];
			curDate = date_format.parse(column_values[dateIndex]+" "+column_values[timeIndex]);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Returns last loaded record's date and time.
	 * @return
	 */
	public Date getTimeStamp() {
		return curDate;
	}

	/**
	 * Returns las loaded record's IP.
	 * @return
	 */
	public String getIP() {
		return curIP;
	}

	public long getInactivityPeriod() {
		return inactivityPeriod;
	}

}
