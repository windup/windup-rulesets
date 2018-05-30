<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns="http://windup.jboss.org/schema/jboss-ruleset"
    xmlns:windupfunctions="windupcustomfunctions"
    xmlns:windup="http://windup.jboss.org/schema/jboss-ruleset">

<xsl:param name="ruleIDPattern" />
<xsl:param name="testDataPath" />
<xsl:param name="testFileStub"/>
<xsl:param name="outputDirectory"/>
<xsl:variable name="rulesetId" select="/windup:ruleset/@id"/>

<xsl:output method="xml" indent="yes"/>


<xsl:template match="/" name="main">
    <ruletest id="{$rulesetId}-test" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://windup.jboss.org/schema/jboss-ruleset http://windup.jboss.org/schema/jboss-ruleset/windup-jboss-ruleset.xsd">

        <xsl:if test="string-length($testFileStub) > 0">
            <xsl:call-template name="generate-test-files" />
        </xsl:if>

        <testDataPath>
            <xsl:value-of select="concat('data/', $rulesetId)"/>
        </testDataPath>
        <ruleset>
            <rules>
                <xsl:for-each select="//windup:rules/windup:rule[matches(@id, $ruleIDPattern)]">
                    <rule id="{./@id}-test">
                        <when>
                            <not>
                                <xsl:for-each select="windup:perform/*">
                                    <xsl:choose>
                                        <xsl:when test="local-name(.) = 'classification'">
                                            <xsl:apply-templates select="."/>
                                        </xsl:when>
                                        <xsl:when test="local-name(.) = 'technology-tag'">
                                            <xsl:apply-templates select="."/>
                                        </xsl:when>
                                        <xsl:when test="local-name(.) = 'technology-identified'">
                                            <xsl:apply-templates select="."/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <fail name="Tag not recognized: {local-name(.)}"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:for-each>
                            </not>
                        </when>

                        <perform>
                            <fail message="Expected data not found for rule {./@id}" />
                        </perform>
                    </rule>
                </xsl:for-each>
            </rules>
        </ruleset>
    </ruletest>
</xsl:template>

<xsl:function name="windupfunctions:escapeForRegex">
    <xsl:param name="text"/>
    <xsl:value-of select="replace($text, '([(.*)])', '\\$1')"/>
</xsl:function>

<xsl:template match="windup:technology-identified">
    <xsl:variable name="textOriginal" select="./@name"/>
    <xsl:variable name="text" select="windupfunctions:escapeForRegex($textOriginal)"/>
    <technology-statistics-exists name="{$text}" number-found="1">
        <xsl:for-each select="./windup:tag">
            <tag name="{./@name}"/>
        </xsl:for-each>
    </technology-statistics-exists>
</xsl:template>

<xsl:template match="windup:technology-tag">
    <xsl:variable name="textOriginal" select="./text()"/>
    <xsl:variable name="text" select="windupfunctions:escapeForRegex($textOriginal)"/>
    <technology-tag-exists technology-tag="{$text}"/>
</xsl:template>

<xsl:template match="windup:classification">
    <xsl:variable name="classificationOriginal" select="@title"/>
    <xsl:variable name="classificationText" select="windupfunctions:escapeForRegex($classificationOriginal)"/>
    <classification-exists classification="{$classificationText}"/>
</xsl:template>

<xsl:template name="print-copy-command">
    <xsl:param name="fileElement"/>
    <xsl:variable name="filename" select="./@filename"/>
    <xsl:variable name="filenameGenerated" select="replace($filename, '\{\*\}', '')"/>
    <xsl:value-of select="concat('cp ', $testFileStub, ' ', $outputDirectory, '/data/', $rulesetId, '/', $filenameGenerated)"/><xsl:text>
</xsl:text>
</xsl:template>

<xsl:template name="generate-test-files">
<xsl:text disable-output-escaping="yes">
        &lt;!--
</xsl:text>
    <file-copy-commands>
<xsl:text>
</xsl:text>
<xsl:value-of select="concat('mkdir -p ', $outputDirectory, '/data/', $rulesetId)"/>
<xsl:text>
</xsl:text>
<xsl:for-each select="//windup:when//windup:file">
<xsl:call-template name="print-copy-command">
    <xsl:with-param name="fileElement" select="."/>
</xsl:call-template>
</xsl:for-each>
    </file-copy-commands>
    <xsl:text disable-output-escaping="yes">
        --&gt;
    </xsl:text>
</xsl:template>


</xsl:transform>
