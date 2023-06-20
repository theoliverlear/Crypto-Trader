import matplotlib
import matplotlib.pyplot as plt
import pandas as pd
import pyodbc
from azure.identity import DefaultAzureCredential

matplotlib.use('TkAgg')


# matplotlib.use('Qt5Agg')
# matplotlib.use('WXAgg')
# matplotlib.use('WXAgg')

def single_item_line_graph(connection_string, auth, currency_code,
                           table_name):
    connection = pyodbc.connect(connection_string, auth=auth)
    cursor = connection.cursor()
    query = "SELECT time_event, exchange_rate FROM " + table_name + \
            " WHERE currency_code = '" + currency_code + "';"
    cursor.execute(query)
    results = cursor.fetchall()
    df = pd.DataFrame.from_records(results, columns=['time_event',
                                                     'exchange_rate'])
    plt.plot(df['time_event'], df['exchange_rate'])
    plt.xlabel('Time')
    plt.ylabel('Exchange Rate in Dollars')
    plt.title('Exchange Rate Distribution ' + currency_code)
    min_rate = df['exchange_rate'].min()
    max_rate = df['exchange_rate'].max()
    plt.ylim(min_rate, max_rate)
    plt.show()
    cursor.close()
    connection.close()


def multiple_item_line_graph(connection_string, auth, table_name,
                             currency_codes):
    connection = pyodbc.connect(connection_string, auth=auth)
    cursor = connection.cursor()
    query = "SELECT time_event, exchange_rate, currency_code FROM "\
            + table_name + ";"
    cursor.execute(query)
    results = cursor.fetchall()
    df = pd.DataFrame.from_records(results, columns=['time_event',
                                                     'exchange_rate',
                                                     'currency_code'])
    colors = plt.cm.get_cmap('tab10', len(currency_codes))
    for i, currency_code in enumerate(currency_codes):
        df_currency = df[df['currency_code'] == currency_code]
        plt.plot(df_currency['time_event'], df_currency['exchange_rate'],
                 color=colors(i), label=currency_code)
    plt.xlabel('Time')
    plt.ylabel('Exchange Rate in Dollars')
    plt.title('Exchange Rate Distribution')
    plt.legend()
    plt.show()
    cursor.close()
    connection.close()


def multiple_item_portfolio_bar_graph(connection_string,
                                      auth, show_profit=False):
    connection = pyodbc.connect(connection_string, auth=auth)
    cursor = connection.cursor()
    query = "SELECT currency_code, total_value FROM Portfolio;"
    cursor.execute(query)
    results = cursor.fetchall()
    df = pd.DataFrame.from_records(results, columns=['currency_code',
                                                     'total_value'])
    if show_profit:
        df['total_value'] -= 1000
    df['total_value'] = pd.to_numeric(df['total_value'])
    df = df.sort_values(by=['total_value'], ascending=False)
    currency_codes = df['currency_code'].unique()
    colors = plt.cm.get_cmap('tab10', len(currency_codes))
    for i, currency_code in enumerate(currency_codes):
        df_currency = df[df['currency_code'] == currency_code]
        plt.bar(df_currency['currency_code'], df_currency['total_value'],
                color=colors(i), label=currency_code)
    plt.xlabel('Currency Code')
    if show_profit:
        plt.ylabel('Total Value Increase in Dollars')
    else:
        plt.ylabel('Total Value in Dollars')
    plt.title('Portfolio Value Distribution')
    max_value = df['total_value'].max() + (df['total_value'].max() * 0.1)
    plt.ylim(0, max_value)
    plt.show()
    cursor.close()
    connection.close()


def single_item_portfolio_line_graph(connection_string, auth, currency_code):
    connection = pyodbc.connect(connection_string, auth=auth)
    cursor = connection.cursor()
    query = "SELECT time_event, total_value FROM PortfolioHistory" \
            " WHERE currency_code = '" + currency_code + "';"
    cursor.execute(query)
    results = cursor.fetchall()
    df = pd.DataFrame.from_records(results, columns=['time_event',
                                                     'total_value'])
    plt.plot(df['time_event'], df['total_value'])
    plt.xlabel('Time')
    plt.ylabel('Total Value in Dollars')
    plt.title(currency_code + " - Portfolio Value Distribution")
    min_value = df['total_value'].min()
    max_value = df['total_value'].max()
    plt.ylim(min_value, max_value)
    plt.show()
    cursor.close()
    connection.close()


def multiple_item_portfolio_line_graph(connection_string, auth,
                                       show_profit=False):
    connection = pyodbc.connect(connection_string, auth=auth)
    cursor = connection.cursor()
    query = "SELECT time_event, total_value, currency_code FROM " \
            "PortfolioHistory;"
    cursor.execute(query)
    results = cursor.fetchall()
    df = pd.DataFrame.from_records(results, columns=['time_event',
                                                     'total_value',
                                                     'currency_code'])
    if show_profit:
        df['total_value'] -= 1000
    colors = plt.cm.get_cmap('tab10', len(df['currency_code'].unique()))
    for i, currency_code in enumerate(df['currency_code'].unique()):
        df_currency = df[df['currency_code'] == currency_code]
        plt.plot(df_currency['time_event'],
                 df_currency['total_value'],
                 color=colors(i),
                 label=currency_code)
    plt.xlabel('Time')
    if show_profit:
        plt.ylabel('Total Value Increase in Dollars')
    else:
        plt.ylabel('Total Value in Dollars')
    plt.title('Portfolio Value Distribution')
    plt.legend()
    plt.show()
    cursor.close()
    connection.close()


