<xsl:stylesheet version="1.0"
   xmlns="http://windup.jboss.org/v1/xml"
   xmlns:windup="http://windup.jboss.org/schema/jboss-ruleset"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   exclude-result-prefixes="windup"
>
    <xsl:output method="html"/>

    <xsl:param name="filename"/>

    <xsl:template match="/">
        <xsl:apply-templates select="//windup:ruleset"/>
    </xsl:template>

    <xsl:template match="windup:ruleset">
        <table border="1">
            <thead>
                <tr>
                    <th colspan="9" style="background-color: green">
                        <xsl:value-of select="$filename"/>
                    </th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td colspan="9">
                        Source Technologies:
                        <xsl:for-each select="sourceTechnology">
                            <xsl:value-of select="@id"/>
                            <xsl:value-of select="' '"/>
                            <xsl:value-of select="@versionRange"/>
                        </xsl:for-each>
                    </td>
                </tr>
                <tr>
                    <td colspan="9">
                        Target Technologies:
                        <xsl:for-each select="sourceTechnology">
                            <xsl:value-of select="@id"/>
                            <xsl:value-of select="' '"/>
                            <xsl:value-of select="@versionRange"/>
                        </xsl:for-each>
                    </td>
                </tr>
                <tr>
                    <td colspan="9">
                        <xsl:value-of select="windup:description"/>
                    </td>
                </tr>
                <xsl:apply-templates select="//windup:rule"/>
            </tbody>
        </table>
    </xsl:template>

    <xsl:template match="windup:rule">

        <xsl:variable name="ruleID" select="@id"/>

        <tr>
            <td>
                <xsl:value-of select="$ruleID"/>
            </td>

            <td>
                <xsl:for-each select=".//windup:hint">
                    <xsl:for-each select=".//windup:link">
                        <div>
                            <xsl:value-of select="@href"/>
                        </div>
                    </xsl:for-each>
                </xsl:for-each>
            </td>

            <td>
                <xsl:for-each select=".//windup:classification">
                    <xsl:for-each select=".//windup:link">
                        <div>
                            <xsl:value-of select="@href"/>
                        </div>
                    </xsl:for-each>
                </xsl:for-each>
            </td>

            <td>
                <xsl:for-each select=".//windup:hint">
                    <div>
                        <xsl:value-of select="@title"/>
                    </div>
                    <div>
                        <xsl:value-of select="windup:message"/>
                    </div>
                </xsl:for-each>
            </td>

            <td>
                <xsl:for-each select=".//windup:classification">
                    <div>
                        <xsl:value-of select="@title"/>
                    </div>
                    <div>
                        <xsl:value-of select="windup:description"/>
                    </div>
                </xsl:for-each>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
