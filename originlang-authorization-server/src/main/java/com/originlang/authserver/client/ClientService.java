// package com.originlang.client;
//
// import com.originlang.data.base.page.PageRequest;
// import com.originlang.data.base.page.PageResponse;
// import org.springframework.transaction.annotation.Transactional;
//
// import java.util.Optional;
//
// public interface ClientService {
//
// Client save(Client client);
//
// Optional<Client> findByClientId(String clientId);
//
// @Transactional(rollbackFor = Exception.class)
// void deleteByClientId(String clientId);
//
// PageResponse<Client> page(PageRequest pageRequest);
// }
