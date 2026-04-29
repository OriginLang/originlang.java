// package com.originlang.client;
//
//
// import com.originlang.data.base.page.PageRequest;
// import com.originlang.data.base.page.PageResponse;
// import lombok.AllArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import java.util.Optional;
//
// @Service
// @Slf4j
// @AllArgsConstructor
// @Transactional(readOnly = true)
// public class ClientServiceImpl implements ClientService {
// private final ClientRepository clientRepository;
//
// @Transactional(rollbackFor = Exception.class)
// @Override
// public Client save(Client client) {
// return clientRepository.save(client);
// }
//
// @Override
// public Optional<Client> findByClientId(String clientId) {
// return clientRepository.findByClientId(clientId);
// }
//
// @Transactional(rollbackFor = Exception.class)
// @Override
// public void deleteByClientId(String clientId) {
// clientRepository.findByClientId(clientId).ifPresent(clientRepository::delete);
// }
//
// @Override
// public PageResponse<Client> page(PageRequest pageRequest) {
// Pageable pageable = pageRequest.toPageable();
// Page<Client> page = clientRepository.findAll(pageable);
// return new PageResponse<>(page);
// }
//
// }
