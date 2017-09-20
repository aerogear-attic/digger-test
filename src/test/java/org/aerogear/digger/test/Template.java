/*
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aerogear.digger.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.json.JSONObject;

/**
 * Represents a template that is to be tested.
 * <p>
 * Please note that 2 templates are considered to be equal
 * if they have the same id.
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "templateId")
@ToString
public class Template {

    private String templateId;
    private String templateName;
    private String repoUrl;
    private String repoBranch;
    private String type;

    public Template(JSONObject jsonTemplate) {
        this(
                jsonTemplate.getString("id"),
                jsonTemplate.getString("name"),
                jsonTemplate.getString("repoUrl"),
                jsonTemplate.getString("repoBranch"),
                jsonTemplate.getString("type")
        );

        if (this.repoBranch.startsWith("refs/heads/")) {
            this.repoBranch = this.repoBranch.substring("refs/heads/".length());
        }
    }

}
