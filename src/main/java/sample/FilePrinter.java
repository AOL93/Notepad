package sample;

import javafx.print.*;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Scale;

public class FilePrinter {

    private Printer printer;
    private PrinterJob job;
    private PageLayout layout;
    private JobSettings settings;


    public FilePrinter() {
        this.printer = Printer.getDefaultPrinter();
        this.layout = printer.createPageLayout(
                Paper.A4,
                PageOrientation.PORTRAIT,
                Printer.MarginType.HARDWARE_MINIMUM);
        this.job = PrinterJob.createPrinterJob();
    }

    public void print(final TextArea textArea) {
        PageLayout printLayout = printer.createPageLayout(
                Paper.A4,
                PageOrientation.PORTRAIT,
                Printer.MarginType.DEFAULT);

        Text toPrint = new Text(textArea.getText());
        toPrint.setWrappingWidth(layout.getPrintableWidth());

        double numberOfPages = Math.ceil(toPrint.getBoundsInParent().getHeight()/layout.getPrintableHeight());
        job.getJobSettings().setPageRanges(new PageRange(1, (int) numberOfPages));

        TextFlow tf = new TextFlow(toPrint);
        tf.setMaxWidth(layout.getPrintableWidth());

        double scaleX = printLayout.getPrintableWidth() / toPrint.getBoundsInParent().getWidth();
        double scaleY = printLayout.getPrintableHeight() / layout.getPrintableHeight();

        Scale scale = new Scale(scaleX,scaleY);

        tf.getTransforms().add(scale);

        if(job != null && job.showPrintDialog(textArea.getScene().getWindow())) {
            settings = job.getJobSettings();

            System.out.println(scaleX);
            System.out.println(scaleY);

            for( PageRange range : settings.getPageRanges() ) {
                for(int p = range.getStartPage()-1;p<range.getEndPage();p++) {
                    toPrint.setTranslateY( (layout.getPrintableHeight()) * (-p));
                    job.printPage(printLayout,tf);
                }
            }
            job.endJob();
        }
    }
}
