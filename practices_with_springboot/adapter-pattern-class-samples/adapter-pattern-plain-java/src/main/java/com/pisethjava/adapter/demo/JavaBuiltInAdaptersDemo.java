package com.pisethjava.adapter.demo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class JavaBuiltInAdaptersDemo {
	public static void run() throws Exception {
		System.out.println("\n5. JAVA BUILT-IN ADAPTERS");
		Reader r = new InputStreamReader(new ByteArrayInputStream("Adapter".getBytes(StandardCharsets.UTF_8)),
				StandardCharsets.UTF_8);
		System.out.println("InputStreamReader: " + (char) r.read());
		List<String> list = Arrays.asList("ABA", "Wing", "Stripe");
		System.out.println("Arrays.asList: " + list);
		Enumeration<String> e = Collections.enumeration(list);
		while (e.hasMoreElements())
			System.out.print(e.nextElement() + " ");
		System.out.println();
	}
}
