package org.aerogear.digger.test;

public class Template {

    private String templateId;
    private String templateName;
    private String repoUrl;
    private String repoBranch;

    public Template(String templateId, String templateName, String repoUrl, String repoBranch) {
        super();
        this.templateId = templateId;
        this.templateName = templateName;
        this.repoUrl = repoUrl;
        this.repoBranch = repoBranch;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getRepoBranch() {
        return repoBranch;
    }

}
