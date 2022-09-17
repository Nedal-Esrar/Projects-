import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Main extends Application {

  @Override
  public void start(Stage stage) {
    BorderPane pane = new BorderPane();
    pane.setStyle("-fx-background-color: grey");

    Scene scene = new Scene(pane, 1024, 768);
    Clock clock = new Clock();

    GGCalendar cal = new GGCalendar();

    Text[] txt = {new Text(clock.currentTime()), new Text(" --- "), new Text(cal.currentDate())};
    for (Text t : txt)
      t.setFont(Font.font("Times New Romans", FontWeight.BOLD, 22));

    HBox text = new HBox(txt);

    pane.setLeft(clock);
    pane.setRight(cal);
    pane.setBottom(text);

    BorderPane.setAlignment(clock, Pos.CENTER);

    text.setAlignment(Pos.CENTER);
    cal.setAlignment(Pos.CENTER);
    cal.setPadding(new Insets(0, 70, 0, 0));

    Thread thread = new Thread(() -> {
      Runnable updater = () -> {
        clock.inc();
        txt[0].setText(clock.currentTime());
        txt[2].setText(cal.currentDate());
      };

      while (true) {
        try {
          Thread.sleep(1000);
        } catch (Exception ex) {
        }
        Platform.runLater(updater);
      }
    });

    thread.start();
    stage.setTitle("Clock and Calendar");
    stage.setScene(scene);

    stage.show();
  }
}