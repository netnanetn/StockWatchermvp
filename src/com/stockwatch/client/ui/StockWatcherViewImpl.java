package com.stockwatch.client.ui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.stockwatch.client.StockPrice;


public class StockWatcherViewImpl extends Composite implements StockWatcherView {

	interface Binder extends UiBinder<Widget, StockWatcherViewImpl> {
	}
	
	private static final Binder binder = GWT.create(Binder.class);
	@UiField FlexTable stocksFlexTable;
	@UiField TextBox newSymbolTextBox;
	@UiField Button addButton;
	@UiField Label lastUpdatedLabel;
	
  
	private Presenter listener;

	private ArrayList <String> stocks = new ArrayList<String>(); 
	
	
	public StockWatcherViewImpl() {
		initWidget(binder.createAndBindUi(this));
		stocksFlexTable.setText(0, 0, "Symbol");
		stocksFlexTable.setText(0, 1, "Price");
		stocksFlexTable.setText(0, 2, "Change");
		stocksFlexTable.setText(0, 3, "Remove");

	    // Add styles to elements in the stock list table.
	    stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
	    stocksFlexTable.addStyleName("watchList");
	    stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");
	
		Timer refreshTimer = new Timer() {
			public void run()
			{
				refreshWatchList();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
		
	}

	private static final int REFRESH_INTERVAL = 5000;
	/*private String name;
	
	@Override
	public void setName(String name) {
		this.name=name;
	}*/

	@Override
	public void setPresenter(Presenter listener) {
		this.listener = listener;
	}
	@UiHandler("addButton")
	void onAddButtonClick(ClickEvent event) {
		addStock();
	}
	
	private void addStock() {
		final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
		newSymbolTextBox.setFocus(true);
		 
		// symbol must be between 1 and 10 chars that are numbers, letters, or dots
		if (!symbol.matches("^[0-9a-zA-Z\\.]{1,10}$"))
		{
		    Window.alert("'" + symbol + "' is not a valid symbol.");
		    newSymbolTextBox.selectAll();
		    return;
		}
			 
		newSymbolTextBox.setText("");
			     
		// don't add the stock if it's already in the watch list
	    if (stocks.contains(symbol))
	        return;

	    // add the stock to the list
	    int row = stocksFlexTable.getRowCount();
	    stocks.add(symbol);
	    stocksFlexTable.setText(row, 0, symbol);
	    //
	    stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");
	    stocksFlexTable.setWidget(row, 2, new Label());

	    // add button to remove this stock from the list
	   
	    Button removeStock = new Button("x");
	    removeStock.addStyleDependentName("remove");
	    removeStock.addClickHandler(new ClickHandler() {
	    public void onClick(ClickEvent event) {					
	        int removedIndex = stocks.indexOf(symbol);
	        stocks.remove(removedIndex);
	        stocksFlexTable.removeRow(removedIndex);
	    }
	    });
	    stocksFlexTable.setWidget(row, 3, removeStock);			
	}
	
	protected void refreshWatchList() {
		final double MAX_PRICE = 100.0; // $100.00
		final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

		StockPrice[] prices = new StockPrice[stocks.size()];
		for (int i = 0; i < stocks.size(); i++)
		{
			double price = Random.nextDouble() * MAX_PRICE;
			double change = price * MAX_PRICE_CHANGE
					* (Random.nextDouble() * 2.0 - 1.0);

			prices[i] = new StockPrice((String) stocks.get(i), price, change);
		}

		updateTable(prices);
		
	}
	

	private void updateTable(StockPrice[] prices){
		for (int i = 0; i < prices.length; i++)
		{
			updateTable(prices[i]);
		}

		// change the last update timestamp
		lastUpdatedLabel.setText("Last update : "
			+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
	}

	private void updateTable(StockPrice stockPrice)	{
		// make sure the stock is still in our watch list
		if (!stocks.contains(stockPrice.getSymbol()))
		{
			return;
		}

		int row = stocks.indexOf(stockPrice.getSymbol()) + 1;

		// Format the data in the Price and Change fields.
		String priceText = NumberFormat.getFormat("#,##0.00").format(stockPrice.getPrice());
		NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
		String changeText = changeFormat.format(stockPrice.getChange());
		String changePercentText = changeFormat.format(stockPrice.getChangePercent());

		// Populate the Price and Change fields with new data.
		stocksFlexTable.setText(row, 1, priceText);
		//stocksFlexTable.setText(row, 2, changeText + " (" + changePercentText + "%)");
		 Label changeWidget = (Label)stocksFlexTable.getWidget(row, 2);
		    changeWidget.setText(changeText + " (" + changePercentText + "%)");
		    // Change the color of text in the Change field based on its value.
		    String changeStyleName = "noChange";
		    if (stockPrice.getChangePercent() < -0.1f) {
		      changeStyleName = "negativeChange";
		    }
		    else if (stockPrice.getChangePercent() > 0.1f) {
		      changeStyleName = "positiveChange";
		    }

		    changeWidget.setStyleName(changeStyleName);

	}
}
