package com.romajs.tx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;

public class TransactionContextFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionContextFilter.class);

	private String persistentUnitName;
	private EntityManagerFactory emFactory;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.debug("init");
		persistentUnitName = filterConfig.getServletContext().getInitParameter("tx-filter-pu");
		emFactory = Persistence.createEntityManagerFactory(persistentUnitName);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
		UUID uuid = UUID.randomUUID();
		LOGGER.trace("Started TX: {}", uuid.toString());
		TransactionContext.config(emFactory);
		TransactionContext.exec((em) -> {
			try {
				filterChain.doFilter(request, response);
			} catch (IOException | ServletException e) {
				LOGGER.error(e.getMessage(), e);
			}
		});
		LOGGER.trace("Finished TX: {}", uuid.toString());
	}

	@Override
	public void destroy() {
		LOGGER.debug("destroy");
		TransactionContext.terminate();
		persistentUnitName = null;
		emFactory = null;
	}
}