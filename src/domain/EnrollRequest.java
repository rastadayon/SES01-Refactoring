package domain;

import domain.exceptions.EnrollmentRulesViolationException;

import java.util.List;

public class EnrollRequest {
    Student student;
    List<CourseOffering> courseOfferings;

    public EnrollRequest(Student student, List<CourseOffering> courseOfferings) {
        this.student = student;
        this.courseOfferings = courseOfferings;
    }

    public Student getStudent() {
        return student;
    }

    private int getUnitsRequested() {
        int unitsRequested = 0;
        for (CourseOffering courseOffering : courseOfferings)
            unitsRequested += courseOffering.getCourse().getUnits();
        return unitsRequested;
    }

    public List<CourseOffering> getCourseOfferings() {
        return courseOfferings;
    }

    void checkHasRequestGPARequirements() throws EnrollmentRulesViolationException {
        if ((student.getGPA() < 12 && getUnitsRequested() > 14) ||
                (student.getGPA() < 16 && getUnitsRequested() > 16) ||
                (getUnitsRequested() > 20))
            throw new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f", getUnitsRequested(), student.getGPA()));
    }

    void checkHasAlreadyPassedCourse() throws EnrollmentRulesViolationException {
        for (CourseOffering o : courseOfferings) {
            if(student.hasPassedCourse(o.getCourse()))
                throw new EnrollmentRulesViolationException(String.format("The student has already passed %s", o.getCourse().getName()));
        }
    }

    void checkPrerequisites() throws EnrollmentRulesViolationException {
        for (CourseOffering o : courseOfferings)
            o.checkHasPassedPrerequisites(student);
    }

    void checkTimeConflicts() throws EnrollmentRulesViolationException {
        for (CourseOffering courseOffering : courseOfferings) {
            for (CourseOffering courseOffering1 : courseOfferings) {
                if (courseOffering == courseOffering1)
                    continue;
                if (courseOffering.hasSameExamTime(courseOffering1))
                    throw new EnrollmentRulesViolationException(String.format(
                            "Two offerings %s and %s have the same exam time", courseOffering, courseOffering1));
                if (courseOffering.isSameCourse(courseOffering1))
                    throw new EnrollmentRulesViolationException(String.format(
                            "%s is requested to be taken twice", courseOffering.getCourse().getName()));
            }
        }
    }
}
