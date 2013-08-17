/**
 * Stock.java
 * Of the project stockreader.
 * Copyright 2013 by Moritz Rupp. All rights reserved.
 */
package de.moritzrupp.stockreader.model;

import java.text.ParseException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author moritz
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Stock {
	
	@XmlElement(name="Symbol")
	private String symbol;
	
	@XmlElement(name="Last")
	private double last;
	
	@XmlElement(name="Date")
	private String date;
	
	@XmlElement(name="Time")
	private String time;
	
	@XmlElement(name="Change")
	private double change;
	
	@XmlElement(name="Open")
	private double open;
	
	@XmlElement(name="High")
	private double high;
	
	@XmlElement(name="Low")
	private double low;
	
	@XmlElement(name="Volume")
	private int volume;
	
	@XmlElement(name="MktCap")
	private double mktCap;
	
	@XmlElement(name="PreviousClose")
	private double previousClose;
	
	@XmlElement(name="PercentageChange")
	private String percentageChange;
	
	@XmlElement(name="AnnRange")
	private String annRange;
	
	@XmlElement(name="Earns")
	private double earns;
	
	@XmlElement(name="P-E")
	private double pe;
	
	@XmlElement(name="Name")
	private String name;
	
	public Stock() { }
	
	/**
	 * @param symbol
	 * @param last
	 * @param date
	 * @param time
	 * @param change
	 * @param open
	 * @param high
	 * @param low
	 * @param volume
	 * @param mktCap
	 * @param previousClose
	 * @param percentageChange
	 * @param annRange
	 * @param earns
	 * @param pe
	 * @param name
	 * @throws ParseException 
	 */
	public Stock(String symbol, double last, String date, String time,
			double change, double open, double high, double low, int volume,
			double mktCap, double previousClose, String percentageChange,
			String annRange, double earns, double pe, String name) throws ParseException {
		super();
		
		this.symbol = symbol;
		this.last = last;
		this.date = date;
		this.time = time;
		this.change = change;
		this.open = open;
		this.high = high;
		this.low = low;
		this.volume = volume;
		this.mktCap = mktCap;
		this.previousClose = previousClose;
		this.percentageChange = percentageChange;
		this.annRange = annRange;
		this.earns = earns;
		this.pe = pe;
		this.name = name;
	}

	/**
	 * getSymbol
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * getLast
	 * @return the last
	 */
	public double getLast() {
		return last;
	}

	/**
	 * getDate
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * getTime
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * getChange
	 * @return the change
	 */
	public double getChange() {
		return change;
	}

	/**
	 * getOpen
	 * @return the open
	 */
	public double getOpen() {
		return open;
	}

	/**
	 * getHigh
	 * @return the high
	 */
	public double getHigh() {
		return high;
	}

	/**
	 * getLow
	 * @return the low
	 */
	public double getLow() {
		return low;
	}

	/**
	 * getVolume
	 * @return the volume
	 */
	public int getVolume() {
		return volume;
	}

	/**
	 * getMktCap
	 * @return the mktCap
	 */
	public double getMktCap() {
		return mktCap;
	}

	/**
	 * getPreviousclosee
	 * @return the previousClose
	 */
	public double getPreviousClose() {
		return previousClose;
	}

	/**
	 * getPercentageChange
	 * @return the percentageChange
	 */
	public String getPercentageChange() {
		return percentageChange;
	}

	/**
	 * getAnnRange
	 * @return the annRange
	 */
	public String getAnnRange() {
		return annRange;
	}

	/**
	 * getEarns
	 * @return the earns
	 */
	public double getEarns() {
		return earns;
	}

	/**
	 * getPe
	 * @return the pe
	 */
	public double getPe() {
		return pe;
	}

	/**
	 * getName
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * setSymbol
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * setLast
	 * @param last the last to set
	 */
	public void setLast(double last) {
		this.last = last;
	}

	/**
	 * setDate
	 * @param date the date to set
	 * @throws ParseException 
	 */
	public void setDate(String date) throws ParseException {

		this.date = date;
	}

	/**
	 * setTime
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * setChange
	 * @param change the change to set
	 */
	public void setChange(double change) {
		this.change = change;
	}

	/**
	 * setOpen
	 * @param open the open to set
	 */
	public void setOpen(double open) {
		this.open = open;
	}

	/**
	 * setHigh
	 * @param high the high to set
	 */
	public void setHigh(double high) {
		this.high = high;
	}

	/**
	 * setLow
	 * @param low the low to set
	 */
	public void setLow(double low) {
		this.low = low;
	}

	/**
	 * setVolume
	 * @param volume the volume to set
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}

	/**
	 * setMktCap
	 * @param mktCap the mktCap to set
	 */
	public void setMktCap(double mktCap) {
		this.mktCap = mktCap;
	}

	/**
	 * setPreviousclosee
	 * @param previousClose the previousclosee to set
	 */
	public void setPreviousClose(double previousClose) {
		this.previousClose = previousClose;
	}

	/**
	 * setPercentageChange
	 * @param percentageChange the percentageChange to set
	 */
	public void setPercentageChange(String percentageChange) {
		this.percentageChange = percentageChange;
	}

	/**
	 * setAnnRange
	 * @param annRange the annRange to set
	 */
	public void setAnnRange(String annRange) {
		this.annRange = annRange;
	}

	/**
	 * setEarns
	 * @param earns the earns to set
	 */
	public void setEarns(double earns) {
		this.earns = earns;
	}

	/**
	 * setPe
	 * @param pe the pe to set
	 */
	public void setPe(double pe) {
		this.pe = pe;
	}

	/**
	 * setName
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Stock [" + (symbol != null ? "symbol=" + symbol + ", " : "")
				+ "last=" + last + ", "
				+ (date != null ? "date=" + date + ", " : "")
				+ (time != null ? "time=" + time + ", " : "") 
				+ "change=" + change + ", open=" + open + ", high=" + high + ", low=" + low
				+ ", volume=" + volume + ", mktCap=" + mktCap
				+ ", previousClose=" + previousClose + ", percentageChange="
				+ percentageChange + ", "
				+ (annRange != null ? "annRange=" + annRange + ", " : "")
				+ "earns=" + earns + ", pe=" + pe + ", "
				+ (name != null ? "name=" + name + ", " : "");
	}
}
