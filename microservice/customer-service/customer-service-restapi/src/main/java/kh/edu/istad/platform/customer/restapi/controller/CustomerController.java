package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.common.domain.query.FilterQuery;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.*;
import kh.edu.istad.platform.customer.domain.usecase.*;
import kh.edu.istad.platform.customer.restapi.dto.*;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerStatisticMapper;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Filter;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
//    private final InitiateCustomerUseCase initiateCustomerUseCase;
//    private final DeactiveCustomerUseCase deactiveCustomerUseCase;
//    private final UpdateCustomerUseCase updateCustomerUseCase;
//    private final GetCustomerByIdUseCase getCustomerByIdUseCase;
    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final DeleteCustomerByIdUseCase deleteCustomerByIdUseCase;
    private final FindByUserNameUseCase findByUserNameUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;
    private final FilterCustomerUseCase filterCustomerUseCase;
    private final UpdateCustomerByIdUseCase updateCustomerByIdUseCase;
    private final GetCustomerStatisticUseCase statisticUseCase;

    private final CustomerWebMapper customerWebMapper;
    private final CustomerStatisticMapper customerStatisticMapper;

//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    public CustomerInitiateResponse initiateCustomer(@Valid @RequestBody CustomerInitiateRequest request){
//        InitiateCustomerResult result = initiateCustomerUseCase.execute(customerWebMapper.toCommand(request));
//        return customerWebMapper.toResponse(result);
//    }

    @PostMapping("/create")
    public Mono<CreateCustomerResponse> createCustomer(@RequestBody @Valid CreateCustomerRequest request){
        return createCustomerUseCase.execute(customerWebMapper.toCommand(request))
                .map(customerWebMapper::toResponse);
    }

    @GetMapping
    public Flux<CustomerGetResponse> findAll(){
        return getCustomerUseCase.execute().map(customerWebMapper::toResponse);
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerGetResponse> findById(@PathVariable("customerId") UUID customerId){
        return getCustomerByIdUseCase.execute(new CustomerId(customerId))
                .map(customerWebMapper::toResponse);
    }

    @GetMapping("/username/{userName}")
    public Mono<CustomerGetResponse> findByUserName(@PathVariable("userName") String userName){
        return findByUserNameUseCase.execute(userName).map(customerWebMapper::toResponse);
    }
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{customerId}")
    public Mono<Void> deleteById(@PathVariable("customerId") UUID customerId){
        return deleteCustomerByIdUseCase.execute(new CustomerId(customerId));
    }

    @PatchMapping("/{customerId}")
    public Mono<CustomerUpdateResponse> updateById(@PathVariable("customerId") UUID customerId,
                                                   @Valid @RequestBody CustomerUpdateByIdRequest request) {
        return updateCustomerByIdUseCase.execute(new CustomerId(customerId), customerWebMapper.toCommand(request))
                .map(customerWebMapper::toResponse);
    }

    @PostMapping("/search")
    public Flux<CustomerGetResponse> filter(@RequestBody FilterQuery filterQuery) {
        return filterCustomerUseCase.execute(filterQuery)
                .map(customerWebMapper::toResponse);
    }

        @GetMapping("/statistic")
        public Mono<CustomerStatisticResponse> statistic() {
            return statisticUseCase.execute().map(customerStatisticMapper::toResponse);
        }

//    @ResponseStatus(HttpStatus.OK)
//    @PatchMapping("/{customerId}/deactive")
//    public CustomerDeactiveResponse deactiveCustomer(@PathVariable("customerId") UUID customerId){
//        DeactiveCustomerCommand command = new DeactiveCustomerCommand(customerId);
//        DeactiveCustomerResult result = deactiveCustomerUseCase.execute(command);
//        return customerWebMapper.toResponse(result);
//
//    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @PatchMapping("/{customerId}")
//    public CustomerUpdateResponse updateCustomer(@PathVariable("customerId") UUID customerId,@Valid @RequestBody CustomerUpdateRequest request){
//        UpdateCustomerCommand command = customerWebMapper.toCommand(customerId, request);
//        UpdateCustomerResult result = updateCustomerUseCase.execute(command);
//        return customerWebMapper.toResponse(result);
//    }
//
//    @GetMapping("/{customerId}")
//    public CustomerLookupResponse getCustomerById(@PathVariable("customerId") UUID customerId){
//        Customer customer = getCustomerByIdUseCase.execute(customerId);
//
//        return new CustomerLookupResponse(customer.getId().value());
//    }
}