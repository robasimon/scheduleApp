import java.util.*;
import java.time.*;
import java.time.format.*;
import shift.*;
import employee.Employee;


public class App {

	public static void main (String [] args) {
    LocalDateTime current = LocalDateTime.now();
		ArrayList<Employee> employees = new ArrayList<Employee>();

		Shift test = new Shift();
    try {
			test.setStart(-2020,3,22,15,1);
		}
		catch (DateTimeException e) {
			System.out.println(e.getMessage());
		}
		test.setStart(2020,3,22,15,1);
    test.setEndFromDuration(60);
		test.printShift();
    System.out.println(test.getShiftWeek());

    // Example try/catch tests for bad constructors parameters
    try {
      Shift badConstructor =
        new Shift(LocalDateTime.now(),
                  LocalDateTime.now().minusDays(4));
    }
    catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
		employees.add(new Employee("Emma Griffin", 0));
		employees.get(0).addShift(new Shift(current, current.plusMinutes(10),
																				"barista"));
		employees.get(0).addShift(new Shift(current.minusMinutes(300),
																				current.minusMinutes(100),
																				"barista"));
		employees.get(0).addShift(new Shift(current.plusMinutes(300),
																				current.plusMinutes(400),
																				"barista"));
		employees.get(0).addShift(new Shift(current.plusMinutes(300),
																				current.plusMinutes(380),
																				"barista"));
		employees.get(0).addShift(new Shift(current.minusMinutes(500),
																				current.minusMinutes(420),
																				"barista"));

		employees.add(new Employee("Alice Anderson", 1));
		employees.get(1).addShift(new Shift(current.plusDays(13)
																				.plusMinutes(33),
																				current.plusDays(13)
																				.plusMinutes(60*8)));

		employees.add(new Employee("Zain Zurich", 2));
		employees.get(2).addShift(new Shift(current.minusDays(23)
																				.plusMinutes(3),
																				current.minusDays(23)
																				.plusMinutes((long)(60*4.5))));
		employees.get(2).addShift(new Shift(current.plusDays(365)
																				.plusMinutes(3),
																				current.plusDays(365)
																				.plusMinutes((long)(60*4.5))));

		// Test lexicographical sorting algo
		Collections.sort(employees, new EmployeeNameComparator());
		for (var emp : employees) {
			System.out.println(emp.getName());
			emp.printAllShifts();
		}

		System.out.println();

		// Test id sorting algo
		Collections.sort(employees, new EmployeeIDComparator());
		for (var emp : employees)
			System.out.println(emp.getName());
  }
}

// Comparator for sorting Employees by their names lexicographically
class EmployeeNameComparator implements Comparator<Employee> {
	public int compare(Employee a, Employee b) {
		return a.getName().compareTo(b.getName());
	}
}

// Comparator for sorting Employees by their names lexicographically
class EmployeeIDComparator implements Comparator<Employee> {
	public int compare(Employee a, Employee b) {
		return (int)a.getID() - (int)b.getID();
	}
}
