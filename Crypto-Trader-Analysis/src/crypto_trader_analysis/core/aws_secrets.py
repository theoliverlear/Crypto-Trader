import json
from functools import lru_cache
import boto3

class SecretsManagerService:
    def __init__(self, region_name: str = "us-east-1") -> None:
        self._client = boto3.client("secretsmanager", region_name=region_name)

    @lru_cache(maxsize=32)
    def get_secret(self, secret_name: str) -> dict[str, str]:
        try:
            response = self._client.get_secret_value(SecretId=secret_name)
        except Exception as e:
            raise RuntimeError(f"Could not retrieve secret {secret_name}: {e}")

        secret_string = response.get("SecretString")
        if not secret_string:
            raise ValueError(f"Secret {secret_name} did not contain SecretString")

        parsed_secret = json.loads(secret_string)
        if not isinstance(parsed_secret, dict):
            raise ValueError(f"Secret {secret_name} did not contain key/value data")

        return parsed_secret
