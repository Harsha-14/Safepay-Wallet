package com.cg.safepaywallet.security;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.cg.safepaywallet.bean.Account;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
	EntityManager entityManager;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    	

		String command = "select account from Account account where account.userName=:userName";
		TypedQuery<Account> query = entityManager.createQuery(command, Account.class);
		query.setParameter("userName",s);
		Account account = query.getResultList().get(0);
        return new User(account.getUserName(), account.getPassword(),new ArrayList<>());
    }
}
