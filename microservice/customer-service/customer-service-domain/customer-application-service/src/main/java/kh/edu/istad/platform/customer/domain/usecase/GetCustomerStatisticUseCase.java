package kh.edu.istad.platform.customer.domain.usecase;


import kh.edu.istad.platform.customer.domain.dto.CustomerStatisticResult;
import kh.edu.istad.platform.customer.domain.port.out.CustomerStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;



@Component
@RequiredArgsConstructor
public class GetCustomerStatisticUseCase {
    private final CustomerStatisticsRepository customerStatisticsRepository;
    public Mono<CustomerStatisticResult> execute() {
        return customerStatisticsRepository.getStatistics();
    }

}