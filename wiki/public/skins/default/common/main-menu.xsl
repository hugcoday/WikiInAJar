<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="main-menu">
		<table cellspacing="0" cellpadding="0" border="0"
			style="padding: 0; margin: 0; border: 0">
			<tr>
				<td width="5px">
					<img src="/public/skins/default/common/ctl.png"
						height="24" />
				</td>
				<td style="padding-left: 10px"></td>
				<xsl:apply-templates select="home-menu-entry" />
				<xsl:apply-templates select="main-menu-entry" />
				<xsl:apply-templates select="article-menu" />
				<xsl:apply-templates select="/page/article/tab-menu" />
				<td width="100%"></td>
				<td>
					<form method="GET" action="/find/pages/"
						name="findform">
						<div id="search-bar">
							<input id="search-txt" type="text"
								tabindex="0" name="query" accesskey="f" title="搜索 [Alt-f]" />
							<input id="search-btn" type="submit"
								value="" title="搜索 [Alt-f]" />
						</div>
					</form>
					<script language="javascript">
						<!-- 
							document.findform.query.focus();
							// -->
					</script>
				</td>
				<td style="padding: 0; text-align: right">
					<img src="/public/skins/default/common/ctr.png"
						height="24" />
				</td>
			</tr>
		</table>
	</xsl:template>

	<xsl:template match="article-menu">
		<td>
			<img src="/public/skins/default/common/left-corner.gif"
				width="4" height="24" />
		</td>
		<td id="tab-selected">
			<xsl:attribute name="bgcolor">#FFFFFF</xsl:attribute>
			<xsl:value-of select="/page/article/id" />
		</td>
		<td>
			<img src="/public/skins/default/common/right-corner.gif"
				width="15" height="24" />
		</td>
	</xsl:template>

	<xsl:template match="tab-menu">
        <xsl:for-each select="tab-menu-entry">	
        <xsl:choose>
            <xsl:when test="@selected = 'yes'">
                <td>
                    <img
                        src="/public/skins/default/common/left-corner.gif" width="4"
                        height="24" />
                </td>
                <td id="tab-selected">
                    <xsl:value-of select="." />
                </td>
                <td>
                    <img
                        src="/public/skins/default/common/right-corner.gif" width="15"
                        height="24" />
                </td>
            </xsl:when>
            <xsl:otherwise>
                <td id="menu-entry">
                    <a style="color: #FFF; font-weight: bold; ">
                        <xsl:attribute name="href">
                            <xsl:value-of select="@link" />
                        </xsl:attribute>
                        <xsl:attribute name="title">
                            <xsl:value-of select="." />
                        </xsl:attribute>
                        <xsl:value-of select="." />
                    </a>
                </td>
            </xsl:otherwise>
        </xsl:choose>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="main-menu-entry">
		<xsl:choose>
			<xsl:when test="@selected = 'yes'">
				<td>
					<img
						src="/public/skins/default/common/left-corner.gif" width="4"
						height="24" />
				</td>
				<td id="tab-selected">
					<xsl:value-of select="@value" />
				</td>
				<td>
					<img
						src="/public/skins/default/common/right-corner.gif" width="15"
						height="24" />
				</td>
			</xsl:when>
			<xsl:otherwise>
				<td id="menu-entry">
					<a style="color: #FFF; font-weight: bold; ">
						<xsl:attribute name="href">
							<xsl:value-of select="@link" />
						</xsl:attribute>
						<xsl:attribute name="title">
							<xsl:value-of select="@title" />
						</xsl:attribute>
						<xsl:attribute name="accesskey">
							<xsl:value-of select="@accesskey" />
						</xsl:attribute>
						<xsl:value-of select="@value" />
					</a>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>