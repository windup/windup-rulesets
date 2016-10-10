﻿<xsl:stylesheet version="1.0"
   xmlns="http://windup.jboss.org/v1/xml"
   xmlns:windup="http://windup.jboss.org/schema/jboss-ruleset"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   exclude-result-prefixes="windup"
>
    <xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>
    <!--xsl:strip-space elements="windup:message windup:javaclass windup:xmlfile windup:file windup:filecontent" /-->
    <xsl:strip-space elements="*" />

    <xsl:template match="/">
        <xsl:apply-templates select="//windup:rule"/>
    </xsl:template>

    <xsl:template match="windup:rule">
        <!-- print rule ID -->
        <xsl:variable name="ruleID" select="@id"/>
        <rule>
            <column name="id">
                <xsl:value-of select="$ruleID"/>
            </column>

            <!-- Print type of condition(s) -->
            <xsl:for-each select=".//windup:when">
                <xsl:apply-templates />
            </xsl:for-each>
            <!-- print what is in PERFORM -->
            <xsl:for-each select=".//windup:perform">
                <xsl:apply-templates />
            </xsl:for-each>
        </rule>      
    </xsl:template>

  <xsl:template match="windup:javaclass|windup:filecontent|windup:xmlfile|windup:file|windup:project">
    <column name="condition">
        <xsl:value-of select="local-name()"/>
    </column>
    <column name="condition-content">
      <!-- rewrite elements in condition  -->
      <xsl:for-each select=".">
        <xsl:element name="{local-name()}">
          <!-- process attributes -->
          <xsl:for-each select="@*">
            <!-- remove attribute prefix -->
            <xsl:attribute name="{local-name()}">
              <xsl:value-of select="."/>
            </xsl:attribute>
          </xsl:for-each>
          <!-- print also nested nodes with attributes -->
          <xsl:for-each select="node()">
            <xsl:copy-of select="."/>
          </xsl:for-each>
        </xsl:element>
      </xsl:for-each>
    </column>
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="windup:location">
    <column name="location">
      <xsl:value-of select="text()"/>
    </column>
  </xsl:template>

  <xsl:template match="windup:hint">
    <column name="hint-title">
        <xsl:value-of select="@title" />
    </column>
    <column name="hint-message">
        <xsl:value-of select="./windup:message" />
    </column>
    <xsl:if test="count(//windup:quickfix)>0">
      <column name="quickfix"> <xsl:value-of select="./windup:quickfix/@type"/></column>
    </xsl:if>
  </xsl:template>

  <xsl:template match="windup:classification">
    <column name="classification">
        <xsl:value-of select="@title" />
    </column>
  </xsl:template>

  <!-- operators in when -->
  <xsl:template match="windup:or|windup:and|windup:not">
    <xsl:apply-templates />
  </xsl:template>

</xsl:stylesheet>
