package com.pisethjava.adapter.demo;

import com.pisethjava.adapter.classadapter.*;

public final class ClassAdapterDemo {
	public static void run() {
		TemperatureReader r = new TemperatureClassAdapter();
		System.out.printf("\n4. CLASS ADAPTER: %.2f C%n", r.readCelsius());
	}
}
