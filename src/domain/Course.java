package domain;
import domain.exceptions.EnrollmentRulesViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Course {
	private String id;
	private String name;
	private int units;
	
	List<Course> prerequisites;

	public Course(String id, String name, int units) {
		this.id = id;
		this.name = name;
		this.units = units;
		prerequisites = new ArrayList<Course>();
	}
	
	public void addPre(Course c) {
		getPrerequisites().add(c);
	}

	public Course withPre(Course... pres) {
		prerequisites.addAll(Arrays.asList(pres));
		return this;
	}

	public List<Course> getPrerequisites() {
		return prerequisites;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(" {");
		for (Course pre : getPrerequisites()) {
			sb.append(pre.getName());
			sb.append(", ");
		}
		sb.append("}");
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public int getUnits() {
		return units;
	}

	public String getId() {
		return id;
	}

	public boolean equals(Object obj) {
		Course other = (Course)obj;
		return id.equals(other.id);
	}

	public void checkHasPassedPrerequisites(Student s) throws EnrollmentRulesViolationException {
		nextPre:
		for (Course pre : getPrerequisites()) {
			for (Map.Entry<Term, Map<Course, Double>> tr : s.getTranscript().entrySet()) {
				for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
					if (r.getKey().equals(pre) && r.getValue() >= 10)
						continue nextPre;
				}
			}
			throw new EnrollmentRulesViolationException(String.format("The student has not passed %s as a prerequisite of %s", pre.getName(), name));
		}
	}
}
