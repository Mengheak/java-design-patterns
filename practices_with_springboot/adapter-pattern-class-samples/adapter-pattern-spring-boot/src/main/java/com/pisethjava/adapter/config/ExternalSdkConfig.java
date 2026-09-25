package com.pisethjava.adapter.config;

import com.pisethjava.adapter.external.aba.*;
import com.pisethjava.adapter.external.stripe.*;
import com.pisethjava.adapter.external.wing.*;
import org.springframework.context.annotation.*;

@Configuration
public class ExternalSdkConfig {
	@Bean
	AbaSdk abaSdk() {
		return new AbaSdk();
	}

	@Bean
	StripeSdk stripeSdk() {
		return new StripeSdk();
	}

	@Bean
	WingClient wingClient() {
		return new WingClient();
	}
}
