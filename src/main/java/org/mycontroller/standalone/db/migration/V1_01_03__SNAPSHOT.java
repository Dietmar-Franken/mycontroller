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
package org.mycontroller.standalone.db.migration;

import java.sql.Connection;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.mycontroller.standalone.settings.MyControllerSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.3
 */
public class V1_01_03__SNAPSHOT extends MigrationBase implements JdbcMigration {
    private static final Logger _logger = LoggerFactory.getLogger(V1_01_03__SNAPSHOT.class.getName());

    @Override
    public void migrate(Connection connection) throws Exception {
        _logger.debug("Migration triggered.");

        //Load dao's
        updateDao();

        /** Migration comments
         *  Description: added new component. dashboard limit
         *  1. Dashboard limit. set default to 5
         **/

        //Migration #1
        //Dashboard limit set default to 5
        MyControllerSettings.builder().dashboardLimit(5).build().save();

        _logger.info("Migration completed successfully.");
    }
}
