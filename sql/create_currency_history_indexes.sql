CREATE INDEX idx_currency_history_code_date_value
    ON currency_history (currency_code, last_updated, currency_value);