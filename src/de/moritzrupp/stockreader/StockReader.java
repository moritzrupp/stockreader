/**
 * StockReader.java
 * Of the project stockreader.
 * Copyright 2013 by Moritz Rupp. All rights reserved.
 */
package de.moritzrupp.stockreader;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.webservicex.StockQuote;
import net.webservicex.StockQuoteSoap;

import com.sun.mail.smtp.SMTPTransport;

import de.moritzrupp.stockreader.model.StockQuotes;

/**
 * @author moritz
 *
 */
public class StockReader {
	
	private char CSV_SEPARATOR;
	private StockQuote stockQuoteService;
	private StockQuoteSoap stockQuoteSOAP;
	private String encoding;
	private File csvFile;
	private List<StockQuotes> stockList;
	private List<String> symbolList;
	
	/**
	 * @param CSV_SEPARATOR The separator for the CSV file. Commonly ',' or ';'.
	 * @param stockQuoteService The {@link StockQuote} object for making the SOAP calls to the web service. Cannot be <tt>null</tt>
	 * @param encoding The encoding of the CSV file. If <tt>null</tt> the default encoding is UTF-8.
	 * @param csvFile The path to the CSV file.
	 * @param symbolList A List with the stock symbols.
	 */
	public StockReader(char CSV_SEPARATOR, StockQuote stockQuoteService,
			String encoding, String csvFile,
			List<String> symbolList) {
				
		this.CSV_SEPARATOR = CSV_SEPARATOR;
		this.symbolList = symbolList;
		this.stockList = new LinkedList<StockQuotes>();
		
		if(csvFile == null) {
			
			this.csvFile = null;
		}
		else {
			
			this.csvFile = new File(csvFile);
		}		
		
		if(stockQuoteService == null) {
			
			throw new IllegalArgumentException("stockQuoteService argument cannot be null.");
		}
		else {
			
			this.stockQuoteService = stockQuoteService;
			setStockQuoteSOAP(this.stockQuoteService.getStockQuoteSoap());
		}
		
		if(encoding == null) {
			
			encoding = "UTF-8";
		}
		else {
			
			this.encoding = encoding;
		}
	}
	
	/**
	 * @param CSV_SEPARATOR The separator for the CSV file. Commonly ',' or ';'.
	 * @param stockQuoteService The {@link StockQuote} object for making the SOAP calls to the web service. Cannot be <tt>null</tt>
	 * @param encoding The encoding of the CSV file. If <tt>null</tt> the default encoding is UTF-8.
	 * @param csvFile The path to the CSV file.
	 */
	public StockReader(char CSV_SEPARATOR, StockQuote stockQuoteService,
			String encoding, String csvFile) {
				
		this.CSV_SEPARATOR = CSV_SEPARATOR;
		this.symbolList = new LinkedList<String>();
		this.stockList = new LinkedList<StockQuotes>();
		
		if(csvFile == null) {
			
			this.csvFile = null;
		}
		else {
			
			this.csvFile = new File(csvFile);
		}		
		
		if(stockQuoteService == null) {
			
			throw new IllegalArgumentException("stockQuoteService argument cannot be null.");
		}
		else {
			
			this.stockQuoteService = stockQuoteService;
			setStockQuoteSOAP(this.stockQuoteService.getStockQuoteSoap());
		}
		
		if(encoding == null) {
			
			encoding = "UTF-8";
		}
		else {
			
			this.encoding = encoding;
		}
	}
	
