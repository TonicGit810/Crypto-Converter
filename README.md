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
```

Replace input_mode and output_mode with either offline or online. For example:
To use the dummy input API and live output API:
```bash
gradle run --args="offline online"
```

To use the live input API and dummy output API:
```bash
gradle run --args="online offline"
```

## Dependencies

- **JavaFX**: For building the graphical user interface.
- **CoinMarketCap API**: To retrieve the list of active cryptocurrencies and their information.
- **Twilio API**: For sending SMS reports.

## Installation

1. Clone the repository to your local machine.
2. Ensure you have Java and JavaFX installed.
3. Install Twilio and configure it with your account SID and authentication token.
4. Configure the phone number to receive SMS reports.
5. Run the application using your preferred Java IDE or command line.

## Contributing

Contributions are welcome! Please fork the repository and submit pull requests for any improvements or bug fixes.
