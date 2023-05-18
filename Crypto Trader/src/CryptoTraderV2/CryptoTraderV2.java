package CryptoTraderV2;
public class CryptoTraderV2 {
    public CryptoTraderV2() {

    }
    public static void main(String[] args) {
        /*
     =============================
               [Main] -> [Thread]
                  |          |
                  V          |
            [Trading Algo] <-
                  |
                  V
            [Purchase Log]
                  |
                  V
             [Portfolio] -> (Dollars)
            /     |     \
           V      V      V
    [Currency][Currency][Currency]
         |        |        |
         V        V        V
      (Shares) (Shares) (Shares)
     =============================

        Main
        - Loops (updates data)
            ~ Loop is a thread with defined runnable
            ~ Polls Trading Algo
        - Evaluates if currency is different from previous
            ~ Displays if different
        Trading Algo
            - Detects current value compared to value at previous purchase
            - If value is higher sell, if lower buy
                ~ If you can sell due to lack of funds do nothing
            - Transfer currency shares into Dollars and vice-versa
        Purchase Log
            - How much purchased at what price
            - Time of purchase
        Portfolio
            - Type of Currency
            - Shares of Currency
            - Dollars
        Currency
            - Current properties ✔
                ~ Name ✔
                ~ Currency Code ✔
                ~ Value ✔
         */
    }
}
