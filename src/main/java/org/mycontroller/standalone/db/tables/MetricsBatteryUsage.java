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
package org.mycontroller.standalone.db.tables;

import org.mycontroller.standalone.db.DB_TABLES;
import org.mycontroller.standalone.metrics.MetricsUtils.AGGREGATION_TYPE;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.2
 */
@DatabaseTable(tableName = DB_TABLES.METRICS_BATTERY_USAGE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(includeFieldNames = true)
public class MetricsBatteryUsage {
    public static final String KEY_NODE_ID = "nodeId";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_AGGREGATION_TYPE = "aggregationType";

    @DatabaseField(canBeNull = false, foreign = true, uniqueCombo = true, columnName = KEY_NODE_ID)
    private Node node;

    @DatabaseField(uniqueCombo = true, canBeNull = false, columnName = KEY_TIMESTAMP)
    private Long timestamp;

    @DatabaseField(canBeNull = false)
    private Integer samples;

    @DatabaseField
    private Double min;

    @DatabaseField
    private Double max;

    @DatabaseField(canBeNull = false)
    private Double avg;

    @DatabaseField(uniqueCombo = true, dataType = DataType.ENUM_INTEGER, canBeNull = false, columnName = KEY_AGGREGATION_TYPE)
    private AGGREGATION_TYPE aggregationType;

    private Long timestampFrom;
    private Long timestampTo;

}
