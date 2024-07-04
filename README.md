# Cryptocurrency Conversion Application

## Overview

This application provides a graphical user interface (GUI) for managing and converting between different cryptocurrencies. Users can add cryptocurrencies from a list, view detailed information about each currency, and perform conversions between selected currencies.

## Features

- **Add Cryptocurrency**: Users can add a cryptocurrency to the main list by selecting from a list of active cryptocurrencies from CoinMarketCap (displaying name and symbol only).
- **View Details**: The main window list displays detailed information about each selected cryptocurrency, including:
  - Logo (visual display)
  - Name
  - Symbol
  - Description
  - Date Launched
  - Website (clickable URL that opens in the default browser)
- **Currency Conversion**: Users can input an amount of a given currency, select a target currency, and view the conversion rate and result.
- **Manage List**: Users can clear the entire list or remove individual currencies from the list.
- **Report Generation**: The application provides two types of reports for conversions:
  - **Long Form Report**: Includes all main-window information about the two selected currencies, conversion rate, starting value, and finishing value.
  - **Short Form Report**: Includes only the selection-window information (name and symbol) about the two selected currencies, conversion rate, starting value, and finishing value.
  - **SMS Report**: Send a 'short' form report to a single configured phone number using Twilio.

## Caching

- The application caches information about each selected currency to optimize performance.
- Conversion rates and results are not cached and are recalculated as needed.

## Usage

1. **Starting the Application**: The main window begins with an empty list.
2. **Adding a Cryptocurrency**:
   - Click on 'Add Currency'.
   - A separate window will display a list of active CoinMarketCap cryptocurrencies (name and symbol).
   - Select a cryptocurrency and close the window. The selected currency will be added to the main window list with detailed information.
3. **Performing Conversions**:
   - Input an amount for a given currency.
   - Select a target currency.
   - The application will display the conversion rate and the resulting value.
4. **Managing the List**:
   - To clear the list, use the 'Clear List' option.
   - To remove an individual currency, select the currency and use the 'Remove Currency' option.
5. **Generating Reports**:
   - To send a short form report via SMS, configure a phone number and use the Twilio integration.

## Twilio Configuration

To enable SMS reports, configure the following Twilio credentials in the application:

- **Twilio Token**: `TWILIO_API_KEY`
- **Twilio SID**: `TWILIO_API_SID`
- **Twilio Send From Number**: `TWILIO_API_FROM`
- **Twilio Send To Number**: `TWILIO_API_TO`

## Dummy APIs

For testing and demonstration purposes, the application includes dummy versions of both the input and output APIs. These APIs can be selected via command line arguments to run in different modes:

- **Offline**: Use dummy API
- **Online**: Use live API

### Command Line Arguments

To run the application with different API modes, use the following command:

```bash
gradle run --args="input_mode output_mode"