	/**
	 * @param CSV_SEPARATOR The separator for the CSV file. Commonly ',' or ';'.
	 * @param stockQuoteService The {@link StockQuote} object for making the SOAP calls to the web service.
	 * @param encoding The encoding of the CSV file. If <tt>null</tt> the default encoding is UTF-8.
	 * @param csvFile The path to the CSV file.
	 * @param symbols Variable number of strings: a string for each stock symbol.
	 */
	public StockReader(char CSV_SEPARATOR, StockQuote stockQuoteService,
			String encoding, String csvFile,
			String ... symbols) {
		
		this.CSV_SEPARATOR = CSV_SEPARATOR;
		this.stockList = new LinkedList<StockQuotes>();
		
		if(csvFile == null) {
			
			this.csvFile = null;
		}
		else {
			
			this.csvFile = new File(csvFile);
		}
		
		if(stockQuoteService == null) {
			
			throw new IllegalArgumentException("stockQuoteService argument cannot be null.");
		}
		else {
			
			this.stockQuoteService = stockQuoteService;
			setStockQuoteSOAP(this.stockQuoteService.getStockQuoteSoap());
		}
		
		if(encoding == null) {
			
			encoding = "UTF-8";
		}
		else {
			
			this.encoding = encoding;
		}
		
		this.symbolList = new LinkedList<String>();
		
		for(int i = 0; i < symbols.length; i++) {
			
			symbolList.add(symbols[i]);
		}
	}

