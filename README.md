This document highlights the capabilities of CryptoTrader v0.71, a sophisticated 
cryptocurrency trading software. In this explanation, I will dissect its diverse functionalities.

The program continuously monitors the current values of cryptocurrencies and logs them 
at both change-dependent and independent intervals. The crucial feature of the software, 
portfolio management, is reliant on this data.

In trading, the essential strategy is to follow the time-honored principle of 
"buy low, sell high". This principle is implemented within the software through
the .poll() method. Whenever the current price, as fetched from the CryptoTraderDatabase,
is higher than the purchase price, the software is prompted to sell. Conversely, if the 
current price is lower, the software will initiate a buy.

It's important to clarify that the primary focus of the current version of this program
is not to conduct an in-depth analysis of the crypto market. Rather, it is designed to
execute trades in a strictly profitable manner.

By version 2.0 or possibly earlier, the aim is to introduce functionalities that would
allow for high-frequency and marginal trading, compatible with platforms such as Binance
or Coinbase. My goal is to make this tool accessible to a broad audience that may wish 
to leverage computer processing for day trading. Please feel free to ask any questions
about CryptoTrader v0.71 in the comments section!

Please note that this project is protected under a BSD-2 license. This means that you
are free to use and modify the code, but you must credit the original author. For more
information, please refer to the LICENSE.md file in the repository.