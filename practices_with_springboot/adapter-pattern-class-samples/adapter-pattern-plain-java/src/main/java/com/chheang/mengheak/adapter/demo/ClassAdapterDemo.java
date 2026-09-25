package com.chheang.mengheak.adapter.demo;

import com.chheang.mengheak.adapter.classadapter.*;

public final class ClassAdapterDemo {
	public static void run() {
		TemperatureReader r = new TemperatureClassAdapter();
		System.out.printf("\n4. CLASS ADAPTER: %.2f C%n", r.readCelsius());
	}
}
