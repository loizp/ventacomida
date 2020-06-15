/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appventa;

import appventa.model.Producto;
import appventa.model.Pedido;
import appventa.repository.PedidoRepository;
import appventa.repository.ProductoRepository;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javafx.scene.text.Text;


import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author loizp
 */
public class VentanaPrincipalController implements Initializable {

    @FXML
    private TextField txt_Editar;
    @FXML
    private Button btn_nuevo;
    @FXML
    private ImageView imglogo;
    @FXML
    private Button btn_modificar;
    @FXML
    private ListView<Pedido> listview;
    @FXML
    private TableColumn<CellTableProducto, Long> idproducto;
    @FXML
    private TableColumn<CellTableProducto, String> nombreproducto;
    @FXML
    private TableColumn<CellTableProducto, BigDecimal> precioproducto;
    @FXML
    private TableColumn<CellTableProducto, String> cantidadproducto;
    @FXML
    private TableView<CellTableProducto> tableview;
    @FXML
    private Text txttotal;

    private Pedido pedidoSelect = new Pedido();

    private BigDecimal Total = BigDecimal.valueOf(0.00);
    @FXML
    private ImageView imgnuevo;
    @FXML
    private ImageView imgmodificar;
    @FXML
    private ImageView imgrecarga;
    @FXML
    private Text txtsesion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image image = new Image(getClass().getResourceAsStream("assets/logo.jpeg"));
        imglogo.setImage(image);
        image = new Image(getClass().getResourceAsStream("assets/reload.png"));
        imgrecarga.setImage(image);
        image = new Image(getClass().getResourceAsStream("assets/add.png"));
        imgnuevo.setImage(image);
        image = new Image(getClass().getResourceAsStream("assets/upgrade.png"));
        imgmodificar.setImage(image);
        /*
        int cols=2, colCnt = 0, rowCnt = 0;
        
        for(Producto p : store.getStoreProductos()){
            gridpane.add(new ProductoCell(p).vboxmain, colCnt, rowCnt);
            colCnt++;
            if (colCnt>cols) {
                rowCnt++;
                colCnt=0;
            }
        }
         */
        txt_Editar.setOnMouseClicked(EventHandler -> {
            if (EventHandler.getButton().equals(MouseButton.PRIMARY) && EventHandler.getClickCount() == 2) {
                txt_Editar.setEditable(true);
            }
        });

