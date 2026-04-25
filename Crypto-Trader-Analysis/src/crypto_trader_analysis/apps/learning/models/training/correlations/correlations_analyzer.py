# correlations_analyzer.py

import logging
import pandas as pd
from src.crypto_trader_analysis.apps.learning.models.database.database import Database
from src.crypto_trader_analysis.apps.learning.models.database.query_type import QueryType

def fetch_joined_data(target_currency: str = "BTC", limit: int = 100000) -> pd.DataFrame:
    db = Database()
    logging.info(f"Fetching up to {limit} rows for {target_currency}.")
    df = db.fetch_data(
        target_currency=target_currency,
        limit=limit,
        query_type=QueryType.HISTORICAL_PRICE
    )
    df.rename(columns={"target_price": f"{target_currency.lower()}_price"}, inplace=True)
    if df.empty:
        logging.warning("DataFrame is empty. Possibly no data returned.")
    return df

def save_joined_data_to_csv(df: pd.DataFrame, filename: str = "joined_data.csv"):
    df.to_csv(filename, index=False)
    logging.info(f"Saved joined data to {filename}")

def compute_correlation_scores(
        df: pd.DataFrame,
        target_currency: str,
        output_filename: str = "correlation_scores.csv"
):
    target_col = f"{target_currency.lower()}_price"
    if target_col not in df.columns:
        logging.warning(f"'{target_col}' not in DataFrame. Correlation skipped.")
        return

    numeric_cols = df.select_dtypes(include=["number"]).columns
    if target_col not in numeric_cols:
        logging.warning(f"'{target_col}' not in numeric_cols. Correlation skipped.")
        return

    correlations = []
    for col in numeric_cols:
        if col == target_col:
            continue
        corr_value = df[target_col].corr(df[col], method="pearson")
        correlations.append((col, corr_value))

    corr_df = pd.DataFrame(correlations, columns=["column_name", "correlation"])
    corr_df.to_csv(output_filename, index=False)
    logging.info(f"Saved correlation scores to {output_filename}")

def main():
    import sys

    logging.basicConfig(
        level=logging.INFO,
        format="[%(asctime)s] [%(levelname)s] %(message)s",
        datefmt="%Y-%m-%d %H:%M:%S"
    )

    if len(sys.argv) > 1:
        target_currency = sys.argv[1]
    else:
        target_currency = "BTC"

    df = fetch_joined_data(target_currency=target_currency, limit=100000)
    if df.empty:
        logging.error("No data fetched. Exiting.")
        return

    save_joined_data_to_csv(df, f"joined_data_{target_currency}.csv")

    compute_correlation_scores(
        df,
        target_currency=target_currency,
        output_filename=f"correlation_scores_{target_currency}.csv"
    )

    logging.info("Done analyzing correlations.")

if __name__ == "__main__":
    main()
