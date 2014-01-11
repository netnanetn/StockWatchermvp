package com.stockwatch.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.stockwatch.client.ui.StockWatcherView;


public interface ClientFactory {

	EventBus getEventBus();

	PlaceController getPlaceController();
	StockWatcherView getStockWatcherView();
}