def get_currency_codes(connection_string, auth):
    connection = pyodbc.connect(connection_string, auth=auth)
    cursor = connection.cursor()
    query = "SELECT currency_code FROM CurrencyValueHistoryInterval;"
    cursor.execute(query)
    results = cursor.fetchall()
    df = pd.DataFrame.from_records(results, columns=['currency_code'])
    cursor.close()
    connection.close()
    return df['currency_code'].unique()


def get_portfolio_profit(connection_string, auth):
    connection = pyodbc.connect(connection_string, auth=auth)
    cursor = connection.cursor()
    return_phrase = ""
    query = "SELECT currency_code, total_value FROM Portfolio;"
    cursor.execute(query)
    results = cursor.fetchall()
    for result in results:
        return_phrase += str(result[0]) + " " + \
                         f"${(result[1] - 1000):.2f}" + "\n"
    cursor.close()
    connection.close()
    return return_phrase


server_name = 'crypto-trader-server.database.windows.net'
database_name = 'CryptoTrader'
credential = DefaultAzureCredential()
connection_string = 'Driver={ODBC Driver 17 for SQL Server};' \
                    'Server=' + server_name + ';Database=' + database_name + \
                    ';Authentication=ActiveDirectoryInteractive'
currency_codes = get_currency_codes(connection_string, credential)
# returns ['SHIB' 'ETH' 'LTC' 'ADA' 'BTC' 'SOL'
# 'DOGE' 'XLM' 'MATIC' 'DOT' 'LINK']

# %% Get Currency Codes
print(get_currency_codes(connection_string, credential))

# %% Get Portfolio Profit
print(get_portfolio_profit(connection_string, credential))

# %% Bitcoin Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'BTC',
                       'CurrencyValueHistoryInterval')

# %% Shiba Inu Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'SHIB',
                       'CurrencyValueHistoryInterval')

# %% Ethereum Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'ETH',
                       'CurrencyValueHistoryInterval')

# %% Dogecoin Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'DOGE',
                       'CurrencyValueHistoryInterval')

# %% Litecoin Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'LTC',
                       'CurrencyValueHistoryInterval')

# %% Cardano Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'ADA',
                       'CurrencyValueHistoryInterval')

# %% Solana Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'SOL',
                       'CurrencyValueHistoryInterval')

# %% Polygon Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'MATIC',
                       'CurrencyValueHistoryInterval')

# %% Chainlink Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'LINK',
                       'CurrencyValueHistoryInterval')

# %% Stellar Lumens Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'XLM',
                       'CurrencyValueHistoryInterval')

# %% Polkadot Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'DOT',
                       'CurrencyValueHistoryInterval')

# %% All Line Graph - CurrencyValueHistoryInterval
multiple_item_line_graph(connection_string, credential,
                         'CurrencyValueHistoryInterval', currency_codes)

# %% Bitcoin Line Graph - Portfolio
single_item_portfolio_line_graph(connection_string, credential, 'BTC')

# %% Shiba Inu Line Graph - Portfolio
single_item_portfolio_line_graph(connection_string, credential, 'SHIB')

# %% Ethereum Line Graph - Portfolio
single_item_portfolio_line_graph(connection_string, credential, 'ETH')

# %% Dogecoin Line Graph - Portfolio
single_item_portfolio_line_graph(connection_string, credential, 'DOGE')

# %% Litecoin Line Graph - Portfolio
single_item_portfolio_line_graph(connection_string, credential, 'LTC')

# %% Cardano Line Graph - Portfolio
single_item_portfolio_line_graph(connection_string, credential, 'ADA')

# %% Solana Line Graph - Portfolio
single_item_portfolio_line_graph(connection_string, credential, 'SOL')

# %% Polygon Line Graph - Portfolio
single_item_portfolio_line_graph(connection_string, credential, 'MATIC')

# %% Chainlink Line Graph - Portfolio
single_item_portfolio_line_graph(connection_string, credential, 'LINK')

# %% Stellar Lumens Line Graph - Portfolio
single_item_portfolio_line_graph(connection_string, credential, 'XLM')

# %% Polkadot Line Graph - Portfolio
single_item_portfolio_line_graph(connection_string, credential, 'DOT')

# %% All Bar Graph - Portfolio Profits
multiple_item_portfolio_bar_graph(connection_string, credential, True)

# %% All Line Graph - Portfolio Profits
multiple_item_portfolio_line_graph(connection_string, credential, True)

# %% All Bar Graph - Portfolio Values
multiple_item_portfolio_bar_graph(connection_string, credential, False)

# %% All Line Graph - Portfolio Values
multiple_item_portfolio_line_graph(connection_string, credential, False)

