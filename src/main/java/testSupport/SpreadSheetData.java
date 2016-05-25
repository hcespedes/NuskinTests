package testSupport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by heidy.cespedes on 5/25/2016.
 */
public class SpreadSheetData {
   private Object[][] data = null;

   public Object[][] getData(String excelFilePath) throws IOException {

      FileInputStream inputStream = null;
      try {
         inputStream = new FileInputStream(new File(excelFilePath));
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }

      Workbook workbook = null;
      try {
         workbook = new XSSFWorkbook(inputStream);
      } catch (IOException e) {
         e.printStackTrace();
      }
      Sheet firstSheet = workbook.getSheetAt(0);
      Iterator<Row> rowIterator = firstSheet.iterator();

      while (rowIterator.hasNext()) {


         Row nextRow = rowIterator.next();
         Iterator<Cell> cellIterator = nextRow.cellIterator();

         while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getCellType()) {
               case Cell.CELL_TYPE_STRING:
                  System.out.print(cell.getStringCellValue());
                  break;
               case Cell.CELL_TYPE_BOOLEAN:
                  System.out.print(cell.getBooleanCellValue());
                  break;
               case Cell.CELL_TYPE_NUMERIC:
                  System.out.print(cell.getNumericCellValue());
                  break;
            }
            System.out.print(" - ");
         }
         System.out.println();
      }

      //workbook.close();
      inputStream.close();

      return null;
   }
}
