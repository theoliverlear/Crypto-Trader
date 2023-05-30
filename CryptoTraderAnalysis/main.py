import matplotlib.pyplot as plt
import pandas as pd
import pyodbc
from azure.identity import DefaultAzureCredential
import matplotlib

matplotlib.use('TkAgg')


# matplotlib.use('') add others
def single_item_line_graph(connection_string, auth, currency_code, table_name):
    connection = pyodbc.connect(connection_string, auth=auth)
    cursor = connection.cursor()
    query = "SELECT exchange_rate, time_event FROM " + table_name + " WHERE currency_code = '" + currency_code + "';"
    cursor.execute(query)
    results = cursor.fetchall()
    df = pd.DataFrame.from_records(results, columns=['time_event', 'exchange_rate'])
    plt.plot(df['exchange_rate'], df['time_event'])
    plt.xlabel('Time')
    plt.ylabel('Exchange Rate')
    plt.title('Exchange Rate Distribution ' + currency_code)
    plt.show()
    cursor.close()
    connection.close()


def multiple_item_line_graph(connection_string, auth, table_name, currency_codes):
    connection = pyodbc.connect(connection_string, auth=auth)
    cursor = connection.cursor()
    query = "SELECT exchange_rate, time_event, currency_code FROM " + table_name + ";"
    cursor.execute(query)
    results = cursor.fetchall()
    df = pd.DataFrame.from_records(results, columns=['time_event', 'exchange_rate', 'currency_code'])
    colors = plt.cm.get_cmap('tab10', len(currency_codes))
    for i, currency_code in enumerate(currency_codes):
        df_currency = df[df['currency_code'] == currency_code]
        plt.plot(df_currency['exchange_rate'], df_currency['time_event'], color=colors(i), label=currency_code)
    plt.xlabel('Time')
    plt.ylabel('Exchange Rate')
    plt.title('Exchange Rate Distribution')
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


server_name = 'crypto-trader-server.database.windows.net'
database_name = 'CryptoTrader'
credential = DefaultAzureCredential()
connection_string = 'Driver={ODBC Driver 17 for SQL Server};Server=' + server_name + ';Database=' + database_name + \
                    ';Authentication=ActiveDirectoryInteractive'
currency_codes = get_currency_codes(connection_string, credential) # returns ['SHIB' 'ETH' 'LTC' 'ADA' 'BTC' 'SOL'
# 'DOGE' 'XLM' 'MATIC' 'DOT' 'LINK']

# %% Get Currency Codes
print(get_currency_codes(connection_string, credential))

# %% Bitcoin Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'BTC', 'CurrencyValueHistoryInterval')

# %% Shiba Inu Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'SHIB', 'CurrencyValueHistoryInterval')

# %% Ethereum Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'ETH', 'CurrencyValueHistoryInterval')

# %% Dogecoin Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'DOGE', 'CurrencyValueHistoryInterval')

# %% Litecoin Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'LTC', 'CurrencyValueHistoryInterval')

# %% Cardano Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'ADA', 'CurrencyValueHistoryInterval')

# %% Solana Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'SOL', 'CurrencyValueHistoryInterval')

# %% Polygon Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'MATIC', 'CurrencyValueHistoryInterval')

# %% Chainlink Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'LINK', 'CurrencyValueHistoryInterval')

# %% Stellar Lumens Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'XLM', 'CurrencyValueHistoryInterval')

# %% Polkadot Line Graph - CurrencyValueHistoryInterval
single_item_line_graph(connection_string, credential, 'DOT', 'CurrencyValueHistoryInterval')

# %% All Line Graph - CurrencyValueHistoryInterval
multiple_item_line_graph(connection_string, credential, 'CurrencyValueHistoryInterval', currency_codes)

