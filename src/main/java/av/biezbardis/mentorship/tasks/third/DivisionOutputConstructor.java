package av.biezbardis.mentorship.tasks.third;

import java.util.Arrays;

public class DivisionOutputConstructor {
    public static final String SPACE = " ";
    public static final String UNDERSCORE = "_";
    private static final String BAR = "-";

    public String construct(int[] fields) {
        String[] rightColumnRows = getRightColumnRows(new int[]{
                fields[fields.length - 1],
                fields[fields.length - 2]
        });

        String[] leftColumnRows = getLeftColumnRows(Arrays.copyOf(fields, fields.length - 2));

        String[] rows = joinColumns(rightColumnRows, leftColumnRows);

        return String.join(System.lineSeparator(), rows);
    }

    private String[] joinColumns(String[] rightColumnRows, String[] leftColumnRows) {
        String[] rows = Arrays.copyOf(leftColumnRows, leftColumnRows.length);

        for (int rowIndex = 0; rowIndex < rightColumnRows.length; rowIndex++) {
            rows[rowIndex] = leftColumnRows[rowIndex] + "|" + rightColumnRows[rowIndex];
        }

        return rows;
    }

    private String[] getRightColumnRows(int[] fields) {
        String divisorString = Integer.toString(fields[0]);
        String quotientString = Integer.toString(fields[1]);

        int divisorWidth = divisorString.length();
        int quotientWidth = quotientString.length();

        int rightColumnWidth = Math.max(divisorWidth, quotientWidth);

        return new String[]{
                divisorString,
                BAR.repeat(rightColumnWidth),
                quotientString};
    }

    private String[] getLeftColumnRows(int[] fields) {
        String[] rows = new String[fields.length * 3 / 2];
        int columnWidth = String.valueOf(fields[0]).length();

        int rowsAmount = fields.length - 1;

        for (int fieldIndex = 0, rowIndex = 0, indent = 0;
             fieldIndex < rowsAmount;
             fieldIndex++) {

            if (fieldIndex == 1) {
                String row = getRowWithSpaces(indent, fields[fieldIndex]);
                rows[rowIndex++] = addBlanks(row, columnWidth);
                rows[rowIndex++] = addBlanks(getBars(row), columnWidth);

            } else if (fieldIndex % 2 != 0) {
                String row = getRowWithSpaces(indent, fields[fieldIndex]);
                rows[rowIndex++] = row;
                rows[rowIndex++] = getBars(row);
                indent = updateIntend(indent, fields[fieldIndex], fields[fieldIndex - 1]);

            } else {
                rows[rowIndex++] = getRowWithUnderscore(indent, fields[fieldIndex]);
            }
        }

        int remainder = fields[fields.length - 1];
        rows[rows.length - 1] = getRowWithSpaces(columnWidth - String.valueOf(remainder).length(), remainder);

        return rows;
    }

    private int updateIntend(int indent, int present, int previous) {
        return indent
                + (int) (Math.log10(previous) + 1)
                - (int) (Math.log10(previous - (double) present) + 1);
    }

    private String addBlanks(String row, int columnWidth) {
        return row + SPACE.repeat(columnWidth - row.length() + 1);
    }

    private String getBars(String row) {
        return row.replaceAll("[^ ]", BAR);
    }

    private String getRowWithSpaces(int intend, int field) {
        return SPACE.repeat(intend + 1) +
                field;
    }

    private String getRowWithUnderscore(int intend, int field) {
        return SPACE.repeat(intend) +
                UNDERSCORE +
                field;
    }
}
