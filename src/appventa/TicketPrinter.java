/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appventa;

import appventa.model.Pedido;
import appventa.model.Producto;
import java.math.BigDecimal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;

/**
 *
 * @author loizp
 */
public class TicketPrinter {
    public void generaTicket(Pedido pedido) {
        if (pedido.getEstado().equals("S") || pedido.getEstado().equals("P")){
            ImageView imgprint = new ImageView();
            Text mombreprint = new Text(pedido.getNombre());
            Text codigoprint = new Text(pedido.getCodigo());
            Text montoprint = new Text();
            Text pagado = new Text("PAGADO");
            pagado.setStyle("-fx-font-weight:bold;");
            mombreprint.setFont(Font.font("Arial",9));
            codigoprint.setFont(Font.font("Arial",9));
            montoprint.setFont(Font.font("Arial",11));
            pagado.setFont(Font.font("Arial",12));
            VBox Contenidoprint = null;
            if (pedido.getEstado().equals("S")) {
                imgprint = new ImageView(new Image(getClass().getResourceAsStream("assets/logo.jpeg"), 54, 50, false, false));
                montoprint.setText("S/. " + pedido.getMontoTotal());
                Contenidoprint = new VBox(imgprint, mombreprint, codigoprint, montoprint);
            } else if (pedido.getEstado().equals("P")) {
                imgprint = new ImageView(new Image(getClass().getResourceAsStream("assets/logo.jpeg"), 54, 50, false, false));
                if(pedido.getMontoTotal().compareTo(BigDecimal.ZERO) > 0) montoprint.setText("S/. " + pedido.getMontoTotal().setScale(2, BigDecimal.ROUND_UP));
                Contenidoprint = new VBox(imgprint, mombreprint, codigoprint, montoprint, pagado, generaListaPrint(pedido));
            }

            Contenidoprint.setPrefSize(150, 300);
            Contenidoprint.setAlignment(Pos.CENTER);
            Contenidoprint.setSpacing(2);
			/*
			 * Contenidoprint.setBorder(new Border(new
			 * BorderStroke(Color.valueOf("#9E9E9E"), BorderStrokeStyle.SOLID,
			 * CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			 */

            imprimirDirecto(Contenidoprint);
            imprimirDirecto(Contenidoprint);
            imprimirDialog(Contenidoprint);
        }
        
    }
    
    public VBox generaListaPrint (Pedido pedido) {        
        Text txtcantidadproducto = null;
        Text txtnombreproducto = null;
        HBox hboxitemlist = null;
        VBox vboxlist = new VBox();
        for(Producto pr : pedido.getProductos()){
            txtcantidadproducto = new Text("( " + pr.getCantidad() + " ) > ");
            txtcantidadproducto.setFont(Font.font("Arial",7));
            txtnombreproducto = new Text(pr.getNombre());
            txtnombreproducto.setFont(Font.font("Arial",7));
            hboxitemlist = new HBox(txtcantidadproducto, txtnombreproducto);
            vboxlist.getChildren().add(hboxitemlist);
        }
        vboxlist.setPadding(new Insets(5,10,5,5));
        vboxlist.setSpacing(2);
        vboxlist.setBorder(new Border(new BorderStroke(Color.valueOf("#9E9E9E"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT)));
            
        return vboxlist;
    }
    
    private void imprimirDirecto(final Node node) {
		/*
		 * Printer printer = Printer.getDefaultPrinter(); PageLayout pageLayout =
		 * printer.createPageLayout(Paper.A6, PageOrientation.PORTRAIT,
		 * Printer.MarginType.HARDWARE_MINIMUM); final double scaleX =
		 * pageLayout.getPrintableWidth()/node.getBoundsInParent().getWidth(); final
		 * double scaleY =
		 * pageLayout.getPrintableHeight()/node.getBoundsInParent().getHeight();
		 * node.getTransforms().add(new Scale(scaleX, scaleY));
		 */
        
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            if (job.printPage(node)) {
                job.endJob();
            }
        }
    }

    private void imprimirDialog(final Node node) {

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            if (job.showPrintDialog(appventa.AppVenta.scene.getWindow())) {
                if (job.printPage(node)) {
                    job.endJob();
                }
            }
        }
    }
}
