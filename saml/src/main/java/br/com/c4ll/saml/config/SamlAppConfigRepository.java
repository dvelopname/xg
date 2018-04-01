package br.com.c4ll.saml.config;

import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SamlAppConfigRepository {

	@Autowired
	private Datastore datastore;

	public void add(SamlAppConfig app) {
		datastore.save(app);
	}

	public SamlAppConfig get(String clientId) {
		SamlAppConfig config = datastore.createQuery(SamlAppConfig.class).field("clientId").equal(clientId).get();
		return config;
	}

	public void update(SamlAppConfig config) {
		datastore.save(config);
	}

}
