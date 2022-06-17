package domain;

import java.util.List;
import java.util.Map;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
	public void enroll(Student s, List<CSE> courses) throws EnrollmentRulesViolationException {
        Map<Term, Map<Course, Double>> transcript = s.getTranscript();
		for (CSE o : courses) {
            for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
                for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
                    if (r.getKey().equals(o.getCourse()) && r.getValue() >= 10)
                        throw new EnrollmentRulesViolationException(String.format("The student has already passed %s", o.getCourse().getName()));
                }
            }
            o.checkHasPassedPrerequisites(s);
            for (CSE o2 : courses) {
                if (o == o2)
                    continue;
                if (o.getExamTime().equals(o2.getExamTime()))
                    throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", o, o2));
                if (o.getCourse().equals(o2.getCourse()))
                    throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", o.getCourse().getName()));
            }
		}
        checkHasRequestGPARequirements(s, courses);
        for (CSE o : courses)
			s.takeCourse(o.getCourse(), o.getSection());
	}

    private void checkHasRequestGPARequirements(Student s, List<CSE> courses) throws EnrollmentRulesViolationException {
        int unitsRequested = 0;
        for (CSE o : courses)
            unitsRequested += o.getCourse().getUnits();
        if ((s.getGPA() < 12 && unitsRequested > 14) ||
				(s.getGPA() < 16 && unitsRequested > 16) ||
				(unitsRequested > 20))
			throw new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f", unitsRequested, s.getGPA()));
    }
}
