package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.customer.domain.usecase.*;
import kh.edu.istad.platform.customer.restapi.dto.CreateCustomerSegmentRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerSegmentResponse;
import kh.edu.istad.platform.customer.restapi.dto.UpdateCustomerSegmentRequest;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerSegmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/segments")
@RequiredArgsConstructor
public class CustomerSegmentController {
    private final CreateCustomerSegmentUseCase createUseCase;
    private final GetCustomerSegmentsUseCase getAllUseCase;
    private final GetCustomerSegmentByIdUseCase getByIdUseCase;
    private final UpdateCustomerSegmentUseCase updateUseCase;
    private final DeleteCustomerSegmentUseCase deleteUseCase;
    private final CustomerSegmentMapper mapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<CustomerSegmentResponse> create(@Valid @RequestBody CreateCustomerSegmentRequest request) {
        return createUseCase.execute(mapper.toCommand(request)).map(mapper::toResponse);
    }

    @GetMapping
    public Flux<CustomerSegmentResponse> findAll() {
        return getAllUseCase.execute().map(mapper::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<CustomerSegmentResponse> findById(@PathVariable String id) {
        return getByIdUseCase.execute(id).map(mapper::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<CustomerSegmentResponse> update(@PathVariable String id,
                                                @Valid @RequestBody UpdateCustomerSegmentRequest request) {
        return updateUseCase.execute(id, mapper.toCommand(request)).map(mapper::toResponse);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return deleteUseCase.execute(id);
    }
}