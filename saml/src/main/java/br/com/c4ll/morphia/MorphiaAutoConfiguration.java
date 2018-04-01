package br.com.c4ll.morphia;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;

@Configuration
public class MorphiaAutoConfiguration {
	
	@Autowired
	private MongoProperties mongoProperties;
	
	/**
	 * Responsável por criar o mapeamento das entidades
	 * @return: mapeamento efetuado
	 */
	private Morphia morphia() {
		final Morphia morphia = new Morphia();
		morphia.mapPackage("br.com.c4ll");
		
		return morphia;
	}
	
	/**
	 * Responsável por criar a datastore para se comunicar com o banco
	 * @param mongoClient: cliente do mongo injetado
	 * @return: datastore
	 */
	@Bean
	public Datastore datastore(MongoClient mongoClient) {
		final Datastore datastore = morphia().createDatastore(mongoClient, mongoProperties.getDatabase());
		datastore.ensureIndexes();
		
		return datastore;
	}
	
}
