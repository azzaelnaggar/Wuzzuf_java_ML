package com.example.jobs_analysis.CLASSES;
public class HTMLBuilder {
    
    private int columns;
    private final StringBuilder table = new StringBuilder();
    public static String HTML_START = "<html>";
    public static String HTML_END = "</html>";
    public static String TABLE_START_BORDER = "<table border=\"1\">";
    public static String TABLE_START = "<table>";
    public static String TABLE_END = "</table>";
    public static String HEADER_START = "<th>";
    public static String HEADER_END = "</th>";
    public static String ROW_START = "<tr>";
    public static String ROW_END = "</tr>";
    public static String COLUMN_START = "<td>";
    public static String COLUMN_END = "</td>";

    public HTMLBuilder() {

    }

    public StringBuilder HTMLT_Buider(){
    StringBuilder builder = new StringBuilder();
        builder.append("<p><button onclick=\"location.href='http://localhost:9090/show_first_records'\" type=\"button\"> Read Sample from Data</button>");
        builder.append("<p><button onclick=\"location.href='http://localhost:9090/show_summary'\" type=\"button\"> Get Data Summary</button>");
        builder.append(" <button onclick=\"location.href='http://localhost:9090/show_structure'\" type=\"button\"> Get Data Structure</button></p>");
        builder.append("<p><button onclick=\"location.href='http://localhost:9090/show_top_companies'\" type=\"button\"> Get the most demanding Companies for Jobs</button>");
        builder.append(" <button onclick=\"location.href='http://localhost:9090/show_top_titles'\" type=\"button\"> Get the most popular job Titles</button></p>");
        builder.append("<p><button onclick=\"location.href='http://localhost:9090/show_top_areas'\" type=\"button\"> Get the most popular Areas</button>");
        builder.append(" <button onclick=\"location.href='http://localhost:9090/show_top_skills'\" type=\"button\"> Get the most demanding Skills</button></p>");
        builder.append("<p><button onclick=\"location.href='http://localhost:9090/show_YearsExp'\" type=\"button\"> Factorize the YearsExp feature</button>");
        builder.append(" <button onclick=\"location.href='http://localhost:9090/title_bar_chart'\" type=\"button\"> Bar-chart for the most popular job Titles</button></p>");
        builder.append("<p><button onclick=\"location.href='http://localhost:9090/location_bar_chart'\" type=\"button\"> Bar-Chart for the most popular Areas</button>");
        builder.append(" <button onclick=\"location.href='http://localhost:9090/show_pie_chart'\" type=\"button\"> Pie-Chart for the most demanding Companies for Jobs</button></p>");
    return builder;
    }

    public HTMLBuilder(String header, boolean border, int rows, int columns) {
        this.columns = columns;
        if (header != null) {
            table.append("<b>");
            table.append(header);
            table.append("</b>");
        }
        table.append(HTML_START);
        table.append(border ? TABLE_START_BORDER : TABLE_START);
        table.append(TABLE_END);
        table.append(HTML_END);
    }
    public void addTableHeader(String... values) {
        if (values.length == columns) {
            int lastIndex = table.lastIndexOf(TABLE_END);
            if (lastIndex > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(ROW_START);
                for (String value : values) {
                    sb.append(HEADER_START);
                    sb.append(value);
                    sb.append(HEADER_END);
                }
                sb.append(ROW_END);
                table.insert(lastIndex, sb.toString());
            }
        }
        else{ System.out.println("Error column lenth");
    }
    }

    public void addRowValues(String... values) {
        if (values.length == columns) {
            int lastIndex = table.lastIndexOf(ROW_END);
            if (lastIndex > 0) {
                int index = lastIndex + ROW_END.length();
                StringBuilder sb = new StringBuilder();
                sb.append(ROW_START);
                for (String value : values) {
                    sb.append(COLUMN_START);
                    sb.append(value);
                    sb.append(COLUMN_END);
                }
                sb.append(ROW_END);
                table.insert(index, sb.toString());
            }
        }
        else {
        System.out.println("Error column length");
    }
    }

    public String build() {
        return table.toString();
    }

}
