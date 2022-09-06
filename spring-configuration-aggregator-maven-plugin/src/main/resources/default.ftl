<#ftl output_format="HTML">
<#-- @ftlvariable name="metadata" type="com.github.egoettelmann.spring.configuration.extensions.aggregator.maven.core.model.ArtifactMetadata" -->
<#-- @ftlvariable name="project" type="org.apache.maven.project.MavenProject" -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <style type="text/css">
        body {
            font-family: 'Arial', sans-serif;
        }

        a {
            color: #1565c0;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        code {
            background: rgba(27,31,35,.05);
            border-radius: 0.25em;
            color: #191e1e;
            font-size: .95em;
            padding: 0.125em 0.25em;
        }

        a code {
            color: #1565c0;
        }

        table caption {
            color: rgb(39, 48, 48);
            font-size: 14px;
            font-style: italic;
            font-weight: 400;
            hyphens: none;
            letter-spacing: .01em;
            padding-bottom: 0.075rem;
            text-align: left;
        }

        table th {
            border-bottom: 2.5px solid rgb(234, 237, 240);
            text-align: left;
        }

        table td span {
            font-size: 15px;
        }

        .h1 {
            color: #141818;
            font-weight: 600;
            hyphens: none;
            line-height: 1.3;
            margin: 0 0 1.3rem 0;
            padding-top: 1.8rem;
            font-size: 2.3em;
        }
    </style>
</head>
<body>
<div class="h1">${(project.description)!"Project overview"}.</div>

<table class="overviewSummary" border="0" cellpadding="3" cellspacing="0"
       summary="Configuration properties table, listing metadata">
    <caption><span>Configuration properties</span><span class="tabEnd">&nbsp;</span></caption>
    <tr>
        <th class="colFirst" scope="col">Name</th>
        <th class="colFirst" scope="col">Type and Description</th>
        <th class="colLast" scope="col">Default Value</th>
    </tr>
    <tbody>
    <#if metadata.properties?size == 0>
        <tr class="altColor">
            <td colspan="3" class="colFirst">No configuration properties found</td>
        </tr>
    <#else>
        <#list metadata.properties as property>
            <tr class="${property?is_even_item?then('altColor','rowColor')}">
                <td class="colFirst">
                    <small>
                        <code>${property.name}</code>
                    </small>
                </td>
                <td class="colFirst">
                    <small>
                        <code>${(property.type)!}</code>
                    </small>
                    <br/>
                    <span>${(property.description)!}</span>
                </td>
                <td class="colLast">
                    <#if property.defaultValue?has_content>
                        <small>
                            <code>${(property.defaultValue)!}</code>
                        </small>
                    </#if>
                </td>
            </tr>
        </#list>
    </#if>
    </tbody>
</table>

<#if metadata.changes??>
    <table class="overviewChanges" border="0" cellpadding="3" cellspacing="0"
           summary="Configuration properties table, listing metadata">
    <caption><span>Configuration properties changes from version <span>${(metadata.changes.baseVersion)!}</span></span><span class="tabEnd">&nbsp;</span></caption>
    <tr>
        <th class="colFirst" scope="col">Name</th>
        <th class="colLast" scope="col">Change type</th>
    </tr>
    <tbody>
        <#list metadata.changes.added as property>
            <tr class="${property?is_even_item?then('altColor','rowColor')}">
                <td class="colFirst">
                    <small>
                        <code>${property}</code>
                    </small>
                </td>
                <td class="colLast">ADDED</td>
            </tr>
        </#list>
        <#list metadata.changes.updated as property>
            <tr class="${property?is_even_item?then('altColor','rowColor')}">
                <td class="colFirst">
                    <small>
                        <code>${property}</code>
                    </small>
                </td>
                <td class="colLast">UPDATED</td>
            </tr>
        </#list>
        <#list metadata.changes.deleted as property>
            <tr class="${property?is_even_item?then('altColor','rowColor')}">
                <td class="colFirst">
                    <small>
                        <code>${property}</code>
                    </small>
                </td>
                <td class="colLast">DELETED</td>
            </tr>
        </#list>
    </tbody>
    </table>
</#if>
</body>
</html>
