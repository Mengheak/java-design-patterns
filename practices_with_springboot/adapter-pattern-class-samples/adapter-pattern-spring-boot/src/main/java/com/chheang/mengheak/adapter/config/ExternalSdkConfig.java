package com.chheang.mengheak.adapter.config;

import com.chheang.mengheak.adapter.external.aba.*;
import com.chheang.mengheak.adapter.external.stripe.*;
import com.chheang.mengheak.adapter.external.wing.*;
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
