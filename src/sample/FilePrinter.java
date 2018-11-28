package sample;

import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Scale;

public class FilePrinter {

    private Printer printer;
    private PrinterJob job;
    private PageLayout layout;
    private PrinterAttributes attr;

    private double scaleX,scaleY;


    public FilePrinter() {
        this.printer = Printer.getDefaultPrinter();
        this.layout = printer.createPageLayout(
                Paper.A4,
                PageOrientation.PORTRAIT,
                Printer.MarginType.HARDWARE_MINIMUM);
        this.attr = printer.getPrinterAttributes();
        this.job = PrinterJob.createPrinterJob();
    }

    public void print(String text) {
        TextFlow toPrint = new TextFlow(new Text(text));

        scaleX = 1 - layout.getPrintableWidth() / toPrint.getBoundsInParent().getWidth();
        scaleY = 1 - layout.getPrintableHeight() / toPrint.getBoundsInParent().getHeight();
        //FIXME PageRange atm 9999 toPrint.getBoundsInParent().getHeight() / layout.getPrintableHeight()
        //FIXME Margins ????

        System.out.println(scaleX + " = " + layout.getPrintableWidth() + "/" + toPrint.getBoundsInParent().getWidth());
        System.out.println(scaleY + " = " + layout.getPrintableHeight() + "/" + toPrint.getBoundsInParent().getHeight());

        Scale scale = new Scale(scaleX,scaleY);

        toPrint.getTransforms().add(scale);
        toPrint.setMaxWidth(layout.getPrintableWidth());

        if(job != null && job.showPrintDialog(null)) {
            if(job.printPage(layout,toPrint)) {
                job.endJob();
            }
        }
        toPrint.getTransforms().remove(scale);
    }
}
