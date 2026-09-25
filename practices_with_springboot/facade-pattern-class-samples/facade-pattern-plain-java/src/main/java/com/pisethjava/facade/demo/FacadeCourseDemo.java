package com.pisethjava.facade.demo;

import java.math.*;
import java.time.*;
import java.util.*;
import com.pisethjava.facade.order.client.*;
import com.pisethjava.facade.order.domain.*;
import com.pisethjava.facade.order.facade.*;
import com.pisethjava.facade.order.payment.*;
import com.pisethjava.facade.order.subsystem.*;
import com.pisethjava.facade.booking.domain.*;
import com.pisethjava.facade.booking.facade.*;
import com.pisethjava.facade.booking.subsystem.*;

public final class FacadeCourseDemo {
	public static void main(String[] args) {
		var c = new CustomerService();
		var v = new OrderValidator();
		var i = new InventoryService();
		var p = new PaymentService(List.of(new AbaPaymentGateway()));
		var s = new ShippingService();
		var inv = new InvoiceService();
		var n = new NotificationService();
		var a = new AuditService();
		var req = new OrderRequest("ORDER-1", "CUSTOMER-1", List.of("ROOM"), new BigDecimal("100.00"), "ABA");
		System.out.println("=== BEFORE FACADE ===");
		System.out.println(new BeforeFacadeOrderController(c, v, i, p, s, inv, n, a).placeOrder(req));
		System.out.println("=== AFTER FACADE ===");
		System.out.println(new OrderController(new OrderFacade(c, v, i, p, s, inv, n, a)).placeOrder(req));
		/*
		var bookingReq = new BookingRequest("GUEST-1", "ROOM-101", LocalDate.of(2026, 9, 10), LocalDate.of(2026, 9, 13),
				"TOKEN");
		var facade = new BookingFacade(new GuestService(), new RoomAvailabilityService(), new PricingService(),
				new RoomReservationService(), new BookingPaymentService(), new BookingWriter(false),
				new BookingNotificationService(), new BookingAuditService(), new LoyaltyService());
		System.out.println(facade.book(bookingReq));
		try {
			new CompensatingBookingFacade(new GuestService(), new RoomAvailabilityService(), new PricingService(),
					new RoomReservationService(), new BookingPaymentService(), new BookingWriter(true),
					new BookingNotificationService(), new BookingAuditService(), new LoyaltyService()).book(bookingReq);
		} catch (Exception e) {
			System.out.println("Expected failure: " + e.getMessage());
		}
		*/
	}
}
