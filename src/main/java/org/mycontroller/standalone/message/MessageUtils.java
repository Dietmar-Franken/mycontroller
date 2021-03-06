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
package org.mycontroller.standalone.message;

import org.mycontroller.standalone.ObjectFactory;
import org.mycontroller.standalone.db.DaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.2
 */
public class MessageUtils {
    private static Logger _logger = LoggerFactory.getLogger(MessageUtils.class.getName());

    private MessageUtils() {

    }

    public synchronized static void sendMessgaeToGateway(RawMessage rawMessage) {
        //Send message to nodes [going out from MyController]
        try {
            if (ObjectFactory.getGateway(rawMessage.getGatewayId()) != null) {
                ObjectFactory.getGateway(rawMessage.getGatewayId()).write(rawMessage);
            } else {
                _logger.error("Selected gateway not available! Gateway:[{}]",
                        DaoUtils.getGatewayDao().getById(rawMessage.getGatewayId()));
            }
        } catch (Exception ex) {
            _logger.error("Message send failed! Message:[{}]", rawMessage, ex);
        }
    }
}