package com.originlang.authserver.client;

import java.util.Optional;

public interface ClientRepository {

	Optional<Client> findByClientId(String clientId);

}
