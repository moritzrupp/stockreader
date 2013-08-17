/**
 * Main.java
 * Of the project stockreader.
 * Copyright 2013 by Moritz Rupp. All rights reserved.
 */
package de.moritzrupp.stockreader;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.webservicex.StockQuote;
import net.webservicex.StockQuoteSoap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.moritzrupp.stockreader.model.StockQuotes;


/**
 * @author moritz
 *
 */
public class Main {

	private static List<String> stockSymbols;
	
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {
		
		stockSymbols = new LinkedList<String>();
		
		/** Build up the CLI option **/
		
		Option help = new Option("h", "print this message");
		Option version = new Option("v", "print the version information and exit");
		
		Options options = new Options();
		
		options.addOption(help);
		options.addOption(version);
		
		HelpFormatter formatter = new HelpFormatter();
		String header = "Pass the stock symbols as arguments and seperate them by spaces. At least one stock symbol is required.\n"
				+ "Example: stockreader DBK.DE SAP.DE\n\n";
		
		String footer = "\nFor more information see http://www.moritzrupp.de.";
		
		CommandLineParser parser = new GnuParser();
		
		if(args.length == 0) {
			
			formatter.printHelp("stockreader", header, options, footer);
			System.exit(0);
		}
		
		
		try {
			
			CommandLine line = parser.parse(options, args);
			
			if(line.hasOption("h")) {
				
				formatter.printHelp("stockreader", header, options, footer);
			}
			
			if(line.hasOption("v")) {
				
				System.out.println("stockreader version 1.0. Copyright 2013 by Moritz Rupp. All rights reserved.");
				System.exit(0);
			}
			
			for(int i = 0; i < args.length; i++) {

				switch(args[i].charAt(0)) {
									
					case '-':
						if(args[i].length() < 2) {
							
							System.err.println("Not a valid argument: " + args[i]);
							formatter.printHelp("stockreader", header, options, footer);
							System.exit(-1);
						}
						break;
					default:
						stockSymbols.add(args[i]);
						break;
				}
			}
			
			List<StockQuotes> stockQuotes = new LinkedList<StockQuotes>();
			
			for(int i = 0; i < stockSymbols.size(); i++) {

				stockQuotes.add(getQuote(stockSymbols.get(i)));
			}
			
//			for(int i = 0; i < stockQuotes.size(); i++) {
//				
//				System.out.println(stockQuotes.get(i));
//			}
			writeToCSV(stockQuotes, "/Users/moritz/Desktop/stocks.csv");
		}
		catch(ParseException exp) {
			// TODO Auto-generated catch block
			exp.printStackTrace();
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * <b>getQuote</b><br/>
	 * <p>Retrieves the stock quote of a stock with the symbol <tt>symbol</tt>.</p>
	 * @param symbol The symbol of the stock.
	 * @return Returns the {@link StockQuotes} object. Returns <tt>null<tt> in case of an error.
	 * @throws JAXBException If the binding of the WSDL to the {@link StockQuotes} objects throws an exception.
	 * @author Moritz Rupp
	 * @version 1.0
	 */
	private static StockQuotes getQuote(String symbol) throws JAXBException {
		
		StockQuote service = new StockQuote();
		StockQuoteSoap stockQuoteSoap = service.getStockQuoteSoap();
		
		String serviceResult = stockQuoteSoap.getQuote(symbol);
		
		InputStream is = new ByteArrayInputStream(serviceResult.getBytes());
		
		StockQuotes sq = null;
					
		JAXBContext jc = JAXBContext.newInstance(StockQuotes.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		sq = (StockQuotes) unmarshaller.unmarshal(is);
		
		return sq;
	}
	
	
	/**
	 * 
	 * <b>writeToCSV</b>
	 * <p>Writes the stocks in the list of stocks into a CSV file separated by comma. The file name can be determined.</p>
	 * @param stockList The list of stocks.
	 * @param file The name and path of the CSV file.
	 * @throws IOException In case of any I/O error during the write of the CSV file.
	 */
	private static void writeToCSV(List<StockQuotes> stockList, String file) throws IOException {
		
		String csv_separator = ",";
		
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        
        for(StockQuotes stock : stockList) {
        	
        	StringBuffer oneLine = new StringBuffer();
        	oneLine.append(stock.getStock().getSymbol());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getLast());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getDate());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getTime());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getChange());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getOpen());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getHigh());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getLow());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getVolume());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getMktCap());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getPreviousClose());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getPercentageChange());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getAnnRange());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getEarns());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getPe());
        	oneLine.append(csv_separator);
        	oneLine.append(stock.getStock().getName());
        	bw.write(oneLine.toString());
        	bw.newLine();
        }
        bw.flush();
        bw.close();
	}
}
