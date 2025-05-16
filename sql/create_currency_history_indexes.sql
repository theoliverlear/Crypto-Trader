CREATE INDEX idx_currency_history_code_date_value
    ON currency_history (currency_code, last_updated, currency_value);

CREATE INDEX idx_currency_history_code_updated
    ON currency_history (currency_code, last_updated DESC);

CREATE INDEX idx_currency_history_updated
    ON currency_history(last_updated DESC);

CREATE INDEX ON currency_history (currency_code, last_updated DESC);

CREATE INDEX idx_code_updated_value ON currency_history (currency_code, last_updated DESC, currency_value);

CREATE INDEX idx_currency_history_code_updated_value
    ON currency_history (currency_code, last_updated DESC, currency_value);

CREATE INDEX idx_currency_history_code_updated
    ON currency_history (currency_code, last_updated DESC);

CREATE INDEX idx_currency_history_updated
    ON currency_history (last_updated DESC);
