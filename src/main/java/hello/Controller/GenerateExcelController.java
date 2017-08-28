package hello.Controller;

import hello.Model.GenerateExelRepository;
import hello.Model.Generateexel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;

import jxl.Workbook;
import jxl.format.*;
import jxl.format.Colour;
import jxl.write.*;
import jxl.write.Number;

@Component
public class GenerateExcelController {

/*
    public GenerateExcelController(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                findAll();
            }
        }).start();
    }
*/

    @Autowired
    GenerateExelRepository generateExelRepository;

    volatile Iterable<Generateexel> generateexelIterator;

    private void findAll() {
        while (true) {
            try {
                generateexelIterator = generateExelRepository.findAll();

                generateexelIterator.forEach(generateexel -> {
                    if (generateexel.getNextexecution().
                            before(new Timestamp(System.currentTimeMillis()))) {
                        if (generateexel.getFrequency() != null) {
                            saveNewNextExecution(generateexel);
                        }
                        ThreadPoolTask.threadPool.submit(new Runnable() {
                            @Override
                            public void run() {
                                generateExcelFile(generateexel);
                                System.out.println(Thread.currentThread().getName() + " Excel id = " + generateexel.getId());
                            }
                        });

                    }
                });
            } catch (NullPointerException e) {
                System.out.println("No excel files");
            }
        }
    }


    private void saveNewNextExecution(Generateexel generateexel) {
        Generateexel generateexelNew = new Generateexel();
        generateexelNew = generateexel;
        generateexelNew.setNextexecution(new Timestamp(System.currentTimeMillis() + shouldConvertTimeBecauseJavaCannotByDepricatedWay(generateexel.getFrequency())));
        generateExelRepository.delete(generateexel.getId());
        generateExelRepository.save(generateexelNew);
    }

    private long shouldConvertTimeBecauseJavaCannotByDepricatedWay(Time time) {
        long timeValue;
        timeValue = time.getHours() * 3600000 + time.getMinutes() * 60000 + time.getSeconds() * 1000;
        return timeValue;
    }

    private void generateExcelFile(Generateexel generateexel) {

        final String EXCEL_FILE_LOCATION = generateexel.getScript() + "\\AntonsTask.xls";

        //1. Create an Excel file
        WritableWorkbook wbook = null;
        try {
            Number number;
            Label label;

            wbook = Workbook.createWorkbook(new File(EXCEL_FILE_LOCATION));

            // create an Excel sheet
            WritableSheet excelSheet = wbook.createSheet("Sheet 1", 0);

            WritableCellFormat cFormat = new WritableCellFormat();
            WritableFont font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true, UnderlineStyle.NO_UNDERLINE, Colour.RED);
            cFormat.setFont(font);
            cFormat.setBackground(Colour.GRAY_25);

            WritableCellFormat cellFormat = new WritableCellFormat();
            WritableFont fontNumbers = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD, false);
            cellFormat.setFont(fontNumbers);

            int column = 1;
            for (int i = 1; i <= 5; i++, column += 3) {
                label = new Label(column, 0, "Multiplication on " + String.valueOf(i), cFormat);
                excelSheet.setColumnView(column, 20);
                excelSheet.addCell(label);
                for (int j = 1; j <= 10; j++) {
                    label = new Label(column, j, String.valueOf(i) + " * " + String.valueOf(j) + " = " + String.valueOf(i * j), cellFormat);
                    excelSheet.addCell(label);
                }
            }

            wbook.write();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } finally {
            if (wbook != null) {
                try {
                    wbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }

        if (generateexel.getFrequency() == null) {
            generateExelRepository.delete(generateexel.getId());
        }
    }

}
