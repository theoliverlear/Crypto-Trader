//package org.theoliverlear.model.thread;
//
//import org.theoliverlear.entity.Currency;
//import org.theoliverlear.service.CurrencyService;
//
//public class CurrencyUpdaterThread extends Thread {
//    Currency currency;
//    CurrencyService currencyService;
//    public CurrencyUpdaterThread(Currency currency, CurrencyService currencyService) {
//        this.currency = currency;
//        this.currencyService = currencyService;
//    }
//    @Override
//    public void run() {
//        while (true) {
//            try {
//                this.currency.updateValue();
////                this.currencyService.saveCurrencies(this.currency);
//                Thread.sleep(5000);
//            } catch (InterruptedException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//    }
//}
