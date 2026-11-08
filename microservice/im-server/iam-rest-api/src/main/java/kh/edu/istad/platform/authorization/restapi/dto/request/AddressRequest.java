package kh.edu.istad.platform.authorization.restapi.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AddressRequest(
        @Size(max = 255) String streetAddress,
        @Size(max = 100) String locality,
        @Size(max = 100) String region,
        @Size(max = 20) String postalCode,
        @Size(max = 100) String country
) {
}

