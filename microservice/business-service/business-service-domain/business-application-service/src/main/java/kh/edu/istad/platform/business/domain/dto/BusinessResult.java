package kh.edu.istad.platform.business.domain.dto;

import java.util.List;
import java.util.UUID;

public record BusinessResult(
        UUID businessId,
        String legalName,
        String displayName,
        String email,
        String phoneNumber,
        boolean active,
        List<ProductResult> products
) {
}
