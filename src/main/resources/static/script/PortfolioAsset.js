export class PortfolioAsset {
    //===========================-Constructors-===============================
    constructor(currencyName, currencyCode, shares, walletDollars, totalAssetValue, currencyLogoSrc) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.shares = shares;
        this.walletDollars = walletDollars;
        this.totalValue = totalAssetValue;
        this.currencyLogoSrc = currencyLogoSrc;
    }
    //=============================-Methods-==================================

    //-----------------------------Build-Html---------------------------------
    buildHtml() {
        return `
                <div class="currency-image-name-code-div your-currency-info-div">
                    <div class="your-currency-image-div">
                        <img class="your-currency-image"
                            src="${this.currencyLogoSrc}"
                            th:src="@{${this.currencyLogoSrc.replace('../static', '')}}"
                            alt="${this.currencyName} logo">
                    </div>

                </div>
                    <div class="currency-name-and-code-div">
                        <h5 class="your-currency-info-text">
                            <span class="your-currency-info-name-text">${this.currencyName}</span> -
                            <span class="your-currency-info-code-text">${this.currencyCode}</span>
                        </h5>
                    </div>
                <div class="simple-space-inline-div your-currency-info-div">
                    <div>
                        <h6 class="your-currency-info-text">
                            Shares: <span class="your-currency-info-shares-text">${this.shares}</span>
                        </h6>
                    </div>
                    <div>
                        <h6 class="your-currency-info-text">
                            Wallet: <span class="your-currency-info-wallet-text">${this.walletDollars}</span>
                        </h6>
                    </div>
                </div>
                <div class="simple-space-inline-div your-currency-info-div">
                    <div>
                        <h6 class="your-currency-info-text">
                            Total Value: <span class="your-currency-info-total-text">${this.totalValue}</span>
                        </h6>
                    </div>
                </div>
                <div class="simple-space-inline-div your-currency-info-div">
                    <div>
                        <h6 class="your-currency-info-text">
                            All Time Profit: $<span class="your-currency-info-profit-text">0.00</span>
                        </h6>
                    </div>
<!--                    <div class="small-button as-percent-button">-->
<!--                        <h6 class="small-button-text">As Percent</h6>-->
<!--                    </div>-->
                </div>
            <hr />
        `;
    }
}