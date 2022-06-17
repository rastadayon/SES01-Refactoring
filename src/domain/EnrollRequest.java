package domain;

import domain.exceptions.EnrollmentRulesViolationException;

import java.util.List;

public class EnrollRequest {
    Student s;
    List<CSE> courses;

    public EnrollRequest(Student s, List<CSE> courses) {
        this.s = s;
        this.courses = courses;
    }

    public Student getStudent() {
        return s;
    }

    private int getUnitsRequested() {
        int unitsRequested = 0;
        for (CSE o : courses)
            unitsRequested += o.getCourse().getUnits();
        return unitsRequested;
    }

    public List<CSE> getCourses() {
        return courses;
    }

    void checkHasRequestGPARequirements() throws EnrollmentRulesViolationException {
        if ((s.getGPA() < 12 && getUnitsRequested() > 14) ||
                (s.getGPA() < 16 && getUnitsRequested() > 16) ||
                (getUnitsRequested() > 20))
            throw new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f", getUnitsRequested(), s.getGPA()));
    }

    void checkHasAlreadyPassedCourse() throws EnrollmentRulesViolationException {
        for (CSE o : courses) {
            if(s.hasPassedCourse(o.getCourse()))
                throw new EnrollmentRulesViolationException(String.format("The student has already passed %s", o.getCourse().getName()));
        }
    }

    void checkPrerequisites() throws EnrollmentRulesViolationException {
        for (CSE o : courses)
            o.checkHasPassedPrerequisites(s);
    }

    void checkTimeConflicts() throws EnrollmentRulesViolationException {
        for (CSE o : courses) {
            for (CSE o2 : courses) {
                if (o == o2)
                    continue;
                if (o.hasSameExamTime(o2))
                    throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", o, o2));
                if (o.isSameCourse(o2))
                    throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", o.getCourse().getName()));
            }
        }
    }
}
