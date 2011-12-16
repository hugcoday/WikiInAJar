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
								<div id="header-logo"><br/><h3>Wiki in a jar</h3></div>
					<div id="header-menu">
						<xsl:apply-templates select="/page/main-menu" />
					</div>
				</div>

				<div id="main-content">
					<xsl:apply-templates select="/page/sub-menu" />
					<div id="content">
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
						</div>
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
								<td><a style="color: #FFF;font-weight:bold;" href="http://sourceforge.net/projects/wiki-in-a-jar">sourceforge.net</a></td>
								<td width="100%" style="color: #FFF;font-size: xx-small;">do good!</td>								
								<!-- I know, the style stuff shouldn't be here, but this is to keep my sanity! -->
								<td><a style="color: #FFF;font-weight:bold;" href="/wiki/show/About Wiki in a Jar.xml">about</a></td>
								<td style="padding-left: 10px"></td>
								<td
									style="padding: 0; text-align: right">
									<img
										src="/public/skins/default/common/cbr.png" height="45" />
								</td>
							</tr>
						</table>
		</div>
		<br/>
		<a href='http://sourceforge.net'><img src='http://sourceforge.net/sflogo.php?group_id=139273&amp;type=1' width='88' height='31' border='0' alt='SourceForge.net Logo' /></a>
				</div>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>
