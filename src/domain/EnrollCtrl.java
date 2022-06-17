package domain;

import domain.exceptions.EnrollmentRulesViolationException;

import java.util.ArrayList;

public class EnrollCtrl {
	public ArrayList<EnrollmentRulesViolationException> enroll(EnrollRequest enrollRequest) {
        ArrayList<EnrollmentRulesViolationException> exceptions = new ArrayList<>();
        try {
            enrollRequest.checkHasAlreadyPassedCourse();
        } catch (EnrollmentRulesViolationException e) {
            exceptions.add(e);
        }
        try {
            enrollRequest.checkHasRequestGPARequirements();
        } catch (EnrollmentRulesViolationException e) {
            exceptions.add(e);
        }
        try {
            enrollRequest.checkPrerequisites();
        } catch (EnrollmentRulesViolationException e) {
            exceptions.add(e);
        }
        try {
            enrollRequest.checkTimeConflicts();
        } catch (EnrollmentRulesViolationException e) {
            exceptions.add(e);
        }
        if (exceptions.size() == 0)
            for (CourseOffering courseOffering : enrollRequest.getCourseOfferings())
                enrollRequest.getStudent().takeCourse(courseOffering.getCourse(), courseOffering.getSection());
        return exceptions;
	}
}
