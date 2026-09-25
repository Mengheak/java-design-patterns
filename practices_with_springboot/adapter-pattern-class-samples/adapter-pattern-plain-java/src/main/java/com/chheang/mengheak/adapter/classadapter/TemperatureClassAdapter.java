package com.chheang.mengheak.adapter.classadapter;

public class TemperatureClassAdapter extends LegacyTemperatureSensor implements TemperatureReader {
	public double readCelsius() {
		return (readFahrenheit() - 32) * 5 / 9;
	}
}
