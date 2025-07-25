package org.theoliverlear.repository;
//=================================-Imports-==================================
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.theoliverlear.entity.currency.Currency;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    //============================-Methods-===================================

    //-------------------Get-Currency-By-Currency-Code------------------------
    Currency getCurrencyByCurrencyCode(String currencyCode);
    //------------------------Get-Currency-By-Name----------------------------
    Currency getCurrencyByName(String currencyName);

    boolean existsByCurrencyCode(String currencyCode);

    @Query("SELECT c.currencyCode FROM Currency c ORDER BY c.currencyCode ASC")
    List<String> findAllCurrencyCodes();
}
