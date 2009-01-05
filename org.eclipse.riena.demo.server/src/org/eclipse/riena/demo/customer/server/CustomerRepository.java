package org.eclipse.riena.demo.customer.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.demo.customer.common.CustomerRecordOverview;
import org.eclipse.riena.demo.customer.common.CustomerSearchBean;
import org.eclipse.riena.demo.customer.common.CustomerStatus;

/**
 * Implementation for accessing Customer Records
 */
public class CustomerRepository implements ICustomerRepository {

	public CustomerRecordOverview[] suche(CustomerSearchBean personValueObject) {
		List<CustomerRecordOverview> custList = new ArrayList<CustomerRecordOverview>();
		CustomerRecordOverview crv = new CustomerRecordOverview();
		// 1.
		crv.setLastName("Mundl");
		crv.setFirstName("Josef");
		crv.setBirthdate("01.01.1950");
		crv.setStreet("Musterstr.1");
		crv.setZipcode("12345");
		crv.setCity("Musterstadt");
		crv.setStatus(CustomerStatus.CUSTOMER);
		crv.setSalesrepno(100000);
		custList.add(crv);
		// 2.
		crv = new CustomerRecordOverview();
		crv.setLastName("Muster");
		crv.setFirstName("Robert");
		crv.setBirthdate("01.04.1955");
		crv.setStreet("Willibaldgasse");
		crv.setZipcode("12366");
		crv.setCity("Musterhaus");
		crv.setStatus(CustomerStatus.CUSTOMER);
		crv.setSalesrepno(100000);
		custList.add(crv);
		// 3.
		crv = new CustomerRecordOverview();
		crv.setLastName("Muster-Maier");
		crv.setFirstName("Trulli");
		crv.setBirthdate("21.06.1964");
		crv.setStreet("Willibaldgasse");
		crv.setZipcode("12345");
		crv.setCity("Musterstadt");
		crv.setStatus(CustomerStatus.CUSTOMER);
		crv.setSalesrepno(100000);
		custList.add(crv);
		// 4.
		crv = new CustomerRecordOverview();
		crv.setLastName("Mustermann");
		crv.setFirstName("Elfriede");
		crv.setBirthdate("01.04.1955");
		crv.setStreet("Musterstr.1");
		crv.setZipcode("12345");
		crv.setCity("Musterstadt");
		crv.setStatus(CustomerStatus.CUSTOMER);
		crv.setSalesrepno(100000);
		custList.add(crv);
		// 5.
		crv = new CustomerRecordOverview();
		crv.setLastName("Mustermann");
		crv.setFirstName("Ingo");
		crv.setBirthdate("01.01.1950");
		crv.setStreet("Musterstr.1");
		crv.setZipcode("12345");
		crv.setCity("Musterstadt");
		crv.setStatus(CustomerStatus.CUSTOMER);
		crv.setSalesrepno(100000);
		custList.add(crv);

		return custList.toArray(new CustomerRecordOverview[custList.size()]);
	}
}
