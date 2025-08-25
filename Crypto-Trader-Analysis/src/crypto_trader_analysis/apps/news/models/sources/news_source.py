from attr import attr
from attrs import define


@define
class NewsSource:
    name: str = attr(default="")
    url: str = attr(default="")