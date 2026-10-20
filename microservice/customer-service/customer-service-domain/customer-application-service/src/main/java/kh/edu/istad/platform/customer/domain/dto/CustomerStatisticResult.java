package kh.edu.istad.platform.customer.domain.dto;

import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;

import java.util.List;
import java.util.Map;

public record CustomerStatisticResult(
        long total,
        List<StatusCount> byStatus,
        List<SegmentTotal> bySegment) {

}