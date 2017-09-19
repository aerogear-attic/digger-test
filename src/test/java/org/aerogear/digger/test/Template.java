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
