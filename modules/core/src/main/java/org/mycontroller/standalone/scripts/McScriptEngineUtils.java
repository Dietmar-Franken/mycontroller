/*
 * Copyright 2015-2016 Jeeva Kandasamy (jkandasa@gmail.com)
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mycontroller.standalone.scripts;

import java.io.File;
import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.io.FileUtils;
import org.mycontroller.standalone.AppProperties;
import org.mycontroller.standalone.scripts.api.McScriptApi;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.3
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class McScriptEngineUtils {
    private static ScriptEngineManager scriptEngineManager = null;
    public static final String MC_API = "mcApi";
    public static final String MC_SCRIPT_RESULT = "mcResult";

    public static synchronized ScriptEngineManager getScriptEngineManager() {
        if (scriptEngineManager == null) {
            scriptEngineManager = new ScriptEngineManager();
        }
        return scriptEngineManager;
    }

    public static File getScriptFile(String scriptFileName) throws IllegalAccessException, IOException {
        File scriptFile = FileUtils.getFile(AppProperties.getInstance().getScriptLocation() + scriptFileName);
        String scriptCanonicalPath = scriptFile.getCanonicalPath();
        String scriptLocation = FileUtils.getFile(AppProperties.getInstance().getScriptLocation())
                .getCanonicalPath();
        //Check is file available and has access to read
        if (!scriptFile.exists() || !scriptFile.canRead()) {
            throw new IllegalAccessException("Unable to access this file '" + scriptCanonicalPath + "'!");
        }
        //Check file location inside scripts location
        if (!scriptCanonicalPath.startsWith(scriptLocation)) {
            throw new IllegalAccessException("Selected file is not under script location! '" + scriptCanonicalPath
                    + "'!");
        }
        return scriptFile;
    }

    //Load mc api details
    public static void updateMcApi(ScriptEngine engine) {
        engine.put(MC_API, new McScriptApi());
    }

    public enum SCRIPT_TYPE {
        CONDITION("Condition"),
        OPERATION("Operation");

        private final String name;

        private SCRIPT_TYPE(String name) {
            this.name = name;
        }

        public String getText() {
            return this.name;
        }

        public static SCRIPT_TYPE get(int id) {
            for (SCRIPT_TYPE type : values()) {
                if (type.ordinal() == id) {
                    return type;
                }
            }
            throw new IllegalArgumentException(String.valueOf(id));
        }

        public static SCRIPT_TYPE fromString(String text) {
            if (text != null) {
                for (SCRIPT_TYPE type : SCRIPT_TYPE.values()) {
                    if (text.equalsIgnoreCase(type.getText())) {
                        return type;
                    }
                }
            }
            return null;
        }
    }

}
