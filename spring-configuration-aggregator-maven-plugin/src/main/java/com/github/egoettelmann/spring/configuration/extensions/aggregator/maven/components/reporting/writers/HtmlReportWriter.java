package com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.components.reporting.writers;

import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.dto.OutputReport;
import com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.ArtifactMetadata;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Locale;

public class HtmlReportWriter extends AbstractReportWriter {

    public HtmlReportWriter(final Log log, final MavenProject project) {
        super(log, project);
    }

    @Override
    public boolean supports(final OutputReport outputReport) {
        return "html".equalsIgnoreCase(outputReport.getType());
    }

    @Override
    public void write(final OutputReport outputReport, final ArtifactMetadata metadata) throws Exception {
        final File outputFile = this.getOutputFile(outputReport.getOutputFile());
        if (outputFile.getParentFile().mkdirs()) {
            this.log.debug("Created folder " + outputFile.getParentFile());
        }

        // Configuring Freemarker
        final Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Loading template
        final String templateName;
        if (outputReport.getTemplateFile() != null) {
            final File reportFile = new File(outputReport.getTemplateFile());
            cfg.setDirectoryForTemplateLoading(reportFile.getParentFile());
            templateName = reportFile.getName();
        } else {
            cfg.setClassForTemplateLoading(this.getClass(), "/");
            templateName = "default.ftl";
        }
        final Template template = cfg.getTemplate(templateName);

        // Building template context
        final HashMap<String, Object> context = new HashMap<>();
        context.put("metadata", metadata);
        context.put("project", this.project);

        // Writing to file
        try (final FileWriter fileWriter = new FileWriter(outputFile, false)) {
            template.process(context, fileWriter);
        }
    }

    private File getOutputFile(final String outputFile) {
        if (StringUtils.isNotBlank(outputFile)) {
            return new File(outputFile);
        }

        return new File(this.project.getBuild().getDirectory() + "/apidocs/configurations.html");
    }

}
