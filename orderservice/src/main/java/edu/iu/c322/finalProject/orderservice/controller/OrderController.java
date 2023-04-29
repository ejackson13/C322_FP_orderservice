package edu.iu.c322.finalProject.orderservice.controller;

import edu.iu.c322.finalProject.orderservice.model.dto.*;
import edu.iu.c322.finalProject.orderservice.model.entity.*;
import edu.iu.c322.finalProject.orderservice.repository.*;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;




@RestController
@RequestMapping("/orders")
public class OrderController {


    updateInventoryDto updateInventoryDto = new updateInventoryDto();


    @Autowired
    private CustomerShippingAddressRepository customerShippingRepository;

    @Autowired
    private CustomerBillingAddressRepository customerBillingAddressRepository;


    @Autowired
    private PaymentRepository paymentMethodRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RentedItemRepository rentedItemRepository;


    @Autowired
    private SellerShippingRepository sellerShippingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerItemRepository sellerItemRepository;

    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setTotal(order.getTotal());
        orderDto.setSellerId(order.getSellerId());

        Customer customer = customerRepository.findById(order.getCustomer().getId()).orElse(null);
        if(customer != null){
            CustomerDto customerDto = new CustomerDto();
            customerDto.setEmail(customer.getEmail());
            customerDto.setName(customer.getName());

            orderDto.setCustomer(customerDto);
        }

        CustomerShippingAddress customerShippingAddress = customerShippingRepository.findById(order.getCustomerAddress().getId()).orElse(null);
        if (customerShippingAddress != null) {
            CustomerShippingAddressDto shippingAddressDto = new CustomerShippingAddressDto();
            // Set the fields for the shippingAddressDto
            shippingAddressDto.setState(customerShippingAddress.getState());
            shippingAddressDto.setCity(customerShippingAddress.getCity());
            shippingAddressDto.setPostalCode(customerShippingAddress.getPostalCode());
            shippingAddressDto.setStreet(customerShippingAddress.getStreet());
            orderDto.setShippingAddress(shippingAddressDto);
        }
        SellerShippingAddress sellerShippingAddress = sellerShippingRepository.findById(order.getSellerShipping().getId()).orElse(null);
        if (sellerShippingAddress != null) {
            SellerShippingAddressDto sellerShippingAddressDto = new SellerShippingAddressDto();
            // Set the fields for the shippingAddressDto
            sellerShippingAddressDto.setState(sellerShippingAddress.getState());
            sellerShippingAddressDto.setCity(sellerShippingAddress.getCity());
            sellerShippingAddressDto.setPostalCode(sellerShippingAddress.getPostalCode());
            sellerShippingAddressDto.setStreet(sellerShippingAddress.getStreet());
            orderDto.setSellerShippingAddress(sellerShippingAddressDto);
        }

        List<RentedItems> orderItems = rentedItemRepository.findByOrderId(order.getId());
        List<ItemsDto> itemDtos = new ArrayList<>();
        for (RentedItems orderItem : orderItems) {
            if (orderItem != null) {
                ItemsDto itemDto = new ItemsDto();
                itemDto.setName(orderItem.getName());
                itemDto.setPrice(orderItem.getPrice());
                itemDto.setQuantity(orderItem.getQuantity()); // Set the quantity from the orderItem table
                itemDto.setDateRented(orderItem.getDateRented());
                itemDto.setReturnByDate(orderItem.getReturnByDate());
                itemDtos.add(itemDto);
            }
        }
        orderDto.setItemsRented(itemDtos);

