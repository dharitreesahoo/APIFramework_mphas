package helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {
public String path;
public FileInputStream fis = null;
public FileOutputStream fileOut = null;
private XSSFWorkbook workbook = null;
private XSSFSheet sheet = null;
private XSSFRow row = null;
private XSSFCell cell = null;

public ExcelHelper(String path) {

FileInputStream fis;

try {
fis = new FileInputStream(path);
workbook = new XSSFWorkbook(fis);
// sheet = workbook.getSheetAt(0);
fis.close();
} catch (Exception e) {
e.printStackTrace();
}

}

public int getCellRowNum(String sheetName, String colName, String cellValue) {

for (int i = 2; i <= getRowCount(sheetName); i++) {
if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
return i;
}
}
return -1;

}

public int getRowCount(String sheetName) {

int index = workbook.getSheetIndex(sheetName);
if (index == -1)
return 0;
else {
sheet = workbook.getSheetAt(index);
int number = sheet.getLastRowNum() + 1;
return number;
}
}

public String getCellData(String sheetName, String colName, int rowNum) {
try {
if (rowNum <= 0)
return "";

int index = workbook.getSheetIndex(sheetName);
int col_Num = -1;
if (index == -1)
return "";

sheet = workbook.getSheetAt(index);
row = sheet.getRow(0);
for (int i = 0; i < row.getLastCellNum(); i++) {
// System.out.println(row.getCell(i).getStringCellValue().trim());
if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
col_Num = i;
}
if (col_Num == -1)
return "";

sheet = workbook.getSheetAt(index);
row = sheet.getRow(rowNum - 1);
if (row == null)
return "";
cell = row.getCell(col_Num);

if (cell == null)
return "";
// System.out.println(cell.getCellType());
if (cell.getCellType() == Cell.CELL_TYPE_STRING)
return cell.getStringCellValue();
else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

String cellText = String.valueOf(cell.getNumericCellValue());
if (HSSFDateUtil.isCellDateFormatted(cell)) {
// format in form of M/D/YY
double d = cell.getNumericCellValue();

Calendar cal = Calendar.getInstance();
cal.setTime(HSSFDateUtil.getJavaDate(d));
cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;

// System.out.println(cellText);

}

return cellText;
} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
return "";
else
return String.valueOf(cell.getBooleanCellValue());

} catch (Exception e) {

e.printStackTrace();
return "row " + rowNum + " or column " + colName + " does not exist in xls";
}
}
public String getXLValue(String sheetName, String colName, int rowNum) {
try {
// If the row number provided is <=0 it should return blank
if (rowNum <= 0)
return "";
// Validate if the Sheet Exists or Not
if (isSheetExist(sheetName)) {
int index = workbook.getSheetIndex(sheetName);
int col_Num = -1;
if (index == -1)
return "";
// Get the index of the sheet based on the sheetName
sheet = workbook.getSheetAt(index);
// Get the Column no based on the Column Name
row = sheet.getRow(0);
for (int i = 0; i < row.getLastCellNum(); i++) {
if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
col_Num = i;
}
// If column No returns -1 then return nothing
if (col_Num == -1)
return "";

sheet = workbook.getSheetAt(index);
row = sheet.getRow(rowNum - 1);
if (row == null)
return "";
cell = row.getCell(col_Num);

if (cell == null)
return "";
// Get the value of the Cell if it is a string so it should read
// string format
if (cell.getCellType() == Cell.CELL_TYPE_STRING)
return cell.getStringCellValue();

else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
String value = cell.getCellFormula();
switch (cell.getCachedFormulaResultType()) {
case Cell.CELL_TYPE_NUMERIC:
value = String.valueOf(cell.getNumericCellValue());
break;
case Cell.CELL_TYPE_STRING:
value = String.valueOf(cell.getRichStringCellValue());
break;
}
return value;
}

else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
String value = String.valueOf(cell.getNumericCellValue());
return value;
}

// Get the value of the Cell if it is a string so it should
// return blank format
else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
return "";
else
// Return a Boolean value
return String.valueOf(cell.getBooleanCellValue());
}
// If the sheet does not exist then return Sheet is not present
return "The Sheet Name: " + sheetName + " does not exist in xls";
} catch (Exception e) {
e.printStackTrace();
return "The Row: " + rowNum + " or the Column Name: " + colName + " does not exist in the .xlsx file";
}

}
public boolean isSheetExist(String sheetName) {
int index = workbook.getSheetIndex(sheetName);
if (index == -1) {
index = workbook.getSheetIndex(sheetName.toUpperCase());
if (index == -1)
return false;
else
return true;
} else
return true;
}
//added on 23rdMay2020 by dharitree
public int getColumnCount(String sheetName) {
int index = workbook.getSheetIndex(sheetName);
if (index == -1)
return 0;
else {
sheet = workbook.getSheetAt(index);
int number = sheet.getRow(0).getPhysicalNumberOfCells();
return number;
}

}
public Object[][] getDataAsMap(String strSheetName) throws Exception {

int rowCount = getRowCount(strSheetName);
int colCount = sheet.getRow(0).getLastCellNum();
// define a map an object array
Object[][] obj = new Object[rowCount-1][1];

for (int i = 0; i < rowCount-1; i++) {
Map<Object, Object> dataMap = new HashMap<Object, Object>();
for (int j = 0; j < colCount; j++) {
dataMap.put(sheet.getRow(0).getCell(j).toString(), sheet.getRow(i + 1).getCell(j).toString());
}
obj[i][0] = dataMap;
}
return obj;

}

}

