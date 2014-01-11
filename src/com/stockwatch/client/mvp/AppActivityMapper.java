package com.stockwatch.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.stockwatch.client.ClientFactory;
import com.stockwatch.client.activity.StockWatcherActivity;
import com.stockwatch.client.place.StockWatcherPlace;

/**
 * ActivityMapper associates each {@link Place} with its corresponding {@link Activity}.
 */
public class AppActivityMapper implements ActivityMapper {

	/**
	 * Provided for {@link Activitie}s.
	 */
	private ClientFactory clientFactory;

	public AppActivityMapper(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {
	  
		if (place instanceof StockWatcherPlace)
			return new StockWatcherActivity((StockWatcherPlace) place, clientFactory);

		return null;
	}

}
