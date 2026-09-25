package com.pisethjava.adapter.external.aba;

public class AbaSdk {
	public AbaResponse submit(String a, double n, String c, String r) {
		return new AbaResponse("ABA-" + r, AbaStatus.APPROVED, "Approved by ABA SDK");
	}
}
