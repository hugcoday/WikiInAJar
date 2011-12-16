<?xml version="1.0" encoding="ISO-8859-1"?>

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
				<xsl:apply-templates select="main-menu-entry" />
				<xsl:apply-templates select="article-menu" />
				<td width="100%"></td>
				<td>
					<form method="GET" action="/find/pages/">
						<div id="search-bar">
							<input id="search-txt" disabled="yes" value="disabled in static version" dtype="text"
								name="query" />
							<input id="search-btn" disabled="yes" type="submit"
								value="" />
						</div>
					</form>
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
						<xsl:value-of select="@value" />
					</a>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>


</xsl:stylesheet>
