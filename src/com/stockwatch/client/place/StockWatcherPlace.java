package com.stockwatch.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * A place object representing a particular state of the UI. A Place can be converted to and from a
 * URL history token by defining a {@link PlaceTokenizer} for each {@link Place}, and the 
 * {@link PlaceHistoryHandler} automatically updates the browser URL corresponding to each 
 * {@link Place} in your app.
 */
public class StockWatcherPlace extends Place {
  
	/**
	 * Sample property (stores token). 
	 */
	private String name;

	public StockWatcherPlace(String token) {
		this.name = token;
	}

	public String getName() {
		return name;
	}

	/**
	 * PlaceTokenizer knows how to serialize the Place's state to a URL token.
	 */
	public static class Tokenizer implements PlaceTokenizer<StockWatcherPlace> {

		@Override
		public String getToken(StockWatcherPlace place) {
			return place.getName();
		}

		@Override
		public StockWatcherPlace getPlace(String token) {
			return new StockWatcherPlace(token);
		}

	}
}
