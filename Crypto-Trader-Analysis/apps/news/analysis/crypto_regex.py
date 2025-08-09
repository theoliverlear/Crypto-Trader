import re as regex


CRYPTO_TERMS = {
    "ada", "altcoin",
    "bitcoin", "btc", "binance", "bnb", "blockchain",
    "cardano", "crypto", "cryptocurrency", "cryptocurrencies", "coinbase", "crypto wallet",
    "doge", "dogecoin", "decentralized exchange", "defi", "dao",
    "ethereum", "eth", "ether",
    "hashing", "hash", "halving",
    "ledger", "ltc", "litecoin",
    "mining", "monero", "memecoin", "mining pool", "mainnet",
    "nft"
    "protocol",
    "rug pull",
    "solana", "sol", "stablecoin", "satoshi",
    "token", "tether",
    "usdt"
    "web3", "wrapped token",
    "xrp",
}
CRYPTO_PATTERN = regex.compile(r"\b(" + "|".join(map(regex.escape, CRYPTO_TERMS)) + r")\b", regex.I)