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
            mombreprint.setFont(Font.font("Arial"));
            codigoprint.setFont(Font.font("Arial"));
            montoprint.setFont(Font.font("Arial"));
            pagado.setFont(Font.font("Arial"));
            VBox Contenidoprint = null;
            if (pedido.getEstado().equals("S")) {
                imgprint = new ImageView(new Image(getClass().getResourceAsStream("assets/shopping.png"), 30, 30, false, false));
                montoprint.setText("S/. " + pedido.getMontoTotal());
                Contenidoprint = new VBox(imgprint, mombreprint, codigoprint, montoprint);
            } else if (pedido.getEstado().equals("P")) {
                imgprint = new ImageView(new Image(getClass().getResourceAsStream("assets/fastfood.png"), 30, 30, false, false));
                if(pedido.getMontoTotal().compareTo(BigDecimal.ZERO) > 0) montoprint.setText("S/. " + pedido.getMontoTotal().setScale(2, BigDecimal.ROUND_UP));
                Contenidoprint = new VBox(imgprint, mombreprint, codigoprint, montoprint, pagado);
            }

            Contenidoprint.setPrefSize(110, 120);
            Contenidoprint.setAlignment(Pos.CENTER);
            Contenidoprint.setSpacing(5);
            Contenidoprint.setBorder(new Border(new BorderStroke(Color.valueOf("#9E9E9E"),
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths.DEFAULT)));

            imprimirDirecto(Contenidoprint);
            imprimirDirecto(Contenidoprint);
        }
        
    }
    
    public void generaListaPrint (Pedido pedido) {
        ImageView imgprint = new ImageView(new Image(getClass().getResourceAsStream("assets/list.png"), 50, 52, false, false));
        Text mombreprint = new Text(pedido.getNombre());
        mombreprint.setStyle("-fx-font-weight:bold;");
        Text codigoprint = new Text(pedido.getCodigo());
        codigoprint.setStyle("-fx-font-weight:bold;");
        VBox vboxtexttitle = new VBox(mombreprint, codigoprint);
        vboxtexttitle.setAlignment(Pos.CENTER_LEFT);
        vboxtexttitle.setSpacing(2);
        HBox hboxtitle = new HBox(imgprint, vboxtexttitle);
        hboxtitle.setAlignment(Pos.CENTER);
        hboxtitle.setStyle("-fx-border-width: 0 0 1px 0; -fx-border-color: #9E9E9E;");
        hboxtitle.setSpacing(5);
        
        Text txtcantidadproducto = null;
        Text txtnombreproducto = null;
        HBox hboxitemlist = null;
        VBox vboxlist = new VBox();
        for(Producto pr : pedido.getProductos()){
            txtcantidadproducto = new Text("( " + pr.getCantidad() + " ) > ");
            txtnombreproducto = new Text(pr.getNombre());
            hboxitemlist = new HBox(txtcantidadproducto, txtnombreproducto);
            vboxlist.getChildren().add(hboxitemlist);
        }
        vboxlist.setPadding(new Insets(0,20,10,10));
        
        VBox Contenidoprint = new VBox(hboxtitle,vboxlist);
        Contenidoprint.setAlignment(Pos.CENTER);
        Contenidoprint.setSpacing(5);
        Contenidoprint.setBorder(new Border(new BorderStroke(Color.valueOf("#9E9E9E"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT)));
            
        imprimirDialog(Contenidoprint);
    }
    
    private void imprimirDirecto(final Node node) {

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
