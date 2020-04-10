package employee;

import shift.*;
import java.util.*;

public class Employee {
	public final static int WEEKS_IN_YR = 52, UNINITIALIZED = -1;
	private static int numEmployees = 0;
	// Map of all employee ids->names. Used to help prevent id-collisions
	// while providing quick name lookup when id is known.
	private static Map<Long,String> idToName = new HashMap<Long,String>();
	private String name;
	private long id;
	private int firstYear = UNINITIALIZED;
	// ArrayList of Lists of shifts used since each day likely only has a
	// couple of shifts so O(n) complexity of list access is not a problem
	// and shifts are quickly inserted/removed.
	private ArrayList<WeekShifts> shifts =
		new ArrayList<WeekShifts>(WEEKS_IN_YR);

	private void initWeeks() {
		for (int i = 0; i < WEEKS_IN_YR; ++i)
			shifts.add(new WeekShifts());
	}

	static public int getNumEmployees() {
		return numEmployees;
	}

	static public String getEmployeeName(long id) {
		return idToName.get(id);
	}

	// Constructors
	public Employee() {++numEmployees; initWeeks();}
	public Employee(String name, long id) {
		++numEmployees;
		initWeeks();
		if (idToName.containsKey(id))
			throw new IllegalArgumentException("Non-unique id value entered.");
		idToName.put(id, name);
		this.name = name;
		this.id = id;
	}

	public void addShift(Shift s) {
		if (firstYear == UNINITIALIZED)
			firstYear = s.getShiftYear();

		shifts.get(s.getShiftWeek()-1).addShift(s);
	}

	public String getName() {
		return name;
	}

	public long getID() {
		return id;
	}

	// wk is initially decremented in calculations since indices start at 0
	// but weeks start at 1Print all shifts in a particular week where the
	// initial year starts at week 0 and ends at week 51, the following year
	// starts at week 52 and ends at 103 and so on.
	public void printWeekShifts(int wk) {
		// TODO: Print week + year rather than just week number which will not
		// be easy to parse.
		System.out.println("Week: " + wk % WEEKS_IN_YR+1);
		if (wk-1 < shifts.size())
			System.out.println();
		else
			shifts.get(wk-1).printShifts();
	}

	public void printAllShifts() {
		for (var s : shifts) {
			// Note s.printShifts() here refers to WeekShifts.printShifts(),
			// not a recursive call to Employee.printShifts()
			if (s.getWeekNumber() != -1) {
				System.out.println("Week: " + s.getWeekNumber());
				s.printShifts();
			}
		}
	}
}
