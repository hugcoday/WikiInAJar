<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="calendar">
		<div id="calendar">
			<table id="month">
				<caption>
					<a title="Previous [Alt-p]" accesskey="p">
						<xsl:attribute name="href">
							<xsl:value-of select="month/@prevlink" />
						</xsl:attribute>
						&#171;
					</a>
					<xsl:value-of select="month/@name" />
					&#160;
					<xsl:value-of select="month/@year" />
					<a title="Next [Alt-n]" accesskey="n">
						<xsl:attribute name="href">
							<xsl:value-of select="month/@nextlink" />
						</xsl:attribute>
						&#187;
					</a>
				</caption>
				<tr>
					<th>S</th>
					<th>M</th>
					<th>T</th>
					<th>W</th>
					<th>T</th>
					<th>F</th>
					<th>S</th>
				</tr>
				<xsl:apply-templates select="month/week" />
			</table>
		</div>
	</xsl:template>

	<xsl:template match="week">
		<tr>
			<xsl:for-each select="filler">
				<td />
			</xsl:for-each>
			<xsl:for-each select="day">
				<td>
					<xsl:choose>
						<xsl:when test="@eventlink">
							<a>
								<xsl:attribute name="href">
									<xsl:value-of select="@eventlink" />
								</xsl:attribute>
								<xsl:value-of select="@dayofmonth" />
							</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="@weekend = 'yes'">
									<b>
										<xsl:value-of
											select="@dayofmonth" />
									</b>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="@dayofmonth" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:for-each>
		</tr>
	</xsl:template>

	<xsl:template match="events">
		<xsl:apply-templates select="past" />
		<xsl:apply-templates select="today" />
		<xsl:apply-templates select="future" />
	</xsl:template>

	<xsl:template match="past">
		<xsl:if test="count(event) &gt; 0">
			<h2>Past Events</h2>
			<ul>
				<xsl:apply-templates select="event" />
			</ul>
		</xsl:if>
	</xsl:template>

	<xsl:template match="future">
		<xsl:if test="count(event) &gt; 0">
			<h2>Upcoming Events</h2>
			<ul>
				<xsl:apply-templates select="event" />
			</ul>
		</xsl:if>
	</xsl:template>

	<xsl:template match="today">
		<xsl:if test="count(event) &gt; 0">
			<h2>Today's Events</h2>
			<ul>
				<xsl:apply-templates select="event" />
			</ul>
		</xsl:if>
	</xsl:template>

	<xsl:template match="event">
		<li>
			<xsl:choose>
				<xsl:when test="@isbirthday = 'true'">
					<a>
						<xsl:attribute name="href">
							/vcard/show/
							<xsl:value-of select="@id" />
						</xsl:attribute>
						<xsl:value-of select="@id" />
						's birthday (
						<xsl:value-of select="@date" />
						)
					</a>
				</xsl:when>
				<xsl:otherwise>
					<a>
						<xsl:attribute name="href">
							/wiki/show/
							<xsl:value-of select="@id" />
						</xsl:attribute>
						<xsl:value-of select="@id" />
					</a>
				</xsl:otherwise>
			</xsl:choose>
		</li>
	</xsl:template>
</xsl:stylesheet>