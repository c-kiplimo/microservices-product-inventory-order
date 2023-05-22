package com.collicode.orderservice.service;

import com.collicode.orderservice.dto.InventoryResponse;
import com.collicode.orderservice.dto.OrderLineItemsDto;
import com.collicode.orderservice.dto.OrderRequest;
import com.collicode.orderservice.event.OrderPlacedEvent;
import com.collicode.orderservice.model.Order;
import com.collicode.orderservice.model.OrderLineItems;
import com.collicode.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
//    private final WebClient.Builder webClientBuilder;
//    private final ObservationRegistry observationRegistry;
//   private final ApplicationEventPublisher applicationEventPublisher;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

//        List<String> skuCodes = order.getOrderLineItemsList().stream()
//                .map(OrderLineItems::getSkuCode)
//                .collect(Collectors.toList());

        // Call Inventory Service, and place order if product is in
        // stock
//        Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup",
//                this.observationRegistry);
//        inventoryServiceObservation.lowCardinalityKeyValue("call", "inventory-service");
//        return inventoryServiceObservation.observe(() -> {
//            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
//                    .uri("http://inventory-service/api/inventory",
//                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
//                    .retrieve()
//                    .bodyToMono(InventoryResponse[].class)
//                    .block();
//
//            boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
//                    .allMatch(InventoryResponse::isInStock);
//
//            if (allProductsInStock) {
//                orderRepository.save(order);
//                // publish Order Placed Event
//                applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
//                return "Order Placed";
//            } else {
//                throw new IllegalArgumentException("Product is not in stock, please try again later");
//            }
//        });
         orderRepository.save(order);
     return "Order Placed";
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
