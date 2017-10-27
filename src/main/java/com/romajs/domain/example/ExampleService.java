package com.romajs.domain.example;

import com.romajs.tx.TransactionContext;

import javax.persistence.EntityManager;

public class ExampleService {

	public Example get(Long id) {
		EntityManager em = TransactionContext.getEntityManager();
		return em.find(Example.class, id);
	}

	public Long post(Example example) {
		TransactionContext.getEntityManager().persist(example);
		return example.getId();
	}

	public void put(Long id, Example example) {
		Example get = get(id);
		get.setName(example.getName());
		TransactionContext.getEntityManager().merge(example);
	}

	public void delete(Long id) {
		TransactionContext.getEntityManager().remove(get(id));
	}
}
