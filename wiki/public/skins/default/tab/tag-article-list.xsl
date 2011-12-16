<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
    <xsl:template match="tag-article-list">
        <xsl:if test="count(list-entry) &gt; 0">
            <div id="tag-article-list">
                <ul>
                <xsl:for-each select="list-entry">
                    <li>
                    <xsl:choose>
                    <xsl:when test="@selected = 'yes'">
                        <xsl:value-of select="." />
                    </xsl:when>
                    <xsl:otherwise>
                    <a>
                        <xsl:attribute name="href">/tab/tag/wiki/<xsl:value-of select="." /></xsl:attribute>
                        <xsl:value-of select="." />
                    </a>
                    </xsl:otherwise>
                    </xsl:choose>
                    </li>
                </xsl:for-each>
                </ul>
            </div>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>