package com.bingo.bootjudge.framework;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.util.Transform;
import org.apache.logging.log4j.util.Strings;

/**
 * 自定义 html 输出.
 * 
 * @author tornado.z.y
 * @date 2017年3月28日 下午2:32:13
 */
@Plugin(name = "HtmlPrintLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public final class HtmlLayout extends AbstractStringLayout {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String DEFAULT_FONT_FAMILY = "arial,sans-serif";

    private static final String TRACE_PREFIX = "<br />&nbsp;&nbsp;&nbsp;&nbsp;";
    private static final String REGEXP = Strings.LINE_SEPARATOR.equals("\n") ? "\n" : Strings.LINE_SEPARATOR + "|\n";
    private static final String DEFAULT_TITLE = "Log4j Log Messages";
    private static final String DEFAULT_CONTENT_TYPE = "text/html";

    private final boolean locationInfo;
    private final String title;
    private final String contentType;
    private final String font;
    private final String fontSize;
    private final String headerSize;
    
    public static enum FontSize {
        SMALLER("smaller"), XXSMALL("xx-small"), XSMALL("x-small"), SMALL("small"), MEDIUM("medium"), LARGE("large"),
        XLARGE("x-large"), XXLARGE("xx-large"),  LARGER("larger");

        private final String size;

        private FontSize(final String size) {
            this.size = size;
        }

        public String getFontSize() {
            return size;
        }

        public static FontSize getFontSize(final String size) {
            for (final FontSize fontSize : values()) {
                if (fontSize.size.equals(size)) {
                    return fontSize;
                }
            }
            return SMALL;
        }

        public FontSize larger() {
            return this.ordinal() < XXLARGE.ordinal() ? FontSize.values()[this.ordinal() + 1] : this;
        }
    }

    private HtmlLayout(final boolean locationInfo, final String title, final String contentType, final Charset charset,
            final String font, final String fontSize, final String headerSize) {
        super(charset);
        this.locationInfo = locationInfo;
        this.title = title;
        this.contentType = addCharsetToContentType(contentType);
        this.font = font;
        this.fontSize = fontSize;
        this.headerSize = headerSize;
        
    }

    /**
     * For testing purposes.
     */
    public String getTitle() {
        return title;
    }

    /**
     * For testing purposes.
     */
    public boolean isLocationInfo() {
        return locationInfo;
    }

    private String addCharsetToContentType(final String contentType) {
        if (contentType == null) {
            return DEFAULT_CONTENT_TYPE + "; charset=" + getCharset();
        }
        return contentType.contains("charset") ? contentType : contentType + "; charset=" + getCharset();
    }

    /**
     * Format as a String.
     *
     * @param event The Logging Event.
     * @return A String containing the LogEvent as HTML.
     */
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilder sbuf = getStringBuilder();

        sbuf.append(Strings.LINE_SEPARATOR).append("<tr "
				+ (event.getLevel().isMoreSpecificThan(Level.WARN) ? "bgcolor=\"#ff9900\" >" : ">")).append(Strings.LINE_SEPARATOR);

        sbuf.append("<td style='white-space: nowrap;'>");
        sbuf.append(FORMAT.format(new Date(event.getTimeMillis())));
        sbuf.append("</td>").append(Strings.LINE_SEPARATOR);

        final String escapedThread = Transform.escapeHtmlTags(event.getThreadName());
        sbuf.append("<td title=\"").append(escapedThread).append(" thread\">");
        sbuf.append(escapedThread);
        sbuf.append("</td>").append(Strings.LINE_SEPARATOR);

        sbuf.append("<td title=\"Level\">");
        if (event.getLevel().equals(Level.DEBUG)) {
            sbuf.append("<font color=\"#339933\">");
            sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
            sbuf.append("</font>");
        } else if (event.getLevel().isMoreSpecificThan(Level.WARN)) {
            sbuf.append("<font color=\"#993300\"><strong>");
            sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
            sbuf.append("</strong></font>");
        } else {
            sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
        }
        sbuf.append("</td>").append(Strings.LINE_SEPARATOR);

        String escapedLogger = Transform.escapeHtmlTags(event.getLoggerName());
        if (escapedLogger.isEmpty()) {
            escapedLogger = LoggerConfig.ROOT;
        }
        sbuf.append("<td title=\"").append(escapedLogger).append(" logger\">");
        sbuf.append(escapedLogger);
        sbuf.append("</td>").append(Strings.LINE_SEPARATOR);

        if (locationInfo) {
            final StackTraceElement element = event.getSource();
            sbuf.append("<td>");
            sbuf.append(Transform.escapeHtmlTags(element.getFileName()));
            sbuf.append(':');
            sbuf.append(element.getLineNumber());
            sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
        }

        sbuf.append("<td title=\"Message\">");
        sbuf.append(Transform.escapeHtmlTags(event.getMessage().getFormattedMessage()).replaceAll(REGEXP, "<br />"));
        sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
        sbuf.append("</tr>").append(Strings.LINE_SEPARATOR);

        if (event.getContextStack() != null && !event.getContextStack().isEmpty()) {
            sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(fontSize);
            sbuf.append(";\" colspan=\"6\" ");
            sbuf.append("title=\"Nested Diagnostic Context\">");
            sbuf.append("NDC: ").append(Transform.escapeHtmlTags(event.getContextStack().toString()));
            sbuf.append("</td></tr>").append(Strings.LINE_SEPARATOR);
        }

        if (event.getContextMap() != null && !event.getContextMap().isEmpty()) {
            sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(fontSize);
            sbuf.append(";\" colspan=\"6\" ");
            sbuf.append("title=\"Mapped Diagnostic Context\">");
            sbuf.append("MDC: ").append(Transform.escapeHtmlTags(event.getContextMap().toString()));
            sbuf.append("</td></tr>").append(Strings.LINE_SEPARATOR);
        }

        final Throwable throwable = event.getThrown();
        if (throwable != null) {
            sbuf.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : ").append(fontSize);
            sbuf.append(";\" colspan=\"6\">");
            appendThrowableAsHtml(throwable, sbuf);
            sbuf.append("</td></tr>").append(Strings.LINE_SEPARATOR);
        }

        return sbuf.toString();
    }

    @Override
    /**
     * @return The content type.
     */
    public String getContentType() {
        return contentType;
    }

    private void appendThrowableAsHtml(final Throwable throwable, final StringBuilder sbuf) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
        } catch (final RuntimeException ex) {
            // Ignore the exception.
        }
        pw.flush();
        final LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
        final ArrayList<String> lines = new ArrayList<>();
        try {
          String line = reader.readLine();
          while (line != null) {
            lines.add(line);
            line = reader.readLine();
          }
        } catch (final IOException ex) {
            if (ex instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            lines.add(ex.toString());
        }
        boolean first = true;
        for (final String line : lines) {
            if (!first) {
                sbuf.append(TRACE_PREFIX);
            } else {
                first = false;
            }
            sbuf.append(Transform.escapeHtmlTags(line));
            sbuf.append(Strings.LINE_SEPARATOR);
        }
    }

    /**
     * Returns appropriate HTML headers.
     * @return The header as a byte array.
     */
    @Override
    public byte[] getHeader() {
        final StringBuilder sbuf = new StringBuilder();
        sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" ");
        sbuf.append("\"http://www.w3.org/TR/html4/loose.dtd\">");
        sbuf.append(Strings.LINE_SEPARATOR);
        sbuf.append("<html>").append(Strings.LINE_SEPARATOR);
        sbuf.append("<head>").append(Strings.LINE_SEPARATOR);
        sbuf.append("<meta charset=\"").append(getCharset()).append("\"/>").append(Strings.LINE_SEPARATOR);
        sbuf.append("<title>").append(title).append("</title>").append(Strings.LINE_SEPARATOR);
        sbuf.append("<style type=\"text/css\">").append(Strings.LINE_SEPARATOR);
        sbuf.append("<!--").append(Strings.LINE_SEPARATOR);
        sbuf.append("body, table {font-family:").append(font).append("; font-size: ");
        sbuf.append(headerSize).append(";}").append(Strings.LINE_SEPARATOR);
        sbuf.append("th {background: #336699; color: #FFFFFF; text-align: left;}").append(Strings.LINE_SEPARATOR);
        sbuf.append("-->").append(Strings.LINE_SEPARATOR);
        sbuf.append("</style>").append(Strings.LINE_SEPARATOR);
        sbuf.append("</head>").append(Strings.LINE_SEPARATOR);
        sbuf.append("<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">").append(Strings.LINE_SEPARATOR);
        sbuf.append("<hr size=\"1\" noshade=\"noshade\">").append(Strings.LINE_SEPARATOR);
        sbuf.append("Log session start time " + new java.util.Date() + "<br>").append(Strings.LINE_SEPARATOR);
        sbuf.append("<br>").append(Strings.LINE_SEPARATOR);
        sbuf.append(
            "<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">");
        sbuf.append(Strings.LINE_SEPARATOR);
        sbuf.append("<tr>").append(Strings.LINE_SEPARATOR);
        sbuf.append("<th>Time</th>").append(Strings.LINE_SEPARATOR);
        sbuf.append("<th>Thread</th>").append(Strings.LINE_SEPARATOR);
        sbuf.append("<th>Level</th>").append(Strings.LINE_SEPARATOR);
        sbuf.append("<th>Logger</th>").append(Strings.LINE_SEPARATOR);
        if (locationInfo) {
            sbuf.append("<th>File:Line</th>").append(Strings.LINE_SEPARATOR);
        }
        sbuf.append("<th>Message</th>").append(Strings.LINE_SEPARATOR);
        sbuf.append("</tr>").append(Strings.LINE_SEPARATOR);
        return sbuf.toString().getBytes(getCharset());
    }

    /**
     * Returns the appropriate HTML footers.
     * @return the footer as a byte array.
     */
    @Override
    public byte[] getFooter() {
        final StringBuilder sbuf = new StringBuilder();
        sbuf.append("</table>").append(Strings.LINE_SEPARATOR);
        sbuf.append("<br>").append(Strings.LINE_SEPARATOR);
        sbuf.append("</body></html>");
        return getBytes(sbuf.toString());
    }

    /**
     * Create an HTML Layout.
     * @param locationInfo If "true", location information will be included. The default is false.
     * @param title The title to include in the file header. If none is specified the default title will be used.
     * @param contentType The content type. Defaults to "text/html".
     * @param charset The character set to use. If not specified, the default will be used.
     * @param fontSize The font size of the text.
     * @param font The font to use for the text.
     * @return An HTML Layout.
     */
    @PluginFactory
    public static HtmlLayout createLayout(
            @PluginAttribute(value = "locationInfo", defaultBoolean = false) final boolean locationInfo,
            @PluginAttribute(value = "title", defaultString = DEFAULT_TITLE) final String title,
            @PluginAttribute("contentType") String contentType,
            @PluginAttribute(value = "charset", defaultString = "UTF-8") final Charset charset,
            @PluginAttribute("fontSize") String fontSize,
            @PluginAttribute(value = "fontName", defaultString = DEFAULT_FONT_FAMILY) final String font) {
        final FontSize fs = FontSize.getFontSize(fontSize);
        fontSize = fs.getFontSize();
        final String headerSize = fs.larger().getFontSize();
        if (contentType == null) {
            contentType = DEFAULT_CONTENT_TYPE + "; charset=" + charset;
        }
        return new HtmlLayout(locationInfo, title, contentType, charset, font, fontSize, headerSize);
    }

    /**
     * Creates an HTML Layout using the default settings.
     *
     * @return an HTML Layout.
     */
    public static HtmlLayout createDefaultLayout() {
        return newBuilder().build();
    }

    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder implements org.apache.logging.log4j.core.util.Builder<HtmlLayout> {

        @PluginBuilderAttribute
        private boolean locationInfo = false;

        @PluginBuilderAttribute
        private String title = DEFAULT_TITLE;

        @PluginBuilderAttribute
        private String contentType = null; // defer default value in order to use specified charset

        @PluginBuilderAttribute
        private Charset charset = StandardCharsets.UTF_8;

        @PluginBuilderAttribute
        private FontSize fontSize = FontSize.SMALL;

        @PluginBuilderAttribute
        private String fontName = DEFAULT_FONT_FAMILY;

        private Builder() {
        }

        public Builder withLocationInfo(final boolean locationInfo) {
            this.locationInfo = locationInfo;
            return this;
        }

        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }

        public Builder withContentType(final String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder withCharset(final Charset charset) {
            this.charset = charset;
            return this;
        }

        public Builder withFontSize(final FontSize fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public Builder withFontName(final String fontName) {
            this.fontName = fontName;
            return this;
        }

        @Override
        public HtmlLayout build() {
            // TODO: extract charset from content-type
            if (contentType == null) {
                contentType = DEFAULT_CONTENT_TYPE + "; charset=" + charset;
            }
            return new HtmlLayout(locationInfo, title, contentType, charset, fontName, fontSize.getFontSize(),
                fontSize.larger().getFontSize());
        }
    }
}