        inittable();
        initlist();
        loadData(3);

    }

    public void initlist() {
        listview.setCellFactory(new Callback<ListView<Pedido>, ListCell<Pedido>>() {
            @Override
            public ListCell<Pedido> call(ListView<Pedido> arg0) {
                return new PedidoCell();
            }
        });
        listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pedido>() {
            @Override
            public void changed(ObservableValue<? extends Pedido> arg0, Pedido arg1, Pedido arg2) {
                if(arg2 != null) selectPedidoList(arg2);
            }

        });
    }

    public void selectPedidoList(Pedido pedido) {
        pedidoSelect = pedido;
        txt_Editar.setText(pedidoSelect.getNombre());
        txt_Editar.setEditable(false);
        cargaPedido();
    }

    private void cargaPedido() {
        try {
            ObservableList<CellTableProducto> tabledata = FXCollections.observableArrayList();
            PedidoRepository store = new PedidoRepository();
            ProductoRepository store2 = new ProductoRepository();
            pedidoSelect = store.selectPedido(pedidoSelect.getId());
            List<Producto> productos = store2.allProductos();
            for (Producto pr : productos) {
                for (Producto pp : pedidoSelect.getProductos()) {
                    if (pr.getId() == pp.getId()) {
                        pr.setCantidad(pr.getCantidad() + pp.getCantidad());
                    }
                }
                tabledata.add(new CellTableProducto(pedidoSelect, pr));
            }
            tableview.getItems().clear();
            tableview.setItems(tabledata);
            Total = pedidoSelect.getMontoTotal().setScale(2, BigDecimal.ROUND_UP);
            txttotal.setText("S/. " + Total);
        } catch (SQLException ex) {
            Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void OnClicRegistrar(ActionEvent event) {
        try {
            PedidoRepository store = new PedidoRepository();
            pedidoSelect = new Pedido(txt_Editar.getText());
            pedidoSelect.setEstado("P");
            Integer cantidad;
            for (CellTableProducto item : tableview.getItems()) {
                cantidad = Integer.parseInt(item.getCantidad());
                if (cantidad != null && cantidad > 0) {
                    pedidoSelect.getProductos().add(new Producto(item.idproducto, item.nombre, "", item.precio, cantidad));
                }
            }
            if (pedidoSelect.getProductos().size() > 0) {
                pedidoSelect = store.insertPedido(pedidoSelect);
                TicketPrinter tk = new TicketPrinter();
                tk.generaTicket(pedidoSelect);
                tk.generaListaPrint(pedidoSelect);
            } else {
                alerta("El pedido no tiene productos seleccionados por lo tanto no se guardará");
            }
        } catch (SQLException ex) {
            Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }

        loadData(3);
    }

    @FXML
    private void OnClickModificar(ActionEvent event) {
        if (Objects.isNull(pedidoSelect) || Objects.isNull(pedidoSelect.getId())) {
            alerta("No existe ningun pedido seleccionado para modificar");
        } else if (pedidoSelect.getEstado().equals("P")) {
            alerta("No se puedo Modificar ya el Pedido seleccionado ya fue pagado");
        } else {
            try {
                PedidoRepository store = new PedidoRepository();
                Integer cantidad;
                pedidoSelect.setNombre(txt_Editar.getText());
                pedidoSelect.getProductos().clear();
                for (CellTableProducto item : tableview.getItems()) {
                    cantidad = Integer.parseInt(item.getCantidad());
                    if (cantidad != null && cantidad > 0) {
                        pedidoSelect.getProductos().add(new Producto(item.idproducto, item.nombre, "", item.precio, cantidad));
                    }
                }
                if (pedidoSelect.getProductos().size() > 0) {
                    store.updatePedido(pedidoSelect);
                } else {
                    alerta("El pedido no tiene productos seleccionados por lo tanto no se guardará");
                }
            } catch (SQLException ex) {
                Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        loadData(3);
    }

    @FXML
    private void OnClickRecarga(MouseEvent event) {
        loadData(3);
    }

    /*
    public class ProductoCell {
    @FXML
    final VBox vboxmain = new VBox();
    private long id;
    private Text txtnombre = new Text();
    private Text txtprecio = new Text();
    private VBox Contenido = new VBox(txtnombre,txtprecio);
    private ImageView imgadd = new ImageView(new Image(getClass().getResourceAsStream("../assets/mas.png"), 32, 32, false, false));
    private ImageView imgres = new ImageView(new Image(getClass().getResourceAsStream("../assets/menos.png"), 32, 32, false, false));
    private Text txtcant = new Text();
    private Button btn_add = new Button(null,imgadd);
    private Button btn_res = new Button(null,imgres);
    private HBox hboxaction = new HBox(txtcant,btn_add,btn_res);

    public ProductoCell(Producto producto) {
        this.id = producto.getId();
        Contenido.setPrefSize(200, 180);
        Contenido.setAlignment(Pos.CENTER);
        vboxmain.setAlignment(Pos.CENTER);
        hboxaction.setAlignment(Pos.CENTER);
        Contenido.setSpacing(5);
        hboxaction.setSpacing(5);
        
        txtnombre.setText(producto.getNombre());
        txtprecio.setText(producto.getPrecio().setScale(2, BigDecimal.ROUND_DOWN).toString());
        
        txtnombre.setTextAlignment(TextAlignment.CENTER);
        txtprecio.setTextAlignment(TextAlignment.CENTER);
        
        HBox.setHgrow(Contenido, Priority.ALWAYS);
        
        vboxmain.getChildren().addAll(Contenido, hboxaction);
        vboxmain.setMaxSize(220, 250);
        vboxmain.setPrefSize(220, 250);
        
        vboxmain.getStyleClass().add("productoCell");
    }
    
}
     */
    public class PedidoCell extends ListCell<Pedido> {

        HBox hboxmain = new HBox();
        private Text txtcodigo = new Text();
        private Text txtnombre = new Text();
        VBox vboxtext = new VBox(txtcodigo, txtnombre);
        ImageView imgpedido = new ImageView(new Image(getClass().getResourceAsStream("assets/fastfood.png"), 44, 44, false, false));
        private ImageView imganular = new ImageView(new Image(getClass().getResourceAsStream("assets/clear.png"), 32, 32, false, false));
        private ImageView imgaceptar = new ImageView(new Image(getClass().getResourceAsStream("assets/done.png"), 32, 32, false, false));
        Button btn_anular = new Button(null, imganular);
        Button btn_aceptar = new Button(null, imgaceptar);

        Pedido lastItem;

        public PedidoCell() {
            super();

            vboxtext.setAlignment(Pos.CENTER_LEFT);

            hboxmain.getChildren().addAll(imgpedido, vboxtext, btn_anular, btn_aceptar);
            HBox.setHgrow(vboxtext, Priority.ALWAYS);
            hboxmain.setSpacing(5);

            btn_anular.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    pasarPedido(lastItem, 1);
                }

            });

            btn_aceptar.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    pasarPedido(lastItem, 2);
                }
            });
        }

        public void pasarPedido(Pedido pedido, int op) {
            Boolean imp = false;
            try {
                if (op == 1) {
                    if(pedido.getEstado().equals("S")) pedido.setEstado("A");
                } else if (pedido.getEstado().equals("S")) {
                    pedido.setEstado("P");
                    imp = true;
                } else if (pedido.getEstado().equals("P")) {
                    pedido.setEstado("E");
                }

                PedidoRepository store = new PedidoRepository();
                store.updateEstadoPedido(pedido);
                if (imp){
                    TicketPrinter tk = new TicketPrinter();
                    tk.generaTicket(pedido);
                }
                ObservableList<Pedido> pedidos = FXCollections.observableArrayList();
                for (Pedido p : store.allPedidosAtender()) {
                    pedidos.add(p);
                }
                this.getListView().setItems(pedidos);
                
                
                Total = BigDecimal.valueOf(0.00).setScale(2, BigDecimal.ROUND_UP);
                txttotal.setText("S/. " + Total);
                
            } catch (SQLException ex) {
                Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void updateItem(Pedido item, boolean empty) {
            super.updateItem(item, empty);

            setText(null);  // No text in label of super class
            if (empty) {
                lastItem = null;
                setGraphic(null);
            } else {
                lastItem = item;
                txtcodigo.setText(item != null ? item.getCodigo() : "<null>");
                txtnombre.setText(item != null ? item.getNombre() : "<null>");
                if (item.getEstado().equals("S")) {
                    txtcodigo.setStyle("-fx-font-weight: bold; -fx-font-size: 13pt; -fx-fill: #FF5722;");
                }
                if (item.getEstado().equals("P")) {
                    btn_anular.setDisable(true);
                    txtcodigo.setStyle("-fx-font-weight: bold; -fx-font-size: 13pt; -fx-fill: #64DD17;");
                }
                setGraphic(hboxmain);
            }
        }
    }

    public class CellTableProducto {

        private long idpedido;
        private long idproducto;
        private String nombre;
        private BigDecimal precio;
        private final StringProperty cantidad =  new SimpleStringProperty();

        public CellTableProducto(Pedido pedido, Producto producto) {
            this.idpedido = pedido.getId();
            this.idproducto = producto.getId();
            this.nombre = producto.getNombre();
            this.precio = producto.getPrecio();
            setCantidad(producto.getCantidad() + "");
        }

        public long getIdpedido() {
            return idpedido;
        }

        public long getIdproducto() {
            return idproducto;
        }

        public String getNombre() {
            return nombre;
        }

        public BigDecimal getPrecio() {
            return precio;
        }

        public String getCantidad() {
        	return this.cantidadProperty().get();
        }

        public void setIdpedido(long idpedido) {
            this.idpedido = idpedido;
        }

        public void setIdproducto(long idproducto) {
            this.idproducto = idproducto;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public void setPrecio(BigDecimal precio) {
            this.precio = precio;
        }
        
        public final StringProperty cantidadProperty() {
            return this.cantidad;
        }

        public final void setCantidad(final String cantidad) {
        	this.cantidadProperty().set(cantidad);
        }

    }

    public void inittable() {
        initcolums();
        // tableview.getColumns().add(createColumn("Cantidad", CellTableProducto::cantidadProperty));
    }

    public void initcolums() {
        idproducto.setCellValueFactory(new PropertyValueFactory<>("idproducto"));
        nombreproducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        precioproducto.setCellValueFactory(new PropertyValueFactory<>("precio"));
        cantidadproducto.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        editablecolum();
    }
    
	/*
	 * private <T> TableColumn<T, String> createColumn(String title, Function<T,
	 * StringProperty> property) { TableColumn<T, String> col = new
	 * TableColumn<>(title); col.setCellValueFactory(cellData ->
	 * property.apply(cellData.getValue()));
	 * 
	 * col.setCellFactory(column -> EditCell.createStringEditCell()); return col ; }
	 */

    public void editablecolum() {
    	cantidadproducto.setCellFactory(TextFieldTableCell.forTableColumn());
    	
        tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int row = tableview.getSelectionModel().getSelectedCells().get(0).getRow();
                Platform.runLater(() -> tableview.edit(row, tableview.getColumns().get(3)));
            }
        });
        cantidadproducto.setOnEditCommit(e -> {
            try {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setCantidad(e.getNewValue());
                Total = BigDecimal.valueOf(0.00).setScale(2, BigDecimal.ROUND_UP);
                for (CellTableProducto item : e.getTableView().getItems()) {
                    Total = Total.add(BigDecimal.valueOf(Long.parseLong(item.getCantidad())).multiply(item.getPrecio()));
                }
                txttotal.setText("S/. " + Total.toString());
            } catch (Exception ex) {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setCantidad("0");
            }
        });
        tableview.setEditable(true);
    }
    
    public void enableText() {
        txt_Editar.setEditable(true);
    }

    public void loadData(int op) {
        Total = BigDecimal.valueOf(0.00).setScale(2, BigDecimal.ROUND_UP);
        pedidoSelect = new Pedido();
        txttotal.setText("S/. " + Total);
        try {
            if (op == 1 || op == 3) {
                ObservableList<CellTableProducto> tabledata = FXCollections.observableArrayList();
                ProductoRepository store = new ProductoRepository();
                for (Producto pr : store.allProductos()) {
                    CellTableProducto cp = new CellTableProducto(new Pedido(), pr);
                    tabledata.add(cp);
                }
                tableview.setItems(tabledata);
            }
            if (op == 2 || op == 3) {
                try {
                    ObservableList<Pedido> pedidos = FXCollections.observableArrayList();
                    PedidoRepository store = new PedidoRepository();
                    for (Pedido p : store.allPedidosAtender()) {
                        pedidos.add(p);
                    }
                    listview.setItems(pedidos);
                } catch (SQLException ex) {
                    Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void alerta(String msg) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}
