package shift;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

// Class to hold a shift with a designated start and end time stored as
// LocalDateTimes
public class Shift {
  // Time format
  static final DateTimeFormatter FORMAT =
		DateTimeFormatter.ofPattern("EEE MM-dd-yyyy HH:mm");


  // Print formatted time
  public static void printTime (LocalDateTime ldt) {
    System.out.println(ldt.format(FORMAT));
  }
  public final static String END_BEFORE_START =
    "End of shift preceeds start.";
  private final int MIN_IN_HR = 60, HR_IN_DAY = 24;
  // Max length of a shift (in minutes). Translates to a 24-hour shift.
  private final int MAX_LEN = MIN_IN_HR * HR_IN_DAY;

  // null default values used to ensure certain operations
  // (such as arithmetic on time) are well defined prior to attempting
  // to execute a statement
  private LocalDateTime start = null, end = null;

  // booleans used to indicate whether start and end of shift fall
  // under the same day, week, and year respectively.
  private boolean sameDay = true, sameWeek = true, sameYear = true;

	// Particular position/job associated with shift
	private String position = null;

	public static boolean shiftsOverlap(Shift a, Shift b) {
		return
			(!a.getStart().isBefore(b.getStart()) && // (a.start >= b.start &&
			 !a.getEnd().isAfter(b.getEnd())) // a.end <= b.end) -> a is inside b
			||
			(!b.getStart().isBefore(a.getStart()) && // (b.start >= a.start &&
			 !b.getEnd().isAfter(a.getEnd()));// b.end <= a.end) -> b is inside a
	}

  // Constructors
  public Shift () {}
  public Shift(LocalDateTime s, LocalDateTime e) {
    // Make sure e comes after s
    if (!e.isAfter(s))
      throw new
        IllegalArgumentException(END_BEFORE_START);
    start = s;
    end = e;
    sameDay = areSameDay(s, e);
    sameWeek = areSameWeek(s, e);
    if (!sameDay)
      System.out.println("Differnt days!");
    if (!sameWeek)
      System.out.println("Different weeks!");
  }
  public Shift(LocalDateTime s, LocalDateTime e, String pos) {
    // Make sure e comes after s
    if (!e.isAfter(s))
      throw new
        IllegalArgumentException(END_BEFORE_START);
		position = pos;
		start = s;
    end = e;
    sameDay = areSameDay(s, e);
    sameWeek = areSameWeek(s, e);
    if (!sameDay)
      System.out.println("Differnt days!");
    if (!sameWeek)
      System.out.println("Different weeks!");
  }

  // Predicate methods
  boolean areSameDay(LocalDateTime s, LocalDateTime e) {
    return s.toLocalDate() == e.toLocalDate();
  }

  boolean areSameWeek(LocalDateTime s, LocalDateTime e) {
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    return s.get(weekFields.weekOfWeekBasedYear()) ==
      e.get(weekFields.weekOfWeekBasedYear());
  }

  // Setters
  public void setStart(int y, int mo, int d, int h, int mi) {
    // Validate year. LocalDateTime will internally throw an exception for
    // all other invalid fields, but will accept negative values for years.
    if (y < 0)
      throw new DateTimeException("Invalid value for year. " +
                                  "Years must be non-negative.");
    if (end != null)
      if (!LocalDateTime.of(y, mo, d, h, mi).isBefore(end))
        throw new IllegalArgumentException(END_BEFORE_START);

    start = LocalDateTime.of(y, mo, d, h, mi);
  }
  public void setEnd(int y, int mo, int d, int h, int mi) {
    // Validate year. LocalDateTime will internally throw an exception for
    // all other invalid fields, but will accept negative values for years.
    if (y < 0)
      throw new DateTimeException("Invalid value for year. " +
                                  "Years must be non-negative.");
    // Make sure e comes after s
    if (start != null)
      if (!LocalDateTime.of(y, mo, d, h, mi).isAfter(start))
        throw new IllegalArgumentException(END_BEFORE_START);

    end = LocalDateTime.of(y, mo, d, h, mi);
  }
  // Set end indirectly by performing addition on start with a duration
  // of time in minutes.
  public void setEndFromDuration(int mi) {
    if (start != null)
      end = start.plusMinutes(mi);

    else
      throw new NullPointerException("Start of shift not set.");
  }

  // Getters
  public LocalDateTime getStart() {
    return start;
  }
  public LocalDateTime getEnd() {
    return end;
  }
  // Return week of year shift falls under. In the event that the shift
  // starts and ends on different weeks (such as in the case of a shift
  // that begins on a Monday night and ends on a Sunday morning) the week
  // matching the start of the week is returned.
  public int getShiftWeek() {
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    return start.get(weekFields.weekOfWeekBasedYear());
  }

	// Return year shift falls under. In the event that the shift
  // starts and ends on different years (such as in the case of a shift
  // that begins on the night of Dec. 31st and ends the morning of Jan. 1st
	// ) the year matching the start of the year is returned.
  public int getShiftYear() {
    return start.getYear();
  }

	public void printShift() {
		if (position != null)
			System.out.println("Position: " + position);
		System.out.print("\tShift start: ");
		printTime(start);
		System.out.print("\tShift end:   ");
		printTime(end);
	}
}
