/**
 * StockQuotes.java
 * Of the project stockreader.
 * Copyright 2013 by Moritz Rupp. All rights reserved.
 */
package de.moritzrupp.stockreader.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author moritz
 *
 */
@XmlRootElement(name="StockQuotes")
@XmlAccessorType(XmlAccessType.FIELD)
public class StockQuotes {
	
	@XmlElement(name="Stock")
	private Stock stock;

	public StockQuotes() { }
	
	/**
	 * @param stock
	 */
	public StockQuotes(Stock stock) {
		super();
		this.stock = stock;
	}

	/**
	 * getStock
	 * @return the stock
	 */
	public Stock getStock() {
		return stock;
	}

	/**
	 * setStock
	 * @param stock the stock to set
	 */
	public void setStock(Stock stock) {
		this.stock = stock;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StockQuotes [" + (stock != null ? "stock=" + stock : "") + "]";
	}
}
