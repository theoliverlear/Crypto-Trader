import matplotlib.pyplot as plt
import pandas as pd
import pyodbc
from azure.identity import DefaultAzureCredential
import matplotlib
matplotlib.use('TkAgg')
#matplotlib.use('') add others

#%% Retrieving data from the database connection
server_name = 'crypto-trader-server.database.windows.net'
database_name = 'CryptoTrader'
credential = DefaultAzureCredential()
connection_string = 'Driver={ODBC Driver 17 for SQL Server};Server=' + server_name + ';Database=' + database_name + ';Authentication=ActiveDirectoryInteractive'
connection = pyodbc.connect(connection_string, auth=credential)
cursor = connection.cursor()
query = "SELECT exchange_rate, time_event FROM CurrencyValueHistoryInterval WHERE currency_code = 'BTC'"
cursor.execute(query)
results = cursor.fetchall()
# for row in results:
#     print(row)
cursor.close()
connection.close()
#%% Plotting the data from the database connection as a line chart
df = pd.DataFrame.from_records(results, columns=['time_event', 'exchange_rate'])
# df['currency_name'] = pd.Categorical(df['currency_name'])
plt.plot(df['exchange_rate'], df['time_event'])
plt.xlabel('Time')
plt.ylabel('Exchange Rate')
plt.title('Exchange Rate Distribution BTC')
plt.show()