        Payment paymentMethod = paymentMethodRepository.findById(order.getPaymentMethod().getId()).orElse(null);
        if (paymentMethod != null) {
            CustomerBillingAddress billingAddress = customerBillingAddressRepository.findById(paymentMethod.getBillingAddress().getId()).orElse(null);
            if (billingAddress != null) {
                CustomerBillingAddressDto billingAddressDto = new CustomerBillingAddressDto();
                // Set the fields for the billingAddressDto
                billingAddressDto.setState(billingAddress.getState());
                billingAddressDto.setCity(billingAddress.getCity());
                billingAddressDto.setPostalCode(billingAddress.getPostalCode());

                PaymentDto paymentMethodDto = new PaymentDto();
                // Set the fields for the paymentMethodDto
                paymentMethodDto.setMethod(paymentMethod.getMethod());
                paymentMethodDto.setNumber(paymentMethod.getCardNumber());
                paymentMethodDto.setBillingAddress(billingAddressDto);
                orderDto.setPayment(paymentMethodDto);
            }
        }
        return orderDto;
    }


    @CrossOrigin()

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderDto>> findByCustomerId(@PathVariable int id){

        List<Order> orders = orderRepository.findByCustomerId(id);
        if (orders.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<OrderDto> orderDtos = orders.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);

    }
    @CrossOrigin()

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderDto> findByOrderId(@PathVariable int id){

        Order orders = orderRepository.findByid(id);
        if (orders == null) {
            return ResponseEntity.notFound().build();
        }
        OrderDto orderDtos = convertToDto(orders);
        return ResponseEntity.ok(orderDtos);

    }

    @ResponseStatus(HttpStatus.CREATED)

    @CrossOrigin()

    @PostMapping
    public int create(@Valid @RequestBody OrderDto orderDto){

        // Save the shipping address
        CustomerShippingAddress shippingAddress = new CustomerShippingAddress(orderDto.getShippingAddress());
        CustomerShippingAddress savedShippingAddress = customerShippingRepository.save(shippingAddress);

        // Save the payment method and billing address
        CustomerBillingAddress billingAddress = new CustomerBillingAddress(orderDto.getPayment().getBillingAddress());
        CustomerBillingAddress savedBillingAddress = customerBillingAddressRepository.save(billingAddress);

        Payment paymentMethod = new Payment(orderDto.getPayment(), savedBillingAddress);
        Payment savedPaymentMethod = paymentMethodRepository.save(paymentMethod);

        SellerShippingAddress sellerShippingAddress = new SellerShippingAddress(orderDto.getSellerShippingAddress());
        //SellerShippingAddress savedSellerShippingAddress = sellerShippingRepository.save(sellerShippingAddress);

        Customer customer = new Customer(orderDto.getCustomer());
        Customer savedCustomer = customerRepository.save(customer);


        // Save the order
        Order order = new Order(orderDto, savedCustomer, sellerShippingAddress, savedPaymentMethod, savedShippingAddress);
        Order addedOrder = orderRepository.save(order);

        // Save the order items
        for (ItemsDto items : orderDto.getItemsRented()) {
            // Assuming you have an itemRepository for fetching existing items by ID


            RentedItems rentedItem = new RentedItems(addedOrder, items, savedCustomer);

            rentedItemRepository.save(rentedItem);

           submitOrder(items);


        }




        return addedOrder.getId();
    }

    @Transactional
    public void submitOrder(ItemsDto items) {
        // your existing order submission logic

        // update the inventory
        int updatedInventory = updateInventoryDto.getInventory() - items.getQuantity();

        SellerItem sellerItem = sellerItemRepository.findBySellerItemId(updateInventoryDto.getItemId());
        sellerItem.setInventory(updatedInventory);
        sellerItemRepository.save(sellerItem);
    }

    @CrossOrigin()

    @GetMapping("/item/{itemId}")
    public ResponseEntity<SellerItemDto> getSellerItemByItemId(@PathVariable("itemId") int itemId) {
        SellerItem sellerItem = sellerItemRepository.findBySellerItemId(itemId);


        if (sellerItem == null) {
            return ResponseEntity.notFound().build();
        }


        // Convert the SellerItem entity to its corresponding DTO
        SellerItemDto sellerItemDTO = new SellerItemDto();
        sellerItemDTO.setSellerItemId(sellerItem.getSellerItemId());
        sellerItemDTO.setDescription(sellerItem.getDescription());
        sellerItemDTO.setInventory(sellerItem.getInventory());
        sellerItemDTO.setSellerId(sellerItem.getSellerId());
        sellerItemDTO.setName(sellerItem.getName());
        sellerItemDTO.setPrice(sellerItem.getPrice());

        updateInventoryDto.setInventory(sellerItemDTO.getInventory());
        updateInventoryDto.setItemId(itemId);
        return ResponseEntity.ok(sellerItemDTO);
    }



}
