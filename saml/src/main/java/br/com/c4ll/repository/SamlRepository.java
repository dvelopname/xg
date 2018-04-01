package br.com.c4ll.repository;

import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SamlRepository {
	
	@Autowired
	private Datastore datastore;
	
	public void add() {
	}
	
}
