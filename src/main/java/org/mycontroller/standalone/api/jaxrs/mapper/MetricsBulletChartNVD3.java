/**
 * Copyright (C) 2015-2016 Jeeva Kandasamy (jkandasa@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mycontroller.standalone.api.jaxrs.mapper;

import lombok.Getter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.3
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricsBulletChartNVD3 {
    private Integer id;
    private String title;
    private String subtitle;
    private Object ranges;
    private Object measures;
    private Object markers;
    private String resourceName;
    private String displayName;
}