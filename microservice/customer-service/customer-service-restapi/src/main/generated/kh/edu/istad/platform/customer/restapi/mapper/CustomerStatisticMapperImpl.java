package kh.edu.istad.platform.customer.restapi.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatisticResult;
import kh.edu.istad.platform.customer.domain.dto.SegmentTotal;
import kh.edu.istad.platform.customer.domain.dto.StatusCount;
import kh.edu.istad.platform.customer.restapi.dto.CustomerStatisticResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-21T00:14:04+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4 (JetBrains s.r.o.)"
)
@Component
public class CustomerStatisticMapperImpl implements CustomerStatisticMapper {

    @Override
    public CustomerStatisticResponse toResponse(CustomerStatisticResult result) {
        if ( result == null ) {
            return null;
        }

        long total = 0L;
        List<CustomerStatisticResponse.StatusCount> byStatus = null;
        List<CustomerStatisticResponse.SegmentTotal> bySegment = null;

        total = result.total();
        byStatus = statusCountListToStatusCountList( result.byStatus() );
        bySegment = segmentTotalListToSegmentTotalList( result.bySegment() );

        CustomerStatisticResponse customerStatisticResponse = new CustomerStatisticResponse( total, byStatus, bySegment );

        return customerStatisticResponse;
    }

    protected CustomerStatisticResponse.StatusCount statusCountToStatusCount(StatusCount statusCount) {
        if ( statusCount == null ) {
            return null;
        }

        String status = null;
        long total = 0L;

        status = statusCount.status();
        total = statusCount.total();

        CustomerStatisticResponse.StatusCount statusCount1 = new CustomerStatisticResponse.StatusCount( status, total );

        return statusCount1;
    }

    protected List<CustomerStatisticResponse.StatusCount> statusCountListToStatusCountList(List<StatusCount> list) {
        if ( list == null ) {
            return null;
        }

        List<CustomerStatisticResponse.StatusCount> list1 = new ArrayList<CustomerStatisticResponse.StatusCount>( list.size() );
        for ( StatusCount statusCount : list ) {
            list1.add( statusCountToStatusCount( statusCount ) );
        }

        return list1;
    }

    protected CustomerStatisticResponse.SegmentTotal segmentTotalToSegmentTotal(SegmentTotal segmentTotal) {
        if ( segmentTotal == null ) {
            return null;
        }

        String segment = null;
        long total = 0L;

        segment = segmentTotal.segment();
        total = segmentTotal.total();

        CustomerStatisticResponse.SegmentTotal segmentTotal1 = new CustomerStatisticResponse.SegmentTotal( segment, total );

        return segmentTotal1;
    }

    protected List<CustomerStatisticResponse.SegmentTotal> segmentTotalListToSegmentTotalList(List<SegmentTotal> list) {
        if ( list == null ) {
            return null;
        }

        List<CustomerStatisticResponse.SegmentTotal> list1 = new ArrayList<CustomerStatisticResponse.SegmentTotal>( list.size() );
        for ( SegmentTotal segmentTotal : list ) {
            list1.add( segmentTotalToSegmentTotal( segmentTotal ) );
        }

        return list1;
    }
}
