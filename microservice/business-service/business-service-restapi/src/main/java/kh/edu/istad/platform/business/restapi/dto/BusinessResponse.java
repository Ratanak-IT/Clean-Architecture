package kh.edu.istad.platform.business.restapi.dto;

import java.util.List;
import java.util.UUID;

public record BusinessResponse(
        UUID businessId,
        String legalName,
        String displayName,
        String email,
        String phoneNumber,
        boolean active,
        List<ProductResponse> products
) {
}
