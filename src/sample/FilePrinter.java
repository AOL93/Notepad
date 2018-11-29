package sample;

import javafx.print.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class FilePrinter {

    private Printer printer;
    private PrinterJob job;
    private PageLayout layout;
    private PrinterAttributes attr;


    public FilePrinter() {
        this.printer = Printer.getDefaultPrinter();
        this.layout = printer.createPageLayout(
                Paper.A4,
                PageOrientation.PORTRAIT,
                Printer.MarginType.DEFAULT);
        this.attr = printer.getPrinterAttributes();
        this.job = PrinterJob.createPrinterJob();
    }

    public void print(String text) {
        TextFlow toPrint = new TextFlow(new Text(text));
        //FIXME PageRange atm 9999
        //FIXME Printing only first page

        double numberOfPages = Math.ceil(layout.getPrintableHeight() / toPrint.getBoundsInParent().getHeight());

        toPrint.setMaxWidth(layout.getPrintableWidth());
        toPrint.setMaxHeight(layout.getPrintableHeight());

        System.out.println(numberOfPages);

        if(job != null && job.showPrintDialog(null)) {
            if(job.printPage(layout,toPrint)) {
                job.endJob();
            }
        }
    }
}
