<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output doctype-public="-//W3C//DTD XHTML 1.1//EN" />

	<xsl:template match="/">
		<html>
			<head>
				<link href="/public/skins/default/master.css"
					rel="stylesheet" type="text/css" />
				<title>
					<xsl:choose>
						<xsl:when test="/page/article/id">
							<xsl:value-of select="/page/article/id" />
						</xsl:when>
						<xsl:otherwise>wiki in a jar</xsl:otherwise>
					</xsl:choose>
				</title>
			</head>

			<body>
				<div id="header-content">
					<div id="header-logo">
						<br />
					</div>
					<div id="header-menu">
						<xsl:apply-templates select="/page/main-menu" />
					</div>
				</div>

				<div id="main-content">
					<xsl:apply-templates select="/page/sub-menu" />
					<div id="content">
						<xsl:apply-templates
							select="/page/article/tag-article-list" />
						<xsl:apply-templates
							select="/page/article/calendar" />
						<div id="inner">

							<xsl:apply-templates
								select="/page/article/show-article" />
							<xsl:apply-templates
								select="/page/article/edit-article" />
							<xsl:apply-templates select="/page/error" />
							<xsl:apply-templates
								select="/page/article/tagtree" />
							<xsl:apply-templates
								select="/page/article/vcard" />
							<xsl:apply-templates
								select="/page/admin-page" />
							<xsl:apply-templates
								select="/page/article/events" />
						</div>
						<xsl:call-template
					       name="quick-append" />
						<xsl:apply-templates
							select="/page/article/tag-list" />
					</div>
				</div>
				<div id="footer-content">
					<div id="footer">
						<table cellspacing="0" cellpadding="0"
							border="0" style="padding: 0; margin: 0; border: 0">
							<tr>
								<td width="5px">
									<img
										src="/public/skins/default/common/cbl.png" height="45" />
								</td>
								<td style="padding-left: 10px"></td>
								<td>
									<a
										style="color: #FFF;font-weight:bold;"
										href="http://wiki-in-a-jar.sourceforge.net/">
										
									</a>
								</td>
								<td width="100%"
									style="color: #FFF;font-size: xx-small;">
									
								</td>
								<!-- I know, the style stuff shouldn't be here, but this is to keep my sanity! -->
								<td>
									<a
										style="color: #FFF;font-weight:bold;"
										href="/wiki/show/About Wiki in a Jar">
										
									</a>
								</td>
								<td style="padding-left: 10px"></td>
								<td
									style="padding: 0; text-align: right">
									<img
										src="/public/skins/default/common/cbr.png" height="45" />
								</td>
							</tr>
						</table>
					</div>
				</div>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template name="quick-append">
	       <xsl:if test="/page/article/show-article">
	   <div id="quick-append">
          <form method="POST" name="append-form">
            <xsl:attribute name="action">/wiki/append/<xsl:value-of select="normalize-space(/page/article/id)" /></xsl:attribute>
        <table>
        <tr>
        <td>    
            <textarea name="article-content" rows="2" cols="80" />
            <input type="hidden" name="article-name">
                <xsl:attribute name="value">
                    <xsl:value-of
                        select="normalize-space(/page/article/id)" />
                </xsl:attribute>
            </input>
            </td>
            </tr>
            <tr>
            <td align="right">
            <input type="submit" value="Append" accesskey="s"
                title="Save [Alt-s]" />
        </td>
        </tr>
       </table> 
        </form>
        </div>
        </xsl:if>
	</xsl:template>

</xsl:stylesheet>