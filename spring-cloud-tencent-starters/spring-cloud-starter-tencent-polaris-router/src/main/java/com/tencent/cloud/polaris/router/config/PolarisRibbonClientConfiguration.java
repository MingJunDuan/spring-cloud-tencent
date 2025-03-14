/*
 * Tencent is pleased to support the open source community by making Spring Cloud Tencent available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.cloud.polaris.router.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.tencent.cloud.polaris.router.PolarisRoutingLoadBalancer;
import com.tencent.cloud.polaris.router.rule.PolarisLoadBalanceRule;
import com.tencent.cloud.polaris.router.rule.PolarisWeightedRandomRule;
import com.tencent.polaris.router.api.core.RouterAPI;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Haotian Zhang
 */
@Configuration
public class PolarisRibbonClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IRule polarisRibbonRule(PolarisRibbonProperties polarisRibbonProperties) {
        switch (PolarisLoadBalanceRule.fromStrategy(polarisRibbonProperties.getPolicy())) {
            case WEIGHTED_RANDOM_RULE:
            default:
                return new PolarisWeightedRandomRule();
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public ILoadBalancer polarisRoutingLoadBalancer(IClientConfig iClientConfig, IRule iRule, IPing iPing,
                                                    ServerList<Server> serverList, RouterAPI polarisRouter) {
        return new PolarisRoutingLoadBalancer(iClientConfig, iRule, iPing, serverList, polarisRouter);
    }
}