	/**
	 * 
	 * <b>getQuote</b>
	 * <p>Retrieves the stock quote of a single stock with the symbol <tt>symbol</tt> and adds it to the list of stock quotes.
	 * The symbol is added to the list of symbols.</p>
	 * @param symbol The symbol of the stock.
	 * @return Returns the {@link StockQuotes} object. Returns <tt>null<tt> in case of an error.
	 * @throws JAXBException If the binding of the WSDL to the {@link StockQuotes} objects throws an exception.
	 */
	public StockQuotes getQuote(String symbol) throws JAXBException {
		
		String soapResponse = stockQuoteSOAP.getQuote(symbol);
		InputStream is = new ByteArrayInputStream(soapResponse.getBytes());
		
		StockQuotes sq = null;
		JAXBContext jc = JAXBContext.newInstance(StockQuotes.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		sq = (StockQuotes) unmarshaller.unmarshal(is);
		
		symbolList.add(symbol);
		stockList.add(sq);		
		return sq;
	}
	
	/**
	 * <b>getQuotes</b>
	 * <p>Queries all the symbols in the list of symbols and returns a list with the corresponding {@link StockQuotes} objects.
	 * @param symbols A {@link List} with the symbol strings to query.
	 * @return Returns a list with the {@link StockQuotes} objects queried by the symbols.
	 * @throws JAXBException f the binding of the WSDL to the {@link StockQuotes} objects throws an exception.
	 */
	public List<StockQuotes> getQuotes(List<String> symbols) throws JAXBException {
		
		for(String symbol : symbols) {
			
			String soapResponse = stockQuoteSOAP.getQuote(symbol);
			InputStream is = new ByteArrayInputStream(soapResponse.getBytes());
			
			StockQuotes sq = null;
			JAXBContext jc = JAXBContext.newInstance(StockQuotes.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			sq = (StockQuotes) unmarshaller.unmarshal(is);
			
			symbolList.add(symbol);
			stockList.add(sq);	
		}
		
		return stockList;
	}
	
	/**
	 * <b>getQuotes</b>
	 * <p>Queries all the symbols stored in the application context and returns a list with the corresponding {@link StockQuotes} objects.
	 * @return Returns a list with the {@link StockQuotes} objects queried by the symbols.
	 * @throws JAXBException f the binding of the WSDL to the {@link StockQuotes} objects throws an exception.
	 */
	public List<StockQuotes> getQuotes() throws JAXBException {
		
		for(String symbol : symbolList) {
			
			String soapResponse = stockQuoteSOAP.getQuote(symbol);
			InputStream is = new ByteArrayInputStream(soapResponse.getBytes());
			
			StockQuotes sq = null;
			JAXBContext jc = JAXBContext.newInstance(StockQuotes.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			sq = (StockQuotes) unmarshaller.unmarshal(is);
			
			stockList.add(sq);	
		}
		
		return stockList;
	}

	
	/**
	 * 
	 * <b>writeToCSV</b>
	 * <p>Writes the stocks in the list of stocks into a CSV file separated by comma. The file name can be determined.</p>
	 * @param stockList The list of stocks.
	 * @param file The name and path of the CSV file.
	 * @throws IOException In case of any I/O error during the write of the CSV file.
	 * @throws ParseException 
	 */
	public void writeToCSV(boolean onlyPrice) throws IOException, ParseException {
		
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), encoding));
        
        String head = "Symbol" + getCSV_SEPARATOR()
				+ "Last" + getCSV_SEPARATOR()
				+ "Date" + getCSV_SEPARATOR()
				+ "Time" + getCSV_SEPARATOR()
				//+ "DateTime" + getCSV_SEPARATOR()
				+ ((onlyPrice) ? "" : "Change" + getCSV_SEPARATOR())
				+ ((onlyPrice) ? "" : "Open" + getCSV_SEPARATOR())
				+ ((onlyPrice) ? "" : "High" + getCSV_SEPARATOR())
				+ ((onlyPrice) ? "" : "Low" + getCSV_SEPARATOR())
				+ ((onlyPrice) ? "" : "Volume" + getCSV_SEPARATOR())
				+ ((onlyPrice) ? "" : "MktCap" + getCSV_SEPARATOR())
				+ ((onlyPrice) ? "" : "PreviousClose" + getCSV_SEPARATOR())
				+ ((onlyPrice) ? "" : "PercentageChange" + getCSV_SEPARATOR())
				+ ((onlyPrice) ? "" : "AnnRange" + getCSV_SEPARATOR())
				+ ((onlyPrice) ? "" : "Earns" + getCSV_SEPARATOR())
				+ ((onlyPrice) ? "" : "P-E" + getCSV_SEPARATOR())
				+ "Name";
        
		bw.write(head);
		bw.newLine();
		
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat outputTimeFormat = new SimpleDateFormat("HH:mm z");
		
		SimpleDateFormat inputDateTimeFormat = new SimpleDateFormat("M/d/yyyy hh:mma Z");
		
		Date inputDateTime;
		
        for(StockQuotes stock : stockList) {
        	
        	StringBuffer oneLine = new StringBuffer();
        	oneLine.append(stock.getStock().getSymbol());
        	oneLine.append(getCSV_SEPARATOR());
        	oneLine.append(stock.getStock().getLast());
        	oneLine.append(getCSV_SEPARATOR());
        	
        	inputDateTime = inputDateTimeFormat.parse(stock.getStock().getDate() + " " + stock.getStock().getTime() + " -0400");
        	
        	oneLine.append(outputDateFormat.format(inputDateTime));
        	oneLine.append(getCSV_SEPARATOR());
        	oneLine.append(outputTimeFormat.format(inputDateTime));
        	oneLine.append(getCSV_SEPARATOR());

        	if(!onlyPrice) {
        	
	        	oneLine.append(stock.getStock().getChange());
	        	oneLine.append(getCSV_SEPARATOR());
	        	oneLine.append(stock.getStock().getOpen());
	        	oneLine.append(getCSV_SEPARATOR());
	        	oneLine.append(stock.getStock().getHigh());
	        	oneLine.append(getCSV_SEPARATOR());
	        	oneLine.append(stock.getStock().getLow());
	        	oneLine.append(getCSV_SEPARATOR());
	        	oneLine.append(stock.getStock().getVolume());
	        	oneLine.append(getCSV_SEPARATOR());
	        	oneLine.append(stock.getStock().getMktCap());
	        	oneLine.append(getCSV_SEPARATOR());
	        	oneLine.append(stock.getStock().getPreviousClose());
	        	oneLine.append(getCSV_SEPARATOR());
	        	oneLine.append(stock.getStock().getPercentageChange());
	        	oneLine.append(getCSV_SEPARATOR());
	        	oneLine.append(stock.getStock().getAnnRange());
	        	oneLine.append(getCSV_SEPARATOR());
	        	oneLine.append(stock.getStock().getEarns());
	        	oneLine.append(getCSV_SEPARATOR());
	        	oneLine.append(stock.getStock().getPe());
	        	oneLine.append(getCSV_SEPARATOR());
        	
        	}
        	
        	oneLine.append(stock.getStock().getName());
        	bw.write(oneLine.toString());
        	bw.newLine();
        }
        bw.flush();
        bw.close();
	}
	
