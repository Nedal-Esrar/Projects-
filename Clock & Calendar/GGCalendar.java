import java.util.Calendar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.HPos;
import javafx.geometry.Insets;

public class GGCalendar extends VBox {
  private Calendar cal;
  private int day, month, yyear;
  private final StackPane cir;
  private static final String[]
          daysArr = {"Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri"},
          monthsArr = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
  private Text year, monthText;
  private VBox vbox;
  private GridPane myCalendar;

  public GGCalendar() {
    Circle c = new Circle(20);
    cir = new StackPane(c);
    c.setFill(null);
    c.setStroke(Color.BLACK);

    myCalendar = new GridPane();

    cal = Calendar.getInstance();

    day = cal.get(Calendar.DAY_OF_MONTH);
    month = cal.get(Calendar.MONTH);
    yyear = cal.get(Calendar.YEAR);
    year = new Text("" + cal.get(Calendar.YEAR));
    monthText = new Text(monthsArr[cal.get(Calendar.MONTH)]);

    vbox = new VBox(year, monthText);

    VBox.setMargin(year, new Insets(12, 15, 0, 12));
    VBox.setMargin(monthText, new Insets(0, 15, 15, 12));

    year.setFont(Font.font("Times New Romans", FontWeight.BOLD, 30));
    monthText.setFont(Font.font("Times New Romans", FontWeight.BOLD, 30));

    draw();
    circleDay();

    vbox.setStyle("-fx-background-color: gainsboro");
    myCalendar.setStyle("-fx-background-color: gainsboro");

    getChildren().addAll(vbox, myCalendar);
  }

  public void draw() {
    myCalendar.getChildren().clear();

    for (String d : daysArr) {
      Text day = new Text(d);
      day.setFont(Font.font("Times New Romans", FontWeight.BOLD, 22));
      myCalendar.addRow(0, day);
      GridPane.setHalignment(day, HPos.CENTER);
      GridPane.setMargin(day, new Insets(15, 15, 15, 12));
    }

    cal.set(Calendar.DAY_OF_MONTH, 1);

    int i = cal.get(Calendar.DAY_OF_WEEK), j = 1, len = i + cal.getActualMaximum(Calendar.DAY_OF_MONTH), c = 0;

    Text num;

    while (i != len) {
      num = new Text(++c + "");
      num.setFont(Font.font("Times New Romans", FontWeight.BOLD, 18));
      myCalendar.add(num, i % 7, j);
      GridPane.setHalignment(num, HPos.CENTER);
      GridPane.setMargin(num, new Insets(12));

      if (++i % 7 == 0)
        ++j;
    }

    cal = Calendar.getInstance();
  }

  private void circleDay() {
    cal.set(Calendar.DAY_OF_MONTH, 1);
    int d = cal.get(Calendar.DAY_OF_WEEK);
    myCalendar.add(cir, (day + d - 1) % 7, (day + d + 6) / 7);
    cal = Calendar.getInstance();
  }

  public String currentDate() {
    cal = Calendar.getInstance();

    if (cal.get(Calendar.YEAR) != yyear) {
      yyear = cal.get(Calendar.YEAR);
      year.setText(yyear + "");
    }

    if (cal.get(Calendar.MONTH) != month) {
      month = cal.get(Calendar.MONTH);
      draw();
      monthText.setText(monthsArr[month]);
    }

    if (cal.get(Calendar.DAY_OF_MONTH) != day) {
      myCalendar.getChildren().remove(cir);
      day = cal.get(Calendar.DAY_OF_MONTH);
      circleDay();
    }

    return String.format("%s - %s %d, %d",
                         daysArr[cal.get(Calendar.DAY_OF_WEEK) % 7],
                         monthsArr[month],
                         day,
                         yyear);
  }

}
