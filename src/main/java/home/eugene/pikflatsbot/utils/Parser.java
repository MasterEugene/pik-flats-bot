package home.eugene.pikflatsbot.utils;

public class Parser {
  private static final String CARS_PAGE_FROM_MARKER = "window.REDUX_INITIAL_STATE = ";
  private static final String CARS_PAGE_TO_MARKER = "};";

  public static String parseCarsPage(String raw) {
    int from = raw.indexOf(CARS_PAGE_FROM_MARKER) + CARS_PAGE_FROM_MARKER.length();
    int to = raw.indexOf(CARS_PAGE_TO_MARKER, from) + 1;
    return raw.substring(from, to);
  }
}
