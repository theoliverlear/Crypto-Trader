CREATE INDEX idx_currency_history_code_date
    ON currency_history (currency_code, last_updated);