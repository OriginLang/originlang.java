// package com.originlang.client;
//
// import com.originlang.data.base.page.PageRequest;
// import com.originlang.data.base.page.PageResponse;
// import lombok.AllArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// @AllArgsConstructor
// @Slf4j
// @RestController
// @RequestMapping("/client")
// public class ClientController {
// private final ClientService clientService;
//
// // @PreAuthorize()
// @GetMapping
// public PageResponse<Client> page(PageRequest pageRequest) {
// return clientService.page(pageRequest);
// }
// }
