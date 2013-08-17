/**
 * Main.java
 * Of the project stockreader.
 * Copyright 2013 by Moritz Rupp. All rights reserved.
 */
package de.moritzrupp.stockreader;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import net.webservicex.StockQuote;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


/**
 * @author moritz
 *
 */
public class Main {

	private static final String VERSION = "0.0.1";
	
	private static HelpFormatter formatter;
	private static String header;
	private static String footer;
	private static Options options;
	private static CommandLineParser clParser;
	
	private static StockReader reader;
	
	private static boolean priceArg = false;
	private static boolean fileArg = false;
	private static File theFile;
	
	
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {
		
		initOptions();
		clParser = new GnuParser();
		
		if(args.length == 0) { // if no arguments
			
			formatter.printHelp("stockreader", header, options, footer);
			System.exit(-1);
		}
		
		try {
			
			CommandLine line = clParser.parse(options, args);
			
			if(line.hasOption("h")) {
				
				printHelp();
			}
			
			if(line.hasOption("v")) {
				
				System.out.println("stockreader version " + VERSION + ". Copyright 2013 by Moritz Rupp. All rights reserved.\n"
						+ "This application is licensed under The MIT License (MIT). See https://github.com/moritzrupp/stockreader/blob/master/LICENSE.md");
				System.exit(0);
			}
			
			if(line.hasOption("p")) {
				
				priceArg = true;
			}
			
			if(line.hasOption("f")) {
				
				fileArg = true;
				theFile = new File(line.getOptionValue("f"));
			}
		}
		catch(ParseException exp) {
			// TODO Auto-generated catch block
			exp.printStackTrace();
		}
		
		try {
			
			reader = new StockReader(';', new StockQuote(), "UTF-8", (fileArg) ? theFile.toString() : new File(".").getCanonicalPath() + ((System.getProperty("os.name").toLowerCase().startsWith("win")) ? "\\" : "/") + "stocks.csv"); 
				
			for(int i = 0; i < args.length; i++) {
	
				switch(args[i].charAt(0)) {
									
					case '-':
						if(args[i].length() < 2) {
							
							System.err.println("Not a valid argument: " + args[i]);
							printHelp();
							System.exit(-1);
						}
						
						if(args[i].charAt(1) == 'f' || (args[i].charAt(1) == '-' && args[i].charAt(2) == 'f')) {
							
							// get the path and set it to "nosymbol"
							args[i+1] = "nosymbol";
						}
						
						break;
					default:
						if(!args[i].equals("nosymbol")) {
							reader.addSymbol(args[i]);
						}
						break;
				}
			}

			reader.getQuotes();
			reader.writeToCSV(priceArg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * initOptions
	 * <p>Initializes the options for the console application</p>
	 */
	@SuppressWarnings("static-access")
	private static void initOptions() {
				
		Option help = new Option("h", "help", false, "prints the help message");
		Option version = new Option("v", "version", false, "prints version and license information and exists");
		Option price = new Option("p", "price", false, "prints only the symbol, the date and the price of the stock");
		Option csv = OptionBuilder.withArgName("FILE")
				.withLongOpt("file")
				.withDescription("The path and file name to the exported file")
				.hasArg()
				.create("f");
		
		options = new Options();
		options.addOption(help);
		options.addOption(version);
		options.addOption(price);
		options.addOption(csv);
		
		formatter = new HelpFormatter();
		header = "Pass the stock symbols as arguments and seperate them by spaces. At least one stock symbol is required.\n"
				+ "Example: stockreader DBK.DE SAP.DE\n\n";
		
		footer = "\nFor more information see http://www.moritzrupp.de.";
	}
	
	private static void printHelp() {
		
		formatter.printHelp("stockreader", header, options, footer);
	}
}
