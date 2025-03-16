package org.theoliverlear.repository;
//=================================-Imports-==================================
import org.springframework.data.jpa.repository.JpaRepository;
import org.theoliverlear.entity.currency.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    //============================-Methods-===================================

    //-------------------Get-Currency-By-Currency-Code------------------------
    Currency getCurrencyByCurrencyCode(String currencyCode);
    //------------------------Get-Currency-By-Name----------------------------
    Currency getCurrencyByName(String currencyName);

    boolean existsByCurrencyCode(String currencyCode);
}
