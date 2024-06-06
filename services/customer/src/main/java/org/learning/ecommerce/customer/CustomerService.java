package org.learning.ecommerce.customer;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.learning.ecommerce.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository repository;
	private final CustomerMapper mapper;

	public String createCustomer(CustomerRequest request) {
		var customer = repository.save(mapper.toCustomer(request));
		return customer.getId();
	}

	public void updateCustomer(CustomerRequest request) {
		var customer = repository.findById(request.id())
				.orElseThrow(() -> new CustomerNotFoundException(
                     format("Cannot update customer:: No customer found with id: %s", request.id())
				));

		mergerCustomer(customer, request);
		repository.save(customer);
	}

	private void mergerCustomer(Customer customer, CustomerRequest request) {
		if (StringUtils.isNotBlank(request.firstName())){
			customer.setFirstName(request.firstName());
		}

		if (StringUtils.isNotBlank(request.lastName())){
			customer.setLastName(request.lastName());
		}

		if (StringUtils.isNotBlank(request.email())){
			customer.setEmail(request.email());
		}

		if (request.address() != null){
			customer.setAddress(request.address());
		}
	}
}
