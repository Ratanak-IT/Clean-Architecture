package kh.edu.istad.platform.business.domain.service;

import kh.edu.istad.platform.business.domain.entity.Business;
import kh.edu.istad.platform.business.domain.event.BusinessRegisteredEvent;
import kh.edu.istad.platform.business.domain.event.OrderApprovalEvent;
import kh.edu.istad.platform.business.domain.event.OrderApprovedEvent;
import kh.edu.istad.platform.business.domain.event.OrderRejectedEvent;
import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class BusinessDomainServiceImpl implements BusinessDomainService {

    @Override
    public BusinessRegisteredEvent validateAndInitiateBusiness(Business business) {
        if (business == null) {
            throw new BusinessDomainException("Business must not be null");
        }
        business.initializeBusiness();                 // validates, then id + ACTIVE
        return new BusinessRegisteredEvent(business, now());
    }

    @Override
    public OrderApprovalEvent validateOrder(Business business, List<String> failureMessages) {
        if (business == null) {
            throw new BusinessDomainException("Business must not be null");
        }
        business.validateOrder(failureMessages);
        if (failureMessages.isEmpty()) {
            business.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(business.getOrderApproval(), business.getId(), failureMessages, now());
        }
        business.constructOrderApproval(OrderApprovalStatus.REJECTED);
        return new OrderRejectedEvent(business.getOrderApproval(), business.getId(), failureMessages, now());
    }

    private ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.of("UTC"));
    }
}
