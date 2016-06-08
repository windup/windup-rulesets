<xsl:stylesheet version="1.0"
   xmlns="http://windup.jboss.org/v1/xml"
   xmlns:windup="http://windup.jboss.org/schema/jboss-ruleset"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   exclude-result-prefixes="windup"
>
    <xsl:output method="xml" omit-xml-declaration="yes"/>

    <xsl:template match="/">
        <ROOT>
            <xsl:apply-templates select="//windup:rule"/>
        </ROOT>
    </xsl:template>

    <xsl:template match="windup:rule">

        <xsl:variable name="ruleID" select="@id"/>
<xsl:text>
</xsl:text>
        <rule>
            <column name="id">
                <xsl:value-of select="$ruleID"/>
            </column>
<xsl:text>
</xsl:text>
            <xsl:for-each select=".//windup:hint">
                <xsl:for-each select=".//windup:link">
                    <column name="link">
                        <xsl:value-of select="@href"/>
                    </column>
<xsl:text>
</xsl:text>
                </xsl:for-each>
            </xsl:for-each>
            <xsl:for-each select=".//windup:classification">
                <xsl:for-each select=".//windup:link">
                    <column name="link">
                        <xsl:value-of select="@href"/>
                    </column>
<xsl:text>
</xsl:text>
                </xsl:for-each>
            </xsl:for-each>
            <xsl:for-each select=".//windup:hint">
<xsl:text>
</xsl:text>
                <column name="title">
                    <xsl:value-of select="@title"/>
                </column>
<xsl:text>
</xsl:text>
                <column name="contents">
                    <xsl:value-of select="windup:message"/>
                </column>
            </xsl:for-each>
            <xsl:for-each select=".//windup:classification">
<xsl:text>
</xsl:text>
                <column name="title">
                    <xsl:value-of select="@title"/>
                </column>
<xsl:text>
</xsl:text>
                <column name="contents">
                    <xsl:value-of select="windup:description"/>
                </column>
            </xsl:for-each>
        </rule>
<xsl:text>
</xsl:text>
    </xsl:template>
</xsl:stylesheet>