	public void sendmail(String from, String to, String subject, String host, String user, String password) {
		
		Properties props = System.getProperties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtps.auth", "true");
		props.put("mail.smtp.starttls.enable", true);
		
		Session session = Session.getInstance(props, null);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy, hh:mma Z");
		
		String message = "Hi,\n\n"
				+ "here are the latest stock quotes from the " + dateFormat.format(new Date()) + ".\n\n";
		
		SMTPTransport t;
		try {
			
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = {new InternetAddress(to)};
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);
			
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText(message);
			
			MimeBodyPart mbp2 = new MimeBodyPart();
			mbp2.attachFile(getCsvFile());
			
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			mp.addBodyPart(mbp2);
			
			msg.setContent(mp);
			msg.setSentDate(new Date());
			
			t = (SMTPTransport) session.getTransport("smtps");
			
			t.connect(host, user, password);
			t.sendMessage(msg, msg.getAllRecipients());
			t.close();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/**
	 * addSymbol
	 * @param symbol the symbol to add
	 */
	public void addSymbol(String symbol) {
		
		getSymbolList().add(symbol);
	}

	/**
	 * getCSV_SEPARATOR
	 * @return the cSV_SEPARATOR
	 */
	public char getCSV_SEPARATOR() {
		return CSV_SEPARATOR;
	}

	/**
	 * setCSV_SEPARATOR
	 * @param cSV_SEPARATOR the cSV_SEPARATOR to set
	 */
	public void setCSV_SEPARATOR(char cSV_SEPARATOR) {
		CSV_SEPARATOR = cSV_SEPARATOR;
	}

	/**
	 * getStockQuoteService
	 * @return the stockQuoteService
	 */
	public StockQuote getStockQuoteService() {
		return stockQuoteService;
	}

	/**
	 * setStockQuoteService
	 * @param stockQuoteService the stockQuoteService to set
	 */
	public void setStockQuoteService(StockQuote stockQuoteService) {
		this.stockQuoteService = stockQuoteService;
		setStockQuoteSOAP(this.stockQuoteService.getStockQuoteSoap());
	}

	/**
	 * getStockQuoteSOAP
	 * @return the stockQuoteSOAP
	 */
	public StockQuoteSoap getStockQuoteSOAP() {
		return stockQuoteSOAP;
	}

	/**
	 * setStockQuoteSOAP
	 * @param stockQuoteSOAP the stockQuoteSOAP to set
	 */
	private void setStockQuoteSOAP(StockQuoteSoap stockQuoteSOAP) {
		this.stockQuoteSOAP = stockQuoteSOAP;
	}

	/**
	 * getEncoding
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * setEncoding
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * getCsvFile
	 * @return the csvFile
	 */
	public File getCsvFile() {
		return csvFile;
	}

	/**
	 * setCsvFile
	 * @param csvFile the csvFile to set
	 */
	public void setCsvFile(File csvFile) {
		this.csvFile = csvFile;
	}

	/**
	 * getStockList
	 * @return the stockList
	 */
	public List<StockQuotes> getStockList() {
		return stockList;
	}

	/**
	 * setStockList
	 * @param stockList the stockList to set
	 */
	public void setStockList(List<StockQuotes> stockList) {
		this.stockList = stockList;
	}

	/**
	 * getSymbolList
	 * @return the symbolList
	 */
	public List<String> getSymbolList() {
		return symbolList;
	}

	/**
	 * setSymbolList
	 * @param symbolList the symbolList to set
	 */
	public void setSymbolList(List<String> symbolList) {
		this.symbolList = symbolList;
	}
}
