package view;


import java.text.DecimalFormat;
import controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;



public class SimulaattorinGUI extends Application implements ISimulaattorinUI {

    //Kontrollerin esittely (tarvitaan käyttöliittymässä)
    private IKontrolleriForV kontrolleri;

    // Käyttöliittymäkomponentit:
    private TextField aika;
    private TextField viive;
    private TextField saapumisvali;
    private TextField palvelupisteAika;
    private TextField basicAika;
    private TextField premiumAika;
    private TextField vahausAika;
    private TextField kuivausAika;
    private TextField premiumOsuus;
    private Label tulos;
    private Label aikaLabel;
    private Label viiveLabel;
    private Label virheLabel;
    private Label saapumisvaliLabel;
    private Label palvelupisteAikaLabel;
    private Label basicAikaLabel;
    private Label premiumAikaLabel;
    private Label vahausAikaLabel;
    private Label kuivausAikaLabel;
    private Label premiumOsuusLabel;
    private Label tulosLabel;

    private Button kaynnistaButton;
    private Button hidastaButton;
    private Button nopeutaButton;

    private IVisualisointi naytto;


    @Override
    public void init() {

        Trace.setTraceLevel(Level.INFO);

        kontrolleri = new Kontrolleri(this);
    }

    @Override
    public void start(Stage primaryStage) {
        // Käyttöliittymän rakentaminen
        try {

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    Platform.exit();
                    System.exit(0);
                }
            });


            primaryStage.setTitle("Simulaattori");

            kaynnistaButton = new Button();
            kaynnistaButton.setText("Käynnistä simulointi");
            kaynnistaButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        double osuus = Double.parseDouble(premiumOsuus.getText());

                        if (osuus < 0 || osuus > 100) {
                            virheLabel.setText("Premium osuuden pitää olla 0-100%.");
                        } else if (getAika() <= 0 || getViive() < 0 || getSaapumisvali() <= 0 || getPalvelupisteAika() <= 0 || getBasicAika() <= 0 || getPremiumAika() <= 0 || getVahausAika() <= 0 || getKuivausAika() <= 0) {
                            virheLabel.setText("Aikojen pitää olla positiivisia.");
                        } else {
                            virheLabel.setText("");
                            kontrolleri.kaynnistaSimulointi();
                            kaynnistaButton.setDisable(true);
                        }
                    } catch (Exception e) {
                        virheLabel.setText("Virheellinen syöte! Syötä vain numeroita.");
                    }
                }
            });

            hidastaButton = new Button();
            hidastaButton.setText("Hidasta");
            hidastaButton.setOnAction(e -> kontrolleri.hidasta());

            nopeutaButton = new Button();
            nopeutaButton.setText("Nopeuta");
            nopeutaButton.setOnAction(e -> kontrolleri.nopeuta());

            Label asetuksetOtsikko = new Label("SIMULAATION ASETUKSET:");
            asetuksetOtsikko.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));

            Label palvelupisteOtsikko = new Label("PALVELUPISTEIDEN AJAT:");
            palvelupisteOtsikko.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));

            aikaLabel = new Label("Simulointiaika (min):");
            aika = new TextField("Syötä aika");

            viiveLabel = new Label("Viive (ms):");
            viive = new TextField("Syötä viive");

            saapumisvaliLabel = new Label("Saapumisväli (min):");
            saapumisvali = new TextField("Syötä saapumisväli");

            premiumOsuusLabel = new Label("Premium osuus (%):");
            premiumOsuus = new TextField("Syötä prosentti premium pesuun menevistä");

            palvelupisteAikaLabel = new Label("Palvelupisteen aika (min):");
            palvelupisteAika = new TextField("Syötä palvelupisteen aika");

            basicAikaLabel = new Label("Basic pesun aika (min):");
            basicAika = new TextField("Syötä basic pesun aika");

            premiumAikaLabel = new Label("Premium pesun aika (min):");
            premiumAika = new TextField("Syötä premium pesun aika");

            vahausAikaLabel = new Label("Vahausaika (min):");
            vahausAika = new TextField("Syötä vahausaika");

            kuivausAikaLabel = new Label("Kuivausaika (min):");
            kuivausAika = new TextField("Syötä kuivausaika");

            tulosLabel = new Label("KOKONAISAIKA:");
            tulosLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
            tulos = new Label();
            tulos.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));
            tulos.setPrefWidth(150);

            virheLabel = new Label();
            virheLabel.setTextFill(Color.RED);
            virheLabel.setFont(Font.font("Tahoma", FontWeight.BOLD,12));

            HBox hBox = new HBox();
            hBox.setPadding(new Insets(15, 12, 15, 12)); // marginaalit ylÃ¤, oikea, ala, vasen
            hBox.setSpacing(10);   // noodien välimatka 10 pikseliä

            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setVgap(10);
            grid.setHgap(5);

            grid.add(asetuksetOtsikko, 0, 0, 2, 1);

            grid.add(aikaLabel, 0, 1);   // sarake, rivi
            grid.add(aika, 1, 1);          // sarake, rivi

            grid.add(viiveLabel, 0, 2);      // sarake, rivi
            grid.add(viive, 1, 2);           // sarake, rivi

            grid.add(saapumisvaliLabel, 0, 3);
            grid.add(saapumisvali, 1, 3);

            grid.add(premiumOsuusLabel, 0, 4);
            grid.add(premiumOsuus, 1, 4);

            grid.add(palvelupisteOtsikko, 0, 5, 2, 1);

            grid.add(palvelupisteAikaLabel, 0, 6);
            grid.add(palvelupisteAika, 1, 6);

            grid.add(basicAikaLabel, 0, 7);
            grid.add(basicAika, 1, 7);

            grid.add(premiumAikaLabel, 0, 8);
            grid.add(premiumAika, 1, 8);

            grid.add(vahausAikaLabel, 0, 9);
            grid.add(vahausAika, 1, 9);

            grid.add(kuivausAikaLabel, 0, 10);
            grid.add(kuivausAika, 1, 10);

            grid.add(tulosLabel, 0, 12);      // sarake, rivi
            grid.add(tulos, 1, 12);           // sarake, rivi

            grid.add(virheLabel, 0, 13, 2, 1);

            naytto = new Visualisointi(600, 300);

            BorderPane canvasReunus = new BorderPane();
            canvasReunus.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
            canvasReunus.setCenter((Canvas) naytto);

            HBox napit = new HBox(40);
            napit.setAlignment(Pos.CENTER);
            napit.getChildren().addAll(nopeutaButton, hidastaButton);

            Label autopesulaOtsikko = new Label("AUTOPESULA");
            autopesulaOtsikko.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));

            VBox oikeapuoli = new VBox(20);
            oikeapuoli.setAlignment(Pos.CENTER);
            oikeapuoli.getChildren().addAll(autopesulaOtsikko, canvasReunus, kaynnistaButton, napit);

            // TÃ¤ytetÃ¤Ã¤n boxi:
            hBox.getChildren().addAll(grid, oikeapuoli);

            Scene scene = new Scene(hBox);
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Käyttöliittymän rajapintametodit (kutsutaan kontrollerista)

    @Override
    public double getAika() {
        return Double.parseDouble(aika.getText());
    }

    @Override
    public long getViive() {
        return Long.parseLong(viive.getText());
    }

    @Override
    public double getSaapumisvali() {
        return Double.parseDouble(saapumisvali.getText());
    }

    @Override
    public double getPalvelupisteAika() {
        return Double.parseDouble(palvelupisteAika.getText());
    }

    @Override
    public double getBasicAika() {
        return Double.parseDouble(basicAika.getText());
    }

    @Override
    public double getPremiumAika() {
        return Double.parseDouble(premiumAika.getText());
    }

    @Override
    public double getVahausAika() {
        return Double.parseDouble(vahausAika.getText());
    }

    @Override
    public double getKuivausAika() {
        return Double.parseDouble(kuivausAika.getText());
    }

    @Override
    public double getPremiumOsuus() {
        return Double.parseDouble(premiumOsuus.getText()) / 100.0;
    }

    @Override
    public void setLoppuaika(double aika) {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        this.tulos.setText(formatter.format(aika));
        kaynnistaButton.setDisable(false);
    }


    @Override
    public IVisualisointi getVisualisointi() {
        return naytto;
    }
}




