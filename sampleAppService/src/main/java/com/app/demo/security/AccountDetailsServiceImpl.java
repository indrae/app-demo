/*
 * (c) Copyright 2005-2012 JAXIO, www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-backend:src/main/java/project/security/AccountDetailsServiceImpl-spring3.p.vm.java
 */
package com.app.demo.security;

import java.util.Collection;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.demo.dao.support.SearchTemplate;
import com.app.demo.dao.support.SearchMode;
import com.app.demo.domain.Person;
import com.app.demo.service.PersonService;

/**
 * An implementation of Spring Security's UserDetailsService.
 */
@Service
public class AccountDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = Logger.getLogger(AccountDetailsServiceImpl.class);

    private PersonService personService;

    public AccountDetailsServiceImpl() {
    }

    @Autowired
    public AccountDetailsServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Retrieve an account depending on its login this method is not case sensitive.<br>
     * use <code>obtainAccount</code> to match the login to either email, login or whatever is your login logic
     *
     * @param login the account login
     * @return a Spring Security userdetails object that matches the login
     * @see #obtainAccount(String)
     * @throws UsernameNotFoundException when the user could not be found
     * @throws DataAccessException when an error occured while retrieving the account
     */
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException, DataAccessException {
        if (login == null || login.trim().isEmpty()) {
            throw new UsernameNotFoundException("Empty login");
        }

        if (log.isDebugEnabled()) {
            log.debug("Security verification for user '" + login + "'");
        }

        Person account = obtainAccount(login);

        if (account == null) {
            if (log.isInfoEnabled()) {
                log.info("Account " + login + " could not be found");
            }
            throw new UsernameNotFoundException("account " + login + " could not be found");
        }

        Collection<GrantedAuthority> grantedAuthorities = obtainGrantedAuthorities(login);

        if (grantedAuthorities == null) {
            grantedAuthorities = SpringSecurityContext.toGrantedAuthorities(account.getRoleNames());
        }

        String password = obtainPassword(login);

        if (password == null) {
            password = account.getPassword();
        }

        boolean enabled = account.getIsEnabled();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new org.springframework.security.core.userdetails.User(login, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, grantedAuthorities);
    }

    /**
     * Return the account depending on the login provided by spring security.
     * @return the person if found
     */
    protected Person obtainAccount(String login) {
        Person account = new Person();
        account.setUsername(login);

        SearchTemplate searchTemplate = new SearchTemplate();
        searchTemplate.setSearchMode(SearchMode.EQUALS);
        searchTemplate.setCaseSensitive(false);

        return personService.findUniqueOrNone(account, searchTemplate);
    }

    /**
     * Returns null. Subclass may override it to provide their own granted authorities.
     */
    protected Collection<GrantedAuthority> obtainGrantedAuthorities(String username) {
        return null;
    }

    /**
     * Returns null. Subclass may override it to provide their own password.
     */
    protected String obtainPassword(String username) {
        return null;
    }
}