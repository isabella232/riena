package org.eclipse.riena.internal.demo.client;

import java.util.List;

import org.eclipse.riena.demo.common.Customer;
import org.eclipse.riena.demo.common.ICustomerService;

public class RemoteClientTest {

	public void bind(ICustomerService customerService) {
		System.out.println("remoteclienttest: customer service bound");
		List<Customer> searchResult = customerService.search("M");
		for (Customer c : searchResult) {
			System.out.println(c.getLastName() + ", " + c.getFirstName());
		}
	}

	public void unbind(ICustomerService customerService) {
		System.out.println("remoteclientest: customer service unbound");

	}

}
