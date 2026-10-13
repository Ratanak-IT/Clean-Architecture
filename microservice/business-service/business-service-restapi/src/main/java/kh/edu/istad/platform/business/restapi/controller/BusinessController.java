package kh.edu.istad.platform.business.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.business.domain.usecase.GetBusinessUseCase;
import kh.edu.istad.platform.business.domain.usecase.RegisterBusinessUseCase;
import kh.edu.istad.platform.business.restapi.dto.BusinessResponse;
import kh.edu.istad.platform.business.restapi.dto.RegisterBusinessRequest;
import kh.edu.istad.platform.business.restapi.mapper.BusinessWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final RegisterBusinessUseCase registerBusinessUseCase;
    private final GetBusinessUseCase getBusinessUseCase;
    private final BusinessWebMapper mapper;


    @PostMapping
    public Mono<ResponseEntity<BusinessResponse>> register(@Valid @RequestBody RegisterBusinessRequest request) {
        return registerBusinessUseCase.execute(mapper.toCommand(request))
                .map(mapper::toResponse)
                .map(response -> ResponseEntity
                        .created(URI.create("/api/businesses/" + response.businessId()))
                        .body(response));
    }

    @GetMapping("/{businessId}")
    public Mono<BusinessResponse> getBusinessById(@PathVariable("businessId") UUID businessId) {
        return getBusinessUseCase.execute(businessId).map(mapper::toResponse);
    }
}
