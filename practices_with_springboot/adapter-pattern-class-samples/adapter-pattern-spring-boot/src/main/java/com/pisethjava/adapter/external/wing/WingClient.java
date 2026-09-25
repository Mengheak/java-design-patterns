package com.pisethjava.adapter.external.wing;

import java.math.BigDecimal;

public class WingClient {
	public WingResponse transfer(String p, BigDecimal a, String n) {
		return new WingResponse(0, "WING-" + n, "Completed by Wing");
	}
}
