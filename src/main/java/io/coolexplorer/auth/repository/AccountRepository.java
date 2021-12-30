package io.coolexplorer.auth.repository;

import io.coolexplorer.auth.model.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
    Account findAccountByEmail(String email);
}
