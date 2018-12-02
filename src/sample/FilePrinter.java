package sample;

import javafx.print.*;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class FilePrinter {

    private Printer printer;
    private PrinterJob job;
    private PageLayout layout;


    public FilePrinter() {
        this.printer = Printer.getDefaultPrinter();
        this.layout = printer.createPageLayout(
                Paper.A4,
                PageOrientation.PORTRAIT,
                Printer.MarginType.DEFAULT);
        this.job = PrinterJob.createPrinterJob();
    }

    public void print(final TextArea textArea) {
        TextFlow toPrint = new TextFlow(new Text(textArea.getText()));

        //FIXME PageRange atm 9999
        //FIXME Printing only first page

        //TODO Look at Layout. There is the bug. MAYBE. Swap toPrint.getBoundsInParent().getHeight() with layout.getPrintableHeight()
        double numberOfPages = Math.ceil(layout.getPrintableHeight() / toPrint.getBoundsInParent().getHeight()); //FIXME WRONG HEIGHTS???!!!
        System.out.println(layout.getPrintableHeight() + "/" + toPrint.getBoundsInParent().getHeight());
        job.getJobSettings().setPageRanges(new PageRange(1, (int) numberOfPages));
        System.out.println(numberOfPages);

        if(job != null && job.showPrintDialog(textArea.getScene().getWindow())) {
            toPrint.setMaxWidth(layout.getPrintableWidth());
            toPrint.setMaxHeight(layout.getPrintableHeight());

            for(int i=0;i<numberOfPages;i++) {
                toPrint.setTranslateY(-i * layout.getPrintableHeight()); //FIXME Think about value
                System.out.println(layout.getTopMargin());
                job.printPage(layout,toPrint);
            }
            job.endJob();
        }

    }
}
