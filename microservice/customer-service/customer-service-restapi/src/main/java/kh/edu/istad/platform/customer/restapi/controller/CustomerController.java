package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.customer.domain.dto.*;
import kh.edu.istad.platform.customer.domain.usecase.DeactiveCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.UpdateCustomerUseCase;
import kh.edu.istad.platform.customer.restapi.dto.*;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import kh.edu.istad.platform.customer.domain.usecase.InitiateCustomerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final InitiateCustomerUseCase initiateCustomerUseCase;
    private final DeactiveCustomerUseCase deactiveCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final CustomerWebMapper customerWebMapper;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CustomerInitiateResponse initiateCustomer(@Valid @RequestBody CustomerInitiateRequest request){
        InitiateCustomerResult result = initiateCustomerUseCase.execute(customerWebMapper.toCommand(request));
        return customerWebMapper.toResponse(result);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{customerId}/deactive")
    public CustomerDeactiveResponse deactiveCustomer(@PathVariable("customerId") UUID customerId){
        DeactiveCustomerCommand command = new DeactiveCustomerCommand(customerId);
        DeactiveCustomerResult result = deactiveCustomerUseCase.execute(command);
        return customerWebMapper.toResponse(result);

    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{customerId}")
    public CustomerUpdateResponse updateCustomer(@PathVariable("customerId") UUID customerId,@Valid @RequestBody CustomerUpdateRequest request){
        UpdateCustomerCommand command = customerWebMapper.toCommand(customerId, request);
        UpdateCustomerResult result = updateCustomerUseCase.execute(command);
        return customerWebMapper.toResponse(result);
    }

}