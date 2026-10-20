package kh.edu.istad.platform.customer.restapi.dto;


import java.util.List;

public record CustomerStatisticResponse(
        long total,
        List<StatusCount> byStatus,
        List<SegmentTotal> bySegment) {

    public record StatusCount(String status, long total) {}
    public record SegmentTotal(String segment, long total) {}
}