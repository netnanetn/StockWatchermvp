package com.stockwatch.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.stockwatch.client.ui.StockWatcherView;
import com.stockwatch.client.ui.StockWatcherViewImpl;

/**
 * Sample implementation of {@link ClientFactory}.
 */
public class ClientFactoryImpl implements ClientFactory {
  
	private static final EventBus eventBus = new SimpleEventBus();
	private static final PlaceController placeController = new PlaceController(eventBus);

	
	private static final StockWatcherView stockwatchview = new StockWatcherViewImpl();

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}


	@Override
	public StockWatcherView getStockWatcherView() {
		// TODO Auto-generated method stub
		return stockwatchview;
	}

}
