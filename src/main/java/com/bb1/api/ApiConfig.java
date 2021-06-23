package com.bb1.api;

import com.bb1.api.config.Config;
import com.bb1.api.config.Storable;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * The config for the main api
 */
public class ApiConfig extends Config {

	public ApiConfig() {
		super("bfapi");
	}
	
	@Storable public boolean debugMode = false;
	
}
