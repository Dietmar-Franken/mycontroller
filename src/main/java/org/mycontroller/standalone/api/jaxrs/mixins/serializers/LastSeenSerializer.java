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
package org.mycontroller.standalone.api.jaxrs.mixins.serializers;

import java.io.IOException;

import org.mycontroller.standalone.MycUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.2
 */
public class LastSeenSerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long timestamp, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        if (timestamp != null) {
            jgen.writeStartObject();
            provider.defaultSerializeField("inString", MycUtils.getDifferenceFriendlyTime(timestamp), jgen);
            provider.defaultSerializeField("timestamp", timestamp, jgen);
            jgen.writeEndObject();
        } else {
            jgen.writeString("Never");
        }
    }

}
