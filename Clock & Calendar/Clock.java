import java.util.Calendar;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

public class Clock extends Pane {

  private final IntegerProperty sec, min, hr;
  private final Circle clock;
  private Calendar c;

  public Clock() {
    clock = new Circle();
    clock.radiusProperty().bind(heightProperty().multiply(0.4));
    clock.setStyle("-fx-stroke: black; -fx-fill: gainsboro");
    clock.centerXProperty().bind(widthProperty().divide(2).add(widthProperty().divide(20)));
    clock.centerYProperty().bind(heightProperty().divide(2));
    clock.setStrokeWidth(3);

    Font font = Font.font("Times New Romans", FontWeight.BOLD, 20);
    Text num;

    getChildren().add(clock);

    for (int i = 0; i < 60; ++i) {
      Line l = new Line();

      l.startXProperty().bind(clock.centerXProperty().add(clock.radiusProperty().multiply(Math.sin(i * Math.PI / 30))));
      l.startYProperty().bind(clock.centerYProperty().subtract(clock.radiusProperty().multiply(Math.cos(i * Math.PI / 30))));
      l.endXProperty().bind(clock.centerXProperty().add(clock.radiusProperty().multiply((i % 5 == 0 ? 0.95 : 0.98) * Math.sin(i * Math.PI / 30))));
      l.endYProperty().bind(clock.centerYProperty().subtract(clock.radiusProperty().multiply((i % 5 == 0 ? 0.95 : 0.98) * Math.cos(i * Math.PI / 30))));

      getChildren().add(l);

      if (i % 5 == 0) {
        num = new Text();
        num.setFont(font);
        if (i == 5 || i == 10) {
          num.xProperty().bind(l.endXProperty().subtract(15));
          num.yProperty().bind(l.endYProperty().add(12));
        } 
        else if (i == 20 || i == 25) {
          num.xProperty().bind(l.endXProperty().subtract(13));
          num.yProperty().bind(l.endYProperty());
        } 
        else if (i == 35 || i == 40) {
          num.xProperty().bind(l.endXProperty().add(2));
          num.yProperty().bind(l.endYProperty());
        } 
        else if (i == 50 || i == 55) {
          num.xProperty().bind(l.endXProperty().add(2));
          num.yProperty().bind(l.endYProperty().add(12));
        } 
        else if (i == 0) {
          num.xProperty().bind(l.endXProperty().subtract(10));
          num.yProperty().bind(l.endYProperty().add(20));
        } 
        else if (i == 15) {
          num.xProperty().bind(l.endXProperty().subtract(14));
          num.yProperty().bind(l.endYProperty().add(7));
        } 
        else if (i == 30) {
          num.xProperty().bind(l.endXProperty().subtract(6));
          num.yProperty().bind(l.endYProperty().subtract(4));
        } 
        else {
          num.xProperty().bind(l.endXProperty().add(4));
          num.yProperty().bind(l.endYProperty().add(7));
        }
        num.setText(i != 0 ? i / 5 + "" : "12");
        getChildren().add(num);
      }
    }

    Line sLine = new Line();
    Line mLine = new Line();
    Line hLine = new Line();

    sLine.startXProperty().bind(clock.centerXProperty());
    sLine.startYProperty().bind(clock.centerYProperty());
    mLine.startXProperty().bind(clock.centerXProperty());
    mLine.startYProperty().bind(clock.centerYProperty());
    hLine.startXProperty().bind(clock.centerXProperty());
    hLine.startYProperty().bind(clock.centerYProperty());

    sLine.setStrokeWidth(1);
    sLine.setStroke(Color.BLACK);
    mLine.setStrokeWidth(3);
    mLine.setStroke(Color.BLUE);
    hLine.setStrokeWidth(5);
    hLine.setStroke(Color.YELLOW);

    c = Calendar.getInstance();
    sec = new SimpleIntegerProperty(c.get(Calendar.SECOND));
    min = new SimpleIntegerProperty(c.get(Calendar.MINUTE));
    hr = new SimpleIntegerProperty(c.get(Calendar.HOUR_OF_DAY));

    sLine.endXProperty().bind(bindX(0.9, 30, sec));
    sLine.endYProperty().bind(bindY(0.9, 30, sec));
    mLine.endXProperty().bind(bindX(0.75, 30, min, sec));
    mLine.endYProperty().bind(bindY(0.75, 30, min, sec));
    hLine.endXProperty().bind(bindX(0.6, 6, hr, min, sec));
    hLine.endYProperty().bind(bindY(0.6, 6, hr, min, sec));

    getChildren().addAll(hLine, mLine, sLine);
  }

  private DoubleBinding bindX(double len, int q, IntegerProperty... p) {
    return new DoubleBinding() {
      {
        bind(clock.centerXProperty(), clock.radiusProperty());

        for (IntegerProperty i : p) 
            bind(i);
      }

      @Override
      public double computeValue() {
        double res = p[0].getValue();

        for (int i = 1; i < p.length; ++i) 
            res += p[i].getValue() / Math.pow(60, i);

        return clock.getCenterX() + len * clock.getRadius() * Math.sin(res * (Math.PI / q));
      }
    };
  }
  private DoubleBinding bindY(double len, int q, IntegerProperty... p) {
    return new DoubleBinding() {
      {
        bind(clock.centerYProperty(), clock.radiusProperty());

        for (IntegerProperty i : p) 
            bind(i);
      }

      @Override
      public double computeValue() {
        double res = p[0].getValue();

        for (int i = 1; i < p.length; ++i)
          res += p[i].getValue() / Math.pow(60, i);

        return clock.getCenterY() - len * clock.getRadius() * Math.cos(res * (Math.PI / q));
      }
    };
  }

  public void inc() {
    c = Calendar.getInstance();
    sec.setValue(c.get(Calendar.SECOND));
    min.setValue(c.get(Calendar.MINUTE));
    hr.setValue(c.get(Calendar.HOUR_OF_DAY));
  }

  public String currentTime() {
    return String.format("%02d:%02d:%02d", hr.getValue(), min.getValue(), sec.getValue());
  }

}
